
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javax.swing.JOptionPane;
import org.jonadabmelendrez.bd.Conexion;
import org.jonadabmelendrez.bean.Empleado;
import org.jonadabmelendrez.bean.TipoEmpleado;
import org.jonadabmelendrez.system.MainApp;


public class EmpleadoController implements Initializable {
    private enum Operacion{NUEVO, GUARDAR, ELIMINAR, EDITAR, ACTUALIZAR, CALCELAR, NINGUNO};
    private Operacion tipoOperacion = Operacion.NINGUNO;
    private MainApp escenarioPrincipal;
    private ObservableList <Empleado>listaEmpleado;
    private ObservableList <TipoEmpleado>listaTipoEmpleado;
    
    @FXML private TableView tblEmpleado;
    @FXML private TableColumn colCodigoEmpleado;
    @FXML private TableColumn colNumeroEmpleado;
    @FXML private TableColumn colApellidosEmpleado;
    @FXML private TableColumn colNombresEmpleado;
    @FXML private TableColumn colDireccionEmpleado;
    @FXML private TableColumn colTelefonoContacto;
    @FXML private TableColumn colGradoCocinero;
    @FXML private TableColumn colCodigoTipoEmpleado;
    @FXML private TextField txtCodigoEmpleado;
    @FXML private TextField txtNumeroEmpleado;
    @FXML private TextField txtApellidosEmpleado;
    @FXML private TextField txtNombresEmpleado;
    @FXML private TextField txtDireccionEmpleado;
    @FXML private TextField txtTelefonoContacto;
    @FXML private TextField txtGradoCocinero;
    @FXML private ComboBox cmbCodigoTipoEmpleado;
    @FXML private Button btnNuevo;
    @FXML private Button btnEditar;
    @FXML private Button btnEliminar;
    @FXML private Button btnReporte;
    
    @Override
    public void initialize(URL url, ResourceBundle resourses) {
        cargarDatos();
        cmbCodigoTipoEmpleado.setItems(getTipoEmpleado());
    }
    
    //Metodo para desactivar controles
    public void desactivarControles(){
        txtCodigoEmpleado.setEditable(false);
        txtNumeroEmpleado.setEditable(false);
        txtApellidosEmpleado.setEditable(false);
        txtNombresEmpleado.setEditable(false);
        txtDireccionEmpleado.setEditable(false);
        txtTelefonoContacto.setEditable(false);
        txtGradoCocinero.setEditable(false);
        cmbCodigoTipoEmpleado.setEditable(false);
        cmbCodigoTipoEmpleado.setDisable(true);
    }
    //Metodo para activar controles
    public void activarControles(){
        //txtCodigoEmpleado.setEditable(true);
        txtNumeroEmpleado.setEditable(true);
        txtApellidosEmpleado.setEditable(true);
        txtNombresEmpleado.setEditable(true);
        txtDireccionEmpleado.setEditable(true);
        txtTelefonoContacto.setEditable(true);
        txtGradoCocinero.setEditable(true);
        cmbCodigoTipoEmpleado.setDisable(false);
    }
    //Metodo para limpiar los controles 
    public void limpiarControles(){
        txtCodigoEmpleado.setText("");
        txtNumeroEmpleado.setText("");
        txtApellidosEmpleado.setText("");
        txtNombresEmpleado.setText("");
        txtDireccionEmpleado.setText("");
        txtTelefonoContacto.setText("");
        txtGradoCocinero.setText("");
        cmbCodigoTipoEmpleado.getSelectionModel().clearSelection();
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
        Empleado registroNuevo = new Empleado();
        registroNuevo.setNumeroEmpleado(Integer.parseInt(txtNumeroEmpleado.getText()));
        registroNuevo.setApellidosEmpleado(txtApellidosEmpleado.getText());
        registroNuevo.setNombresEmpleado(txtNombresEmpleado.getText());
        registroNuevo.setDireccionEmpleado(txtDireccionEmpleado.getText());
        registroNuevo.setTelefonoContacto(txtTelefonoContacto.getText());
        registroNuevo.setGradoCocinero(txtGradoCocinero.getText());
        registroNuevo.setCodigoTipoEmpleado(((TipoEmpleado)cmbCodigoTipoEmpleado.getSelectionModel().getSelectedItem()).getCodigoTipoEmpleado());
        try{
           PreparedStatement sp = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarEmpleados(?,?,?,?,?,?,?)}");
           sp.setInt(1, registroNuevo.getNumeroEmpleado());
           sp.setString(2, registroNuevo.getApellidosEmpleado());
           sp.setString(3, registroNuevo.getNombresEmpleado());
           sp.setString(4, registroNuevo.getDireccionEmpleado());
           sp.setString(5, registroNuevo.getTelefonoContacto());
           sp.setString(6, registroNuevo.getGradoCocinero());
           sp.setInt(7, registroNuevo.getCodigoTipoEmpleado());
           sp.execute();
           listaEmpleado.add(registroNuevo);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
     //Metodo para cargar los datos a la vista
    public void cargarDatos(){
        tblEmpleado.setItems(getEmpleado());
        colCodigoEmpleado.setCellValueFactory(new PropertyValueFactory<Empleado, Integer>("codigoEmpleado"));
        colNumeroEmpleado.setCellValueFactory(new PropertyValueFactory<Empleado, Integer>("numeroEmpleado"));
        colApellidosEmpleado.setCellValueFactory(new PropertyValueFactory<Empleado, String>("apellidosEmpleado"));
        colNombresEmpleado.setCellValueFactory(new PropertyValueFactory<Empleado, String>("nombresEmpleado"));
        colDireccionEmpleado.setCellValueFactory(new PropertyValueFactory<Empleado, String>("direccionEmpleado"));
        colTelefonoContacto.setCellValueFactory(new PropertyValueFactory<Empleado, String>("telefonoContacto"));
        colGradoCocinero.setCellValueFactory(new PropertyValueFactory<Empleado, Integer>("gradoCocinero"));
        colCodigoTipoEmpleado.setCellValueFactory(new PropertyValueFactory<Empleado, Integer>("codigoTipoEmpleado"));
        desactivarControles();
    }
    
    //Metodo para ejecutar el procedimiento almacenado y llenaruna lista del resulset
    public ObservableList<Empleado> getEmpleado(){
        ArrayList<Empleado> lista = new ArrayList<Empleado>();
        try{
           PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarEmpleados()}");
           ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
            lista.add(new Empleado(resultado.getInt("codigoEmpleado"),
                    resultado.getInt("numeroEmpleado"),
                    resultado.getString("apellidosEmpleado"),
                    resultado.getString("nombresEmpleado"),
                    resultado.getString("direccionEmpleado"),
                    resultado.getString("telefonoContacto"),
                    resultado.getString("gradoCocinero"),
                    resultado.getInt("tipoEmpleado_codigoTipoEmpleado")));                        
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return listaEmpleado = FXCollections.observableArrayList(lista);
    }
    
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
        txtCodigoEmpleado.setText(String.valueOf(((Empleado)tblEmpleado.getSelectionModel().getSelectedItem()).getCodigoEmpleado()));
        txtNumeroEmpleado.setText(String.valueOf(((Empleado)tblEmpleado.getSelectionModel().getSelectedItem()).getNumeroEmpleado()));
        txtApellidosEmpleado.setText(((Empleado)tblEmpleado.getSelectionModel().getSelectedItem()).getApellidosEmpleado());
        txtNombresEmpleado.setText(((Empleado)tblEmpleado.getSelectionModel().getSelectedItem()).getNombresEmpleado()); 
        txtDireccionEmpleado.setText(((Empleado)tblEmpleado.getSelectionModel().getSelectedItem()).getDireccionEmpleado()); 
        txtTelefonoContacto.setText(((Empleado)tblEmpleado.getSelectionModel().getSelectedItem()).getTelefonoContacto()); 
        txtGradoCocinero.setText(((Empleado)tblEmpleado.getSelectionModel().getSelectedItem()).getGradoCocinero()); 
        cmbCodigoTipoEmpleado.getSelectionModel().select(buscarTipoEmpleado(((Empleado)tblEmpleado.getSelectionModel().getSelectedItem()).getCodigoTipoEmpleado()));
    }
    public TipoEmpleado buscarTipoEmpleado(int codigoTipoEmpleado){
        TipoEmpleado resultado = null;
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_BuscarTipoEmpleado(?)}");
            procedimiento.setInt(1, codigoTipoEmpleado);
            ResultSet registro = procedimiento.executeQuery();
            while (registro.next()){
                resultado = new TipoEmpleado(registro.getInt("codigoTipoEmpleado"),
                                    registro.getString("descripcion"));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        
        return resultado;
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
                if(tblEmpleado.getSelectionModel().getSelectedItem() != null){
                    int respuesta = JOptionPane.showConfirmDialog(null, "¿Esta seguro de eliminar el registro?","Eliminar Empleado", JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
                    if(respuesta == JOptionPane.YES_OPTION){
                        try{
                            PreparedStatement sp = Conexion.getInstance().getConexion().prepareCall("{call sp_EliminarEmpleados(?)}");
                            sp.setInt(1, ((Empleado)tblEmpleado.getSelectionModel().getSelectedItem()).getCodigoEmpleado());
                            sp.execute();
                            listaEmpleado.remove(tblEmpleado.getSelectionModel().getSelectedIndex());
                            limpiarControles();
                            JOptionPane.showMessageDialog(null, "Empresa eliminada con exito!!");
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
                if(tblEmpleado.getSelectionModel().getSelectedItem() != null){
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
        PreparedStatement sp = Conexion.getInstance().getConexion().prepareCall("{call sp_ActualizarEmpleados(?,?,?,?,?,?,?)}");
        Empleado empleadoActualizado = ((Empleado)tblEmpleado.getSelectionModel().getSelectedItem());
        //Obteniendo los datos de la vista al modelo en java
        empleadoActualizado.setCodigoEmpleado(Integer.parseInt(txtCodigoEmpleado.getText()));
        empleadoActualizado.setNumeroEmpleado(Integer.parseInt(txtNumeroEmpleado.getText()));
        empleadoActualizado.setApellidosEmpleado(txtApellidosEmpleado.getText());
        empleadoActualizado.setNombresEmpleado(txtNombresEmpleado.getText());
        empleadoActualizado.setDireccionEmpleado(txtDireccionEmpleado.getText());
        empleadoActualizado.setTelefonoContacto(txtTelefonoContacto.getText());
        empleadoActualizado.setGradoCocinero(txtGradoCocinero.getText());
        //Enviando los datos actualizados a ejecutar en el objeto sp
        sp.setInt(1,empleadoActualizado.getCodigoEmpleado());
        sp.setInt(2,empleadoActualizado.getNumeroEmpleado());
        sp.setString(3,empleadoActualizado.getApellidosEmpleado());
        sp.setString(4,empleadoActualizado.getNombresEmpleado());
        sp.setString(5,empleadoActualizado.getDireccionEmpleado());
        sp.setString(6,empleadoActualizado.getTelefonoContacto());
        sp.setString(7,empleadoActualizado.getGradoCocinero());
        sp.execute();
        JOptionPane.showMessageDialog(null, "Datos Actualizados");
    }catch(Exception e){
        e.printStackTrace();
    }
}
   
    public void tipoEmpleado(){
        escenarioPrincipal.tipoEmpleado();
    }
}
