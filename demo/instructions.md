# Eclipse Ditto

In questo documento è illustrato la procedura per creare un cluster ditto eseguito all'interno di un container Docker.
Successivamente vengono spiegate alcune query di base per interagire con ditto (la comunicazione parte al di fuori del container in cui ditto sta eseguendo, può essere un altro container o la macchina sulla quale gira il container).

Per provare il tutto suggerisco di installare su una macchina Ubuntu o Debian (nella documentazione i requisiti minimi fisici sono un quad core e 4gb di ram)


## Prerequisiti
- Installa git
``` 
$ sudo apt install git-all 
```
- Installa curl
``` 
$ sudo apt install curl 
```
- Installa Docker
``` 
$ sudo apt install curl
$ sudo service docker start
$ sudo usermod -a -G docker <your-username> 
```
Fai un logout e poi un login per settare il gruppo "docker"

## Setop del cluster ditto
- Installa docker compose
```
$ sudo curl -L "https://github.com/docker/compose/releases/download/1.25.4/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose
$ sudo chmod +x /usr/local/bin/docker-compose
```

- Clona ditto, serve per il file docker-compose.yaml e altre risorse per eseguire Ditto con Docker Componse
```
$ git clone --depth 1 https://github.com/eclipse/ditto.git
```
- Startup ditto cluster, assicurarsi che la porta 80 non sia in uso, se in uso mettine una libera
```
$ cd ditto/deployment/docker/
$ export DITTO_EXTERNAL_PORT=80
$ docker-compose up -d
```
- Verifica che ditto stia funzionando correttamente
```
$ docker-compose ps
```
L'output dovrebbe essere così:
```
  Name                       Command               State           Ports         
----------------------------------------------------------------------------------------
docker_concierge_1       /sbin/tini -- java -jar st ...   Up      8080/tcp              
docker_connectivity_1    /sbin/tini -- java -jar st ...   Up      8080/tcp              
docker_gateway_1         /sbin/tini -- java -Dditto ...   Up      0.0.0.0:8081->8080/tcp
docker_mongodb_1         docker-entrypoint.sh mongo ...   Up      27017/tcp             
docker_nginx_1           nginx -g daemon off;             Up      0.0.0.0:80->80/tcp    
docker_policies_1        /sbin/tini -- java -jar st ...   Up      8080/tcp              
docker_swagger-ui_1      nginx -g daemon off;             Up      80/tcp, 8080/tcp      
docker_things-search_1   /sbin/tini -- java -jar st ...   Up      8080/tcp              
docker_things_1          /sbin/tini -- java -jar st ...   Up      8080/tcp
```
Verifica ora che il tuo cluster ditto sia *sano*
```
curl -u devops:foobar http://localhost:${DITTO_EXTERNAL_PORT}/status/health
```
Dovresti ottenere come output questo:
```
{"label":"roles","status":"UP", ... }
```

## Query
Ditto fornisce un'API per creare ed interagire con i nostri things. 
Di seguito alcune query di base per poter interagire con il cluster creato in precedenza.
Le seguenti operazioni devono essere lanciati dal terminale, è possibile farlo esternamente dal cluster ditto in esecuzione sul container.

### Crea oggetti all'interno de cluster
Creazione di un *Thing* attraverso *curl*, l'autenticazione utilizzerà le credenziali dello user *"ditto"* create di default da nginx via "docker".
```
$   curl -u ditto:ditto -X PUT -H 'Content-Type: application/json' -d '{
    "definition": "digitaltwin:DigitaltwinExample:1.0.0",
    "attributes": {
        "manufacturer": "ACME",
        "VIN": "0815666337"
    },
    "features": {
        "transmission": {
        "properties": {
            "automatic": true, 
            "mode": "eco",
            "cur_speed": 90, 
            "gear": 5
        }
        },
        "environment-scanner": {
        "properties": {
            "temperature": 20.8,
            "humidity": 73,
            "barometricPressure": 970.7,
            "location": {
            "longitude": 47.682170,
            "latitude": 9.386372
            },
            "altitude": 399
        }
        }
    }
    }' 'http://localhost:8080/api/2/things/org.eclipse.ditto:fancy-car'

```
Il risultato è un digital twin in Thing Notation. L'ID è **org.eclipse.ditto:fancy-car**. Un ID eve necessariamente contenere un namespace prima dei ":".

### Cerca un thing conoscendone l'ID
```
$ curl -u ditto:ditto -X GET 'http://localhost:8080/api/2/things/org.eclipse.ditto:fancy-car'

```

### Cerca una specifica proprietà
Cerco *cur_speed* della mia *fancy-car*
```
$ curl -u ditto:ditto -X GET 'http://localhost:8080/api/2/things/org.eclipse.ditto:fancy-car/features/transmission/properties/cur_speed'
```

### Aggiornamento di una proprietà
Aggiorno *curr_speed* della mia *fancy-car*
```
$ curl -u ditto:ditto -X PUT -H 'Content-Type: application/json' -d '77' 'http://localhost:8080/api/2/things/org.eclipse.ditto:fancy-car/features/transmission/properties/cur_speed'
```

### Certo tutti i things
Cerco tutti gli oggetti che hanno il campo *manufacturer* contenente il valore *ACME*
```
$ curl -u ditto:ditto -X GET 'http://localhost:8080/api/2/search/things?filter=eq(attributes/manufacturer,"ACME")'
```

## Applicazione Scala per interagire con il cluster
Basi per creare un'applicazione Scala per poter interagire con il cluster ditto.
- Versione Scala: Scala 3.1.3
- JDK: 11.0.17

Importando la seguente libreria di sistema:
```
import scala.sys.process.*
```
Sarà possibile effettuare richieste da terminale utilizzando curl, quindi si possono sfruttare le istruzioni per interagire con il cluster illustrate in precedenza.
Importare
```
import scala.language.postfixOps
```
E successivamente scrivere dentro un main la seguente riga (sostituire DITTO_EXTERNAL_PORT con la porta settata in precedenza)
```
"curl -u ditto:ditto -X GET 'http://localhost:DITTO_EXTERNAL_PORT/api/2/things/org.eclipse.ditto:fancy-car'" !
```
Il risultato sarà il json di risposta alla query.
