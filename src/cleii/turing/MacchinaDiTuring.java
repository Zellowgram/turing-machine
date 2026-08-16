package cleii.turing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

// Il "motore" del progetto. Assembla nastro, stati e regole per eseguire la computazione.
public class MacchinaDiTuring {
	
	// Componenti fondamentali della macchina
	private final Nastro nastro;
	private final ArrayList<Stato> stati;
	private final Stato statoIniziale;
	private final int maxLunghezzaEvoluzione; // Limite di passi per evitare loop infiniti
	private int posTestina;
	
	// Struttura dati ottimizzata (O(1)) per cercare l'istruzione da applicare.
	// La chiave è la coppia (Stato corrente, Carattere letto), il valore è (Nuovo stato, Nuovo carattere, Movimento).
	private final HashMap<ChiaveIstruzione, ValoreIstruzione> istruzioniMappa;
	
	// Lista di supporto per memorizzare le istruzioni nell'ordine in cui vengono inserite
	// (utile esclusivamente per stamparle poi nel toString() in modo ordinato).
	private final ArrayList<Istruzione> listaIstruzioni;
	
	// Memorizza l'intera cronologia (le "fotografie") della macchina passo dopo passo.
	private ArrayList<Configurazione> evoluzione;
	
	// 0 = non eseguita, 1 = ok (stato finale), 2 = blocco (no istruzione), 3 = max passi superato
	private int condizione; 
	// Flag di sicurezza per impedire di aggiungere regole a macchina già avviata
	private boolean eseguita;
	
	// Costruttore completo
	public MacchinaDiTuring(String input, char beta, ArrayList<Stato> stati,
			Stato statoIniziale, int maxLunghezzaEvoluzione, int posInizialeTestina) {

		this.nastro = new Nastro(input, beta);
		this.stati = new ArrayList<>(stati);
		
		// Controllo di coerenza: se lo stato iniziale non è stato inserito nella lista 'stati', lo aggiungiamo noi
		if (!this.stati.contains(statoIniziale)) {
			this.stati.add(statoIniziale);
		}
		
		this.statoIniziale = statoIniziale;
		this.maxLunghezzaEvoluzione = maxLunghezzaEvoluzione;
		this.posTestina = posInizialeTestina;
		
		this.istruzioniMappa = new HashMap<>();
		this.listaIstruzioni = new ArrayList<>();
		this.condizione = 0;
		this.eseguita = false;
	}
	
	// Costruttore "breve" con valori di default (5000 passi massimi, testina all'indice 0)
	public MacchinaDiTuring(String input, char beta, ArrayList<Stato> stati, Stato statoIniziale) {
        this(input, beta, stati, statoIniziale, 5000, 0);
    }
	
	// Aggiunge una nuova regola di transizione alla macchina
	public boolean aggiungiIstruzione(Istruzione i) {
		// Controlli rigorosi per rispettare la teoria delle MdT e la traccia del progetto:
        if (eseguita) return false; // Non si aggiungono regole a motore già usato
        if (i.getS() instanceof StatoFinale) return false; // Non ci sono transizioni in uscita da stati finali
        if (!stati.contains(i.getS())) return false;       // 's' deve far parte degli stati noti della macchina
        if (!stati.contains(i.getT())) return false;       // 't' deve appartenere agli stati noti
        if (i.getM() < -1 || i.getM() > 1) return false;   // Il movimento deve essere valido (-1, 0, 1)

        // Creiamo la chiave per la mappa usando la nostra classe privata annidata
        ChiaveIstruzione chiave = new ChiaveIstruzione(i.getS(), i.getA());
        
        // La nostra MdT è Deterministica: per una data coppia (Stato, Carattere) può esistere UNA sola regola
        if (istruzioniMappa.containsKey(chiave)) return false; 

		// Salviamo l'istruzione scompattata nella mappa per l'esecuzione veloce
        istruzioniMappa.put(chiave, new ValoreIstruzione(i.getT(), i.getB(), i.getM()));
        // E la salviamo intera nella lista per poterla stampare in seguito
        listaIstruzioni.add(i);
        
        return true;
    }
	
	// Avvia la computazione
	public void esegui() {
        if (eseguita) return; // Si può eseguire il nastro una sola volta
        eseguita = true;
        evoluzione = new ArrayList<>();

        Stato statoCorrente = statoIniziale;
        
        // Registra la configurazione iniziale (passo 0) prima di fare qualsiasi movimento
        evoluzione.add(new Configurazione(statoCorrente, nastro, posTestina));

        int passi = 0;
        // Ciclo principale di esecuzione: si ferma se raggiunge il limite di sicurezza
        while (passi < maxLunghezzaEvoluzione) {
            
            // CONDIZIONE 1: Se entriamo in uno stato finale, la computazione termina con successo
            if (statoCorrente instanceof StatoFinale) {
                condizione = 1;
                return;
            }

            // Guardiamo cosa c'è scritto sul nastro alla posizione attuale della testina
            char carattereLetto = nastro.get(posTestina);
            // Prepariamo la chiave di ricerca per la mappa
            ChiaveIstruzione chiave = new ChiaveIstruzione(statoCorrente, carattereLetto);

            // CONDIZIONE 2: Blocco. Se non esiste una regola per lo stato corrente e il carattere letto, la macchina si ferma.
            if (!istruzioniMappa.containsKey(chiave)) {
                condizione = 2;
                return;
            }

            // APPLICAZIONE DELL'ISTRUZIONE: estraiamo la tripla (t, b, m)
            ValoreIstruzione val = istruzioniMappa.get(chiave);
            
            nastro.set(posTestina, val.getB()); // 1. Scrive il nuovo carattere sul nastro (sovrascrivendo)
            posTestina += val.getM();           // 2. Muove la testina (a sinistra, destra, o ferma)
            statoCorrente = val.getT();         // 3. Cambia lo stato interno della macchina

            // Salva la nuova "fotografia" della macchina dopo aver applicato il passo
            evoluzione.add(new Configurazione(statoCorrente, nastro, posTestina));
            passi++;
        }

        // CONDIZIONE 3: Se usciamo dal while significa che abbiamo superato i passi massimi.
        // Facciamo un ultimo controllo nel caso fortuito in cui all'ultimo passo esatto sia giunta in uno stato finale.
        if (statoCorrente instanceof StatoFinale) {
            condizione = 1; 
        } else {
            condizione = 3; // Timeout per loop infinito
        }
    }

	// Restituisce la cronologia solo se la macchina è stata eseguita
    public ArrayList<Configurazione> getEvoluzione() {
        return (condizione > 0) ? evoluzione : null;
    }

    public int getCondizione() { return condizione; }
    public Nastro getNastro() { return nastro; }

    @Override
    // Genera un recap testuale completo includendo definizione, istruzioni, condizione ed evoluzione
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== MACCHINA DI TURING ===\n");
        sb.append("Condizione attuale: ").append(condizione).append("\n");
        sb.append("Stato Iniziale: ").append(statoIniziale).append("\n");
        sb.append("Istruzioni registrate:\n");
        for (Istruzione i : listaIstruzioni) {
            sb.append("  ").append(i.toString()).append("\n");
        }
        if (condizione > 0 && evoluzione != null) {
            sb.append("\nCronologia Evoluzione:\n");
            for (int j = 0; j < evoluzione.size(); j++) {
                sb.append("Passo ").append(j).append(": ").append(evoluzione.get(j)).append("\n");
            }
        }
        return sb.toString();
    }


    // ==========================================
    // CLASSI ANNIDATE PRIVATE DI SUPPORTO
    // ==========================================

    /**
     * Rappresenta la coppia (Stato corrente, Carattere letto).
     * 
     * PERCHÉ PRIVATE: Rendendola private all'interno di MacchinaDiTuring, la nascondiamo 
     * completamente al mondo esterno. Solo MacchinaDiTuring sa che esiste.
     * PERCHÉ STATIC: Una classe annidata "static" non ha un legame invisibile con l'istanza 
     * della classe esterna. Dato che questa chiave serve solo a contenere due dati (s, a) 
     * e non deve accedere alle variabili di MacchinaDiTuring (come nastro o posTestina), 
     * dichiararla static fa risparmiare memoria ed è concettualmente più pulito.
     */
    private static class ChiaveIstruzione {
        
        private final Stato s; // Lo stato in cui si trova la macchina
        private final char a;  // Il carattere letto sul nastro
        
        public ChiaveIstruzione(Stato s, char a) {
            this.s = s;
            this.a = a;
        }
        
        @Override
        // Sovrascriviamo equals per istruire la HashMap: due chiavi sono logiacamente uguali se 
        // hanno lo stesso stato e lo stesso carattere, ignorando il loro indirizzo di memoria.
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            ChiaveIstruzione that = (ChiaveIstruzione) o;
            return a == that.a && Objects.equals(s, that.s);
        }
        
        @Override
        // Generiamo il "codice fiscale" basato sui valori reali. Indispensabile 
        // affinché la HashMap di MacchinaDiTuring trovi istantaneamente l'istruzione salvata.
        public int hashCode() {
            return Objects.hash(s, a);
        }
    }

    /**
     * Rappresenta la tripla (Nuovo stato, Carattere da scrivere, Movimento).
     * Costituisce il risultato (valore) restituito dalla HashMap quando interpellata 
     * con una specifica ChiaveIstruzione.
     * 
     * Anche questa è 'private static' per garantire massimo incapsulamento 
     * e indipendenza dalle variabili di istanza della MacchinaDiTuring.
     */
    private static class ValoreIstruzione {
        
        private final Stato t; // Lo stato di destinazione in cui entrerà la macchina
        private final char b;  // Il nuovo carattere che verrà sovrascritto sul nastro
        private final byte m;  // Direzione del movimento: -1 (sinistra), 0 (fermo), 1 (destra)
        
        public ValoreIstruzione(Stato t, char b, byte m) {
            this.t = t;
            this.b = b;
            this.m = m;
        }
        
        // Semplici getter per permettere al metodo esegui() di leggere i valori 
        // per aggiornare il nastro, la posizione e lo stato della macchina.
        public Stato getT() { return t; }
        public char getB() { return b; }
        public byte getM() { return m; }
    }
}