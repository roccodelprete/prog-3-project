# prog-3-project
Prog 3 project 2023-2024

# Sistema SICVE
Si vuole sviluppare un sistema SICVE (Sistema Informativo per il Controllo della Velocità) autostradale, detto anche Tutor (vedi sotto). Il Tutor è un sistema per la misurazione della velocità media dei veicoli.

Il Tutor è costituito da una serie di postazioni o sensori (Autovelox), che collegati ad un computer, oltre a svolgere le normali funzioni, operano il calcolo della velocità media.

Ove sia attivo un sistema Tutor (composto da una stazione di rilevazione detta “entrata” e da una stazione detta “uscita”), è possibile fare tre infrazioni che verranno intese come unica, il computer selezionerà la più grave e la invierà al comando di Polizia per la trascrizione.

Il superamento della velocità massima quando si transita sotto ai sensori è anch’esso sanzionato.

Qualora l’eccesso di velocità perduri tanto a lungo da coprire più tratte sorvegliate da diversi sistemi di tutor, potranno essere verbalizzate più di una multa per la ripetizione della stessa effrazione al codice stradale in comuni diversi fra loro.

Scrivere un programma per la gestione del sistema Tutor. Il sistema automaticamente riconosce gli autoveicoli che hanno commesso un’infrazione (istantanea o media) e invia un avviso alla Stazione di Polizia di competenza.

Il sistema deve prevedere l’accesso in modalità amministratore e in modalità
autoveicolo.

L’amministratore può effettuare le seguenti operazioni:

- inserire una nuova tratta autostradale controllata da Tutor
- modificare i parametri per il controllo di una tratta
- effettuare periodicamente statistiche sulle velocità per le singole tratte coperte dai Tutor

L’utente (autoveicolo) può effettuare le seguenti operazioni:
- entrare e uscire da una tratta coperta da Tutor
- richiedere di essere avvisato automaticamente dal sistema (e.g., sms) quando sta entrando in una tratta controllata dal Tutor

## Sistema SICVE
Per i dettagli di sviluppo vedere: https://it.wikipedia.org/wiki/SICVE

## Note di sviluppo
La prova d’esame richiede la progettazione e lo sviluppo della traccia proposta. Lo studente può scegliere di sviluppare il progetto nelle due modalità: Applicazione Web o programma standalone con supporto grafico.

Il progetto deve essere sviluppato secondo le seguenti linee:

- usare almeno due pattern per persona (almeno uno per chi sceglie la modalità Web Application) tra i design pattern noti;

- attenersi ai principi della programmazione SOLID;

- usare il linguaggio Java;

- inserire sufficienti commenti (anche per Javadoc) e annotazioni;

- gestione delle eccezioni;

- usare i file o database;

E' possibile costruire l'applicazione standalone con supporto grafico tramite l'utilizzo di strumenti per la realizzazione di interfacce grafiche presenti in molti IDE (GUI Designer in IntelliJ e WindowsBuilder in Eclipse) oppure utilizzare tools compatibili con JavaFx come Scene Builder (compatibile con gli IDE).

## Consegna progetto
Lo studente deve presentare una relazione sintetica (per chi usa latex è possibile scaricare un template dalla piattaforma e-learning). La relazione deve contenere:

- una breve descrizione dei requisiti del progetto;
- il diagramma UML delle classi;
- altri diagrammi se opportuni;
- parti rilevanti del codice sviluppato;

Per chi usa latex si consiglia di utilizzare la piattaforma Overleaf:
https://www.overleaf.com/

La consegna potrà avvenire tramite email a tutti i docenti, con in allegato un archiovio con tutto relazione, codice e presentazione. In alternativa è possibile utilizzare Microsoft Teams con le stesse modalità.
