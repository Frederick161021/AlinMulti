package com.mycompany.alinamientomultiple;
import java.util.ArrayList;

/**
 *
 * @author Erick Toledo
 */
public class CelulaAliniada extends Celula {
    private int numGabs;
    private int numColAliniadas;
    private int calificacion;

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

    public void setCalificacion(int calificacion) {
        this.calificacion = calificacion;
    }
    
    
}
