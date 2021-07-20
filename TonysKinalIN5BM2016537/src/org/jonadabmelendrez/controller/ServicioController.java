
package org.jonadabmelendrez.controller;
import eu.schudt.javafx.controls.calendar.DatePicker;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
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
import org.jonadabmelendrez.bean.Empresa;
import org.jonadabmelendrez.bean.Servicio;
import org.jonadabmelendrez.report.GenerarReporte;
import org.jonadabmelendrez.system.MainApp;


public class ServicioController implements Initializable {
    private MainApp escenarioPrincipal;
    
    private enum operaciones {NUEVO, GUARDAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO}
    private operaciones tipoOperacion = operaciones.NINGUNO;
    private ObservableList<Servicio> listaServicio;
    private ObservableList<Empresa> listaEmpresa;
    private DatePicker fecha;
    
    @FXML private TextField txtHoraServicio;
    @FXML private TextField txtTipoServicio;
    @FXML private TextField txtCodigoServicio;
    @FXML private TextField txtLugarServicio;
    @FXML private TextField txtTelefonoContacto;
    @FXML private GridPane grpFechaServicio;
    @FXML private ComboBox cmbCodigoEmpresa;
    @FXML private TableView tblServicios;
    @FXML private TableColumn colCodigoServicio;
    @FXML private TableColumn colFechaServicio;
    @FXML private TableColumn colTipoServicio;
    @FXML private TableColumn colHoraServicio;
    @FXML private TableColumn colLugarServicio;
    @FXML private TableColumn colTelefonoContacto;
    @FXML private TableColumn colCodigoEmpresa;
    @FXML private Button btnNuevo;
    @FXML private Button btnEditar;
    @FXML private Button btnEliminar;
    @FXML private Button btnReporte;
    

    @Override
    public void initialize(URL url, ResourceBundle resourses) {
        cargarDatos();
        fecha = new DatePicker(Locale.ENGLISH);
        fecha.setDateFormat (new SimpleDateFormat("yyy-MM-dd"));
        fecha.getCalendarView().todayButtonTextProperty().set("Today");
        fecha.getCalendarView().setShowWeeks(false);
        grpFechaServicio.add(fecha, 1, 1);
        fecha.getStylesheets().add("/org/jonadabmelendrez/resource/DatePicker.css");          
        cmbCodigoEmpresa.setItems(getEmpresa());
    }    
    public void cargarDatos(){
        tblServicios.setItems(getServicio());
        colCodigoServicio.setCellValueFactory(new PropertyValueFactory<Servicio,Integer>("codigoServicio"));
        colFechaServicio.setCellValueFactory(new PropertyValueFactory<Servicio,Date>("fechaServicio"));
        colTipoServicio.setCellValueFactory(new PropertyValueFactory<Servicio,String>("tipoServicio"));
        colHoraServicio.setCellValueFactory(new PropertyValueFactory<Servicio,String>("horaServicio"));
        colLugarServicio.setCellValueFactory(new PropertyValueFactory<Servicio,String>("lugarServicio"));
        colTelefonoContacto.setCellValueFactory(new PropertyValueFactory<Servicio,String>("telefonoContacto"));
        colCodigoEmpresa.setCellValueFactory(new PropertyValueFactory<Servicio,Integer>("codigoEmpresa"));
        desactivarControles();
    }
    
        public void seleccionarElemento(){
            txtCodigoServicio.setText(String.valueOf(((Servicio)tblServicios.getSelectionModel().getSelectedItem()).getCodigoServicio()));
            fecha.selectedDateProperty().set(((Servicio)tblServicios.getSelectionModel().getSelectedItem()).getFechaServicio());
            txtTipoServicio.setText(((Servicio)tblServicios.getSelectionModel().getSelectedItem()).getTipoServicio());
            txtHoraServicio.setText(((Servicio)tblServicios.getSelectionModel().getSelectedItem()).getHoraServicio());
            txtLugarServicio.setText(((Servicio)tblServicios.getSelectionModel().getSelectedItem()).getLugarServicio());
            txtTelefonoContacto.setText(((Servicio)tblServicios.getSelectionModel().getSelectedItem()).getTelefonoContacto());
            cmbCodigoEmpresa.getSelectionModel().select(buscarEmpresa(((Servicio)tblServicios.getSelectionModel().getSelectedItem()).getCodigoEmpresa()));
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
    
    public ObservableList<Empresa> getEmpresa(){
        ArrayList<Empresa> lista = new ArrayList<Empresa>();
        try{
           PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarEmpresas()}");
           ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
            lista.add(new Empresa(resultado.getInt("codigoEmpresa"),
                    resultado.getString("nombreEmpresa"),
                    resultado.getString("direccion"),
                    resultado.getString("telefono")));                        
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return listaEmpresa = FXCollections.observableArrayList(lista);
    }
    
    public Empresa buscarEmpresa(int codigoEmpresa){
        Empresa resultado = null;
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_BuscarEmpresas(?)}");
            procedimiento.setInt(1, codigoEmpresa);
            ResultSet registro = procedimiento.executeQuery();
            while (registro.next()){
                resultado = new Empresa(registro.getInt("codigoEmpresa"),
                                    registro.getString("nombreEmpresa"),
                                    registro.getString("direccion"),
                                    registro.getString("telefono"));
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
        Servicio registro = new Servicio();
        //registro.setCodigoServicio(Integer.parseInt(txtCodigoServicio.getText()));
        registro.setFechaServicio(fecha.getSelectedDate());
        registro.setTipoServicio(txtTipoServicio.getText());
        registro.setHoraServicio(txtHoraServicio.getText());
        registro.setLugarServicio(txtLugarServicio.getText());
        registro.setTelefonoContacto(txtTelefonoContacto.getText());
        registro.setCodigoEmpresa(((Empresa)cmbCodigoEmpresa.getSelectionModel().getSelectedItem()).getCodigoEmpresa());
        try{
           PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarServicios(?,?,?,?,?,?)}");
           //procedimiento.setInt(1, registro.getCodigoServicio());
           procedimiento.setDate(1, new java.sql.Date(registro.getFechaServicio().getTime()));
           procedimiento.setString(2, registro.getTipoServicio());
           procedimiento.setString(3, registro.getHoraServicio());
           procedimiento.setString(4, registro.getLugarServicio());
           procedimiento.setString(5, registro.getTelefonoContacto());
           procedimiento.setInt(6, registro.getCodigoEmpresa());
           procedimiento.execute();
           listaServicio.add(registro);
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
                if(tblServicios.getSelectionModel().getSelectedItem() != null){
                    int respuesta = JOptionPane.showConfirmDialog(null,"¿Esta seguro de eliminar el registro?", "Eliminar Servicio", JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
                    if(respuesta == JOptionPane.YES_OPTION){
                        try{
                            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EliminarServicios(?)}");
                            procedimiento.setInt(1,((Servicio) tblServicios.getSelectionModel().getSelectedItem()).getCodigoServicio());
                            procedimiento.execute();
                            listaServicio.remove(tblServicios.getSelectionModel().getSelectedIndex());
                            limpiarControles();
                            tblServicios.getSelectionModel().clearSelection();
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
                if(tblServicios.getSelectionModel().getSelectedItem() != null){
                    btnEditar.setText("Actuaizar");
                    btnReporte.setText("Cancelar");
                    btnNuevo.setDisable(true);
                    btnEliminar.setDisable(true);
                    activarControles();
                    cmbCodigoEmpresa.setEditable(false);
                    cmbCodigoEmpresa.setDisable(true);
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
         PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ActualizarServicios(?,?,?,?,?,?)}");
         Servicio registro = (Servicio)tblServicios.getSelectionModel().getSelectedItem();
        registro.setCodigoServicio(Integer.parseInt(txtCodigoServicio.getText()));
        registro.setFechaServicio(fecha.getSelectedDate());
        registro.setTipoServicio(txtTipoServicio.getText());
        registro.setHoraServicio(txtHoraServicio.getText());
        registro.setLugarServicio(txtLugarServicio.getText());
        registro.setTelefonoContacto(txtTelefonoContacto.getText());
        procedimiento.setInt(1, registro.getCodigoServicio());
        procedimiento.setDate(2, new java.sql.Date(registro.getFechaServicio().getTime()));
        procedimiento.setString(3, registro.getTipoServicio());
        procedimiento.setString(4, registro.getHoraServicio());
        procedimiento.setString(5, registro.getLugarServicio());
        procedimiento.setString(6, registro.getTelefonoContacto());
        procedimiento.execute();
     }catch(Exception e){
         e.printStackTrace();
     }   
    }
    
    public void generarReporte(){
        switch(tipoOperacion){
            case NINGUNO:
                if(tblServicios.getSelectionModel().getSelectedItem() != null){
                imprimirReporte();
            }else{
                    JOptionPane.showMessageDialog(null, "Debe seleccionar un elemento");
                    }
                break;    
        }
    }
  
    public void imprimirReporte(){
        Map parametros = new HashMap();
        int codServicio = (Integer.parseInt(txtCodigoServicio.getText()));
        parametros.put("codServicio", codServicio);
        GenerarReporte.mostrarReporte("ReporteServicios.jasper","Reporte de Servicios", parametros);
    }
    
    public void desactivarControles(){
        txtCodigoServicio.setEditable(false);
        txtTipoServicio.setEditable(false);
        txtHoraServicio.setEditable(false);
        txtLugarServicio.setEditable(false);
        txtTelefonoContacto.setEditable(false);
        grpFechaServicio.setDisable(true);
        cmbCodigoEmpresa.setEditable(false);
        cmbCodigoEmpresa.setDisable(true);
    }
    
    public void activarControles(){
        txtCodigoServicio.setEditable(false);
        txtTipoServicio.setEditable(true);
        txtHoraServicio.setEditable(true);
        txtLugarServicio.setEditable(true);
        txtTelefonoContacto.setEditable(true);
        grpFechaServicio.setDisable(false);
        cmbCodigoEmpresa.setDisable(false);
    }
    public void limpiarControles(){
        txtCodigoServicio.setText("");
        txtTipoServicio.setText("");
        txtHoraServicio.setText("");
        txtLugarServicio.setText("");
        txtTelefonoContacto.setText("");
        fecha.selectedDateProperty().set(null);
        cmbCodigoEmpresa.getSelectionModel().clearSelection();
        //cmbEmpresas_codigoEmpresa.getSelectionModel().select(null);
    }
    
     public MainApp getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(MainApp escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
    
    public void empresa(){
        escenarioPrincipal.empresa();
    }
}
