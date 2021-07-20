
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
import org.jonadabmelendrez.bean.TipoPlato;
import org.jonadabmelendrez.system.MainApp;

public class TipoPlatoController implements Initializable {
private MainApp escenarioPrincipal;
private enum Operacion{NUEVO, GUARDAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO }
private Operacion tipoOperacion = Operacion.NINGUNO;
private ObservableList <TipoPlato>listaTipoPlato;

    @FXML private TextField txtCodigoTipoPlato;
    @FXML private TextField txtDescripcionTipo;
    @FXML private TableView tblTipoPlato;
    @FXML private TableColumn colCodigoTipoPlato;
    @FXML private TableColumn colDescripcionTipo;
    @FXML private Button btnNuevo;
    @FXML private Button btnEditar;
    @FXML private Button btnEliminar;
    @FXML private Button btnReporte;

    @Override
    public void initialize(URL url, ResourceBundle resourses) {
        cargarDatos();
    } 
    
    public void desactivarControles(){
        txtCodigoTipoPlato.setEditable(false);
        txtDescripcionTipo.setEditable(false);
    }

    public void activarControles(){
        txtCodigoTipoPlato.setEditable(false);
        txtDescripcionTipo.setEditable(true);
    }

    public void limpiarControles(){
        txtCodigoTipoPlato.setText("");
        txtDescripcionTipo.setText("");
    }
    
    public void cargarDatos(){
        tblTipoPlato.setItems(getTipoPlato());
        colCodigoTipoPlato.setCellValueFactory(new PropertyValueFactory<TipoPlato, Integer>("codigoTipoPlato"));
        colDescripcionTipo.setCellValueFactory(new PropertyValueFactory<TipoPlato, String>("descripcionTipo"));
        desactivarControles();
    }
    
    public ObservableList<TipoPlato> getTipoPlato(){
        ArrayList<TipoPlato> lista = new ArrayList<TipoPlato>();
        try{
           PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarTipoPlato()}");
           ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
            lista.add(new TipoPlato(resultado.getInt("codigoTipoPlato"),
                    resultado.getString("descripcionTipo")));                        
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return listaTipoPlato = FXCollections.observableArrayList(lista);
    }
    
    public void seleccionarElementos(){
        txtCodigoTipoPlato.setText(String.valueOf(((TipoPlato)tblTipoPlato.getSelectionModel().getSelectedItem()).getCodigoTipoPlato()));
        txtDescripcionTipo.setText(((TipoPlato)tblTipoPlato.getSelectionModel().getSelectedItem()).getDescripcionTipo());  
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
        TipoPlato registroNuevo = new TipoPlato();
        registroNuevo.setDescripcionTipo(txtDescripcionTipo.getText());
        try{
           PreparedStatement sp = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarTipoPlato(?)}");
           sp.setString(1, registroNuevo.getDescripcionTipo());
           sp.execute();
           listaTipoPlato.add(registroNuevo);
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
           
                if(tblTipoPlato.getSelectionModel().getSelectedItem() != null){
                    int respuesta = JOptionPane.showConfirmDialog(null, "¿Esta seguro de eliminar el registro?","Eliminar Tipo Plato", JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
                    if(respuesta == JOptionPane.YES_OPTION){
                        try{
                            PreparedStatement sp = Conexion.getInstance().getConexion().prepareCall("{call sp_EliminarTipoPlato(?)}");
                            sp.setInt(1, ((TipoPlato)tblTipoPlato.getSelectionModel().getSelectedItem()).getCodigoTipoPlato());
                            sp.execute();
                            listaTipoPlato.remove(tblTipoPlato.getSelectionModel().getSelectedIndex());
                            limpiarControles();
                            JOptionPane.showMessageDialog(null, "Tipo Plato eliminado con exito!!");
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
                if(tblTipoPlato.getSelectionModel().getSelectedItem() != null){
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
        PreparedStatement sp = Conexion.getInstance().getConexion().prepareCall("{call sp_ActualizarTipoPlato(?,?)}");
        TipoPlato registroActualizado = ((TipoPlato)tblTipoPlato.getSelectionModel().getSelectedItem());
        registroActualizado.setDescripcionTipo(txtDescripcionTipo.getText());
        sp.setInt(1,registroActualizado.getCodigoTipoPlato());
        sp.setString(2,registroActualizado.getDescripcionTipo());
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
    
    public void plato(){
        escenarioPrincipal.plato();
    }
}
