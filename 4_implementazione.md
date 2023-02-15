# Implementazione

Si è deciso che per ogni bounded context si sarebbe creato l'analogo repository, questo per via del fatto che i bounded context tra di loro siano indipendenti. Questa scelta ha aiutato durante la fase di implementazione in quanto la scrittura di codice poteva sviluppare in maniera più facilitata dal momento che non vi erano dipendenze.

Si è partiti con il microservizio relativo agli users, gli step per l'implementazione sono:
1. realizazione di tutte le interfacce relative a questo bounded context
2. implementazione dei tre aggregati della parte di domain
3. implementazione della parte di application

Una volta che il bounded context degli users era in procinto di terminare (alla sua prima release) si è proceduto alla realizzazione delle interfacce di tutti gli aggregates di tutti i bounded context. Una volta ultimata si è passato all'implementazione in parallelo dei due successivi bounded context: items e carts

Si prevede che terminati questi tre bounded context siano capaci di coprire e mettere in mostra le tecnologie affrontate durante il corso.

## Tecnologie

Durante l'implementazione dei vari microservizi si è reso necessario l'utilizzo di determinate tecnologie, alcune di esse sono legate all'architettura mentre altre sono indipendenti da essa.
Di seguito le tecnologie principali adottate:
1. Postgres: è un DBMS relazionale ed abbiamo adottato questa tecnologia per mantenere la persistenza all'interno di ogni microservizio. Questa tecnologia è indipendente dall'architettura in quanto non è esposta all'esterno se non attraverso apposite interfacce 
0. Eclipse Ditto: è un framework che aiuta a costruire digital twins. Ditto funge da middleware IoT, fornendo un livello di astrazione per le soluzioni IoT che interagiscono con dispositivi fisici tramite il modello del Digital Twin. Ditto serve a due bounded context in particolare: carts e stores (questo per via del fatto che gestiscono insieme tutti i dispositivi IoT)
0. RabbitMQ: è un middleware message-oriented, nel nostro contesto è utilizzato per effettuare lo scambio di messaggi tra gli attori dei vari bounded context.
0. Web Socket: è una tecnologia che fornisce un sistema di messaggistica. Nel nostro contesto viene utilizzato per fare comunicare alcuni bounded context
0. Docker: è una tecnologia per eseguire applicazioni all'interno di ambienti isolati. Docker è stato utilizzato per creare dei container per ogni microservizio, lo si utilizza anche in ambito di test.