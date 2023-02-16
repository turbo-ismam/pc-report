# Test

Il testing è stato svolto sfruttando la libreria *ScalaTest*.
Gli unit test sono stati implementati nella maniera classica senza riscontrare difficoltà.
Tuttavia per poter effettuare gli integration test è stato necessario introdurre i container di docker, per fare ciò abbiamo utilizzato la libreria *TestContainers-scala*.
Questa libreria è un wrapper di *TestContainers-Java* e permette di utilizzare contenitori di docker.
I container servono per una serie di test, tra i quali:
- test dei Repository di ogni bounded context: essi al proprio interno salvano i dati del proprio domain, in questo caso il container serve per creare un ambiente dove possa essere fatto il deploy del database per potere testare il codice che interagisce con esso
- test con rabbitMQ: nei microservizi dove è richiesto l'uso di rabbitMQ si è creato un ambiente con un container contenente il suo broker per poter testare l'attore che comunica con esso.
- test con ditto: nei microservizi dove è richiesto l'uso di ditto si è creato un ambiente con tutti i container richiesti per poter testare l'attore che si occupa di comunicare con ditto.