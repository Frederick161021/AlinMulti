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
    

    /**
     * Este método se utiliza para agregar un nuevo nucleótido a una célula. En el primer método, 
     * se reciben dos cadenas de caracteres como parámetros, que representan el encabezado y la secuencia del 
     * nucleótido respectivamente. Dentro del método, se realizan comparaciones para determinar el tamaño más p
     * equeño y los dos tamaños más grandes de las secuencias de nucleótidos, utilizando un mapa llamado 
     * extremosSec. 
     * Posteriormente, se crea un nuevo objeto de tipo Nucleotido y se agrega a una lista llamada celula. 
     * @param encabezado
     * @param secuencia 
     */
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
        if ((secuencia.length() == extremosSec.get("tamañoGrande") && celula.size() != extremosSec.get("indexGrande"))
                || (secuencia.length() > extremosSec.get("tamañoGrande2") && secuencia.length() < extremosSec.get("tamañoGrande"))) {
            extremosSec.replace("tamañoGrande2", secuencia.length());
            extremosSec.replace("indexGrande2", celula.size());
        }

        Nucleotido nucleotido = new Nucleotido(encabezado, secuencia);
        celula.add(nucleotido);
        numNucleotidos++;
        numFilas++;
    }

    /**
     * En este metodo  se recibe un objeto Nucleotido directamente y se realizan las mismas 
     * operaciones de comparación utilizando su tamaño. Además, se utiliza la función SerializationUtils.
     * clone para asegurarse de que se agregue una copia del objeto Nucleotido. 
     * Finalmente, el número de nucleótidos y filas se actualiza después de cada operación de agregado.
     * @param nucleotido 
     */
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

        if ((nucleotido.getTamaño() == extremosSec.get("tamañoGrande") && celula.size() != extremosSec.get("indexGrande"))
                || (nucleotido.getTamaño() > extremosSec.get("tamañoGrande2") && nucleotido.getTamaño() < extremosSec.get("tamañoGrande"))) {
            extremosSec.replace("tamañoGrande2", nucleotido.getTamaño());
            extremosSec.replace("indexGrande2", celula.size());
        }
        celula.add(SerializationUtils.clone(nucleotido));
        numNucleotidos++;
        numFilas++;
    }
    
    /**
     * Este método tiene como objetivo agregar un nuevo objeto de tipo Nucleotido a una lista denominada celula. 
     * El parámetro nucleotido que se recibe es un objeto de tipo Nucleotido que se agrega directamente a la lista 
     * celula sin realizar ninguna operación adicional. 
     * Este método parece ser una forma simple de añadir un nuevo objeto de tipo Nucleotido a una lista existente.
     * @param nucleotido 
     */
    public void agregarNuevoNucleotido(Nucleotido nucleotido) {
        celula.add(nucleotido);
    }

    /**
     * Este método, llamado getCelula, devuelve la lista de objetos de tipo Nucleotido llamada celula. 
     * Al llamar a este método, se obtiene la lista completa de los nucleótidos que han sido agregados a la célula. 
     * Es una forma de acceder a los elementos almacenados en la lista celula desde fuera de la clase que contiene este método.
     * @return 
     */
    public List<Nucleotido> getCelula() {
        return celula;
    }

    /**
     * Devuelve la lista de objetos de tipo Nucleotido denominada celula. Por otro lado, getNumNucleotidos 
     * retorna el número total de nucleótidos presentes en la célula, proporcionando así una manera de 
     * acceder tanto a la lista completa de nucleótidos como al recuento total de nucleótidos en la célula.
     * @return 
     */
    public int getNumNucleotidos() {
        return numNucleotidos;
    }
    
    /**
     * devuelve la lista de objetos de tipo Nucleotido denominada celula. Por otro lado, 
     * getNumNucleotidos retorna el número total de nucleótidos presentes en la célula, 
     * proporcionando así una manera de acceder tanto a la lista completa de nucleótidos 
     * como al recuento total de nucleótidos en la célula.
     * @param numNucleotidos 
     */
    public void setNumNucleotidos(int numNucleotidos) {
        this.numNucleotidos = numNucleotidos;
    }

    /**
     * Retorna el número de columnas de la célula.
     * @return 
     */
    public int getNumColumnas() {
        return numColumnas;
    }
    
    /**
     * Retorna el número de filas de la célula.
     * @return 
     */
    public int getNumFilas() {
        return numFilas;
    }

    /**
     * Establece el número de columnas de la célula.
     * @param nColumnas 
     */
    public void setNumColumnas(int nColumnas) {
        this.numColumnas = nColumnas;
    }
    /**
     *  Establece el número de filas de la célula.
     * @param nFilas 
     */
    public void setNumFilas(int nFilas) {
        this.numFilas = nFilas;
    }

    /**
     *  Obtiene el tamaño del nucleótido más grande en la célula
     * @return 
     */
    public int getTamañoNucleotidoGrande() {
        return extremosSec.get("tamañoGrande");
    }

    /**
     * Establece el tamaño del nucleótido más grande en la célula.
     * @param tamaño 
     */
    public void setTamañoNucleotidoGrande(int tamaño) {
        extremosSec.replace("tamañoGrande", tamaño);
    }

    /**
     * Obtiene el índice del nucleótido más grande en la célula.
     * @return 
     */
    public int getIndexNucleotidoGrande() {
        return extremosSec.get("indexGrande");
    }

    /**
     * Obtiene el segundo tamaño de nucleótido más grande en la célula.
     * @return 
     */
    public int getTamañoNucleotidoGrande2() {
        return extremosSec.get("tamañoGrande2");
    }

    /**
     *  Establece el segundo tamaño de nucleótido más grande en la célula.
     * @param tamaño 
     */
    public void setTamañoNucleotidoGrande2(int tamaño) {
        extremosSec.replace("tamañoGrande2", tamaño);
    }

    /**
     * Obtiene el segundo índice de nucleótido más grande en la célula.
     * @return 
     */
    public int getIndexNucleotidoGrande2() {
        return extremosSec.get("indexGrande2");
    }

    /**
     * Establece el segundo índice de nucleótido más grande en la célula.
     * @param index 
     */
    public void setIndexNucleotidoGrande2(int index) {
        extremosSec.replace("indexGrande2", index);
    }

    /**
     * Obtiene el tamaño del nucleótido más pequeño en la célula.
     * @return 
     */
    public int getTamañoNucleotidoPequeño() {
        return extremosSec.get("tamañoPequeño");
    }

    /**
     * Establece el tamaño del nucleótido más pequeño en la célula.
     * @param tamaño 
     */
    public void setTamañoNucleotidoPequeño(int tamaño) {
        extremosSec.replace("tamañoPequeño", tamaño);
    }

    /**
     * Obtiene el índice del nucleótido más pequeño en la célula
     * @return 
     */
    public int getIndexNucleotidoPequeño() {
        return extremosSec.get("indexPequeño");
    }

    /**
     * Actualiza los valores de las propiedades de la célula en función de los nucleótidos presentes.
     */
    public void actualizarDatos() {
        int numNucleotidos = celula.size();
        for (Nucleotido n : celula) {
            n.setTamaño(n.getNucleotido().size());
            setNumColumnas(n.getNucleotido().size());
        }
        setNumNucleotidos(numNucleotidos);
        setNumFilas(numNucleotidos);
    }

}
