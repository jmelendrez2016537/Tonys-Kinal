
package org.jonadabmelendrez.bean;

import java.util.Date;

public class Presupuesto {
    
    private int codigoPresupuesto;
    private Date fechaSolicitud;
    private int cantidadPresupuesto;
    private int codigoEmpresa;

    public Presupuesto() {
    }

    public Presupuesto(int codigoPresupuesto, Date fechaSolicitud, int cantidadPresupuesto, int codigoEmpresa) {
        this.codigoPresupuesto = codigoPresupuesto;
        this.fechaSolicitud = fechaSolicitud;
        this.cantidadPresupuesto = cantidadPresupuesto;
        this.codigoEmpresa = codigoEmpresa;
    }

    public int getCodigoPresupuesto() {
        return codigoPresupuesto;
    }

    public void setCodigoPresupuesto(int codigoPresupuesto) {
        this.codigoPresupuesto = codigoPresupuesto;
    }

    public Date getFechaSolicitud() {
        return fechaSolicitud;
    }

    public void setFechaSolicitud(Date fechaSolicitud) {
        this.fechaSolicitud = fechaSolicitud;
    }

    public int getCantidadPresupuesto() {
        return cantidadPresupuesto;
    }

    public void setCantidadPresupuesto(int cantidadPresupuesto) {
        this.cantidadPresupuesto = cantidadPresupuesto;
    }

    public int getCodigoEmpresa() {
        return codigoEmpresa;
    }

    public void setCodigoEmpresa(int codigoEmpresa) {
        this.codigoEmpresa = codigoEmpresa;
    }

    
    public String toString() {
        return codigoPresupuesto + " | " + cantidadPresupuesto ;
    }
   
    
}

