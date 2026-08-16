package cleii.turing;

// Sottoclasse di Stato per marcare in modo speciale gli stati terminali[cite: 1].
public class StatoFinale extends Stato {
	
	public StatoFinale(String nome) {
		// Richiama il costruttore della superclasse (Stato) passando il nome.
		// Serve a inizializzare correttamente la variabile 'nome' che risiede nella classe padre.
		super(nome); 
	}
	
	// Nota: non serve sovrascrivere equals/hashCode o toString,
	// perché li eredita già perfettamente funzionanti dalla classe Stato.
	// A runtime, il check (statoCorrente instanceof StatoFinale) ci permetterà di 
	// capire se fermare la computazione con successo.
}