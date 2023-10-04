package com.mycompany.alinamientomultiple;
import java.util.ArrayList;

/**
 * Objeto Nucleotido que contien el tamaño de la secuencia, la secuencia dentro de un ArrayList<Character> y su encabezado
 * @author Erick Toledo
 */
public class Nucleotido {
    private String encabezado;
    private ArrayList<Character> nucleotido;
    private int tamaño;
    
    /**
     * Constructro del objeto nucleotido vacio
     */
    public Nucleotido(){}
    
    /**
     * Constructor del objeto nucletido para crear una  nueva secuncia para la celula
     * crea el nucleotido como ArrayList<Character>
     * para lo que nesecita las secuencia
     * y define el encabezado de la misma 
     * y su tamaño
     * @param encabezado
     * @param secuencia 
     */
    public Nucleotido(String encabezado, String secuencia){
        this.nucleotido = new ArrayList();
        this.setTamaño(secuencia.length());
        this.setNucleotido(secuencia.toUpperCase());
        this.setEncabezado(encabezado);
    }

    /**
     * Regresa el Nucleotido de la forma: ArrayList<Character> nucleotido
     * @return 
     */
    public ArrayList<Character> getNucleotido() {
        return nucleotido;
    }

    /**
     * Asigna el valor de la cadena nucleotido con los caracteres del String secuencia
     * @param secuencia 
     */
    private void setNucleotido(String secuencia) {
        for (char caracter : secuencia.toCharArray()) {
           this.nucleotido.add(caracter);
        }
    }

    /**
     * Regresa el encabezado del nucleocito
     * @return 
     */
    public String getEncabezado() {
        return encabezado;
    }

    /**
     * Ssigna el encabezado del nuclecito
     * @param encabezado 
     */
    private void setEncabezado(String encabezado) {
        this.encabezado = ">"+encabezado;
    }

    /**
     * Regresa el tamaño del arreglo de nucleocito
     * @return 
     */
    public int getTamaño() {
        return tamaño;
    }

    /**
     * Asigna el tamaño del arreglo si es que este es modificado
     * @param tamaño 
     */
    private void setTamaño(int tamaño) {
        this.tamaño = tamaño;
    }
}
