# Implementazione

Si è deciso di utilizzare per ogni bounded context si sarebbe creato l'analogo repository, questo in quanto i bounded context tra di loro sono indipendenti. Questa scelta ha aiutato durante la fase di implementazione in quanto la scrittura di codice poteva sviluppare in maniera più facilitata dal momento che non vi erano dipendenze.

Si è partiti con il microservizio relativo agli users, gli step per l'implementazione sono:
1. realizazione di tutte le interfacce relative a questo bounded context
2. implementazione dei tre aggregati della parte di domain
3. implementazione della parte di application

Una volta che il bounded context degli users era in procinto di terminare (ovvero alla sua prima release) si è proceduto alla realizzazione delle interfacce di tutti gli aggregates di tutti i bounded context. Una volta ultimata si è passato all'implementazione in parallelo dei due successivi bounded context: items e carts

Si prevede che terminati questi tre bounded context siano capaci di coprire e mettere in mostra le tecnologie affrontate durante il corso.