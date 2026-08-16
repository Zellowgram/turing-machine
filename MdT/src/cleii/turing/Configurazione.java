package cleii.turing;

import java.util.Objects;

// Classe pubblica che rappresenta una "fotografia" istantanea della MdT in un certo momento[cite: 1].
public class Configurazione {
	
	private final Stato s;
	private final Nastro n; 
	private final int posTestina;
	
	public Configurazione(Stato s, Nastro n, int posTestina) {
		this.s = s;
		// CHIAMATA FONDAMENTALE: Usiamo il costruttore di copia profonda per salvare uno "storico".
		// Se non clonassimo il nastro, tutte le configurazioni punterebbero allo stesso nastro in evoluzione
		// e alla fine avremmo una lista con N configurazioni tutte mostranti il risultato finale![cite: 1]
		this.n = new Nastro(n);
		this.posTestina = posTestina;
	}
	
	public Stato getStato() { return s; }
	public Nastro getNastro() { return n; }
	public int getPosTestina() { return posTestina; }
	
	@Override
	public String toString() {
		return "[Stato: " + s +", Nastro: " + n.toString() + ", Testina: " + posTestina + "]";
	}
	
	@Override
	// Come visto in precedenza, l'uguaglianza si basa su posizione, stato (tramite il suo equals)
	// e nastro (tramite il profondo equals della HashMap in Nastro.java).
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Configurazione that = (Configurazione) o;
        return this.posTestina == that.posTestina && 
               Objects.equals(this.s, that.s) && 
               Objects.equals(this.n, that.n);
    }
	
	@Override
	public int hashCode() {
		return Objects.hash(s, n, posTestina);
	}
}