# Turing Machine

## English

An educational Java implementation of deterministic, single-tape Turing machines. The project follows the specification for the `cleii.turing` package and provides the data structures required to define and execute a machine.

### Features

- A potentially infinite tape that supports negative and positive positions.
- States and final states represented by immutable objects.
- Five-field transition instructions: current state, read symbol, next state, written symbol and head movement.
- Machine configurations containing the current state, a tape snapshot and the head position.
- Deterministic instruction lookup through a state/symbol pair.
- Execution with a configurable maximum number of steps to prevent non-terminating runs.
- Execution conditions for not started, final-state termination, missing-transition termination and step-limit termination.
- Access to the execution history and the current tape.

### Main classes

- `Nastro`: sparse tape backed by a `HashMap`, with support for negative indexes.
- `Stato` and `StatoFinale`: normal and final machine states.
- `Istruzione`: an immutable transition instruction.
- `Configurazione`: a snapshot of a machine during execution.
- `MacchinaDiTuring`: machine definition, instruction management and execution engine.

### Project structure

```text
src/
├── module-info.java
├── cleii/turing/
│   ├── Configurazione.java
│   ├── Istruzione.java
│   ├── MacchinaDiTuring.java
│   ├── Nastro.java
│   ├── Stato.java
│   └── StatoFinale.java
└── test/
    └── Main.java
```

### Requirements

- Java Development Kit (JDK) 21 or newer.
- The implementation uses standard `java.lang` classes together with `ArrayList` and `HashMap`.

### Running the examples

Run the `test.Main` class from a Java IDE. The example exercises string transformation, missing transitions, movement to negative tape positions, timeouts and deterministic instruction validation.

Build output and IDE metadata are intentionally excluded from version control.

## Italiano

Implementazione didattica Java di macchine di Turing deterministiche a nastro singolo. Il progetto segue la specifica del package `cleii.turing` e fornisce le strutture necessarie per definire ed eseguire una macchina.

### Funzionalita

- Un nastro potenzialmente infinito, con supporto per posizioni negative e positive.
- Stati e stati finali rappresentati da oggetti immutabili.
- Istruzioni di transizione a cinque campi: stato corrente, simbolo letto, stato successivo, simbolo scritto e movimento della testina.
- Configurazioni contenenti stato corrente, copia del nastro e posizione della testina.
- Ricerca deterministica delle istruzioni tramite coppia stato/simbolo.
- Esecuzione con numero massimo di passi configurabile per evitare esecuzioni infinite.
- Condizioni di esecuzione per programma non avviato, arresto in stato finale, arresto per istruzione mancante e raggiungimento del limite di passi.
- Accesso alla cronologia dell'esecuzione e al nastro corrente.

### Classi principali

- `Nastro`: nastro sparso basato su `HashMap`, con supporto per indici negativi.
- `Stato` e `StatoFinale`: stati normali e finali della macchina.
- `Istruzione`: istruzione di transizione immutabile.
- `Configurazione`: istantanea della macchina durante l'esecuzione.
- `MacchinaDiTuring`: definizione della macchina, gestione delle istruzioni e motore di esecuzione.

### Struttura del progetto

```text
src/
├── module-info.java
├── cleii/turing/
│   ├── Configurazione.java
│   ├── Istruzione.java
│   ├── MacchinaDiTuring.java
│   ├── Nastro.java
│   ├── Stato.java
│   └── StatoFinale.java
└── test/
    └── Main.java
```

### Requisiti

- Java Development Kit (JDK) 21 o superiore.
- L'implementazione usa le classi standard di `java.lang` insieme a `ArrayList` e `HashMap`.

### Esecuzione degli esempi

Eseguire la classe `test.Main` da un IDE Java. Gli esempi verificano l'inversione di una stringa, l'assenza di transizioni, il movimento verso posizioni negative, il timeout e il controllo del determinismo delle istruzioni.

I file generati dalla compilazione e i metadati dell'IDE sono esclusi dal versionamento.
