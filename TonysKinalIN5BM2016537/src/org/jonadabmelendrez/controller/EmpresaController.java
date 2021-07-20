
package org.jonadabmelendrez.controller;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
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
import org.jonadabmelendrez.system.MainApp;
import org.jonadabmelendrez.bean.Empresa;
import org.jonadabmelendrez.bd.Conexion;
import org.jonadabmelendrez.report.GenerarReporte;


public class EmpresaController implements Initializable {
    private enum Operacion{NUEVO, GUARDAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO }
    private Operacion tipoOperacion = Operacion.NINGUNO;
    private MainApp escenarioPrincipal;
    private ObservableList <Empresa>listaEmpresa;
            
    @FXML
    private TextField txtCodigoEmpresa;
    @FXML
    private TextField txtNombreEmpresa;
    @FXML
    private TextField txtDireccion;
    @FXML
    private TextField txtTelefono;
    @FXML
    private Button btnNuevo;
    @FXML
    private Button btnEliminar;
    @FXML
    private Button btnEditar;
    @FXML
    private Button btnReporte;
    @FXML
    private TableView tblEmpresas;
    @FXML
    private TableColumn colCodigoEmpresa;
    @FXML
    private TableColumn colNombreEmpresa;
    @FXML
    private TableColumn colDireccion;
    @FXML
    private TableColumn colTelefono;
    //Metodo para desactivar controles
    public void desactivarControles(){
        txtCodigoEmpresa.setEditable(false);
        txtNombreEmpresa.setEditable(false);
        txtDireccion.setEditable(false);
        txtTelefono.setEditable(false);
    }
    //Metodo para activar controles
    public void activarControles(){
        //txtCodigoEmpresa.setEditable(true);
        txtNombreEmpresa.setEditable(true);
        txtDireccion.setEditable(true);
        txtTelefono.setEditable(true);
    }
    //Metodo para limpiar los controles 
    public void limpiarControles(){
        txtCodigoEmpresa.setText("");
        txtNombreEmpresa.setText("");
        txtDireccion.setText("");
        txtTelefono.setText("");
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
        Empresa empresaNueva = new Empresa();
        empresaNueva.setNombreEmpresa(txtNombreEmpresa.getText());
        empresaNueva.setDireccion(txtDireccion.getText());
        empresaNueva.setTelefono(txtTelefono.getText());
        try{
           PreparedStatement sp = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarEmpresas(?,?,?)}");
           sp.setString(1, empresaNueva.getNombreEmpresa());
           sp.setString(2, empresaNueva.getDireccion());
           sp.setString(3, empresaNueva.getTelefono());
           sp.execute();
           listaEmpresa.add(empresaNueva);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    //Metodo para cargar los datos a la vista
    public void cargarDatos(){
        tblEmpresas.setItems(getEmpresa());
        colCodigoEmpresa.setCellValueFactory(new PropertyValueFactory<Empresa, Integer>("codigoEmpresa"));
        colNombreEmpresa.setCellValueFactory(new PropertyValueFactory<Empresa, String>("nombreEmpresa"));
        colDireccion.setCellValueFactory(new PropertyValueFactory<Empresa, String>("direccion"));
        colTelefono.setCellValueFactory(new PropertyValueFactory<Empresa, String>("telefono"));
        desactivarControles();
    }
    
    //Metodo para ejecutar el procedimiento almacenado y llenaruna lista del resulset
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

    //Metodo para seleccionar elementos de la table y mostrarlos en los compos de texto
    public void seleccionarElementos(){
        txtCodigoEmpresa.setText(String.valueOf(((Empresa)tblEmpresas.getSelectionModel().getSelectedItem()).getCodigoEmpresa()));
        txtNombreEmpresa.setText(((Empresa)tblEmpresas.getSelectionModel().getSelectedItem()).getNombreEmpresa());
        txtDireccion.setText(String.valueOf(((Empresa)tblEmpresas.getSelectionModel().getSelectedItem()).getDireccion()));
        txtTelefono.setText(String.valueOf(((Empresa)tblEmpresas.getSelectionModel().getSelectedItem()).getTelefono()));   
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
                if(tblEmpresas.getSelectionModel().getSelectedItem() != null){
                    int respuesta = JOptionPane.showConfirmDialog(null, "¿Esta seguro de eliminar el registro?","Eliminar Empresa", JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
                    if(respuesta == JOptionPane.YES_OPTION){
                        try{
                            PreparedStatement sp = Conexion.getInstance().getConexion().prepareCall("{call sp_EliminarEmpresas(?)}");
                            sp.setInt(1, ((Empresa)tblEmpresas.getSelectionModel().getSelectedItem()).getCodigoEmpresa());
                            sp.execute();
                            listaEmpresa.remove(tblEmpresas.getSelectionModel().getSelectedIndex());
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
                if(tblEmpresas.getSelectionModel().getSelectedItem() != null){
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
        PreparedStatement sp = Conexion.getInstance().getConexion().prepareCall("{call sp_ActualizarEmpresas(?,?,?,?)}");
        Empresa empresaActualizada = ((Empresa)tblEmpresas.getSelectionModel().getSelectedItem());
        //Obteniendo los datos de la vista al modelo en java
        empresaActualizada.setNombreEmpresa(txtNombreEmpresa.getText());
        empresaActualizada.setDireccion(txtDireccion.getText());
        empresaActualizada.setTelefono(txtTelefono.getText());
        //Enviando los datos actualizados a ejecutar en el objeto sp
        sp.setInt(1,empresaActualizada.getCodigoEmpresa());
        sp.setString(2,empresaActualizada.getNombreEmpresa());
        sp.setString(3,empresaActualizada.getDireccion());
        sp.setString(4,empresaActualizada.getTelefono());
        sp.execute();
        JOptionPane.showMessageDialog(null, "Datos Actualizados");
    }catch(Exception e){
        e.printStackTrace();
    }
}

public void generarReporte(){
    switch(tipoOperacion){
        case NINGUNO:
               imprimirReporte();
               break;
               
 
    }
}

public void imprimirReporte(){
    Map parametros = new HashMap();
    parametros.put("codigoEmpresa", null);
    GenerarReporte.mostrarReporte("ReporteEmpresas.jasper", "Reporte de Empresas", parametros);
}

    @Override
    public void initialize(URL url, ResourceBundle resourses) {
      cargarDatos();
      
    }  
    
     public MainApp getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(MainApp escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
  //para llegar a menuPrincipal por medio de empresa
    public void menuPrincipal(){
        escenarioPrincipal.menuPrincipal();
    }
    //para llegar a servicio por medio de empresa
     public void servicio(){
         escenarioPrincipal.servicio();
     }
     //para legar a presupuesto por medio de empresa
     public void presupuesto(){
         escenarioPrincipal.presupuesto();
     }
}
