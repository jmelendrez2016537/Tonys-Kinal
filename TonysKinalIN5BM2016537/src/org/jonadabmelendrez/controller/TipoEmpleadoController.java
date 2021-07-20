
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
import org.jonadabmelendrez.bean.TipoEmpleado;
import org.jonadabmelendrez.system.MainApp;

public class TipoEmpleadoController implements Initializable {
    private enum Operacion{NUEVO, GUARDAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO }
    private Operacion tipoOperacion = Operacion.NINGUNO;
    private MainApp escenarioPrincipal;
    private ObservableList <TipoEmpleado>listaTipoEmpleado;
    
    @FXML
    private TextField txtCodigoTipoEmpleado;
    @FXML
    private TextField txtDescripcion;
    @FXML
    private TableView tblTipoEmpleado;
    @FXML
    private TableColumn colCodigoTipoEmpleado;
    @FXML
    private TableColumn colDescripcion;
    @FXML
    private Button btnNuevo;
    @FXML
    private Button btnEditar;
    @FXML
    private Button btnEliminar;
    @FXML
    private Button btnReporte;
    //Metodo para desactivar controles
    public void desactivarControles(){
        txtCodigoTipoEmpleado.setEditable(false);
        txtDescripcion.setEditable(false);
    }
    //Metodo para activar controles
    public void activarControles(){
        //txtCodigoTipoEmpleado.setEditable(true);
        txtDescripcion.setEditable(true);
    }
    //Metodo para limpiar los controles 
    public void limpiarControles(){
        txtCodigoTipoEmpleado.setText("");
        txtDescripcion.setText("");
    }
    //Metodo para el boton nuevo
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
    //Metodo para boton guardar
    public void guardar(){
        TipoEmpleado tipoEmpleadoNuevo = new TipoEmpleado();
        tipoEmpleadoNuevo.setDescripcion(txtDescripcion.getText());
        try{
           PreparedStatement sp = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarTipoEmpleado(?)}");
           sp.setString(1, tipoEmpleadoNuevo.getDescripcion());
           sp.execute();
           listaTipoEmpleado.add(tipoEmpleadoNuevo);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    //Metodo para cargar los datos a la vista
    public void cargarDatos(){
        tblTipoEmpleado.setItems(getTipoEmpleado());
        colCodigoTipoEmpleado.setCellValueFactory(new PropertyValueFactory<TipoEmpleado, Integer>("codigoTipoEmpleado"));
        colDescripcion.setCellValueFactory(new PropertyValueFactory<TipoEmpleado, String>("descripcion"));
        desactivarControles();
    }
    //Metodo para ejecutar el procedimiento almacenado y llenaruna lista del resulset
    public ObservableList<TipoEmpleado> getTipoEmpleado(){
        ArrayList<TipoEmpleado> lista = new ArrayList<TipoEmpleado>();
        try{
           PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarTipoEmpleado()}");
           ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
            lista.add(new TipoEmpleado(resultado.getInt("codigoTipoEmpleado"),
                    resultado.getString("descripcion")));                        
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return listaTipoEmpleado = FXCollections.observableArrayList(lista);
    }
    
    public MainApp getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(MainApp escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
   
    //Metodo para seleccionar elementos de la table y mostrarlos en los compos de texto
    public void seleccionarElementos(){
        txtCodigoTipoEmpleado.setText(String.valueOf(((TipoEmpleado)tblTipoEmpleado.getSelectionModel().getSelectedItem()).getCodigoTipoEmpleado()));
        txtDescripcion.setText(((TipoEmpleado)tblTipoEmpleado.getSelectionModel().getSelectedItem()).getDescripcion());  
    }
      //Metodo para eliminar empresas
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
                //Verificar que tenga seleccionado un registro de la tabla
                if(tblTipoEmpleado.getSelectionModel().getSelectedItem() != null){
                    int respuesta = JOptionPane.showConfirmDialog(null, "¿Esta seguro de eliminar el registro?","Eliminar Tipo Empresa", JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
                    if(respuesta == JOptionPane.YES_OPTION){
                        try{
                            PreparedStatement sp = Conexion.getInstance().getConexion().prepareCall("{call sp_EliminarTipoEmpleado(?)}");
                            sp.setInt(1, ((TipoEmpleado)tblTipoEmpleado.getSelectionModel().getSelectedItem()).getCodigoTipoEmpleado());
                            sp.execute();
                            listaTipoEmpleado.remove(tblTipoEmpleado.getSelectionModel().getSelectedIndex());
                            limpiarControles();
                            JOptionPane.showMessageDialog(null, "Tipo Empleado eliminado con exito!!");
                         }catch(Exception e){
                             e.printStackTrace();
                         }
                    }
            }else{
                   JOptionPane.showMessageDialog(null, "Debe seleccionar un registro de la tabla"); 
                   
            }
        }
    }
     //Metodo para editar Empresas
    public void editar(){
        switch(tipoOperacion){
            case NINGUNO:
                if(tblTipoEmpleado.getSelectionModel().getSelectedItem() != null){
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
    //Metodo para actualizar los datos del modelo y del tableview ejecutando el sp
public void actualizar(){
    try{
        PreparedStatement sp = Conexion.getInstance().getConexion().prepareCall("{call sp_ActualizarTipoEmpleado(?,?)}");
        TipoEmpleado tipoEmpleadoActualizado = ((TipoEmpleado)tblTipoEmpleado.getSelectionModel().getSelectedItem());
        //Obteniendo los datos de la vista al modelo en java
        tipoEmpleadoActualizado.setDescripcion(txtDescripcion.getText());
        //Enviando los datos actualizados a ejecutar en el objeto sp
        sp.setInt(1,tipoEmpleadoActualizado.getCodigoTipoEmpleado());
        sp.setString(2,tipoEmpleadoActualizado.getDescripcion());
        sp.execute();
        JOptionPane.showMessageDialog(null, "Datos Actualizados");
    }catch(Exception e){
        e.printStackTrace();
    }
}

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargarDatos();
    }    
   
     public void menuPrincipal(){
        escenarioPrincipal.menuPrincipal();
    }
     //para llegar a empleado por mediop de tipoEmpleado
     public void empleado(){
         escenarioPrincipal.empleado();
     }
}
