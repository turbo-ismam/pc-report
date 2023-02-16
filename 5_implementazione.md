# Implementazione

Si è deciso che per ogni microservizio si sarebbe creato un corrispondente repository, questo per fare in modo che siano tra di loro quanto più possibile indipendenti per seguire la loro definzione. Questa scelta ha aiutato durante la fase di implementazione in quanto la mancanza di dipendenze ha reso più semplice la stesura del codice.

Si è partiti con il microservizio relativo agli users, gli step per l'implementazione sono stati:
1. realizzazione di tutte le interfacce relative al suo bounded context
2. implementazione dei tre aggregati del livello di domain
3. implementazione del livello application

Una volta che il microservizio users era in procinto di terminare si è passato alla realizzazione delle interfacce di tutti gli aggregates di tutti i bounded context. Una volta ultimata si è passato all'implementazione in parallelo dei due successivi microservizi: items e carts.

Questi tre microservizi sono capaci di coprire e mettere in mostra le tecnologie affrontate durante il corso che abbiamo deciso di adottare.

## Tecnologie

Durante l'implementazione dei vari microservizi si è reso necessario l'utilizzo di tecnologie apposite, di seguito le principali adottate:
1. Postgres: è un DBMS relazionale che abbiamo adottato per mantenere la persistenza all'interno di ogni microservizio. Questa tecnologia è indipendente dall'architettura in quanto non è esposta all'esterno, è utilizzata unicamente dal data persistence layer del microservizio.
2. Eclipse Ditto: è un middleware che aiuta a costruire digital twins. Ditto appartiene alla categoria dei middleware IoT, fornendo un livello di astrazione per le soluzioni IoT che interagiscono con dispositivi fisici tramite il modello del Digital Twin. Ditto viene usato da due microservizi: carts e stores (questi due insieme gestiscono tutti i dispositivi IoT del sistema)
3. RabbitMQ: è un middleware message-oriented, nel nostro contesto è stato utilizzato per eseguire lo scambio di messaggi tra gli attori dei vari microservizi
4. Websocket: è una tecnologia che fornisce un protocollo per lo scambio di messaggi full-duplex potenzialmente in tempo reale. Nel nostro contesto viene utilizzato per fare comunicare i microservizi con l'applicazione e la dashboard (in particolare per quanto riguarda gli eventi).
5. Docker: è una tecnologia per eseguire applicazioni all'interno di ambienti isolati. Docker è stato utilizzato per creare dei container per ogni microservizio, lo si utilizza sia in ambito di deploy che di test.
