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

    public Aliniamiento(Celula celulaOriginal) {
        nucleotidoGrande =celulaOriginal.getCelula().get(celulaOriginal.getIndexNucleotidoGrande());
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

    private void selectMetodo(Celula celulaOriginal){
        if (celulaOriginal.getTamañoNucleotidoGrande() == celulaOriginal.getTamañoNucleotidoPequeño()) {
            
        } else {
            for (int i = 0; i < 5; i++) {
                baseMax(celulaOriginal);
                baseMin(celulaOriginal);
            }
            if (candidatosGrandes.isEmpty()) {
                for (int i = 0; i < candidatos.size(); i++) {
                    this.crearCelulasPrueba(SerializationUtils.clone(nucleotidoGrande), candidatos.get(i));
                }
                
            }
            for (CelulaAliniada c : celulasAliniadas) {
                System.out.println("celula");
                for (Nucleotido n : c.getCelula()) {
                    System.out.println(n.getNucleotido());   
                }
            }
        }
    }
    
    private void baseMax(Celula celulaOriginal) {
        int diferencia = celulaOriginal.getTamañoNucleotidoGrande() - celulaOriginal.getTamañoNucleotidoGrande2();
        proporcion = (celulaOriginal.getIndexNucleotidoGrande()*30)/100;
        Nucleotido temp = new Nucleotido();
        temp = SerializationUtils.clone(celulaOriginal.getCelula().get(celulaOriginal.getIndexNucleotidoGrande2()));
        if(diferencia > proporcion){
            for (int i = 0; i < diferencia; i++) {
                //Generar un número entero aleatorio entre 1 y 100 (ambos inclusive)
                int index = random.nextInt(temp.getTamaño());
                temp.getNucleotido().add(index, '-');
                temp.setTamaño(temp.getNucleotido().size());
            }
            temp.setTamaño(temp.getNucleotido().size());
            candidatos.add(temp);
        }else{
            Nucleotido tempGrande = new Nucleotido();
            tempGrande = SerializationUtils.clone(nucleotidoGrande);
            int tamaño = proporcion + diferencia;
            for (int i = 0; i < tamaño; i++) {
                int index = random.nextInt(temp.getTamaño());
                temp.getNucleotido().add(index, '-');
                temp.setTamaño(temp.getNucleotido().size());
            }
            temp.setTamaño(temp.getNucleotido().size());
            candidatos.add(temp);
            
            
            for (int i = 0; i < proporcion; i++) {
                int index = random.nextInt(tempGrande.getTamaño());
                tempGrande.getNucleotido().add(index, '-');
                tempGrande.setTamaño(tempGrande.getNucleotido().size());
            }
            tempGrande.setTamaño(tempGrande.getNucleotido().size());
            candidatos.add(tempGrande);
        }
    }   

    private void baseMin(Celula celulaOriginal){
        Nucleotido temp = new Nucleotido();
        temp = SerializationUtils.clone(celulaOriginal.getCelula().get(celulaOriginal.getIndexNucleotidoPequeño()));
        int diferencia = celulaOriginal.getTamañoNucleotidoGrande() - celulaOriginal.getTamañoNucleotidoPequeño();
        if (candidatosGrandes.size() == 0) {
            for (int i = 0; i < diferencia; i++) {
                int index = random.nextInt(temp.getTamaño());
                temp.getNucleotido().add(index, '-');
                temp.setTamaño(temp.getNucleotido().size());
            }
            temp.setTamaño(temp.getNucleotido().size());
            candidatos.add(temp);
        }
        else{
            int tamaño = proporcion + diferencia;
             for (int i = 0; i < tamaño; i++) {
                int index = random.nextInt(temp.getTamaño());
                temp.getNucleotido().add(index, '-');
                temp.setTamaño(temp.getNucleotido().size());
            }
            temp.setTamaño(temp.getNucleotido().size());
            candidatos.add(temp);
        }
    }
    
    private void crearCelulasPrueba(Nucleotido n1, Nucleotido n2){
        CelulaAliniada celula = new CelulaAliniada();
        celula.agregarNucleotido(n1);
        celula.agregarNucleotido(n2);
        celulasAliniadas.add(celula);
    }
    
    private void mutacion(Celula celulaOriginal){
        for(Nucleotido nucleotido : celulaOriginal.getCelula()){
            
        }
    }
}
