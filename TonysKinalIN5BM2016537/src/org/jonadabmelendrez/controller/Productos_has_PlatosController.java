
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
import org.jonadabmelendrez.bean.Producto;
import org.jonadabmelendrez.bean.Productos_has_Platos;
import org.jonadabmelendrez.system.MainApp;

public class Productos_has_PlatosController implements Initializable {
private MainApp escenarioPrincipal;
private ObservableList<Productos_has_Platos> listaProductos_has_Platos;
private ObservableList<Producto> listaProducto;
private ObservableList<Plato> listaPlato;
  
    @FXML private ComboBox cmbCodigoProducto;
    @FXML private ComboBox cmbCodigoPlato;
    @FXML private TableView tblProductos_has_Platos;
    @FXML private TableColumn colCodigoProducto;
    @FXML private TableColumn colCodigoPlato;

    @Override
    public void initialize(URL url, ResourceBundle resourses) {
        cargarDatos();
        cmbCodigoProducto.setItems(getProducto());
        cmbCodigoPlato.setItems(getPlato());
    }  
    
    public void cargarDatos(){
        tblProductos_has_Platos.setItems(getProductos_has_Platos());
        colCodigoProducto.setCellValueFactory(new PropertyValueFactory<Productos_has_Platos, Integer>("codigoProducto"));
        colCodigoPlato.setCellValueFactory(new PropertyValueFactory<Productos_has_Platos, Integer>("codigoPato"));
    }
    
    public ObservableList<Productos_has_Platos> getProductos_has_Platos(){
        ArrayList<Productos_has_Platos> lista = new ArrayList<Productos_has_Platos>();
        try{
           PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarProductos_has_Platos()}");
           ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
            lista.add(new Productos_has_Platos(resultado.getInt("codigoProducto"),
                    resultado.getInt("codigoPlato")));                        
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return listaProductos_has_Platos = FXCollections.observableArrayList(lista);
    }
    
     public ObservableList<Producto> getProducto(){
        ArrayList<Producto> lista = new ArrayList<Producto>();
        try{
           PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarProductos()}");
           ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
            lista.add(new Producto(resultado.getInt("codigoProducto"),
                                    resultado.getString("nombreProducto"),
                                    resultado.getInt("cantidad")));                        
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return listaProducto = FXCollections.observableArrayList(lista);
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
        cmbCodigoProducto.getSelectionModel().select(buscarProducto(((Productos_has_Platos)tblProductos_has_Platos.getSelectionModel().getSelectedItem()).getCodigoProducto()));
        cmbCodigoPlato.getSelectionModel().select(buscarPlato(((Productos_has_Platos)tblProductos_has_Platos.getSelectionModel().getSelectedItem()).getCodigoPlato()));
    }
     
     public Producto buscarProducto(int codigoProducto){
        Producto resultado = null;
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_BuscarProductos(?)}");
            procedimiento.setInt(1, codigoProducto);
            ResultSet registro = procedimiento.executeQuery();
            while (registro.next()){
                resultado = new Producto(registro.getInt("codigoProducto"),
                                    registro.getString("nombreProducto"),
                                    registro.getInt("cantidad"));
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
