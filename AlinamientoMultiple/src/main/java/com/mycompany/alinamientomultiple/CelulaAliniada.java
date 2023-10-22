package com.mycompany.alinamientomultiple;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Erick Toledo
 */
public class CelulaAliniada extends Celula implements Serializable {

    private int numGabs = 0;
    private int numColAliniadas = 0;
    private int calificacion = 0;
    protected Map<Character, Integer> caracteres = new HashMap<>();

    public CelulaAliniada() {
        super.celula = new ArrayList();
        super.extremosSec.put("indexGrande", 0);
        super.extremosSec.put("tamañoGrande", 0);
        super.extremosSec.put("indexGrande2", 0);
        super.extremosSec.put("tamañoGrande2", 0);
        super.extremosSec.put("indexPequeño", 0);
        super.extremosSec.put("tamañoPequeño", 0);
    }

    public int getNumGabs() {
        return numGabs;
    }

    public void setNumGabs(int numGabs) {
        this.numGabs = numGabs;
    }

    public int getNumColAliniadas() {
        return numColAliniadas;
    }

    public void setNumColAliniadas(int numColAliniadas) {
        this.numColAliniadas = numColAliniadas;
    }

    public int getCalificacion() {
        return calificacion;
    }

    public List<Nucleotido> getCelula() {
        return celula;
    }

    public void setCelula(List<Nucleotido> celula) {
        this.celula = celula;
    }

    public void mapiarCelula() {
        caracteres.put('-', 0);
        for (int i = 0; i < super.getNumColumnas(); i++) {
            for (int j = 0; j < super.getNumFilas(); j++) {
                Character c = super.getCelula().get(j).getNucleotido().get(i);
                if (!caracteres.containsKey(c)) {
                    caracteres.put(c, 0);
                }
            }
        }
//        System.out.println("caracteres\n"+caracteres);
    }

    private void resetRepeticiones() {
        for (Map.Entry<Character, Integer> entry : caracteres.entrySet()) {
            entry.setValue(0);
        }
    }

    public void setCalificacion() {
        int limitSemiAliniada = super.getNumColumnas() / 2;
        
        for (int i = 0; i < super.getNumColumnas(); i++) {
            
            for (int j = 0; j < super.getNumFilas(); j++) {
                Character c = this.getCelula().get(j).getNucleotido().get(i);
                caracteres.replace(c, caracteres.get(c) + 1);
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
                            calificacion += valor.getValue();
                        }
                    }
                }
            }
            calificacion -= caracteres.get('-');
            this.numGabs += caracteres.get('-');
            resetRepeticiones();
        }
    }

}
