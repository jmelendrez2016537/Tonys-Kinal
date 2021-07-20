
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
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javax.swing.JOptionPane;
import org.jonadabmelendrez.bd.Conexion;
import org.jonadabmelendrez.bean.Producto;
import org.jonadabmelendrez.system.MainApp;

public class ProductoController implements Initializable {
private MainApp escenarioPrincipal;
private enum Operacion{NUEVO, GUARDAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO }
private Operacion tipoOperacion = Operacion.NINGUNO;
private ObservableList <Producto>listaProducto;
    
    @FXML private TextField txtCodigoProducto;
    @FXML private TextField txtNombreProducto;
    @FXML private TextField txtCantidad;
    @FXML private TableView tblProducto;
    @FXML private TableColumn colCodigoProducto;
    @FXML private TableColumn colNombreProducto;
    @FXML private TableColumn colCantidad;
    @FXML private Button btnNuevo;
    @FXML private Button btnEditar;
    @FXML private Button btnEliminar;
    @FXML private Button btnReporte;

    @Override
    public void initialize(URL url, ResourceBundle resourses) {
       cargarDatos();
    }    
    
    public void desactivarControles(){
        txtCodigoProducto.setEditable(false);
        txtNombreProducto.setEditable(false);
        txtCantidad.setEditable(false);
    }

    public void activarControles(){
        txtCodigoProducto.setEditable(false);
        txtNombreProducto.setEditable(true);
        txtCantidad.setEditable(true);
    }

    public void limpiarControles(){
        txtCodigoProducto.setText("");
        txtNombreProducto.setText("");
        txtCantidad.setText("");
    }
    
    public void cargarDatos(){
        tblProducto.setItems(getProducto());
        colCodigoProducto.setCellValueFactory(new PropertyValueFactory<Producto, Integer>("codigoProducto"));
        colNombreProducto.setCellValueFactory(new PropertyValueFactory<Producto, String>("nombreProducto"));
        colCantidad.setCellValueFactory(new PropertyValueFactory<Producto, Integer>("cantidad"));
        desactivarControles();
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
    
    public void seleccionarElementos(){
        txtCodigoProducto.setText(String.valueOf(((Producto)tblProducto.getSelectionModel().getSelectedItem()).getCodigoProducto()));
        txtNombreProducto.setText(((Producto)tblProducto.getSelectionModel().getSelectedItem()).getNombreProducto()); 
        txtCantidad.setText(String.valueOf(((Producto)tblProducto.getSelectionModel().getSelectedItem()).getCantidad()));
    }
    
    public void nuevo(){
        switch(tipoOperacion){
            case NINGUNO:
                activarControles();
                limpiarControles();
                btnNuevo.setText("Guardar");
                btnEliminar.setText("Cancelar");
                btnEditar.setDisable(true);
                btnReporte.setDisable(true);
                tipoOperacion = Operacion.GUARDAR;
                break;
            case GUARDAR:
                guardar();
                desactivarControles();
                limpiarControles();
                btnNuevo.setText("Nuevo");
                btnEliminar.setText("Eliminar");
                btnEditar.setDisable(false);
                btnReporte.setDisable(false);
                tipoOperacion = Operacion.NINGUNO;
                cargarDatos();
                break;
        }
    }
   
    public void guardar(){
        Producto registroNuevo = new Producto();
        registroNuevo.setNombreProducto(txtNombreProducto.getText());
        registroNuevo.setCantidad(Integer.parseInt(txtCantidad.getText()));
        try{
           PreparedStatement sp = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarProductos(?,?)}");
           sp.setString(1, registroNuevo.getNombreProducto());
           sp.setInt(2, registroNuevo.getCantidad());
           sp.execute();
           listaProducto.add(registroNuevo);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void eliminar(){
        switch(tipoOperacion){
            case GUARDAR:
                desactivarControles();
                limpiarControles();
                btnNuevo.setText("Nuevo");
                btnEliminar.setText("Eliminar");
                btnEditar.setDisable(false);
                btnReporte.setDisable(false);
                tipoOperacion = Operacion.NINGUNO;
                break;
            default:
           
                if(tblProducto.getSelectionModel().getSelectedItem() != null){
                    int respuesta = JOptionPane.showConfirmDialog(null, "¿Esta seguro de eliminar el registro?","Eliminar Producto", JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
                    if(respuesta == JOptionPane.YES_OPTION){
                        try{
                            PreparedStatement sp = Conexion.getInstance().getConexion().prepareCall("{call sp_EliminarProductos(?)}");
                            sp.setInt(1, ((Producto)tblProducto.getSelectionModel().getSelectedItem()).getCodigoProducto());
                            sp.execute();
                            listaProducto.remove(tblProducto.getSelectionModel().getSelectedIndex());
                            limpiarControles();
                            JOptionPane.showMessageDialog(null, "Presupuesto eliminado con exito!!");
                         }catch(Exception e){
                             e.printStackTrace();
                         }
                    }
            }else{
                   JOptionPane.showMessageDialog(null, "Debe seleccionar un registro de la tabla"); 
                   
            }
        }
    }
    
    public void editar(){
        switch(tipoOperacion){
            case NINGUNO:
                if(tblProducto.getSelectionModel().getSelectedItem() != null){
                    btnEditar.setText("Actualizar");
                    btnReporte.setText("Calcelar");
                    btnNuevo.setDisable(true);
                    btnEliminar.setDisable(true);
                    activarControles();
                    tipoOperacion = Operacion.ACTUALIZAR;
                    
                }else{
                    JOptionPane.showMessageDialog(null, "Debes seleccionar un registro");
                }
                break;
            case ACTUALIZAR: 
                actualizar();
                limpiarControles();
                btnEditar.setText("Editar");
                btnReporte.setText("Reporte");
                btnNuevo.setDisable(false);
                btnEliminar.setDisable(false);
                tipoOperacion = Operacion.NINGUNO;
                cargarDatos();
                break;
        }
}
    
public void actualizar(){
    try{
        PreparedStatement sp = Conexion.getInstance().getConexion().prepareCall("{call sp_ActualizarProductos(?,?,?)}");
        Producto registroActualizado = ((Producto)tblProducto.getSelectionModel().getSelectedItem());
        registroActualizado.setNombreProducto(txtNombreProducto.getText());
        registroActualizado.setCantidad(Integer.parseInt(txtCantidad.getText()));
        sp.setInt(1,registroActualizado.getCodigoProducto());
        sp.setString(2,registroActualizado.getNombreProducto());
        sp.setInt(3, registroActualizado.getCantidad());
        sp.execute();
        JOptionPane.showMessageDialog(null, "Datos Actualizados");
    }catch(Exception e){
        e.printStackTrace();
    }
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
