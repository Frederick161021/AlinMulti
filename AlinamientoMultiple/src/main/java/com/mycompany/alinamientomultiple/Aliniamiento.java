package com.mycompany.alinamientomultiple;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.apache.commons.lang3.SerializationUtils;

public class Aliniamiento {

    private SecureRandom random = new SecureRandom();
    private List<Nucleotido> candidatosGrandes = new ArrayList<>();
    private List<Nucleotido> candidatos = new ArrayList<>();
    private List<CelulaAliniada> celulasAliniadas = new ArrayList<>();
    private Nucleotido nucleotidoGrande;
    private int proporcion;
    private boolean porDiferencia = true;

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
        ArrayList<CelulaAliniada> temp = new ArrayList(celulasAliniadas);
        celulasAliniadas.clear();
        for (int i = 0; i < celulaOriginal.getNumNucleotidos(); i++) {
            if (i != celulaOriginal.getIndexNucleotidoGrande()) {
                for (CelulaAliniada c : temp) {
                    c.actualizarDatos();
                    this.crearCandidatos(c, celulaOriginal.getCelula().get(i));
                    completarCelula(c, i);
                    c.actualizarDatos();
                    if (!celulasAliniadas.isEmpty()) {
//                        System.out.println("candidatos:");
//                        for (CelulaAliniada ca : celulasAliniadas) {
//                            for (Nucleotido n : ca.getCelula()) {
//                                System.out.println(n.getNucleotido());
//                            }
//                        }
                        eliminarColumnasGaps();
                        for (CelulaAliniada ca : celulasAliniadas) {
                            ca.actualizarDatos();
                            ca.mapiarCelula();
                            ca.setCalificacion();
                        }

                    }
                }
                seleccionCalificacion(numCandidatos);
            }
        }
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

    private void crearCelulasPrueba(Nucleotido n1, Nucleotido n2, int indexIgnore) {
        CelulaAliniada celula = new CelulaAliniada();
        celula.agregarNucleotido(n1);
        celula.agregarNucleotido(n2);
        celula.setIndexIgnore(indexIgnore);
        celulasAliniadas.add(celula);
    }

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

    private void crearCandidatos(Celula c, Nucleotido n) {
        candidatos.clear();
        SecureRandom r = new SecureRandom();
        Nucleotido temp = SerializationUtils.clone(n);
        int diferencia = (c.getNumColumnas() - n.getTamaño());
        for (int j = 0; j < diferencia; j++) {
            int index = r.nextInt(temp.getTamaño());
            temp.getNucleotido().add(index, '-');
            temp.setTamaño(temp.getNucleotido().size());
        }
        candidatos.add(temp);
    }

    private void completarCelula(CelulaAliniada c, int indexCandidato) {
        List<Nucleotido> temp = new ArrayList<>(candidatos);
        candidatos.clear();
        for (Nucleotido n : temp) {
            CelulaAliniada clon = SerializationUtils.clone(c);
            if (n.getTamaño() == clon.getCelula().get(0).getTamaño() && indexCandidato != c.getIndexIgnore()) {
                clon.agregarNuevoNucleotido(n);
                celulasAliniadas.add(clon);
            }
        }
    }

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

    private void mutacion(Celula celulaOriginal) {
        for (Nucleotido nucleotido : celulaOriginal.getCelula()) {
            // Lógica para mutación
        }
    }
}
