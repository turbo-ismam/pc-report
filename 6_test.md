# Test

Il testing è stato svolto sfruttando la libreria *ScalaTest*.
Gli unit test sono stati implementati nella maniera classica senza riscontrare difficoltà.
Tuttavia per poter effettuare gli integration test è stato necessario introdurre i container di docker, per fare ciò abbiamo utilizzato *TestContainers-scala*.
Questa libreria è un wrapper di *TestContainers-Java* e permette di utilizzare contenitori di docker.
I container servono per una serie di test, tra i quali:
- test dei Repository di ogni bounded context: essi al proprio interno salvano i dati del proprio domain, in questo caso il container serve per creare un ambiente dove possa essere fatto il deploy del database per potere testare il codice che interagisce con esso
- test con rabbitMQ (dove e come viene usato?)