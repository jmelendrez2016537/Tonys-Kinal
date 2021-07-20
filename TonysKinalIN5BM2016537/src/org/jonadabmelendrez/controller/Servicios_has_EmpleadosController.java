
package org.jonadabmelendrez.controller;

import eu.schudt.javafx.controls.calendar.DatePicker;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
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
import javafx.scene.layout.GridPane;
import javax.swing.JOptionPane;
import org.jonadabmelendrez.bd.Conexion;
import org.jonadabmelendrez.bean.Empleado;
import org.jonadabmelendrez.bean.Servicio;
import org.jonadabmelendrez.bean.Servicios_has_Empleados;
import org.jonadabmelendrez.system.MainApp;

public class Servicios_has_EmpleadosController implements Initializable {
private MainApp escenarioPrincipal;
private enum operaciones {NUEVO, GUARDAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO}
    private operaciones tipoOperacion = operaciones.NINGUNO;
    private ObservableList<Servicios_has_Empleados> listaServicios_has_Empleados;
    private ObservableList<Servicio> listaServicio;
    private ObservableList<Empleado> listaEmpleado;
    private DatePicker fecha;
    
    @FXML private GridPane grpFechaEvento;
    @FXML private TextField txtServicios_codigoServicio;
    @FXML private TextField txtHoraEvento;
    @FXML private TextField txtLugarEvento;
    @FXML private ComboBox cmbCodigoServicio;
    @FXML private ComboBox cmbCodigoEmpleado;
    @FXML private TableView tblServicios_has_Empleados;
    @FXML private TableColumn colServicios_codigoServicio;
    @FXML private TableColumn colCodigoServicio;
    @FXML private TableColumn colCodigoEmpleado;
    @FXML private TableColumn colFechaEvento;
    @FXML private TableColumn colHoraEvento;
    @FXML private TableColumn colLugarEvento;
    @FXML private Button btnNuevo;
    @FXML private Button btnEditar;
    @FXML private Button btnEliminar;
    @FXML private Button btnReporte;

    @Override
    public void initialize(URL url, ResourceBundle resourses) {
      //cargarDatos();
      fecha = new DatePicker(Locale.ENGLISH);
        fecha.setDateFormat (new SimpleDateFormat("yyy-MM-dd"));
        fecha.getCalendarView().todayButtonTextProperty().set("Today");
        fecha.getCalendarView().setShowWeeks(false);
        grpFechaEvento.add(fecha, 3, 0);
        fecha.getStylesheets().add("/org/jonadabmelendrez/resource/DatePicker.css");          
        cmbCodigoServicio.setItems(getServicio());
        cmbCodigoEmpleado.setItems(getEmpleado());
    }    
    
     public void cargarDatos(){
        tblServicios_has_Empleados.setItems(getServicios_has_Empleados());
        colServicios_codigoServicio.setCellValueFactory(new PropertyValueFactory<Servicios_has_Empleados, Integer>("Servicios_codigoServicio"));
        colCodigoServicio.setCellValueFactory(new PropertyValueFactory<Servicios_has_Empleados,Integer>("codigoServicio"));
        colCodigoEmpleado.setCellValueFactory(new PropertyValueFactory<Servicios_has_Empleados,Integer>("codigoEmpleado"));
        colFechaEvento.setCellValueFactory(new PropertyValueFactory<Servicios_has_Empleados,Date>("fechaEvento"));
        colHoraEvento.setCellValueFactory(new PropertyValueFactory<Servicios_has_Empleados,String>("horaEvento"));
        colLugarEvento.setCellValueFactory(new PropertyValueFactory<Servicios_has_Empleados,String>("lugarEvento"));
        desactivarControles();
    }
    
     public void seleccionarElemento(){
        txtServicios_codigoServicio.setText(String.valueOf(((Servicios_has_Empleados)tblServicios_has_Empleados.getSelectionModel().getSelectedItem()).getServicios_codigoServicio())); 
        cmbCodigoServicio.getSelectionModel().select(buscarServicio(((Servicios_has_Empleados)tblServicios_has_Empleados.getSelectionModel().getSelectedItem()).getCodigoServicio()));
        cmbCodigoEmpleado.getSelectionModel().select(buscarEmpleado(((Servicios_has_Empleados)tblServicios_has_Empleados.getSelectionModel().getSelectedItem()).getCodigoEmpleado()));
        fecha.selectedDateProperty().set(((Servicios_has_Empleados)tblServicios_has_Empleados.getSelectionModel().getSelectedItem()).getFechaEvento());
        txtHoraEvento.setText(((Servicios_has_Empleados)tblServicios_has_Empleados.getSelectionModel().getSelectedItem()).getHoraEvento());
        txtLugarEvento.setText(((Servicios_has_Empleados)tblServicios_has_Empleados.getSelectionModel().getSelectedItem()).getLugarEvento());
    }
    
     public ObservableList<Servicios_has_Empleados> getServicios_has_Empleados(){
        ArrayList<Servicios_has_Empleados> lista = new ArrayList<Servicios_has_Empleados>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarServicios_has_Empleados()}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new Servicios_has_Empleados(resultado.getInt("Servicios_codigoServicio"),
                                    resultado.getInt("codigoServicio"),
                                    resultado.getInt("codigoEmpleado"),
                                    resultado.getDate("fechaEvento"),
                                    resultado.getString("horaEvento"),
                                    resultado.getString("lugarEvento")));
            }
            
        }catch(Exception e){
            e.printStackTrace();
        }
        
        return listaServicios_has_Empleados = FXCollections.observableArrayList(lista);
    }
     
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
                    resultado.getInt("codigoTipoEmpleado")));                        
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return listaEmpleado = FXCollections.observableArrayList(lista);
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
      
      public Empleado buscarEmpleado(int codigoEmpleado){
        Empleado resultado = null;
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_BuscarEmpleado(?)}");
            procedimiento.setInt(1, codigoEmpleado);
            ResultSet registro = procedimiento.executeQuery();
            while (registro.next()){
                resultado = new Empleado(registro.getInt("codigoEmpleado"),
                                    registro.getInt("numeroEmpleado"),
                                    registro.getString("apellidosEmpleado"),
                                    registro.getString("nombresEmpleado"),
                                    registro.getString("direccionEmpleado"),
                                    registro.getString("telefonoEmpleado"),
                                    registro.getString("gradoCocinero"),
                                    registro.getInt("codigoTipoEmpleado"));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        
        return resultado;
    }
     
      public void nuevo(){
        switch(tipoOperacion){
            case NINGUNO:
            activarControles();
            btnNuevo.setText("Guardar");
            btnEliminar.setText("Cancelar");
            btnEliminar.setDisable(false);
            btnEditar.setDisable(true);
            btnReporte.setDisable(true);
            tipoOperacion = operaciones.GUARDAR;
            break;
            case GUARDAR:
                guardar();
                desactivarControles();
                limpiarControles();
                btnNuevo.setText("Nuevo");
                btnEliminar.setText("Eliminar");
                btnEliminar.setDisable(false);
                btnEditar.setDisable(false);
                btnReporte.setDisable(false);
                tipoOperacion = operaciones.NINGUNO;
                cargarDatos();
                break;
        }
    }
    
    public void guardar(){
        Servicios_has_Empleados registro = new Servicios_has_Empleados();
        registro.setCodigoServicio(((Servicio)cmbCodigoServicio.getSelectionModel().getSelectedItem()).getCodigoServicio());
        registro.setCodigoEmpleado(((Empleado)cmbCodigoEmpleado.getSelectionModel().getSelectedItem()).getCodigoEmpleado());   
        registro.setFechaEvento(fecha.getSelectedDate());
        registro.setHoraEvento(txtHoraEvento.getText());
        registro.setLugarEvento(txtLugarEvento.getText());
        try{
           PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_Servicios_has_Empleados(?,?,?,?,?)}");
           procedimiento.setInt(1, registro.getCodigoServicio());
           procedimiento.setInt(2, registro.getCodigoEmpleado());
           procedimiento.setDate(3, new java.sql.Date(registro.getFechaEvento().getTime()));
           procedimiento.setString(4, registro.getHoraEvento());
           procedimiento.setString(5, registro.getLugarEvento());
           procedimiento.execute();
           listaServicios_has_Empleados.add(registro);
        }catch(Exception e){
            e.printStackTrace();
        }       
    }
      
    public void eliminar(){
        switch(tipoOperacion){
            case GUARDAR:
                desactivarControles();
                btnNuevo.setText("Nuevo");
                btnEliminar.setText("Eliminar");
                btnEditar.setDisable(false);
                btnReporte.setDisable(false);
                tipoOperacion =operaciones.NINGUNO;
                break;
            default:
                if(tblServicios_has_Empleados.getSelectionModel().getSelectedItem() != null){
                    int respuesta = JOptionPane.showConfirmDialog(null,"¿Esta seguro de eliminar el registro?", "Eliminar Servicios_has_Empleados", JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
                    if(respuesta == JOptionPane.YES_OPTION){
                        try{
                            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EliminarServicios_has_Empleados(?)}");
                            procedimiento.setInt(1,((Servicios_has_Empleados) tblServicios_has_Empleados.getSelectionModel().getSelectedItem()).getServicios_codigoServicio());
                            procedimiento.execute();
                            listaServicios_has_Empleados.remove(tblServicios_has_Empleados.getSelectionModel().getSelectedIndex());
                            limpiarControles();
                            tblServicios_has_Empleados.getSelectionModel().clearSelection();
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                    }
                }else{
                    JOptionPane.showMessageDialog(null, "Debe seleccionar un elemento");
                }
        }
    }
    
     public void editar(){
        switch(tipoOperacion){
            case NINGUNO:
                if(tblServicios_has_Empleados.getSelectionModel().getSelectedItem() != null){
                    btnEditar.setText("Actuaizar");
                    btnReporte.setText("Cancelar");
                    btnNuevo.setDisable(true);
                    btnEliminar.setDisable(true);
                    activarControles();
                    tipoOperacion = operaciones.ACTUALIZAR;
            }else{
                    JOptionPane.showMessageDialog(null, "Debe seleccionar un elemneto");
                }
                break;
            case  ACTUALIZAR:
                actualizar();
                btnEditar.setText("Editar");
                btnReporte.setText("Reporte");
                btnNuevo.setDisable(false);
                btnEliminar.setDisable(false);
                tipoOperacion = operaciones.NINGUNO;
                cargarDatos();
                break;
                
        }
    }
    
    public void actualizar(){
     try{
         PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ActualizarServicios_has_Empleados(?,?,?,?)}");
        Servicios_has_Empleados registro = (Servicios_has_Empleados)tblServicios_has_Empleados.getSelectionModel().getSelectedItem();
        registro.setServicios_codigoServicio(Integer.parseInt(txtServicios_codigoServicio.getText()));
        registro.setFechaEvento(fecha.getSelectedDate());
        registro.setHoraEvento(txtHoraEvento.getText());
        registro.setLugarEvento(txtLugarEvento.getText());
        procedimiento.setInt(1, registro.getServicios_codigoServicio());
        procedimiento.setDate(2, new java.sql.Date(registro.getFechaEvento().getTime()));
        procedimiento.setString(3, registro.getHoraEvento());
        procedimiento.setString(4, registro.getLugarEvento());
        procedimiento.execute();
     }catch(Exception e){
         e.printStackTrace();
     }   
    }
    
     public void desactivarControles(){
        txtServicios_codigoServicio.setEditable(false);
        cmbCodigoServicio.setEditable(false);
        cmbCodigoServicio.setDisable(true);
        cmbCodigoEmpleado.setEditable(false);
        cmbCodigoEmpleado.setDisable(true);
        grpFechaEvento.setDisable(true);
        txtHoraEvento.setEditable(false);
        txtLugarEvento.setEditable(false); 
    }
    
    public void activarControles(){
        txtServicios_codigoServicio.setEditable(false);
        cmbCodigoServicio.setEditable(true);
        cmbCodigoServicio.setDisable(false);
        cmbCodigoEmpleado.setEditable(true);
        cmbCodigoEmpleado.setDisable(false);
        grpFechaEvento.setDisable(false);
        txtHoraEvento.setEditable(true);
        txtLugarEvento.setEditable(true);
    }
    public void limpiarControles(){
        txtServicios_codigoServicio.setText("");
        cmbCodigoServicio.getSelectionModel().clearSelection();
        cmbCodigoEmpleado.getSelectionModel().clearSelection();
        fecha.selectedDateProperty().set(null);
        txtHoraEvento.setText("");
        txtLugarEvento.setText("");
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
