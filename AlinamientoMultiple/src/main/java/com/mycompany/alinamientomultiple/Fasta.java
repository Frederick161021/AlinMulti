package com.mycompany.alinamientomultiple;

import org.biojava.nbio.core.sequence.io.FastaReaderHelper;
import org.biojava.nbio.core.sequence.template.Sequence;
import java.io.File;
import java.io.*;
import java.util.ArrayList;
import java.util.Map;
import org.biojava.nbio.core.sequence.DNASequence;

public class Fasta {

    public Fasta() {}
    
    public Fasta(String nombre, Celula celula){
        this.crearFasta(nombre, celula);
    }

    public Celula leerFasta(String rutaArchivoFasta, Celula celula) {
        try {
            File archivo = new File(rutaArchivoFasta);
            Map<String, DNASequence> secuencias = FastaReaderHelper.readFastaDNASequence(archivo);

            for (Map.Entry<String, DNASequence> entry : secuencias.entrySet()) {
                String encabezado = entry.getKey();
                String secuencia = entry.getValue().getSequenceAsString();

                System.out.println("Encabezado: " + encabezado);
                System.out.println("Secuencia: " + secuencia);
                
                celula.agregarNucleotido(encabezado, secuencia);
            }
            if (celula.getTamañoNucleotidoGrande2() == 0) {
                celula.setTamañoNucleotidoGrande2(celula.getIndexNucleotidoPequeño());
                celula.setIndexNucleotidoGrande2(celula.getIndexNucleotidoGrande2());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return celula;
    }

    public void crearFasta(String nombre, Celula celula) {

        String nombreArchivo = nombre + ".fasta";
        String ruta = "C:/Users/togae/OneDrive/Documentos/NetBeansProjects/AlinamientoMultiple/AlinamientoMultiple/src/main/resources/fasta/" + nombreArchivo;

        try {
            FileWriter archivo = new FileWriter(ruta);
            PrintWriter escritor = new PrintWriter(archivo);
            for (Nucleotido nucleotido : celula.getCelula()) {
                // Encabezado de la secuencia
                escritor.println(nucleotido.getEncabezado());

                // Secuencia
                String secuencia = "";
                for(Character c : nucleotido.getNucleotido()){
                    secuencia += c; 
                }
                escritor.println(secuencia);
            }

            // Cierra el archivo cuando hayas terminado de escribir en él
            escritor.close();
            archivo.close();

            System.out.println("Archivo FASTA creado con éxito: " + nombreArchivo);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
