
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
import org.jonadabmelendrez.bean.Presupuesto;
import org.jonadabmelendrez.report.GenerarReporte;
import org.jonadabmelendrez.system.MainApp;

public class PresupuestoController implements Initializable {
private MainApp escenarioPrincipal;
    private enum operaciones {NUEVO, GUARDAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO}
    private operaciones tipoOperacion = operaciones.NINGUNO;
    private ObservableList<Presupuesto> listaPresupuesto;
    private ObservableList<Empresa> listaEmpresa;
    private DatePicker fecha;
    
    @FXML private GridPane grpFechaSolicitud;
    @FXML private TextField txtCodigoPresupuesto;
    @FXML private TextField txtCantidadPresupuesto;
    @FXML private ComboBox cmbCodigoEmpresa;
    @FXML private TableView tblPresupuesto;
    @FXML private TableColumn colCodigoPresupuesto;
    @FXML private TableColumn colFechaSolicitud;
    @FXML private TableColumn colCantidadPresupuesto;
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
        grpFechaSolicitud.add(fecha, 1, 1);
        fecha.getStylesheets().add("/org/jonadabmelendrez/resource/DatePicker.css");          
        cmbCodigoEmpresa.setItems(getEmpresa());
        
    }    
    
     public void cargarDatos(){
        tblPresupuesto.setItems(getPresupuesto());
        colCodigoPresupuesto.setCellValueFactory(new PropertyValueFactory<Presupuesto,Integer>("codigoPresupuesto"));
        colFechaSolicitud.setCellValueFactory(new PropertyValueFactory<Presupuesto,Date>("fechaSolicitud"));
        colCantidadPresupuesto.setCellValueFactory(new PropertyValueFactory<Presupuesto,Integer>("cantidadPresupuesto"));
        colCodigoEmpresa.setCellValueFactory(new PropertyValueFactory<Presupuesto,Integer>("codigoEmpresa"));
        desactivarControles();
    }
     
    public void seleccionarElemento(){
        txtCodigoPresupuesto.setText(String.valueOf(((Presupuesto)tblPresupuesto.getSelectionModel().getSelectedItem()).getCodigoPresupuesto()));
        fecha.selectedDateProperty().set(((Presupuesto)tblPresupuesto.getSelectionModel().getSelectedItem()).getFechaSolicitud());
        txtCantidadPresupuesto.setText(String.valueOf(((Presupuesto)tblPresupuesto.getSelectionModel().getSelectedItem()).getCantidadPresupuesto()));
        cmbCodigoEmpresa.getSelectionModel().select(buscarEmpresa(((Presupuesto)tblPresupuesto.getSelectionModel().getSelectedItem()).getCodigoEmpresa()));
    }
     
    public ObservableList<Presupuesto> getPresupuesto(){
        ArrayList<Presupuesto> lista = new ArrayList<Presupuesto>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarPresupuesto()}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new Presupuesto(resultado.getInt("codigoPresupuesto"),
                                    resultado.getDate("fechaSolicitud"),
                                    resultado.getInt("cantidadPresupuesto"),
                                    resultado.getInt("codigoEmpresa")));
            }
            
        }catch(Exception e){
            e.printStackTrace();
        }
        
        return listaPresupuesto = FXCollections.observableArrayList(lista);
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
        Presupuesto registro = new Presupuesto();
        //registro.setCodigoServicio(Integer.parseInt(txtCodigoServicio.getText()));
        registro.setFechaSolicitud(fecha.getSelectedDate());
        registro.setCantidadPresupuesto(Integer.parseInt(txtCantidadPresupuesto.getText()));
        registro.setCodigoEmpresa(((Empresa)cmbCodigoEmpresa.getSelectionModel().getSelectedItem()).getCodigoEmpresa());
        try{
           PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarPresupuesto(?,?,?)}");
           //procedimiento.setInt(1, registro.getCodigoServicio());
           procedimiento.setDate(1, new java.sql.Date(registro.getFechaSolicitud().getTime()));
           procedimiento.setInt(2, registro.getCantidadPresupuesto());
           procedimiento.setInt(3, registro.getCodigoEmpresa());
           procedimiento.execute();
           listaPresupuesto.add(registro);
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
                if(tblPresupuesto.getSelectionModel().getSelectedItem() != null){
                    int respuesta = JOptionPane.showConfirmDialog(null,"¿Esta seguro de eliminar el registro?", "Eliminar Presupuesto", JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
                    if(respuesta == JOptionPane.YES_OPTION){
                        try{
                            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EliminarPresupuesto(?)}");
                            procedimiento.setInt(1,((Presupuesto) tblPresupuesto.getSelectionModel().getSelectedItem()).getCodigoPresupuesto());
                            procedimiento.execute();
                            listaPresupuesto.remove(tblPresupuesto.getSelectionModel().getSelectedIndex());
                            limpiarControles();
                            tblPresupuesto.getSelectionModel().clearSelection();
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
                if(tblPresupuesto.getSelectionModel().getSelectedItem() != null){
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
         PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ActualizarPresupuesto(?,?,?)}");
         Presupuesto registro = (Presupuesto)tblPresupuesto.getSelectionModel().getSelectedItem();
        registro.setCodigoPresupuesto(Integer.parseInt(txtCodigoPresupuesto.getText()));
        registro.setFechaSolicitud(fecha.getSelectedDate());
        registro.setCantidadPresupuesto(Integer.parseInt(txtCantidadPresupuesto.getText()));
        procedimiento.setInt(1, registro.getCodigoPresupuesto());
        procedimiento.setDate(2, new java.sql.Date(registro.getFechaSolicitud().getTime()));
        procedimiento.setInt(3, registro.getCantidadPresupuesto());
        procedimiento.execute();
     }catch(Exception e){
         e.printStackTrace();
     }   
    }
    
    public void generarReporte(){
        switch(tipoOperacion){
            case NINGUNO:
                if(tblPresupuesto.getSelectionModel().getSelectedItem() != null){
                imprimirReporte();
            }else{
                    JOptionPane.showMessageDialog(null, "Debe seleccionar un elemento");
                    }
                break;    
        }
    }
    
    public void imprimirReporte(){
        Map parametros = new HashMap();
        int codEmpresa = Integer.valueOf(((Empresa)cmbCodigoEmpresa.getSelectionModel().getSelectedItem()).getCodigoEmpresa());
        parametros.put("codEmpresa", codEmpresa);
        GenerarReporte.mostrarReporte("ReportePresupuesto.jasper","Reporte de Presupuesto", parametros);
    }
    
    public void desactivarControles(){
        txtCodigoPresupuesto.setEditable(false);
        txtCantidadPresupuesto.setEditable(false);
        grpFechaSolicitud.setDisable(true);
        cmbCodigoEmpresa.setEditable(false);
        cmbCodigoEmpresa.setDisable(true);
    }
    
    public void activarControles(){
        txtCodigoPresupuesto.setEditable(false);
        txtCantidadPresupuesto.setEditable(true);
        grpFechaSolicitud.setDisable(false);
        cmbCodigoEmpresa.setEditable(true);
        cmbCodigoEmpresa.setDisable(false);
    }
    public void limpiarControles(){
        txtCodigoPresupuesto.setText("");
        txtCantidadPresupuesto.setText("");
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
