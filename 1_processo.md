# Processo

## Tool
Gli strumenti e le tecnologie di cui ci siamo avvalsi durante lo sviluppo sono state le seguenti:
- "scalafmt" per formattare in modo uniforme il codice
- "scalafix" per formattare in modo uniforme il codice
- "sonarcloud" per il rilevamento della qualità del codice
- "codecov" per analizzare la code coverage delle nostre suite di test

## GitHub

Lo sviluppo ha previsto la creazione di un'organizzazione su github, all'interno sono stati create una serie di repository:
- *toys-store-bc-template*: un template per creare le repository di produzione
- *toys-store-context-mapping*: contiene i diagrammi dei casi d'uso, diagramma dell'architettura e l'architettura di tutti i bounded context

Le altre repository sono relative ai vari bounded context e rappresentano ogni microservizio.

All'interno di ogni repository abbiamo organizzato il workflow nel seguente modo:
- il branch "main" è dedicato alle versioni principali dell'applicazione
- 