
package org.jonadabmelendrez.controller;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.jonadabmelendrez.bd.Conexion;
import org.jonadabmelendrez.bean.Plato;
import org.jonadabmelendrez.bean.Servicio;
import org.jonadabmelendrez.bean.Servicios_has_Platos;
import org.jonadabmelendrez.system.MainApp;

public class Servicios_has_PlatosController implements Initializable {
private MainApp escenarioPrincipal;
private ObservableList<Servicios_has_Platos> listaServicios_has_Platos;  
private ObservableList<Servicio> listaServicio;
private ObservableList<Plato> listaPlato;

    @FXML private ComboBox cmbCodigoServicio;
    @FXML private ComboBox cmbCodigoPlato;
    @FXML private TableView tblServicios_has_Platos;
    @FXML private TableColumn colCodigoServicio;
    @FXML private TableColumn colCodigoPlato;

    @Override
    public void initialize(URL url, ResourceBundle resourses) {
       cargarDatos();
       cmbCodigoServicio.setItems(getServicio());
       cmbCodigoPlato.setItems(getPlato());
    }    
    
    public void cargarDatos(){
        tblServicios_has_Platos.setItems(getServicios_has_Platos());
        colCodigoServicio.setCellValueFactory(new PropertyValueFactory<Servicios_has_Platos, Integer>("codigoServicio"));
        colCodigoPlato.setCellValueFactory(new PropertyValueFactory<Servicios_has_Platos, Integer>("codigoPato"));
    }
    
     public ObservableList<Servicios_has_Platos> getServicios_has_Platos(){
        ArrayList<Servicios_has_Platos> lista = new ArrayList<Servicios_has_Platos>();
        try{
           PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarServicios_has_Platos()}");
           ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
            lista.add(new Servicios_has_Platos(resultado.getInt("codigoServicio"),
                    resultado.getInt("codigoPlato")));                        
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return listaServicios_has_Platos = FXCollections.observableArrayList(lista);
    }
     
     public ObservableList<Servicio> getServicio(){
        ArrayList<Servicio> lista = new ArrayList<Servicio>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarServicios()}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new Servicio(resultado.getInt("codigoServicio"),
                                    resultado.getDate("fechaServicio"),
                                    resultado.getString("tipoServicio"),
                                    resultado.getString("horaServicio"),
                                    resultado.getString("lugarServicio"),
                                    resultado.getString("telefonoContacto"),
                                    resultado.getInt("codigoEmpresa")));
            }
            
        }catch(Exception e){
            e.printStackTrace();
        }
        
        return listaServicio = FXCollections.observableArrayList(lista);
    }
     
     public ObservableList<Plato> getPlato(){
        ArrayList<Plato> lista = new ArrayList<Plato>();
        try{
           PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarPlatos()}");
           ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
            lista.add(new Plato(resultado.getInt("codigoPlato"),
                    resultado.getInt("cantidad"),
                    resultado.getString("nombrePlato"),
                    resultado.getString("descripcionPlato"),
                    resultado.getInt("precioPlato"),
                    resultado.getInt("codigoTipoPlato")));                        
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return listaPlato = FXCollections.observableArrayList(lista);
    }
     
      public void seleccionarElementos(){
        cmbCodigoServicio.getSelectionModel().select(buscarServicio(((Servicios_has_Platos)tblServicios_has_Platos.getSelectionModel().getSelectedItem()).getCodigoServicio()));
        cmbCodigoPlato.getSelectionModel().select(buscarPlato(((Servicios_has_Platos)tblServicios_has_Platos.getSelectionModel().getSelectedItem()).getCodigoPlato()));
    }
      
       public Servicio buscarServicio(int codigoServicio){
        Servicio resultado = null;
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_BuscarServicios(?)}");
            procedimiento.setInt(1, codigoServicio);
            ResultSet registro = procedimiento.executeQuery();
            while (registro.next()){
                resultado = new Servicio(registro.getInt("codigoServicio"),
                                    registro.getDate("fechaServicio"),
                                    registro.getString("tipoServicio"),
                                    registro.getString("horaServicio"),
                                    registro.getString("lugarServicio"),
                                    registro.getString("telefonoContacto"),
                                    registro.getInt("codigoEmpresa"));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        
        return resultado;
    }
     
        public Plato buscarPlato(int codigoPlato){
        Plato resultado = null;
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_BuscarPlatos(?)}");
            procedimiento.setInt(1, codigoPlato);
            ResultSet registro = procedimiento.executeQuery();
            while (registro.next()){
                resultado = new Plato(registro.getInt("codigoPlato"),
                                    registro.getInt("cantidad"),
                                    registro.getString("nombrePlato"),
                                    registro.getString("descripcionPlato"),
                                    registro.getInt("precioPlato"),
                                    registro.getInt("codigoTipoPlato"));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        
        return resultado;
    }

        
    public MainApp getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(MainApp escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
    
    public void menuPrincipal(){
        escenarioPrincipal.menuPrincipal();
    }
}
