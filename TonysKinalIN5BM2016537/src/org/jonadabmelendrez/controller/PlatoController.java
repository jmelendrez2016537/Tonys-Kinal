
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
import org.jonadabmelendrez.bean.Plato;
import org.jonadabmelendrez.bean.TipoPlato;
import org.jonadabmelendrez.system.MainApp;

public class PlatoController implements Initializable {
private MainApp escenarioPrincipal;
  private enum Operacion{NUEVO, GUARDAR, ELIMINAR, EDITAR, ACTUALIZAR, CALCELAR, NINGUNO};
    private Operacion tipoOperacion = Operacion.NINGUNO;
    private ObservableList <Plato>listaPlato;
    private ObservableList <TipoPlato>listaTipoPlato;

    @FXML private TextField txtCodigoPlato;
    @FXML private TextField txtCantidad;
    @FXML private TextField txtNombrePlato;
    @FXML private TextField txtDescripcionPlato;
    @FXML private TextField txtPrecioPlato;
    @FXML private ComboBox cmbCodigoTipoPlato;
    @FXML private TableView tblPlato;
    @FXML private TableColumn colCodigoPlato;
    @FXML private TableColumn colCantidad;
    @FXML private TableColumn colNombrePlato;
    @FXML private TableColumn colDescripcionPlato;
    @FXML private TableColumn colPrecioPlato;
    @FXML private TableColumn colCodigoTipoPlato;
    @FXML private Button btnNuevo;
    @FXML private Button btnEditar;
    @FXML private Button btnEliminar;
    @FXML private Button btnReporte;

    @Override
    public void initialize(URL url, ResourceBundle resourses) {
        cargarDatos();
        cmbCodigoTipoPlato.setItems(getTipoPlato());
    }    
    
    //Metodo para desactivar controles
    public void desactivarControles(){
        txtCodigoPlato.setEditable(false);
        txtCantidad.setEditable(false);
        txtNombrePlato.setEditable(false);
        txtDescripcionPlato.setEditable(false);
        txtPrecioPlato.setEditable(false);
        cmbCodigoTipoPlato.setEditable(false);
        cmbCodigoTipoPlato.setDisable(true);
    }
    //Metodo para activar controles
    public void activarControles(){
        txtCodigoPlato.setEditable(false);
        txtCantidad.setEditable(true);
        txtNombrePlato.setEditable(true);
        txtDescripcionPlato.setEditable(true);
        txtPrecioPlato.setEditable(true);
        cmbCodigoTipoPlato.setDisable(false);
    }
    //Metodo para limpiar los controles 
    public void limpiarControles(){
        txtCodigoPlato.setText("");
        txtCantidad.setText("");
        txtNombrePlato.setText("");
        txtDescripcionPlato.setText("");
        txtPrecioPlato.setText("");
        cmbCodigoTipoPlato.getSelectionModel().clearSelection();
    }
    
    public void cargarDatos(){
        tblPlato.setItems(getPlato());
        colCodigoPlato.setCellValueFactory(new PropertyValueFactory<Plato, Integer>("codigoPlato"));
        colCantidad.setCellValueFactory(new PropertyValueFactory<Plato, Integer>("cantidad"));
        colNombrePlato.setCellValueFactory(new PropertyValueFactory<Plato, String>("nombrePlato"));
        colDescripcionPlato.setCellValueFactory(new PropertyValueFactory<Plato, String>("descripcionPlato"));
        colPrecioPlato.setCellValueFactory(new PropertyValueFactory<Plato, Integer>("precioPlato"));
        colCodigoTipoPlato.setCellValueFactory(new PropertyValueFactory<Plato, Integer>("codigoTipoPlato"));
        desactivarControles();
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
        txtCodigoPlato.setText(String.valueOf(((Plato)tblPlato.getSelectionModel().getSelectedItem()).getCodigoPlato()));
        txtCantidad.setText(String.valueOf(((Plato)tblPlato.getSelectionModel().getSelectedItem()).getCantidad()));
        txtNombrePlato.setText(((Plato)tblPlato.getSelectionModel().getSelectedItem()).getNombrePlato());
        txtDescripcionPlato.setText(((Plato)tblPlato.getSelectionModel().getSelectedItem()).getDescripcionPlato()); 
        txtPrecioPlato.setText(String.valueOf(((Plato)tblPlato.getSelectionModel().getSelectedItem()).getPrecioPlato()));  
        cmbCodigoTipoPlato.getSelectionModel().select(buscarTipoPlato(((Plato)tblPlato.getSelectionModel().getSelectedItem()).getCodigoTipoPlato()));
    }
      public TipoPlato buscarTipoPlato(int codigoTipoPlato){
        TipoPlato resultado = null;
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_BuscarTipoPlato(?)}");
            procedimiento.setInt(1, codigoTipoPlato);
            ResultSet registro = procedimiento.executeQuery();
            while (registro.next()){
                resultado = new TipoPlato(registro.getInt("codigoTipoPlato"),
                                    registro.getString("descripcionTipo"));
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
        Plato platoNuevo = new Plato();
        platoNuevo.setCantidad(Integer.parseInt(txtCantidad.getText()));
        platoNuevo.setNombrePlato(txtNombrePlato.getText());
        platoNuevo.setDescripcionPlato(txtDescripcionPlato.getText());
        platoNuevo.setPrecioPlato(Integer.parseInt(txtPrecioPlato.getText()));
        platoNuevo.setCodigoTipoPlato(((TipoPlato)cmbCodigoTipoPlato.getSelectionModel().getSelectedItem()).getCodigoTipoPlato());
        try{
           PreparedStatement sp = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarPlato(?,?,?,?,?)}");
           sp.setInt(1, platoNuevo.getCantidad());
           sp.setString(2, platoNuevo.getNombrePlato());
           sp.setString(3, platoNuevo.getDescripcionPlato());
           sp.setInt(4, platoNuevo.getPrecioPlato());
           sp.setInt(5, platoNuevo.getCodigoTipoPlato());
           sp.execute();
           listaPlato.add(platoNuevo);
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
                //Verificar que tenga seleccionado un registro de la tabla
                if(tblPlato.getSelectionModel().getSelectedItem() != null){
                    int respuesta = JOptionPane.showConfirmDialog(null, "¿Esta seguro de eliminar el registro?","Eliminar Plato", JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
                    if(respuesta == JOptionPane.YES_OPTION){
                        try{
                            PreparedStatement sp = Conexion.getInstance().getConexion().prepareCall("{call sp_EliminarPlato(?)}");
                            sp.setInt(1, ((Plato)tblPlato.getSelectionModel().getSelectedItem()).getCodigoPlato());
                            sp.execute();
                            listaPlato.remove(tblPlato.getSelectionModel().getSelectedIndex());
                            limpiarControles();
                            JOptionPane.showMessageDialog(null, "Plato eliminado con exito!!");
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
                if(tblPlato.getSelectionModel().getSelectedItem() != null){
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
        PreparedStatement sp = Conexion.getInstance().getConexion().prepareCall("{call sp_ActualizarPlato(?,?,?,?,?,?,?)}");
        Plato platoActualizado = ((Plato)tblPlato.getSelectionModel().getSelectedItem());
        platoActualizado.setCodigoPlato(Integer.parseInt(txtCodigoPlato.getText()));
        platoActualizado.setCantidad(Integer.parseInt(txtCantidad.getText()));
        platoActualizado.setNombrePlato(txtNombrePlato.getText());
        platoActualizado.setDescripcionPlato(txtDescripcionPlato.getText());
        platoActualizado.setPrecioPlato(Integer.parseInt(txtPrecioPlato.getText()));
        sp.setInt(1,platoActualizado.getCodigoPlato());
        sp.setInt(2,platoActualizado.getCantidad());
        sp.setString(3,platoActualizado.getNombrePlato());
        sp.setString(4,platoActualizado.getDescripcionPlato());
        sp.setInt(5,platoActualizado.getPrecioPlato());
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
    
    public void tipoPlato(){
        escenarioPrincipal.tipoPlato();
    }
}
