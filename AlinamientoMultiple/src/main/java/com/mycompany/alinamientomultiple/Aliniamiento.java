package com.mycompany.alinamientomultiple;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import org.apache.commons.lang3.SerializationUtils;

/**
 * *
 *
 * @author Erick Toledo
 */
public class Aliniamiento {

    private SecureRandom random = new SecureRandom();
    private List<Nucleotido> candidatosGrandes = new ArrayList<>();
    private List<Nucleotido> candidatos = new ArrayList<>();
    private List<CelulaAliniada> celulasAliniadas = new ArrayList<>();
    private Nucleotido nucleotidoGrande;
    private int proporcion;
    private boolean porDiferencia = true;

    /**
     * Inicia un proceso de alineación basado en diferentes condiciones en la
     * célula original.
     *
     * @param celulaOriginal
     */
    public Aliniamiento(Celula celulaOriginal) {
        nucleotidoGrande = celulaOriginal.getCelula().get(celulaOriginal.getIndexNucleotidoGrande());
        int numNucleotidos = celulaOriginal.getNumNucleotidos();
        switch (numNucleotidos) {
            case 0:
                System.out.println("La celula no tiene secuencias!");
                break;
            case 1:
                System.out.println("No es posible hacer un aliniamiento con solo una secuencia!");
                break;
            default:
                try {
                    selectMetodo(celulaOriginal);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    e.printStackTrace();
                }
                break;
        }
    }

    /**
     * Selecciona un método de alineación específico en función de ciertos
     * cálculos y condiciones en la célula original.
     */
    private void selectMetodo(Celula celulaOriginal) {
        int repeticiones = 100;
        int numCandidatos = 10;
        int diferencia = celulaOriginal.getTamañoNucleotidoGrande() - celulaOriginal.getTamañoNucleotidoGrande2();
        proporcion = (celulaOriginal.getTamañoNucleotidoGrande() * 30) / 100;

        if (diferencia > proporcion) {
            for (int i = 0; i < repeticiones * repeticiones; i++) {
                baseMax(celulaOriginal);
            }
            for (int i = 0; i < repeticiones * repeticiones; i++) {
                baseMin(celulaOriginal);
            }
        } else {
            porDiferencia = false;
            for (int i = 0; i < repeticiones; i++) {
                baseMax(celulaOriginal);
            }
            for (int i = 0; i < repeticiones; i++) {
                baseMin(celulaOriginal);
            }
        }

        if (porDiferencia) {
            int candidatosSize = candidatos.size();
            int halfCandidatosSize = candidatosSize / 2;
            for (int i = 0; i < halfCandidatosSize; i++) {
                this.crearCelulasPrueba(SerializationUtils.clone(nucleotidoGrande), SerializationUtils.clone(candidatos.get(i)), celulaOriginal.getIndexNucleotidoGrande2());
            }
            for (int i = halfCandidatosSize; i < candidatosSize; i++) {
                this.crearCelulasPrueba(SerializationUtils.clone(nucleotidoGrande), SerializationUtils.clone(candidatos.get(i)), celulaOriginal.getIndexNucleotidoPequeño());
            }
        } else {
            for (Nucleotido candidatoGrande : candidatosGrandes) {
                int halfCandidatosSize = candidatos.size() / 2;
                for (int j = 0; j < halfCandidatosSize; j++) {
                    this.crearCelulasPrueba(SerializationUtils.clone(candidatoGrande), SerializationUtils.clone(candidatos.get(j)), celulaOriginal.getIndexNucleotidoGrande2());
                }
                for (int j = halfCandidatosSize; j < candidatos.size(); j++) {
                    this.crearCelulasPrueba(SerializationUtils.clone(candidatoGrande), SerializationUtils.clone(candidatos.get(j)), celulaOriginal.getIndexNucleotidoPequeño());
                }
            }
        }

        for (CelulaAliniada c : celulasAliniadas) {
            c.mapiarCelula();
            c.setCalificacion();
        }
        seleccionCalificacion(numCandidatos);
//        ArrayList<CelulaAliniada> temp = new ArrayList(celulasAliniadas);
//        celulasAliniadas.clear();

        for (CelulaAliniada c : celulasAliniadas) {
            completarCelula(celulaOriginal, c);
        }
        seleccionCalificacion(numCandidatos);

        eliminarColumnasGaps();
        for (CelulaAliniada c : celulasAliniadas) {
            c.mapiarCelula();
            c.setCalificacion();
        }

        System.out.println("Mejores candidatos");
        for (CelulaAliniada c : celulasAliniadas) {
            System.out.println("Calificacion: " + c.getCalificacion());
            System.out.println("Numero de columnas aliniadas: " + c.getNumColAliniadas());
            System.out.println("Numero de Gabs: " + c.getNumGabs());
            for (Nucleotido n : c.getCelula()) {
                System.out.println(n.getNucleotido());
            }
        }
    }

    /**
     * Genera candidatos alineados basados en el nucleótido más grande y otras
     * condiciones predefinidas.
     *
     * @param celulaOriginal
     */
    private void baseMax(Celula celulaOriginal) {
        SecureRandom r = new SecureRandom();
        int diferencia = celulaOriginal.getTamañoNucleotidoGrande() - celulaOriginal.getTamañoNucleotidoGrande2();
        Nucleotido temp = SerializationUtils.clone(celulaOriginal.getCelula().get(celulaOriginal.getIndexNucleotidoGrande2()));
        if (porDiferencia) {
            for (int i = 0; i < diferencia; i++) {
                int index = r.nextInt(temp.getTamaño());
                temp.getNucleotido().add(index, '-');
                temp.setTamaño(temp.getNucleotido().size());
            }
            temp.setTamaño(temp.getNucleotido().size());
            candidatos.add(temp);
        } else {
            porDiferencia = false;
            int tamaño = proporcion + nucleotidoGrande.getTamaño();
            diferencia = tamaño - celulaOriginal.getTamañoNucleotidoGrande2();
            Nucleotido tempGrande = SerializationUtils.clone(nucleotidoGrande);
            for (int i = 0; i < diferencia; i++) {
                int index = random.nextInt(temp.getTamaño());
                temp.getNucleotido().add(index, '-');
            }
            temp.setTamaño(temp.getNucleotido().size());
            candidatos.add(temp);
            for (int i = 0; i < proporcion; i++) {
                int index = random.nextInt(tempGrande.getTamaño());
                tempGrande.getNucleotido().add(index, '-');
            }
            tempGrande.setTamaño(tempGrande.getNucleotido().size());
            candidatosGrandes.add(tempGrande);
        }
    }

    /**
     * Genera candidatos alineados basados en el nucleótido más pequeño y otras
     * condiciones predefinidas.
     *
     * @param celulaOriginal
     */
    private void baseMin(Celula celulaOriginal) {
        SecureRandom r = new SecureRandom();
        Nucleotido temp = SerializationUtils.clone(celulaOriginal.getCelula().get(celulaOriginal.getIndexNucleotidoPequeño()));
        int diferencia = celulaOriginal.getTamañoNucleotidoGrande() - celulaOriginal.getTamañoNucleotidoPequeño();
        if (porDiferencia) {
            for (int i = 0; i < diferencia; i++) {
                int index = r.nextInt(temp.getTamaño());
                temp.getNucleotido().add(index, '-');
                temp.setTamaño(temp.getNucleotido().size());
            }
            temp.setTamaño(temp.getNucleotido().size());
            candidatos.add(temp);
        } else {
            int tamaño = candidatosGrandes.get(0).getTamaño() - temp.getTamaño();
            for (int i = 0; i < tamaño; i++) {
                int index = random.nextInt(temp.getTamaño());
                temp.getNucleotido().add(index, '-');
                temp.setTamaño(temp.getNucleotido().size());
            }
            temp.setTamaño(temp.getNucleotido().size());
            candidatos.add(temp);
        }
    }

    /**
     * Crea células de prueba para el proceso de alineación utilizando dos
     * nucleótidos específicos
     *
     * @param n1
     * @param n2
     * @param indexIgnore
     */
    private void crearCelulasPrueba(Nucleotido n1, Nucleotido n2, int indexIgnore) {
        CelulaAliniada celula = new CelulaAliniada();
        celula.agregarNucleotido(n1);
        celula.agregarNucleotido(n2);
        celula.setIndexIgnore(indexIgnore);
        celulasAliniadas.add(celula);
    }

    /**
     * Selecciona las mejores células alineadas basadas en la calificación
     * asignada a cada célula.
     *
     * @param numCandidatos
     */
    private void seleccionCalificacion(int numCandidatos) {
        Collections.sort(celulasAliniadas, (c1, c2) -> Integer.compare(c2.getCalificacion(), c1.getCalificacion()));
        List<CelulaAliniada> temp = new ArrayList<>(celulasAliniadas);
        celulasAliniadas.clear();
        for (int i = 0; i < numCandidatos && i < temp.size(); i++) {
            CelulaAliniada c = temp.get(i);
            CelulaAliniada copia = new CelulaAliniada();
            for (Nucleotido n : c.getCelula()) {
                copia.agregarNucleotido(SerializationUtils.clone(n));
            }
            copia.setCalificacion(c.getCalificacion());
            copia.setNumColAliniadas(c.getNumColAliniadas());
            copia.setNumGabs(c.getNumGabs());
            copia.setIndexIgnore(c.getIndexIgnore());
            celulasAliniadas.add(copia);
        }
    }

    /**
     * Crea candidatos para la alineación en función de un candidato específico
     * y otros parámetros.
     *
     * @param c
     * @param n
     */
//    private void crearCandidatos(Celula c, Nucleotido n) {
//        candidatos.clear();
//        SecureRandom r = new SecureRandom();
//        Nucleotido temp = SerializationUtils.clone(n);
//        int diferencia = (c.getNumColumnas() - n.getTamaño());
//        for (int j = 0; j < diferencia; j++) {
//            int index = r.nextInt(temp.getTamaño());
//            temp.getNucleotido().add(index, '-');
//            temp.setTamaño(temp.getNucleotido().size());
//        }
//        candidatos.add(temp);
//    }
    private void crearCandidatos(CelulaAliniada c, Nucleotido n) {
        candidatos.clear();
        SecureRandom r = new SecureRandom();
        Nucleotido temp = SerializationUtils.clone(n);
        int diferencia = (c.getNumColumnas() - n.getTamaño());

        if (diferencia > 0) {
            for (int j = 0; j < diferencia; j++) {
                int index = r.nextInt(temp.getTamaño());
                temp.getNucleotido().add(index, '-');
            }
//        } else if (diferencia < 0) {
//            int gapCount = Math.abs(diferencia);
//            for (int j = 0; j < gapCount; j++) {
//                temp.getNucleotido().remove(temp.getTamaño() - 1); // Eliminar el último nucleótido
//            }
        }

        temp.setTamaño(temp.getNucleotido().size()); // Asegurar que el tamaño sea el mismo que el número de columnas
        candidatos.add(temp);
    }

    /**
     * Completa la célula con los candidatos adecuados en función de ciertos
     * criterios predefinidos
     *
     * @param c
     * @param indexCandidato
     */
//    private void completarCelula(CelulaAliniada c, int indexCandidato) {
//        List<Nucleotido> temp = new ArrayList<>(candidatos);
//        candidatos.clear();
//        for (Nucleotido n : temp) {
//            CelulaAliniada clon = SerializationUtils.clone(c);
//            if (n.getTamaño() == clon.getCelula().get(0).getTamaño() && indexCandidato != c.getIndexIgnore()) {
//                clon.agregarNuevoNucleotido(n);
//                celulasAliniadas.add(clon);
//            }
//        }
//    }
    private void completarCelula(Celula co, CelulaAliniada c) {
        SecureRandom r = new SecureRandom();
        for (int i = 0; i < co.getNumNucleotidos(); i++) {
            if (i != co.getIndexNucleotidoGrande() && i != c.getIndexIgnore()) {
                Nucleotido temp = SerializationUtils.clone(co.getCelula().get(i));
                int diferencia = (c.getNumColumnas() - temp.getTamaño());
                for (int j = 0; j < diferencia; j++) {
                    int index = r.nextInt(temp.getTamaño());
                    temp.getNucleotido().add(index, '-');
                }
                c.getCelula().add(temp);
            }
        }
    }

    /**
     * Elimina las columnas de gaps de las células alineadas para ajustar la
     * alineación de manera adecuada.
     */
    public void eliminarColumnasGaps() {
        for (CelulaAliniada c : celulasAliniadas) {
            int numColumnas = c.getNumColumnas();
            for (int i = 0; i < numColumnas; i++) {
                boolean esColumnaGap = true;
                for (Nucleotido nucleotido : c.getCelula()) {
                    List<Character> nucleotidoList = nucleotido.getNucleotido();
                    if (i >= c.getCelula().size() || nucleotidoList.get(i) != '-') {
                        esColumnaGap = false;
                        break;
                    }
                }
                if (esColumnaGap) {
                    for (Nucleotido nucleotido : c.getCelula()) {
                        nucleotido.getNucleotido().remove(i);
                    }
                    c.setNumColumnas(c.getNumColumnas() - 1);
                    numColumnas--;
                    i--;
                }
            }
        }
    }

//    private void eliminarColumnasGaps() {
//        for (CelulaAliniada c : celulasAliniadas) {
//            for (int i = 0; i < c.getNumColumnas(); i++) {
//                for (int j = 0; j < c.getNumFilas(); j++) {
//                    if (c.getCelula().get(j).getNucleotido().get(i) == '-') {
//                        if (j == c.getNumFilas() - 1) {
////                            System.out.println("col de gabs"+ i);
//                            eleminarColumna(i, c);
//                        }
//                    } else {
//                        j = c.getNumFilas();
//                    }
//                }
//            }
//        }
//    }
//
//    private void eleminarColumna(int index, CelulaAliniada c) {
//        int t = 0;
//        for (int j = 0; j < c.getNumFilas(); j++) {
//            Nucleotido n = c.getCelula().get(j);
////            System.out.println("antes");
////            System.out.println(n.getNucleotido());
//            n.getNucleotido().remove(index);
////            System.out.println("despues");
////            System.out.println(n.getNucleotido());
//            t = n.getTamaño()-1;
//            n.setTamaño(t);
//        }
//        c.setNumColumnas(c.getNumColumnas()-1);
//    }
    /**
     * Implementa lógica para la mutación de los nucleótidos en la célula
     * original.
     *
     * @param celulaOriginal
     */
    private void mutacion(Celula celula, int cantidadGaps) {

    }

}
