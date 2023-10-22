package com.mycompany.alinamientomultiple;

import java.security.SecureRandom;
import java.util.ArrayList;
import org.apache.commons.lang3.SerializationUtils;

/**
 *
 * @author Erick Toledo
 */
public class Aliniamiento {

    private SecureRandom random = new SecureRandom();
    private ArrayList<Nucleotido> candidatosGrandes = new ArrayList();
    private ArrayList<Nucleotido> candidatos = new ArrayList();
    private ArrayList<CelulaAliniada> celulasAliniadas = new ArrayList();
    private Nucleotido nucleotidoGrande;
    private int proporcion;
    private boolean porDiferencia = true;

    public Aliniamiento(Celula celulaOriginal) {
        nucleotidoGrande = celulaOriginal.getCelula().get(celulaOriginal.getIndexNucleotidoGrande());
        switch (celulaOriginal.getNumNucleotidos()) {
            case 0:
                System.out.println("La celula no tiene secuencias!");
                break;
            case 1:
                System.out.println("No es posible hacer un aliniamiento con solo una secuencia!");
                break;
            default:
                selectMetodo(celulaOriginal);
                break;
        }
    }

    private void selectMetodo(Celula celulaOriginal) {
        for (int i = 0; i < 5; i++) {
            baseMax(celulaOriginal);
            baseMin(celulaOriginal);
        }
        if (porDiferencia) {
            for (int i = 0; i < candidatos.size(); i++) {
                this.crearCelulasPrueba(SerializationUtils.clone(nucleotidoGrande), SerializationUtils.clone(candidatos.get(i)));
            }
        } else {
            for (int i = 0; i < candidatosGrandes.size(); i++) {
                for (int j = 0; j < candidatos.size(); j++) {
//                    System.out.println("Candidatos grande:\n"+candidatosGrandes.get(i).getNucleotido());
//                    System.out.println("Candidatos:\n"+candidatos.get(j).getNucleotido());
                    this.crearCelulasPrueba(SerializationUtils.clone(candidatosGrandes.get(i)), SerializationUtils.clone(candidatos.get(j)));
                }
//                for (CelulaAliniada c : celulasAliniadas) {
//                    System.out.println("Celula Aliniada: ");
//                    for (Nucleotido n : c.getCelula()) {
//                        System.out.println(n.getNucleotido());
//                    }
//                }
            }
        }
        
//        System.out.println("candidatos");
//        for (Nucleotido n: candidatos) {
//            System.out.println(n.getEncabezado());
//            System.out.println(n.getNucleotido());
//        }
//        System.out.println("candidatos granades");
//        for (Nucleotido n : candidatosGrandes) {
//            System.out.println(n.getEncabezado());
//            System.out.println(n.getNucleotido());
//        }
//
        for (CelulaAliniada c : celulasAliniadas) {
            c.mapiarCelula();
            c.setCalificacion();
            System.out.println("celula Calificadas");
            System.out.println("calificacion: " + c.getCalificacion());
            System.out.println("# col Aliniadas: "+c.getNumColAliniadas());
            System.out.println("# gabs: "+c.getNumGabs());
            for (Nucleotido n : c.getCelula()) {
                System.out.println(n.getNucleotido());
            }
        }
    }

    private void baseMax(Celula celulaOriginal) {
        SecureRandom r = new SecureRandom();
        int diferencia = celulaOriginal.getTamañoNucleotidoGrande() - celulaOriginal.getTamañoNucleotidoGrande2();
        proporcion = (celulaOriginal.getIndexNucleotidoGrande() * 30) / 100;
        Nucleotido temp = new Nucleotido();
        temp = SerializationUtils.clone(celulaOriginal.getCelula().get(celulaOriginal.getIndexNucleotidoGrande2()));
        if (diferencia > proporcion && porDiferencia) {
            for (int i = 0; i < diferencia; i++) {
                //Generar un número entero aleatorio entre 1 y 100 (ambos inclusive)
                int index = r.nextInt(temp.getTamaño());
                temp.getNucleotido().add(index, '-');
                temp.setTamaño(temp.getNucleotido().size());
            }
            temp.setTamaño(temp.getNucleotido().size());
            candidatos.add(temp);
        } else {
            porDiferencia = false;
            int tamaño = proporcion - diferencia;
            tamaño += nucleotidoGrande.getTamaño();
            Nucleotido tempGrande = new Nucleotido();
            tempGrande = SerializationUtils.clone(nucleotidoGrande);
            
            for (int i = 0; i < tamaño; i++) {
                int index = random.nextInt(temp.getTamaño());
                temp.getNucleotido().add(index, '-');
            }
            
            temp.setTamaño(temp.getNucleotido().size());
            candidatos.add(temp);

            for (int i = 0; i < tamaño; i++) {
                int index = random.nextInt(tempGrande.getTamaño());
                tempGrande.getNucleotido().add(index, '-');
            }
            
            tempGrande.setTamaño(tempGrande.getNucleotido().size());
            candidatosGrandes.add(tempGrande);
        }
    }

    private void baseMin(Celula celulaOriginal) {
        SecureRandom r = new SecureRandom();
        Nucleotido temp = new Nucleotido();
        temp = SerializationUtils.clone(celulaOriginal.getCelula().get(celulaOriginal.getIndexNucleotidoPequeño()));
        int diferencia = celulaOriginal.getTamañoNucleotidoGrande() - celulaOriginal.getTamañoNucleotidoPequeño();
        
        if (porDiferencia) {
            for (int i = 0; i < diferencia; i++) {
                int index = r.nextInt(temp.getTamaño());
                temp.getNucleotido().add(index, '-');
                temp.setTamaño(temp.getNucleotido().size());
            }
            temp.setTamaño(temp.getNucleotido().size());
            candidatos.add(temp);
        } 
        else {
            int tamaño = candidatosGrandes.get(0).getTamaño()- temp.getTamaño();
            for (int i = 0; i < tamaño; i++) {
                int index = random.nextInt(temp.getTamaño());
                temp.getNucleotido().add(index, '-');
                temp.setTamaño(temp.getNucleotido().size());
            }
            temp.setTamaño(temp.getNucleotido().size());
            candidatos.add(temp);
        }
    }

    private void crearCelulasPrueba(Nucleotido n1, Nucleotido n2) {
        CelulaAliniada celula = new CelulaAliniada();
        celula.agregarNucleotido(n1);
        celula.agregarNucleotido(n2);
//        System.out.println("celula en metodo\n");
//        for (Nucleotido n : celula.getCelula()) {
//            System.out.println(n.getNucleotido());
//        }
        celulasAliniadas.add(celula);
    }

    private void mutacion(Celula celulaOriginal) {
        for (Nucleotido nucleotido : celulaOriginal.getCelula()) {

        }
    }
}
