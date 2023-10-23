package com.mycompany.alinamientomultiple;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.SerializationUtils;

/**
 * Objeto Celula es una matriz del tipo List<Nucleotido>, que contiene como
 * variables ademas de la propia matiz, el numero de columnas y numero de filas
 * de esta y si es la celula original, es decir la que no tiene ningun Gab}
 *
 * @author Erick Toledo
 */
public class Celula implements Serializable {

    protected List<Nucleotido> celula;//Celula, lista de nucleotidos osea termina siendo la matriz
    private int numNucleotidos = 0;
    private int numColumnas = 0;
    private int numFilas = 0;
    protected Map<String, Integer> extremosSec = new HashMap<>();//contine el arreglo mas grande

    /**
     * Constructor del objeto Celular que inicializa una nueva celula y define
     * el numero de filas de esta
     */
    public Celula() {
        this.celula = new ArrayList();
        extremosSec.put("indexGrande", 0);
        extremosSec.put("tamañoGrande", 0);
        extremosSec.put("indexGrande2", 0);
        extremosSec.put("tamañoGrande2", 0);
        extremosSec.put("indexPequeño", 0);
        extremosSec.put("tamañoPequeño", 0);
    }

    public void agregarNucleotido(String encabezado, String secuencia) {
        //inicializa la secuencia con el tamaño mas pequeño y el segundo mas grande para tener una referencia a comparar
        if (extremosSec.get("tamañoPequeño") == 0) {
            extremosSec.replace("tamañoPequeño", secuencia.length());
            extremosSec.replace("indexPequeño", celula.size());
        }
        if (secuencia.length() < extremosSec.get("tamañoPequeño")) {
            extremosSec.replace("tamañoPequeño", secuencia.length());
            extremosSec.replace("indexPequeño", celula.size());
        }
        if (secuencia.length() > extremosSec.get("tamañoGrande")) {
            extremosSec.replace("tamañoGrande2", extremosSec.get("tamañoGrande"));
            extremosSec.replace("indexGrande2", extremosSec.get("indexGrande"));
            extremosSec.replace("tamañoGrande", secuencia.length());
            extremosSec.replace("indexGrande", celula.size());
            numColumnas = extremosSec.get("tamañoGrande");
        }
        if ((secuencia.length() == extremosSec.get("tamañoGrande") && celula.size() != extremosSec.get("indexGrande")) || 
         (secuencia.length() > extremosSec.get("tamañoGrande2") && secuencia.length() < extremosSec.get("tamañoGrande"))) {
            extremosSec.replace("tamañoGrande2", secuencia.length());
            extremosSec.replace("indexGrande2", celula.size());
        }

        Nucleotido nucleotido = new Nucleotido(encabezado, secuencia);
        celula.add(nucleotido);
        numNucleotidos++;
        numFilas++;
    }

    public void agregarNucleotido(Nucleotido nucleotido) {
        //inicializa la secuencia con el tamaño mas pequeño y el segundo mas grande para tener una referencia a comparar
        if (extremosSec.get("tamañoPequeño") == 0) {
            extremosSec.replace("tamañoPequeño", nucleotido.getTamaño());
            extremosSec.replace("indexPequeño", celula.size());
        }
        if (nucleotido.getTamaño() < extremosSec.get("tamañoPequeño")) {
            extremosSec.replace("tamañoPequeño", nucleotido.getTamaño());
            extremosSec.replace("indexPequeño", celula.size());
        }
        if (nucleotido.getTamaño() > extremosSec.get("tamañoGrande")) {
            extremosSec.replace("tamañoGrande2", extremosSec.get("tamañoGrande"));
            extremosSec.replace("indexGrande2", extremosSec.get("indexGrande"));
            extremosSec.replace("tamañoGrande", nucleotido.getTamaño());
            extremosSec.replace("indexGrande", celula.size());
            numColumnas = extremosSec.get("tamañoGrande");
        }
        
        if ((nucleotido.getTamaño() == extremosSec.get("tamañoGrande") && celula.size() != extremosSec.get("indexGrande")) || 
         (nucleotido.getTamaño() > extremosSec.get("tamañoGrande2") && nucleotido.getTamaño() < extremosSec.get("tamañoGrande"))) {
            extremosSec.replace("tamañoGrande2", nucleotido.getTamaño());
            extremosSec.replace("indexGrande2", celula.size());
        }
        celula.add(SerializationUtils.clone(nucleotido));
        numNucleotidos++;
        numFilas++;
    }

    public List<Nucleotido> getCelula() {
        return celula;
    }

    public int getNumNucleotidos() {
        return numNucleotidos;
    }

    public int getNumColumnas() {
        return numColumnas;
    }

    public int getNumFilas() {
        return numFilas;
    }

    public int getTamañoNucleotidoGrande() {
        return extremosSec.get("tamañoGrande");
    }

    public void setTamañoNucleotidoGrande(int tamaño) {
        extremosSec.replace("tamañoGrande", tamaño);
    }

    public int getIndexNucleotidoGrande() {
        return extremosSec.get("indexGrande");
    }

    public int getTamañoNucleotidoGrande2() {
        return extremosSec.get("tamañoGrande2");
    }

    public void setTamañoNucleotidoGrande2(int tamaño) {
        extremosSec.replace("tamañoGrande2", tamaño);
    }

    public int getIndexNucleotidoGrande2() {
        return extremosSec.get("indexGrande2");
    }

    public void setIndexNucleotidoGrande2(int index) {
        extremosSec.replace("indexGrande2", index);
    }

    public int getTamañoNucleotidoPequeño() {
        return extremosSec.get("tamañoPequeño");
    }

    public void setTamañoNucleotidoPequeño(int tamaño) {
        extremosSec.replace("tamañoPequeño", tamaño);
    }

    public int getIndexNucleotidoPequeño() {
        return extremosSec.get("indexPequeño");
    }
}
