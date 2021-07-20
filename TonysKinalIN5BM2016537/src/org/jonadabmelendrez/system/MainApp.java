package org.jonadabmelendrez.system;

import java.io.InputStream;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.jonadabmelendrez.controller.EmpleadoController;
import org.jonadabmelendrez.controller.EmpresaController;
import org.jonadabmelendrez.controller.MenuPrincipalController;
import org.jonadabmelendrez.controller.PlatoController;
import org.jonadabmelendrez.controller.PresupuestoController;
import org.jonadabmelendrez.controller.ProductoController;
import org.jonadabmelendrez.controller.Productos_has_PlatosController;
import org.jonadabmelendrez.controller.ServicioController;
import org.jonadabmelendrez.controller.Servicios_has_EmpleadosController;
import org.jonadabmelendrez.controller.Servicios_has_PlatosController;
import org.jonadabmelendrez.controller.TipoEmpleadoController;
import org.jonadabmelendrez.controller.TipoPlatoController;

public class MainApp extends Application {
    private final String PAQUETE_VISTA = "/org/jonadabmelendrez/view/";
    private Stage escenarioPrincipal;
    private Scene escena;
    
     @Override
    public void start(Stage escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
        this.escenarioPrincipal.setTitle("Tony's Kinal IN5BM 2016537");
        menuPrincipal();
        escenarioPrincipal.show();
        }
    
      public void menuPrincipal(){
       try{
        MenuPrincipalController menuPrincipalController = (MenuPrincipalController)cambiarEscena("MenuPrincipal.fxml",700,400);
        menuPrincipalController.setEscenarioPrincipal(this);
       } catch(Exception e){
           e.printStackTrace();
       }   
    }
      
    public void servicio(){
        try{
            ServicioController servicioController = (ServicioController)cambiarEscena("Servicio.fxml",755,501);
            servicioController.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public void empresa(){
        try {
            EmpresaController empresaController = (EmpresaController) cambiarEscena("Empresa.fxml",645,429);
                empresaController.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public void tipoEmpleado(){
        try{
            TipoEmpleadoController tipoEmpleadoController = (TipoEmpleadoController) cambiarEscena("TipoEmpleado.fxml",630,410);
            tipoEmpleadoController.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public void empleado(){
        try{
            EmpleadoController empleadoController = (EmpleadoController) cambiarEscena("Empleado.fxml",750,490);
            empleadoController.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public void presupuesto(){
        try{
            PresupuestoController presupuestoController = (PresupuestoController) cambiarEscena("Presupuesto.fxml",671,443);
            presupuestoController.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public void tipoPlato(){
        try{
            TipoPlatoController tipoPlatoController = (TipoPlatoController) cambiarEscena("TipoPlato.fxml",623,400);
            tipoPlatoController.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public void plato(){
        try{
            PlatoController platoController = (PlatoController) cambiarEscena("Plato.fxml",726,466);
            platoController.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public void producto(){
        try{
            ProductoController productoController = (ProductoController) cambiarEscena("Producto.fxml",600,400);
            productoController.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public void servicios_has_empleados(){
        try{
            Servicios_has_EmpleadosController servicios_has_empleados = (Servicios_has_EmpleadosController) cambiarEscena("Servicios_has_Empleados.fxml",750,492);
            servicios_has_empleados.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public void servicios_has_platos(){
        try{
            Servicios_has_PlatosController servicios_has_platos = (Servicios_has_PlatosController) cambiarEscena("Servicios_has_Platos.fxml",620,400);
            servicios_has_platos.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public void productos_has_platos(){
        try{
            Productos_has_PlatosController productos_has_platos = (Productos_has_PlatosController) cambiarEscena("Productos_has_Platos.fxml",620,400);
            productos_has_platos.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
   
     public static void main(String[] args) {
       launch(args);
    }
   
      public Initializable cambiarEscena(String fxml, int ancho, int alto) throws Exception{
        Initializable resultado = null;
        FXMLLoader cargadorFXML = new FXMLLoader();
        InputStream archivo = MainApp.class.getResourceAsStream(PAQUETE_VISTA+fxml);
        cargadorFXML.setBuilderFactory(new JavaFXBuilderFactory());
        cargadorFXML.setLocation(MainApp.class.getResource(PAQUETE_VISTA+fxml));
        escena = new Scene((AnchorPane)cargadorFXML.load(archivo), ancho, alto);
        escenarioPrincipal.setScene(escena);
        escenarioPrincipal.sizeToScene();
        resultado = (Initializable) cargadorFXML.getController();
        return resultado;
    }
        
   
    
}
