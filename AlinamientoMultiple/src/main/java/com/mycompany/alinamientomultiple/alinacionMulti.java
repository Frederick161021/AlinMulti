package com.mycompany.alinamientomultiple;

import java.util.Scanner;

/**
 * La clase alinacionMulti es la clase principal en la que se ejecutara el
 * sistema de alinacion multiple de secuencias
 *
 * @author togae
 */
public class alinacionMulti {
    private  static Scanner s = new Scanner(System.in);// Se importa el scanner de java para interactuar con el usuario
    private static Celula celula;//Celula con la que se va a trabajar
    private static Fasta fasta;

    /**
     * Metodo que le pide los parametros al usuario para crear una nueva celula,
     * ademas crea un archivo del tipo Fasta y lo guara en la ruta:
     * NetBeansProjects\AlinamientoMultiple\AlinamientoMultiple\src\main\resources\fasta
     */
    public static void crearCelula() {
        try {
            //Pregunta cuantas secuencias tendra la celula
            System.out.println("Cuantas secuencias tiene la celula?");
            int numSec = s.nextInt();
            celula = new Celula();
            s.nextLine();
            //mete las secuencias en la celula
            for (int i = 0; i < numSec; i++) {
                System.out.println("Digite el encabezado de la secuencia numero " + (i + 1));
                String encabezado = s.nextLine().toLowerCase();
                System.out.println("Digite la secuencia numero " + (i + 1));
                String secuencia = s.nextLine().toUpperCase();
                celula.agregarNucleotido(encabezado, secuencia);
            }
            //crea y nombra el archivo fasta en el que lo va a guardar
            System.out.println("Digite un nombre para el documento Fasta:");
            String nombre = s.next();
            fasta = new Fasta(nombre, celula);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    
    /**
     * Este metodo selecciona un documento le pide al usuario la ruta del archivo que quiere abrir 
     * para poder crear ytrabajar con la celula creada con las secuencias del arhicvo fasta
     */
    public static void SeleccionarCelulaFasta() {
        celula = new Celula();
        System.out.println("Digite la ruta del Documento Fasta");
        s.nextLine();
        String ruta = s.nextLine();
        Fasta archivo = new Fasta();
        celula = archivo.leerFasta(ruta, celula);
    }
    
    public static void imprimirCelula(){
        System.out.println("******************************Celula******************************");
        System.out.println("Num Nucleotidos: "+celula.getNumNucleotidos());
        System.out.println("Numero Columnas: "+celula.getNumColumnas());
        System.out.println("Numero Filas: "+celula.getNumFilas());
        System.out.println("Tamaño de la secuencia mas grande: "+celula.getTamañoNucleotidoGrande());
        System.out.println("Index de la secuencia mas grande: "+celula.getIndexNucleotidoGrande());
        System.out.println("Tamaño de la segunda secuencia mas grande: "+celula.getTamañoNucleotidoGrande2());
        System.out.println("Index de la segunda secuencia mas grande: "+celula.getIndexNucleotidoGrande2());
        System.out.println("Tamaño de la secuencia mas pequeña: "+celula.getTamañoNucleotidoPequeño());
        System.out.println("Index de la secuencia mas pequeña: "+celula.getIndexNucleotidoPequeño());
        
        for(Nucleotido nucleotido:celula.getCelula()){
            System.out.println("Encabezado:\n"+nucleotido.getEncabezado());
            System.out.println("Tamaño:\n"+nucleotido.getTamaño());
            System.out.println("Secuencia:\n"+nucleotido.getNucleotido());
        }
    }
    
    public static void AlinacionMultiple(){
    }

    /**
     * Metodo main del sistema, sera el encargado de ejecutar las clases e
     * interactuar con el usuario de forma de menu por medio de la consola
     *
     * @param args
     */
    public static void main(String[] args) {
        
        int seleccion = 0;
        //!Menu
        do {
            System.out.println("**********Bienvenido al Sistema de alinamiento multiple!**********");
            System.out.println("Elige el numero de la opcion que deseas:");
            System.out.println("1.-Crear nueva celula Con archivo Fasta\n"
                    + "2.-Elegir un archivo Fasta\n"
                    + "3.-Imprimir Celula\n"
                    + "4.-Iniciar proceso de Alinacion Multiple\n"
                    + "5.-Salir");
            try {
                seleccion = s.nextInt();
                switch (seleccion) {
                    case 1:
                        crearCelula();
                        break;
                    case 2:
                        SeleccionarCelulaFasta();
                        break;
                    case 3:
                        imprimirCelula();
                        break;
                    case 4:
                        AlinacionMultiple();
                        break;
                    case 5:
                        System.exit(0);
                        break;
                    default:
                        System.out.println("Elige un numero que este en el menu");

                }
            } catch (Exception e) {
                System.out.println("Elige un numero del menu");
            }
        } while (seleccion != 5);
    }

}
//C:\Users\togae\OneDrive\Documentos\NetBeansProjects\AlinamientoMultiple\AlinamientoMultiple\src\main\resources\fasta\ejemplo.fasta
//SecureRandom secureRandom = new SecureRandom();
//        
//        // Generar un número entero aleatorio entre 1 y 100 (ambos inclusive)
//        int numeroEntero = secureRandom.nextInt(100) + 1;
