# Continuous Integration

## Trunk based development

Il workflow adottato nel progetto è *trunk-based*, che differisce dal classico *git workflow* in quanto favorisce agilità e rapidità in concordanza con l’approccio *continuous deployment*

Si opera nel seguente modo: un branch *main* (il *trunk*) da cui si creano *short-lived feature branches* in modo da programmare su codice aggiornato. Ogni branch è operata da uno sviluppatore alla volta. Ogni operazione di merging sul trunk lancia una *release*.


## Organizzazione delle repository

Ogni microservizio del progetto viene sviluppato in una propria repository GitHub; tali repo sono poi importate come *submodules* nella repo principale, *pervasive-cats/**toys-store***.

La repository *pervasive-cats/**toys-store-bc-template*** è appunto un template contenente le impostazioni comuni alle repository dei vari microservizi.

Ogni repository nata dal template avrà il suo Dockerfile, configurato di default nel template ma liberamente adattabile. Il comportamento di base dei Dockerfile è come segue: ogni Dockerfile scarica il JAR dell’ultima release della propria repository per poi eseguirlo.

L’operazione di installazione dell’applicazione consisterà nell’eseguire tutti i Dockerfile con un adeguato Docker compose per lanciare tutti i container e far partire il sistema.

## Releases

L’automazione delle *releases* è realizzata con [semantic-release](https://github.com/semantic-release/semantic-release).

L’app *semantic-release* genera il numero di versione della release in rispetto della convenzione [Semantic Versioning](https://semver.org/) leggendo commmit che seguono il formato [Angular Commit Message Conventions](https://github.com/angular/angular/blob/master/CONTRIBUTING.md#-commit-message-format).


## Code review

- [sonarcloud](https://sonarcloud.io/) - produce report sulla sicurezza del codice, code coverage e code smells
- [codecov](https://about.codecov.io/) - produce report sulla code coverage direttamente nel workflow
- [scalafix](https://scalacenter.github.io/scalafix/) - linting per Scala
- [wartremover](https://www.wartremover.org/) - linting per Scala aggiuntivo
- [scalafmt](https://scalameta.org/scalafmt/) - formattazione per Scala

## Security

In ogni repository è stata abilita la scansione da parte di [GitGuardian](https://www.gitguardian.com/) al fine di individuare vulnerabilità di sicurezza, principalmente la pubblicazione in chiaro di chiavi private e password.

## Dependencies

L’automazione dell’aggiornamento delle *dependencies* del progetto è realizzata con [Renovate](https://github.com/renovatebot/renovate).

Le dipendenze gestite da Renovate sono:

- GitHub Actions; aggiornamenti contrassegnati come “ci”.
- Build Automation dependencies: aggiornamenti contrassegnati come “build”.

Inoltre ad ogni release di un *submodule*la repo principale viene aggiornata automaticamente.

Renovate gestisce anche le vulnerabilità di sicurezza individuate tramite la lettura dei report di sicurezza di Dependabot.
