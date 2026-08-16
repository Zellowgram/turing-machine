package cleii.turing;

import java.util.Objects;

// Classe pubblica che rappresenta lo stato (oggetti immutabili)[cite: 1].
public class Stato {
	
	// Essendo immutabile, il campo è final. Una volta creato, il nome non cambia mai.
	private final String nome;
	
	public Stato(String nome) {
		this.nome = nome;
	}
	
	public String getNome() {
		return nome;
	}
	
	@Override
	// Serve a considerare "uguali" due stati se hanno lo stesso nome testuale,
	// indipendentemente da dove sono stati allocati in memoria.
	// Fondamentale per i metodi come ArrayList.contains() usati in MacchinaDiTuring.
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Stato stato = (Stato) o;
		return Objects.equals(nome, stato.nome);
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(nome);
	}
	
	@Override
	// Utile per restituire direttamente il nome quando stampiamo la Configurazione.
	public String toString() {
		return nome;
	}
}