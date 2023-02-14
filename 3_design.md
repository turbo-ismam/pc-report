# Design

## Design architetturale

Si è seguito il processo di Domain-Driven Design volto alla costruzione di uno smart pervasive system.

Il team discutendone internamente ha creato un primo use case diagram, una volta prodotte si è passati al domain storytelling.
Durante la realizzazione di esso si è continuato a modificare gli use case, specificando azioni precedentemente non considerate e precisando le interfacce utente che sono emerse.
Una volta terminato il domain storytelling e gli use case diagram il team ha effettuato l’event storming allo scopo di esplorare propriamente la timeline di eventi e fare emergere i bounded context.

I bounded context che sono emersi sono i seguenti:
- **shopping context** - core subdomain: individuato dall’inserimento di un prodotto nel carrello, avviando così il processo di acquisto, fino alla terminazione dello stesso, determinata dall’uscita del cliente dal negozio. Shopping context è un core domain in quanto è responsabile per l’acquisto automatico dei prodotti, ovvero l’innovazione del processo di acquisto.
- **stores context** - similmente, questo context è un core domain poichè contiene gli strumenti necessari a effettuare acquisti smart.
- **carts context** - supporting subdomain: i carrelli smart si distinguono dai normali carrelli in quanto dotati di sensori e capaci di effettuare operazioni in autonomia, essendo comunque piuttosto semplici non sono però un core subdomain.
- **products context** - supporting subdomain: ogni prodotto ha un sensore.
- **users context** - supporting subdomain: normale gestione di account utente.
- **payment context** - generic subdomain: il sistema di pagamento è delegato ad un third-party service.

### Diagrammi dei casi d'uso

<!--<img title="Applicazione" alt="Diagramma dei casi d'uso dell'applicazione" src="res/Applicazione.jpg">

<img title="Dashboard responsabili" alt="Diagramma dei casi d'uso della dashboard dei responsabili" src="res/DashboardResponsabile.jpg">

<img title="Dashboard amministratori" alt="Diagramma dei casi d'uso della dashboard degli amministratori" src="res/DashboardAmministrazione.jpg">

<img title="Carrello" alt="Diagramma dei casi d'uso dei carrelli" src="res/Carrello.jpg">

<img title="Sistema restituzione" alt="Diagramma dei casi d'uso del sistema di restituzione" src="res/SistemaRestituzione.jpg">

<img title="Sistema antitaccheggio" alt="Diagramma dei casi d'uso del sistema antitaccheggio" src="res/SistemaAntitaccheggio.jpg">

<img title="Gondola" alt="Diagramma dei casi d'uso della gondola" src="res/Gondola.jpg"> -->

A seguito di un incontro è emerso che la scelta migliore sia quella di utilizzare u'architettura a microservizi, nello specifico, uno per ogni bounded context, quindi un sistema composto da un totale di 6 microservizi. L'architettura del sistema è stata poi sviluppata sulle linee guida della clean architecture di Martin Fowler. I microservizi sono:
- Users
- Carts
- Items
- Stores
- Shopping
- Payments

<!--<img title="Architettura" alt="Architettura" src="res/Architecture.jpg"> -->

L'architettura del singolo microservizio è uguale per tutti ed è composta su due livelli:
- **core**: questo livello contiene tutte le entità del dominio del microservizio e per progettazione è ad un alto livello di astrazione. Contiene moduli che descrivono il dominio tramite aggregate, entities, value objects, services e domain events.
Ogni aggregate inoltre espone attravero il proprio repository le sue funzionalità all'esterno.
- **application**: questo livello fornisce i casi d'uso del microservizio, contiene quindi tutta la logica dell'applicazione, ha una dipendenza dal livello core. Questo livello fornisce un'interfaccia da implementare dall'esterno (attravero l'invio e la ricezione di messaggi).
Contiene due moduli:
    - **actors**, questi descrivono il comportamento a livello applicazione. Ogni attore al proprio interno gestisce una serie di richieste e fornisce delle risposte. I messaggi che gli attori ricevono sono di tipo *replyTo* (richieste che esigono risposte), l'attore che riceve il messaggio conosce il nome dell'attore che ha inviato la richiesta ed invia la risposta (questa viene poi deserializzata in una entity di risposta).
    Ogni bounded context possiede almeno il root actor, è colui che si occupa di lanciare gli attori che sono presenti in quel bounded context.
    - **routes** specificano le informazioni su come il microservizio può essere utilizzato dall'esterno, lo si fa costruendo un server con akkahttp.
    Questo espone delle uri accessibili attraverso le quali possono essere fatte richieste get, post, put o delete.
    Quando la route riceve richieste in entrata, viene fatto unmarshalling dei json per trasformarli in delle entity, si estraggono le informazioni per ottenere l'indirizzo del server a cui inviare il messaggio e lo si invia (ma come risposta o all'attore???). Gli invii di messaggi che vogliouno una risposta (una ask) generano un attore temporaneo per un certo timeout, se il messaggio torna indietro è corretto, altrimenti si ottiene una dead letter. Una ask è una future e quando è completa riceve il messaggio di risposta.
    


## Design nel dettaglio

### Microservizio "Users"
"Users" è il microservizio adibito alla gestione dei dati relativi agli utenti. 
Le business operations al suo interno sono:
- cliente che effettua login con email e password
- responsabile negozio che effettua login con email e password
- amministratore che effettua login con email e password

Questo microservizio non gestisce le sessioni di login.

Come ogni microservizio ha una serie di comunicazioni in entrata ed in uscita:
- comunicazioni in entrata: 
    - registrazione o de-registrazione di nuovi utenti (da applicazione)
    - modifica dei dati o della password di un utente (da applicazione)
    - verifica password (da applicazione e dashboard)
    - registrazione o de-registrazione di nuovi store manager (da dashboard)
    - modifica dei dati o della password di uno store manager (da dashboard)
    - modifica della password di un administration (da dashboard)
- comunicazioni in uscita:
    - notifica del cliente de-registrato (verso shopping)
    - notifica del cliente de-registrato (verso carts)
    - notifica del cliente de-registrato (verso payments)

Le operazioni di modifica password sono dei servizi (effettuano letture), le altre operazioni sono operazioni che vanno ad apportare modifiche ai dati gestiti all'interno del microservizio.

### Livello core
Questo bounded context è responsabile dei seguenti aggregates:
- user: è una generalizzazione dei vari tipi di utenti
- administration: è un'estensione di user e rappresenta un amministratore del sistema
- customer: è un'estensione di user e rappresente ogni cliente che utilizzerà l'appicazione
- store manager è un'estensione di user e rappresenta il manager del negozio

L'administration non può essere registrato come i customer e gli store manager ma deve essere messo hardcoded, lo storemanager è invece associato univocamente al proprio negozio.
Tutte e tre le estensioni di user hanno un servizio per poter criptare le password ed espongono un'interfaccia per poter essere create, eliminate ed aggiornate.

### Livello application
Per definire il comportamento di questo bounded context troviamo i seguenti attori:
- administration server actor: può gestire due tipologie di messaggi:
    - login: si verificano i dati e si da una risposta positiva in caso di dati di accesso corretti e negativa in caso contrario
    - update: si verificano i dati e viene data una risposta
- customer server actor: può gestire cinque tipologie di messaggi:
    - registrazione
    - deregistrazione
    - login
    - update dei dati
    - update della password
- store manager server actor: gestisce gli stessi tipi di messaggi di un attore di tipo customer, con la differenza di alcuni campi all'interno dei messaggi
- attore per il message broker: in questo bounded context abbiamo necessità di questo attore per poter rispondere agli eventi in uscita, informa che un utente si sia de-registrato. Questo attore viene utilizzato da administration, customer e storemanager.

### Microservizio "Items"
"Items" è il microservizio adibito alla gestione dei dati relativi agli prodotti.
Le business decision che riguardano questo bounded context sono:
- la possibilità di effettuare le operazioni con la dashboard dipende dal tipo di utente che le effettua
- l'eliminazione di una tipologia di prodotto non è possibile finché vi è associato almeno un prodotto in catalogo
- l'eliminazione di un prodotto in catalogo non è possibile finchè vi è associato almeno un prodotto
- l'eliminazione di un prodotto non è possibile finchè questo è parte dell'allestimento di almeno un negozio e parte del processo di acquisto di almeno un cliente

Come ogni microservizio ha una serie di comunicazioni in entrata ed in uscita:
- comunicazioni in entrata:
    - operazioni per effettuare azioni (creazione, rimozione o aggiornamento dei dati):
        - aggiungere e rimuovere di nuove categorie di prodotto (da dashboard)
        - aggiungere e rimuovere di nuovi prodotti in catalogo (da dashboard)
        - aggiungere di nuovi prodotti (da dashboard)
        - eliminare un prodotto (da dashboard)
    - operazioni per visualizzare dati:
        - visualizzare prodotti in catalogo (da dashboard e applicazione e shopping)
        - visualizzare prodotti restituiti (da dashboard)
        - visualizzare prodotti in catalogo sollevati (da dashboard)
        - visualizzare tipologia di prodotto (da dashboard e store)
    - eventi
        - notifica di prodotto rimesso a posto (dashboard)
        - notifica prodotto in catalogo rimesso a posto (dashboard)
        - notifica sollevamento prodotto in catalogo (shopping, store)
        - notifica aggiunta prodotto in carrello (shopping e cart)
        - notifica restituzione prodotto (store)
- comunicazioni in uscita:
    - visualizza presenza allestimenti con prodotto (store)
    - visualizza presenza processi d'acqusito con prodotto (shopping)


### Livello core
Questo bounded context è responsabile dei seguenti aggregati:
- item category: rappresenta una categoria di prodotto, ovvero un insieme di prodotti uguali
- catalog item: rappresenta i prodotti in catalogo di ogni negozio. possono essere di due tipi:
    - in place: rappresentano i prodotti in catalogo che sono al momento al proprio posto
    - lifted: rappresentano i prodotti in catalogo che sono al momento sollevati
- item: rappresenta il singolo prodotto, possono essere di tre tipi:
    - in place: rappresentano i prodotti sullo scaffale
    - in cart: rappresentano i prodotti nel carrello
    - returned: rappresentano i prodotti resi

L'aggregato item al suo interno utilizza il proprio catalog item che a sua volta utilizza item category.
Ogni aggregato espone tutte le sue funzionalità attraverso il proprio Repository.

### Livello application
Per definire il comportamento di questo bounded context troviamo i seguenti attori:
- item category actor: gestisce le operazioni relative alle categorie di prodotti, al suo interno vengono gestiti i messaggi che permettono di creare, rimuovere, manipolare o effettuare query sulle categorie di prodotti
- catalog item actor: gestisce le operazioni relative ai prodotti in catalogo,  al suo interno vengono gestiti i messaggi che permettono di creare, rimuovere, manipolare o effettuare query sui prodotti in catalogo
- item actor:  gestisce le operazioni relative ai prodotti,  al suo interno vengono gestiti i messaggi che permettono di creare, rimuovere, manipolare o effettuare query sui prodotti


### Microservizio "Carts"

### Microservizio "Stores"

### Microservizio "Shopping"

### Microservizio "Payments"


## Design nel dettaglio

