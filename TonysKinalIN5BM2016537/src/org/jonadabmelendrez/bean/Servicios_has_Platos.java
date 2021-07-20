
package org.jonadabmelendrez.bean;


public class Servicios_has_Platos {

private int codigoServicio;
private int codigoPlato;

    public Servicios_has_Platos() {
    }

    public Servicios_has_Platos(int codigoServicio, int codigoPlato) {
        this.codigoServicio = codigoServicio;
        this.codigoPlato = codigoPlato;
    }

    public int getCodigoServicio() {
        return codigoServicio;
    }

    public void setCodigoServicio(int codigoServicio) {
        this.codigoServicio = codigoServicio;
    }

    public int getCodigoPlato() {
        return codigoPlato;
    }

    public void setCodigoPlatos(int codigoPlato) {
        this.codigoPlato = codigoPlato;
    }

    @Override
    public String toString() {
        return "Servicios_has_Platos{" + "codigoServicio=" + codigoServicio + ", codigoPlatos=" + codigoPlato + '}';
    }


    
}
