package View.coordinador.controller;

import DAO.DAOProyecto;
import Models.Organizacion;
import Models.Proyecto;
import Models.ResponsableProyecto;
import View.MainController;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import tools.LimitadorTextfield;
import tools.Logger;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class VerProyectoController implements Initializable {
	
	@FXML
	private Label lbOrganizacion;
	@FXML
	private JFXTextField txtNombre;
	@FXML
	private JFXTextArea txtDescripcion;
	@FXML
	private JFXTextField txtObjetivoGeneral;
	@FXML
	private JFXTextField txtObjetivoMediato;
	@FXML
	private JFXTextField txtObjetivoInmediato;
	@FXML
	private JFXTextField txtMetodologia;
	@FXML
	private JFXTextArea txtRecursos;
	@FXML
	private JFXTextArea txtResponsabilidades;
	@FXML
	private JFXTextField txtCapacidad;
	@FXML
	private JFXTextField txtPosicionResponsable;
	@FXML
	private JFXTextField txtEmailResponsable;
	@FXML
	private JFXTextField txtNombreResponsable;
	@FXML
	private JFXTextField txtApellidosResponsable;
	@FXML
	private JFXComboBox<String> cmbArea;
	@FXML
	private JFXComboBox<String> cmbPeriodo;
	
	@FXML
	private JFXDatePicker fechaInicial;
	@FXML
	private JFXDatePicker fechaFinal;
	
	private Proyecto proyecto;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		ObservableList<String> listaAreas = FXCollections.observableArrayList();
		try {
			Proyecto.fillAreaTable(listaAreas);
		} catch (SQLException throwable) {
			Logger.staticLog(throwable, true);
		}
		cmbArea.setItems(listaAreas);
		
		ObservableList<String> listaPeriodos = FXCollections.observableArrayList();
		listaPeriodos.add("FEB-JUL");
		listaPeriodos.add("AGO-ENE");
		cmbPeriodo.setItems(listaPeriodos);
		limitarTextfields();
		
		proyecto = (Proyecto) MainController.get("proyecto");
		this.inicializarCampos();
	}
	
	public void limitarTextfields() {
		LimitadorTextfield.soloTexto(txtNombre);
		
		LimitadorTextfield.soloTexto(txtObjetivoGeneral);
		LimitadorTextfield.soloTexto(txtObjetivoMediato);
		LimitadorTextfield.soloTexto(txtObjetivoInmediato);
		LimitadorTextfield.soloTexto(txtMetodologia);
		
		LimitadorTextfield.soloTexto(txtPosicionResponsable);
		LimitadorTextfield.soloTexto(txtNombreResponsable);
		LimitadorTextfield.soloTexto(txtApellidosResponsable);
		LimitadorTextfield.soloNumeros(txtCapacidad);
		
		LimitadorTextfield.limitarTamanio(txtNombre, 50);
		LimitadorTextfield.limitarTamanio(txtObjetivoGeneral, 50);
		LimitadorTextfield.limitarTamanio(txtObjetivoMediato, 50);
		LimitadorTextfield.limitarTamanio(txtObjetivoInmediato, 50);
		LimitadorTextfield.limitarTamanio(txtMetodologia, 50);
		LimitadorTextfield.limitarTamanio(txtPosicionResponsable, 20);
		LimitadorTextfield.limitarTamanio(txtNombreResponsable, 20);
		LimitadorTextfield.limitarTamanio(txtApellidosResponsable, 20);
		LimitadorTextfield.limitarTamanio(txtEmailResponsable, 20);
		LimitadorTextfield.limitarTamanio(txtCapacidad, 2);
		
		LimitadorTextfield.limitarTamanioArea(txtDescripcion, 200);
		LimitadorTextfield.limitarTamanioArea(txtRecursos, 200);
		LimitadorTextfield.limitarTamanioArea(txtResponsabilidades, 200);
	}
	
	public void eliminar() {
		if (MainController.alert(Alert.AlertType.CONFIRMATION,
			"Eliminar Proyecto",
			"¿Seguro que desea eliminar el Proyecto?")) {
			try {
				if (proyecto.eliminarProyecto()) {
					MainController.alert(Alert.AlertType.INFORMATION,
						"Proyecto eliminado",
						"Proyecto eliminado exitosamente");
					MainController.activate(
						"ListaProyectos",
						"Lista Proyectos",
						MainController.Sizes.MID
					);
				} else {
					MainController.alert(Alert.AlertType.ERROR,
						"Sin conexión con BD",
						"Sin conexión con Base de Datos");
				}
			} catch (SQLException throwable) {
				Logger.staticLog(throwable, true);
			}
		}
	}
	
	public void inicializarCampos() {
		lbOrganizacion.setText(proyecto.getOrganization().getNombre());
		txtNombre.setText(proyecto.getNombre());
		txtDescripcion.setText(proyecto.getDescripcion());
		txtObjetivoGeneral.setText(proyecto.getObjetivoGeneral());
		txtObjetivoMediato.setText(proyecto.getObjetivoMediato());
		txtObjetivoInmediato.setText(proyecto.getObjetivoInmediato());
		txtMetodologia.setText(proyecto.getMetodologia());
		txtRecursos.setText(proyecto.getRecursos());
		txtResponsabilidades.setText(proyecto.getResponsabilidades());
		
		txtPosicionResponsable.setText(proyecto.getResponsable().getPosicion());
		txtEmailResponsable.setText(proyecto.getResponsable().getEmail());
		txtEmailResponsable.setEditable(false);
		txtNombreResponsable.setText(proyecto.getResponsable().getNombres());
		txtApellidosResponsable.setText(proyecto.getResponsable().getApellidos());
		
		txtCapacidad.setText(String.valueOf(proyecto.getCapacidad()));
		cmbArea.setValue(proyecto.getArea());
		cmbPeriodo.setValue(proyecto.getPeriodo());
		
		fechaInicial.setValue(LocalDate.parse(proyecto.getFechaInicio()));
		fechaFinal.setValue(LocalDate.parse(proyecto.getFechaFin()));
	}
	
	public void salir() {
		String pantallaAnterior = (String) MainController.get("pantallaAnterior");

		if (pantallaAnterior.equals("listaProyectos")){
			MainController.activate("ListaProyectos", "Lista de Proyectos", MainController.Sizes.MID);
		}else{
			MainController.activate("AdministrarPracticante", "Administrar Practicante", MainController.Sizes.MID);
		}


	}
	
	public void actualizar() {
		if (MainController.alert(Alert.AlertType.CONFIRMATION,
			"Actualizar Proyecto",
			"¿Seguro que desea Actualizar el Proyecto?")) {
			try {
				String idProyecto = DAOProyecto.getIdConNombre(this.proyecto.getNombre());
				this.proyecto.setNombre(txtNombre.getText());
				this.proyecto.setDescripcion(txtDescripcion.getText());
				this.proyecto.setObjetivoGeneral(txtObjetivoGeneral.getText());
				this.proyecto.setObjetivoMediato(txtObjetivoMediato.getText());
				this.proyecto.setObjetivoInmediato(txtObjetivoInmediato.getText());
				this.proyecto.setMetodologia(txtMetodologia.getText());
				this.proyecto.setRecursos(txtRecursos.getText());
				this.proyecto.setResponsabilidades(txtResponsabilidades.getText());
				this.proyecto.setCapacidad(Integer.parseInt(txtCapacidad.getText()));
				
				this.proyecto.setResponsable(new ResponsableProyecto());
				this.proyecto.getResponsable().setPosicion(txtPosicionResponsable.getText());
				this.proyecto.getResponsable().setEmail(txtEmailResponsable.getText());
				this.proyecto.getResponsable().setNombre(txtNombreResponsable.getText());
				this.proyecto.getResponsable().setApellido(txtApellidosResponsable.getText());
				
				this.proyecto.setArea(cmbArea.getValue());
				this.proyecto.setPeriodo(cmbPeriodo.getValue());
				this.proyecto.setFechaInicio(fechaInicial.getValue().toString());
				this.proyecto.setFechaFin(fechaFinal.getValue().toString());
				
				this.proyecto.setOrganization(new Organizacion());
				if (this.proyecto.actualizar(idProyecto)) {
					MainController.alert(Alert.AlertType.INFORMATION,
						"Proyecto actualizado",
						"Proyecto actualizado exitosamente");
					MainController.activate(
						"MenuCoordinador",
						"Menu Coordinador",
						MainController.Sizes.MID);
				}
			} catch (SQLException throwable) {
				Logger.staticLog(throwable, true);
			}
		}
	}
}
