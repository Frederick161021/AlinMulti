package com.mycompany.alinamientomultiple;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
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
        
        if (celulaOriginal.getNumFilas() > 2) {
            for (CelulaAliniada c : celulasAliniadas) {
                completarCelula(celulaOriginal, c);
            }
        }

        eliminarColumnasGaps();
        for (CelulaAliniada c : celulasAliniadas) {
            c.mapiarCelula();
            c.setCalificacion();
        }
        seleccionCalificacion(numCandidatos);

        ArrayList<CelulaAliniada> temp = new ArrayList(celulasAliniadas);
        for (int i = 0; i < repeticiones; i++) {
            for (CelulaAliniada c : temp) {
                mutacion(c);
            }
            eliminarColumnasGaps();
            for (CelulaAliniada ca : celulasAliniadas) {
                ca.actualizarDatos();
                ca.mapiarCelula();
                ca.setCalificacion();
            }
            seleccionCalificacion(numCandidatos);
        }
        eliminarColumnasGaps();
        for (CelulaAliniada c : celulasAliniadas) {
            c.actualizarDatos();
            c.mapiarCelula();
            c.setCalificacion();
        }
        seleccionCalificacion(numCandidatos);

        System.out.println("Mejores candidatos");
        for (CelulaAliniada c : celulasAliniadas) {
            c.mapiarCelula();
            c.getCalificacion();
            System.out.println("Calificacion: " + c.getCalificacion());
            System.out.println("Numero de columnas aliniadas: " + c.getNumColAliniadas());
            System.out.println("Numero de Gaps: " + c.getNumGaps());
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
        if (celulasAliniadas == null || celulasAliniadas.isEmpty()) {
            return; // No hacer nada si la lista está vacía o es nula
        }

        // Ordenar en orden descendente de calificación
        celulasAliniadas.sort(Comparator.comparingInt(CelulaAliniada::getCalificacion).reversed());

        // Mantener solo un número específico de células de alta calificación
        if (celulasAliniadas.size() > numCandidatos) {
            celulasAliniadas.subList(numCandidatos, celulasAliniadas.size() - 1).clear();
        }
    }

    /**
     * Completa la célula con los candidatos adecuados en función de ciertos
     * criterios predefinidos
     *
     * @param c
     * @param indexCandidato
     */
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
            c.actualizarDatos();
            int numColumnas = c.getNumColumnas();
            for (int i = 0; i < numColumnas; i++) {
                boolean esColumnaGap = true;
                for (int j = 0; j < c.getNumFilas(); j++) {
                    if (i < c.getCelula().get(j).getTamaño() && c.getCelula().get(j).getNucleotido().get(i) != '-') {
                        esColumnaGap = false;
                        break;
                    }
                }
                if (esColumnaGap) {
                    for (int j = 0; j < c.getNumFilas(); j++) {
                        if (i < c.getCelula().get(j).getTamaño()) {
                            c.getCelula().get(j).getNucleotido().remove(i);
                        }
                    }
                    c.actualizarDatos();
                    numColumnas--;
                    i--;
                }
            }
        }
    }

    /**
     * Implementa lógica para la mutación de los nucleótidos en la célula
     * original.
     *
     * @param celulaOriginal
     */
    private void mutacion(CelulaAliniada c) {
        CelulaAliniada temp = new CelulaAliniada();
        SecureRandom r = new SecureRandom();
        temp = SerializationUtils.clone(c);
        int iteraciones = 0;
        int index = 0;
        if (temp.getCalificacion() > 40) {
            iteraciones = (int) (temp.getNumColumnas() * .05);
        } else {
            iteraciones = (int) (temp.getNumColumnas() * .1);
        }
        for (Nucleotido n : temp.getCelula()) {
            for (int i = 0; i < iteraciones; i++) {
                index = r.nextInt(n.getTamaño());
                n.getNucleotido().add(index, '-');
            }
        }
        celulasAliniadas.add(temp);
    }

}
