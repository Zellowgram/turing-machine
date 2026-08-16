package test;

import cleii.turing.*;
import java.util.ArrayList;
import java.util.Arrays;

public class Main {

    public static void main(String[] args) {
        System.out.println("=================================================");
        System.out.println("   TEST 1: INVERSIONE DI UNA STRINGA BINARIA");
        System.out.println("=================================================");
        testInversioneBinaria();
        
        System.out.println("\n=================================================");
        System.out.println("   TEST 2: BLOCCO (Mancanza di istruzione)");
        System.out.println("=================================================");
        testBlocco();

        System.out.println("\n=================================================");
        System.out.println("   TEST 3: MOVIMENTO A SINISTRA (Indici negativi)");
        System.out.println("=================================================");
        testMovimentoSinistra();

        System.out.println("\n=================================================");
        System.out.println("   TEST 4: TIMEOUT (Max passi superato)");
        System.out.println("=================================================");
        testTimeout();

        System.out.println("\n=================================================");
        System.out.println("   TEST 5: DETERMINISMO (Rifiuto istruzioni doppie)");
        System.out.println("=================================================");
        testDeterminismo();
    }

    // Metodo di utilità per stampare la cronologia in modo uniforme
    private static void stampaCronologia(MacchinaDiTuring mdt) {
        ArrayList<Configurazione> evoluzione = mdt.getEvoluzione();
        if (evoluzione != null) {
            System.out.println("\nCronologia Evoluzione:");
            for (int i = 0; i < evoluzione.size(); i++) {
                System.out.println("Passo " + i + ": " + evoluzione.get(i));
            }
        } else {
            System.out.println("\nCronologia non disponibile (condizione: " + mdt.getCondizione() + ")");
        }
    }

    private static void testInversioneBinaria() {
        Stato q0 = new Stato("q0");
        StatoFinale qF = new StatoFinale("qF");
        ArrayList<Stato> stati = new ArrayList<>(Arrays.asList(q0, qF));

        MacchinaDiTuring mdt = new MacchinaDiTuring("1011", '_', stati, q0);
        mdt.aggiungiIstruzione(new Istruzione(q0, '1', q0, '0', (byte) 1));
        mdt.aggiungiIstruzione(new Istruzione(q0, '0', q0, '1', (byte) 1));
        mdt.aggiungiIstruzione(new Istruzione(q0, '_', qF, '_', (byte) 0));

        mdt.esegui();
        System.out.println("Condizione di uscita: " + mdt.getCondizione() + " (Atteso: 1)");
        stampaCronologia(mdt);
    }

    private static void testBlocco() {
        Stato q0 = new Stato("q0");
        ArrayList<Stato> stati = new ArrayList<>(Arrays.asList(q0));
        MacchinaDiTuring mdt = new MacchinaDiTuring("A", '_', stati, q0);
        
        mdt.esegui();
        System.out.println("Condizione di uscita: " + mdt.getCondizione() + " (Atteso: 2)");
        stampaCronologia(mdt);
    }

    private static void testMovimentoSinistra() {
        Stato q0 = new Stato("q0");
        StatoFinale qF = new StatoFinale("qF");
        ArrayList<Stato> stati = new ArrayList<>(Arrays.asList(q0, qF));
        
        MacchinaDiTuring mdt = new MacchinaDiTuring("1", '_', stati, q0, 100, 0);
        mdt.aggiungiIstruzione(new Istruzione(q0, '1', qF, 'X', (byte) -1));
        
        mdt.esegui();
        System.out.println("Condizione di uscita: " + mdt.getCondizione() + " (Atteso: 1)");
        stampaCronologia(mdt);
    }

    private static void testTimeout() {
        Stato q0 = new Stato("q0");
        ArrayList<Stato> stati = new ArrayList<>(Arrays.asList(q0));
        MacchinaDiTuring mdt = new MacchinaDiTuring("1", '_', stati, q0, 5, 0);
        
        mdt.aggiungiIstruzione(new Istruzione(q0, '1', q0, '1', (byte) 0));
        
        mdt.esegui();
        System.out.println("Condizione di uscita: " + mdt.getCondizione() + " (Atteso: 3)");
        stampaCronologia(mdt);
    }

    private static void testDeterminismo() {
        Stato q0 = new Stato("q0");
        Stato q1 = new Stato("q1");
        ArrayList<Stato> stati = new ArrayList<>(Arrays.asList(q0, q1));
        MacchinaDiTuring mdt = new MacchinaDiTuring("1", '_', stati, q0);
        
        mdt.aggiungiIstruzione(new Istruzione(q0, '1', q1, '0', (byte) 1));
        boolean raddoppio = mdt.aggiungiIstruzione(new Istruzione(q0, '1', q0, 'X', (byte) 1));
        
        System.out.println("Aggiunta seconda regola duplicata: " + raddoppio + " (Atteso: false)");
        // Qui non eseguiamo, quindi non stampiamo cronologia
    }
}