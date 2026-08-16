package cleii.turing;

import java.util.Objects;

// Classe pubblica che rappresenta una istruzione della MdT (oggetti immutabili)[cite: 1].
// Modella matematicamente la funzione di transizione: (s, a) -> (t, b, m)
public class Istruzione {
	
	private final Stato s; // Stato di partenza
	private final char a;  // Carattere letto sotto la testina
	private final Stato t; // Stato di arrivo (nuovo stato)
	private final char b;  // Nuovo carattere da scrivere sul nastro (sovrascrive 'a')
	private final byte m;  // Movimento: -1 (Sinistra), 0 (Fermo), 1 (Destra)[cite: 1]

	// Costruttore che crea la quintupla dell'istruzione[cite: 1].
	public Istruzione(Stato s, char a, Stato t, char b, byte m) {
		this.s = s;
		this.a = a;
		this.t = t;
		this.b = b;
		this.m = m;
	}
	
	public Stato getS() { return s; }
	public char getA() { return a; }
	public Stato getT() { return t; }
	public char getB() { return b; }
	public byte getM() { return m; }
	
	@Override
	// Due istruzioni sono identiche se tutte e 5 le loro componenti coincidono.
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Istruzione that = (Istruzione) o;
		return a == that.a && b == that.b && m == that.m && 
		       Objects.equals(s, that.s) && Objects.equals(t, that.t);
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(s, a, t, b, m);
	}
	
	@Override
	// Formattazione leggibile dell'istruzione, utile per la stampa della Macchina.
	public String toString() {
		return "(" + s + ", " + a + " -> " + t + ", " + b + ", " + m + ")";
	}
}