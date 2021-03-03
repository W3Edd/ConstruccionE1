package View.practicante.controller;

import Models.Asignacion;
import Models.Practicante;
import Models.Proyecto;
import View.MainController;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ResourceBundle;

public class ChooseProjectController implements Initializable {
	
	@FXML
	public TableView<Proyecto> projectTable;
	
	@FXML
	public TableColumn<Proyecto, String> tableColumn;
	public Label name;
	public JFXTextArea generalObjective;
	public JFXTextArea resources;
	public JFXTextArea responsabilities;
	public JFXTextField area;
	public JFXTextField organization;
	
	private ObservableList<Proyecto> proyectoObservableList;
	
	private Proyecto[] selectedProyectos;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		checkConditions();
		proyectoObservableList = FXCollections.observableArrayList();
		Proyecto.fillTable(proyectoObservableList);
		projectTable.setItems(proyectoObservableList);
		tableColumn.setCellValueFactory(new PropertyValueFactory<Proyecto, String>("name"));
		checkProject();
		generalObjective.setEditable(false);
		this.resources.setEditable(false);
		responsabilities.setEditable(false);
		area.setEditable(false);
		organization.setEditable(false);
	}
	
	public void checkConditions() {
		selectedProyectos = Asignacion.requestedProjects((Practicante) MainController.get("user"));
		if (selectedProyectos.length >= 3) {
			MainController.alert(
				Alert.AlertType.WARNING,
				"Limite de proyectos seleccionados",
				"Ya ha llegado a su límite de 3 proyectos"
			);
			MainController.activate(
				"MainMenuStudent",
				"Menu Principal Practicante",
				MainController.Sizes.MID
			);
		}
	}
	
	public void checkProject() {
		projectTable.getSelectionModel().selectedItemProperty().addListener(
			(observableValue, oldValue, newValue) -> {
				if (newValue != null) {
					name.setText(newValue.getNombre());
					generalObjective.setText(newValue.getGeneralObjective());
					resources.setText(newValue.getResources());
					responsabilities.setText(newValue.getResponsibilities());
					area.setText(newValue.getArea());
					organization.setText(newValue.getOrganization().getNombre());
				}
			});
	}
	
	public void selectProject() {
		Proyecto selectedProyecto = projectTable.getSelectionModel().getSelectedItem();
		if (selectedProyecto != null) {
			if (isSelected(selectedProyectos, selectedProyecto)) {
				MainController.alert(
					Alert.AlertType.WARNING,
					"Proyecto ya seleccionado",
					"El proyecto que intenta seleccionado ya ha sido seleccionado previamente"
				);
			} else {
				Asignacion.saveRequest((Practicante) MainController.get("user"), selectedProyecto);
				MainController.alert(
					Alert.AlertType.INFORMATION,
					"Proyecto seleccionado exitosamente",
					"Pulse aceptar para continuar"
				);
				exit();
			}
		} else {
			MainController.alert(
				Alert.AlertType.WARNING,
				"No se ha seleccionado ningún proyecto",
				"Pulse aceptar para continuar"
			);
		}
	}
	
	public boolean isSelected(Proyecto[] selectedProyectos, Proyecto toSelect) {
		boolean selected = false;
		for (Proyecto proyecto: selectedProyectos) {
			if (proyecto.getNombre().equals(toSelect.getNombre())) {
				selected = true;
				break;
			}
		}
		return selected;
	}
	
	public void exit() {
		MainController.activate(
			"MainMenuStudent",
			"Menu Principal Practicante",
			MainController.Sizes.MID);
	}
}