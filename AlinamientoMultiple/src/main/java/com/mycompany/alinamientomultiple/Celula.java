package com.mycompany.alinamientomultiple;
import java.util.ArrayList;
import java.util.List;
/**
 * Objeto Celula es una matriz  del tipo List<Nucleotido>, 
 * que contiene como variables ademas de la propia matiz, el numero de columnas 
 * y numero de filas de esta y si es la celula original, es decir la que no tiene ningun Gab}
 * 
 * @author Erick Toledo
 */
public class Celula {
    private  List<Nucleotido> celula;
    private int numNucleotidos = 0;
    private int numColumnas = 0;
    private int numFilas = 0;

    /**
     * Constructor del objeto Celular que inicializa una nueva celula y define el numero de filas de esta
     * @param numNucleotidos 
     */
    public Celula(int numNucleotidos) {
        this.celula = new ArrayList();
        this.setCelula(numNucleotidos);
    }

    public void agregarNucleotido(String encabezado, String secuencia) {
        Nucleotido nucleotido = new Nucleotido(encabezado, secuencia);
        celula.add(nucleotido);
        numNucleotidos++;
    }

    public List<Nucleotido> getCelula() {
        return celula;
    }

    private void setCelula(List<Nucleotido> celula) {
        this.celula = celula;
    }

    public int getNumNucleotidos() {
        return numNucleotidos;
    }

    public void setNumNucleotidos(int numNucleotidos) {
        this.numNucleotidos = numNucleotidos;
    }

    public int getNumColumnas() {
        return numColumnas;
    }

    public void setNumColumnas(int numColumnas) {
        this.numColumnas = numColumnas;
    }

    public int getNumFilas() {
        return numFilas;
    }

    public void setNumFilas(int numFilas) {
        this.numFilas = numFilas;
    }
    
}
