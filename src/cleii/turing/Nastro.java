package cleii.turing;

import java.util.HashMap;
import java.util.Objects;

public class Nastro {
	
	private final HashMap<Integer, Character> nastro;
	private final char beta;
	
	// Costruttore che inizializza il nastro a partire da una stringa[cite: 1]
	public Nastro(String s, char beta) {
		this.beta = beta;
		
		// HashMap per mappare le posizioni del nastro. 
		// La chiave è l'indice intero (che può essere negativo, nullo o positivo), il valore è il carattere.[cite: 1]
		// NOTA BENE: Gli spazi normali (es. ' ') vengono salvati normalmente. 
		// Il carattere 'beta' invece non serve che venga inserito, risparmiando memoria 
		// e permettendoci di simulare un nastro potenzialmente infinito in entrambe le direzioni[cite: 1].
		this.nastro = new HashMap<>();
		if (s != null) {
			for (int i = 0; i < s.length(); i++) {
				this.nastro.put(i, s.charAt(i));
			}
		}
	}
	
	// Costruttore di copia profonda (clone): crea un nuovo nastro con una mappa identica in memoria[cite: 1].
	public Nastro(Nastro n) {
		this.beta = n.beta;
		// Clonando la mappa, separiamo le aree di memoria: le modifiche al nuovo nastro non intaccheranno il vecchio.
		this.nastro = new HashMap<>(n.nastro);
	}
	
	// Restituisce il carattere corrispondente all'indice i[cite: 1]
	public char get(int i) {
		// La vera magia per gestire l'infinito: se l'indice 'i' non è presente nella mappa 
		// (perché la testina visita una casella vergine o precedentemente svuotata),
		// getOrDefault restituisce il carattere di default, ovvero 'beta'.
		return nastro.getOrDefault(i, beta);
	}
	
	// Scrive in posizione i il carattere c[cite: 1]
	public void set(int i, char c) {
		if (c == beta) {
			// Ottimizzazione memoria: se il carattere da scrivere è proprio 'beta' (la casella vuota),
			// rimuoviamo fisicamente la chiave dalla mappa (se c'era).
			// Ci penserà poi il metodo get() a restituire 'beta' quando interrogato.
			nastro.remove(i); 
		} else {
			// Inseriamo o sovrascriviamo un carattere reale nella posizione i.
			nastro.put(i, c);
		}
	}
	
	public char getBeta() {
		return beta;
	}
	
	@Override 
	// Sovrascriviamo toString() per restituire l'esatta sequenza di caratteri utile[cite: 1].
	public String toString() {
		// Se la mappa è vuota, il nastro contiene teoricamente solo infiniti 'beta'.
		// Usiamo String.valueOf(beta) per convertire il singolo carattere primitivo (char) 
		// in un vero e proprio oggetto String, poiché la firma del metodo lo richiede.
		if (nastro.isEmpty()) {
			return String.valueOf(beta); 
		}
		
		// Impostiamo i confini della nostra ricerca agli estremi dei numeri interi
		int minIndex = Integer.MAX_VALUE; 
		int maxIndex = Integer.MIN_VALUE; 
		
		// Scorriamo tutte le posizioni memorizzate per trovare la porzione "viva" del nastro,
		// ovvero gli indici estremi che contengono lettere diverse da 'beta'.
		for (Integer index : nastro.keySet()) {
			if (nastro.get(index) != beta) {
				if (index < minIndex) minIndex = index;
				if (index > maxIndex) maxIndex = index;
			}
		}
		
		// Controllo di fallback di sicurezza: se dopo il ciclo minIndex è rimasto identico al valore iniziale,
		// significa che la mappa non conteneva nessun carattere "utile". Il nastro equivale a un nastro vuoto.
		if (minIndex == Integer.MAX_VALUE) {
			return String.valueOf(beta); 
		} 
		
		// Costruiamo la stringa finale tagliando gli infiniti vuoti alle due estremità.
		// Facendo un ciclo continuo da minIndex a maxIndex e usando il metodo get(i),
		// ci assicuriamo di stampare anche eventuali 'beta' intermedi tra due lettere valide!
		StringBuilder sb = new StringBuilder();
		for (int i = minIndex; i <= maxIndex; i++) {
			sb.append(get(i));
		}
		return sb.toString();
	}
	
	@Override 
	// Sovrascriviamo equals() perché quello di default (ereditato da Object) confronta solo gli indirizzi RAM.
	public boolean equals(Object o) {
		// 1. Ottimizzazione: se confrontiamo l'oggetto con sé stesso (stesso indirizzo RAM), sono uguali.
		if (this == o) return true; 
		// 2. Controllo di sicurezza: se 'o' è nullo o è di una classe completamente diversa, sono diversi.
		if (o == null || getClass() != o.getClass()) return false; 
		
		// 3. Cast sicuro: ora sappiamo che 'o' è un Nastro e possiamo esplorare le sue proprietà.
		Nastro nastro1 = (Nastro) o; 
		
		// Due nastri sono logicamente "uguali" se utilizzano lo stesso simbolo per il vuoto (beta)
		// E se le loro due HashMap interne contengono gli stessi caratteri negli stessi indici.
		return this.beta == nastro1.beta && Objects.equals(this.nastro, nastro1.nastro);
	}
	
	@Override 
	// Sovrascriviamo hashCode() per rispettare il contratto fondamentale di Java:
	// due oggetti che equals() considera "uguali", DEVONO restituire lo stesso hashCode.
	public int hashCode() {
		// Generiamo un hash (un intero identificativo o "codice fiscale") basato NON sulla posizione in memoria,
		// ma esclusivamente sul contenuto logico: il carattere beta e il contenuto della HashMap.
		// In questo modo, un Nastro originale e un suo clone produrranno lo stesso identico codice,
		// comportandosi coerentemente se inseriti come chiavi in altre HashMap o in HashSet.
		return Objects.hash(nastro, beta);
	}
}