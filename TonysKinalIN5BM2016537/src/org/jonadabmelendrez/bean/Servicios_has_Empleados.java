
package org.jonadabmelendrez.bean;

import java.util.Date;

public class Servicios_has_Empleados {
    
    private int Servicios_codigoServicio;
    private int codigoServicio;
    private int codigoEmpleado;
    private Date fechaEvento;
    private String horaEvento;
    private String lugarEvento;

    public Servicios_has_Empleados() {
        
    }

    public Servicios_has_Empleados(int Servicios_codigoServicio, int codigoServicio, int codigoEmpleado, Date fechaEvento, String horaEvento, String lugarEvento) {
        this.Servicios_codigoServicio = Servicios_codigoServicio;
        this.codigoServicio = codigoServicio;
        this.codigoEmpleado = codigoEmpleado;
        this.fechaEvento = fechaEvento;
        this.horaEvento = horaEvento;
        this.lugarEvento = lugarEvento;
    }

    public int getServicios_codigoServicio() {
        return Servicios_codigoServicio;
    }

    public void setServicios_codigoServicio(int Servicios_codigoServicio) {
        this.Servicios_codigoServicio = Servicios_codigoServicio;
    }

    public int getCodigoServicio() {
        return codigoServicio;
    }

    public void setCodigoServicio(int codigoServicio) {
        this.codigoServicio = codigoServicio;
    }

    public int getCodigoEmpleado() {
        return codigoEmpleado;
    }

    public void setCodigoEmpleado(int codigoEmpleado) {
        this.codigoEmpleado = codigoEmpleado;
    }

    public Date getFechaEvento() {
        return fechaEvento;
    }

    public void setFechaEvento(Date fechaEvento) {
        this.fechaEvento = fechaEvento;
    }

    public String getHoraEvento() {
        return horaEvento;
    }

    public void setHoraEvento(String horaEvento) {
        this.horaEvento = horaEvento;
    }

    public String getLugarEvento() {
        return lugarEvento;
    }

    public void setLugarEvento(String lugarEvento) {
        this.lugarEvento = lugarEvento;
    }

  @Override
    public String toString() {
        return "Servicios_has_Empleados{" + "Servicios_codigoServicio=" + Servicios_codigoServicio + ", codigoServicio=" + codigoServicio + ", codigoEmpleado=" + codigoEmpleado + ", fechaEvento=" + fechaEvento + ", horaEvento=" + horaEvento + ", lugarEvento=" + lugarEvento + '}';
    }

    
    
    
    
}
