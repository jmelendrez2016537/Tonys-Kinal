
package org.jonadabmelendrez.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import org.jonadabmelendrez.system.MainApp;

public class MenuPrincipalController implements Initializable {
    private MainApp escenarioPrincipal;

    public MainApp getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(MainApp escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
    //para llegar a empresa por medio de menuPrincipal
     public void empresa(){
        escenarioPrincipal.empresa();
    }
    
     //para llegar a tipoEmpleado por medio de menuPrincipal
     public void tipoEmpleado(){
         escenarioPrincipal.tipoEmpleado();
     }

     //para llegar a tipoPlato por medio de menuPrincipal
     public void tipoPlato(){
         escenarioPrincipal.tipoPlato();
     }
     public void producto(){
         escenarioPrincipal.producto();
     }
     public void servicios_has_empleados(){
         escenarioPrincipal.servicios_has_empleados();
     }
     public void servicios_has_platos(){
         escenarioPrincipal.servicios_has_platos();
     }
     public void productos_has_platos(){
         escenarioPrincipal.productos_has_platos();
     }
     
    @Override
    public void initialize(URL url, ResourceBundle resourses) {
      
    }  
    
   
    
}
