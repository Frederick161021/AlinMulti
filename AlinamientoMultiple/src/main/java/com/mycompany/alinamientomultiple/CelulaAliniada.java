package com.mycompany.alinamientomultiple;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * Clase que extiende de celula que cotine datos para evaluar la celula
 *
 * @author Erick Toledo
 */
public class CelulaAliniada extends Celula implements Serializable {

    private int numGaps = 0;
    private int numColAliniadas = 0;
    private int calificacion = 0;
    private int indexIgnore;
    protected Map<Character, Integer> caracteres = new HashMap<>();

    /**
     * Constructor que crea e inicializa los parametros para una celula
     */
    public CelulaAliniada() {
        super.celula = new ArrayList();
        super.extremosSec.put("indexGrande", 0);
        super.extremosSec.put("tamañoGrande", 0);
        super.extremosSec.put("indexGrande2", 0);
        super.extremosSec.put("tamañoGrande2", 0);
        super.extremosSec.put("indexPequeño", 0);
        super.extremosSec.put("tamañoPequeño", 0);
    }

    /**
     * retorna el numero de gabs
     *
     * @return
     */
    public int getNumGaps() {
        return numGaps;
    }

    /**
     * Establece el numero de gabs
     *
     * @param numGabs
     */
    public void setNumGaps(int numGabs) {
        this.numGaps = numGabs;
    }

    /**
     * Retorna el numero de columnas aliniadas de la celula
     *
     * @return
     */
    public int getNumColAliniadas() {
        return numColAliniadas;
    }

    /**
     * Establece el número de columnas alineadas en la célula.
     *
     * @param numColAliniadas
     */
    public void setNumColAliniadas(int numColAliniadas) {
        this.numColAliniadas = numColAliniadas;
    }

    /**
     * Retorna la calificación actual de la célula.
     *
     * @return
     */
    public int getCalificacion() {
        return calificacion;
    }

    /**
     * Establece la calificación de la célula basada en diferentes criterios de
     * evaluación.
     *
     * @param calificacion
     */
    public void setCalificacion(int calificacion) {
        this.calificacion = calificacion;
    }

    /**
     * Retorna la lista de objetos de tipo Nucleotido llamada celula
     *
     * @return
     */
    public List<Nucleotido> getCelula() {
        return celula;
    }

    /**
     * Retorna el índice ignorado actualmente
     *
     * @return
     */
    public int getIndexIgnore() {
        return indexIgnore;
    }

    /**
     * Establece el índice a ignorar
     *
     * @param indexIgnore
     */
    public void setIndexIgnore(int indexIgnore) {
        this.indexIgnore = indexIgnore;
    }

    /**
     * Establece la lista de objetos de tipo Nucleotido en la célula.
     *
     * @param celula
     */
    public void setCelula(List<Nucleotido> celula) {
        this.celula = celula;
    }

//    public void mapiarCelula() {
//        caracteres.put('-', 0);
//        for (int i = 0; i < super.getNumColumnas(); i++) {
//            for (int j = 0; j < super.getNumFilas(); j++) {
//                Character c = super.getCelula().get(j).getNucleotido().get(i);
//                if (!caracteres.containsKey(c)) {
//                    caracteres.put(c, 0);
//                }
//            }
//        }
////        System.out.println("caracteres\n"+caracteres);
//    }
//    public void mapiarCelula() {
//    if (super.getCelula() != null) {
//        caracteres.put('-', 0);
//        int numFilas = super.getNumFilas();
//        int numColumnas = super.getNumColumnas();
//        for (int i = 0; i < numColumnas; i++) {
//            for (int j = 0; j < numFilas; j++) {
//                if (j < super.getCelula().size() && super.getCelula().get(j).getNucleotido() != null && i < super.getCelula().get(j).getNucleotido().size()) {
//                    Character c = super.getCelula().get(j).getNucleotido().get(i);
//                    if (!caracteres.containsKey(c)) {
//                        caracteres.put(c, 0);
//                    }
//                } else {
//                    // Lógica de manejo de límites o null aquí
//                }
//            }
//        }
//    } else {
//        // Lógica de manejo de null aquí
//    }
//}
    /**
     * Mapea la distribución de caracteres en la célula y maneja los casos
     * límite y nulos.
     */
    public void mapiarCelula() {
        if (super.getCelula() != null) {
            caracteres.put('-', 0);
            int numFilas = super.getNumFilas();
            int numColumnas = super.getNumColumnas();
            for (int i = 0; i < numColumnas; i++) {
                for (int j = 0; j < numFilas; j++) {
                    if (j < super.getCelula().size() && super.getCelula().get(j).getNucleotido() != null && i < super.getCelula().get(j).getNucleotido().size()) {
                        Character c = super.getCelula().get(j).getNucleotido().get(i);
                        if (!caracteres.containsKey(c)) {
                            caracteres.put(c, 0);
                        }
                    } else {
                        // Lógica de manejo de límites o null aquí
                    }
                }
            }
        } else {
            // Lógica de manejo de null aquí
        }
    }

    /**
     * Reinicia los valores de repeticiones en la distribución de caracteres.
     */
    private void resetRepeticiones() {
        for (int i = 0; i < caracteres.size(); i++) {
            caracteres.replaceAll((k, v) -> 0);
        }
    }

    /**
     * Calcula la calificación de la célula basada en el nivel de alineación y
     * otros criterios definidos.
     */
    public void setCalificacion() {
        numColAliniadas = 0;
        numGaps = 0;
        calificacion = 0;
        int numSemiAliniadas = 0;
        int limitSemiAliniada = (int) (super.getNumColumnas() * .5);
        for (int i = 0; i < super.getNumColumnas(); i++) {
            for (int j = 0; j < super.getNumFilas(); j++) {
                if (i < this.getCelula().get(j).getNucleotido().size()) {
                    Character c = this.getCelula().get(j).getNucleotido().get(i);
                    caracteres.replace(c, caracteres.get(c) + 1);
                }
            }

            for (Map.Entry<Character, Integer> valor : caracteres.entrySet()) {
                if (valor.getValue() == super.getNumFilas()) {
                    if (valor.getKey() != '-') {
                        calificacion += valor.getValue() * 2;
                        this.numColAliniadas++;
                    }
                } else {
                    if (valor.getValue() > limitSemiAliniada) {
                        if (valor.getKey() != '-') {
                            numSemiAliniadas++;
                        }
                    }
                }
            }
            this.numGaps += caracteres.get('-');
            resetRepeticiones();
        }
        // Normalizar la calificación
        double propColAliniadas = Math.min(1, Math.max(0, (double) numColAliniadas / super.getNumColumnas()));
        double propGabs = Math.min(1, Math.max(0, (double) numGaps / super.getNumColumnas()));
        double propSemiAliniadas = Math.min(1, Math.max(0, (double) numSemiAliniadas / super.getNumColumnas()));

        int califColAliniadas = (int) (propColAliniadas * 500); // Premio columnas completamente alineadas
        int califGabs = (int) (1 - propGabs); // Penalización espacios en blanco
        int califSemiAliniadas = (int) (propSemiAliniadas * 200);

        calificacion = Math.min(100, Math.max(0, (califColAliniadas + califSemiAliniadas) - califGabs)); // Calificación final estandarizada
    }

}
