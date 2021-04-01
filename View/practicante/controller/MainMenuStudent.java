package View.practicante.controller;

import Models.Proyecto;
import View.MainController;
import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class MainMenuStudent implements Initializable {
	
	@FXML
	public JFXButton solicitarProyecto;
	
	@FXML
	public JFXButton generarReporte;
	
	@FXML
	public JFXButton anadirActividad;
	
	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
	
	}
	
	public void chooseProject(MouseEvent mouseEvent) {
		try {
			if (Proyecto.contarProyectos() > 0) {
				MainController.activate(
					"ChooseProject",
					"Solicitar Proyecto",
					MainController.Sizes.MID
				);
			} else {
				MainController.alert(
					Alert.AlertType.INFORMATION,
					"Sin proyectos",
					"No hay proyectos registrados"
				);
			}
		} catch (SQLException throwables) {
			MainController.alert(
				Alert.AlertType.ERROR,
				"ErrorBD",
				"No se pudo establecer conexión con la base de datos"
			);
		}
	}
	
	public void generateProject(MouseEvent mouseEvent) {
		MainController.activate(
			"GenerateReport",
			"Generar Reporte",
			MainController.Sizes.MID
		);
		
	}
	
	public void addActivity(MouseEvent mouseEvent) {
		MainController.activate(
			"AddActivityPlan",
			"Añadir Plan de Actividades",
			MainController.Sizes.MID
		);
	}
}
