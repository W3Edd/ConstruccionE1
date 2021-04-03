package View.coordinador.controller;

import Exceptions.CustomException;
import Models.Practicante;
import Models.Professor;
import View.MainController;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.StringConverter;
import tools.Logger;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class AdministrarPracticanteController implements Initializable {
    @FXML
    private TableView<Practicante> tblViewPracticante;
    @FXML
    private TableColumn<Practicante, String> clmnNombre;
    @FXML
    private TableColumn<Practicante, String> clmnApellido;
    @FXML
    private TableColumn<Practicante, String> clmnMatricula;
    @FXML
    private JFXComboBox<Professor> cmbProfesor;


    @FXML
    JFXTextField txtNombre;
    @FXML
    JFXTextField txtApellido;
    @FXML
    JFXTextField txtMatricula;
    @FXML
    JFXTextField txtEmail;
    @FXML
    JFXTextField txtContrasenia;

    @FXML
    JFXButton btnRegistrar;

    private ObservableList<Practicante> listPracticante;
    private ObservableList<Professor> listProfesor;

    private Practicante practicante;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        listPracticante = FXCollections.observableArrayList();
        try {
            new Practicante().llenarTablaPracticantes(listPracticante);
        } catch (NullPointerException e) {

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        tblViewPracticante.setItems(listPracticante);
        clmnNombre.setCellValueFactory(new PropertyValueFactory<Practicante, String>("nombres"));
        clmnApellido.setCellValueFactory(new PropertyValueFactory<Practicante, String>("apellidos"));
        clmnMatricula.setCellValueFactory(new PropertyValueFactory<Practicante, String>("matricula"));

        listProfesor = FXCollections.observableArrayList();
        try{
            new Professor().obtenerProfesores(listProfesor);
        }catch (NullPointerException e){
            MainController.alert(
                    Alert.AlertType.WARNING,
                    "No hay profesores",
                    "No hay profesores registrados para asociar a los practicantes"
            );
            MainController.activate("MainMenuCoordinator");
        }catch (Exception e){
            MainController.alert(
                    Alert.AlertType.WARNING,
                    "ErrorBD",
                    "Error al conectar con la base de datos"
            );
            MainController.activate("MainMenuCoordinator");
        }

        cmbProfesor.setItems(listProfesor);
        eventManager();

        cmbProfesor.setConverter(new StringConverter<Professor>() {
            @Override
            public String toString(Professor professor) {
                return professor.getNombres();
            }

            @Override
            public Professor fromString(String string) {
                return null;
            }
        });
    }

    public void eventManager() {
        tblViewPracticante.getSelectionModel().selectedItemProperty().addListener(
                new ChangeListener<Practicante>() {
                    @Override
                    public void changed(ObservableValue<? extends Practicante> observable, Practicante oldValue,
                                        Practicante newValue) {
                        if (newValue != null) {
                            practicante = newValue;
                            txtNombre.setText(newValue.getNombres());
                            txtApellido.setText(newValue.getApellidos());
                            txtEmail.setText(newValue.getEmail());
                            txtMatricula.setText(newValue.getMatricula());
                            txtContrasenia.setText(newValue.getContrasena());
                            practicante = tblViewPracticante.getSelectionModel().getSelectedItem();
                            MainController.save("student", practicante);
                            enableEdit();
                        } else {
                            cleanFormStudent();
                            enableRegister();
                        }
                    }
                }
        );
    }

    public void enableEdit() {
    }

    public void enableRegister() {
    }

    public void cleanFormStudent() {
        txtNombre.setText(null);
        txtApellido.setText(null);
        txtMatricula.setText(null);
        txtEmail.setText(null);
    }

    @FXML
    public void registrarPracticante() {
        Practicante practicante = new Practicante();
        this.instanceStudent(practicante);
        if (practicante.estaCompleto()) {
            try {
                if (practicante.registrar()) {
                    listPracticante.add(practicante);
                    MainController.alert(
                            Alert.AlertType.INFORMATION,
                            "Practicante registrado correctamente",
                            "Pulse aceptar para continuar"
                    );
                } else {
                    MainController.alert(
                            Alert.AlertType.WARNING,
                            "Error al conectar con la base de datos",
                            "Pulse aceptar para continuar"
                    );
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        } else {
            MainController.alert(
                    Alert.AlertType.INFORMATION,
                    "Llene todos los campos correctamente",
                    "Pulse aceptar para continuar"
            );
        }
    }

    private void instanceStudent(Practicante practicante) {
        practicante.setNombres(txtNombre.getText());

        practicante.setApellidos(txtApellido.getText());
        practicante.setMatricula(txtMatricula.getText());
        practicante.setEmail(txtEmail.getText());
        practicante.setContrasena(txtContrasenia.getText());
        practicante.setProfesor(cmbProfesor.getValue());

        System.out.println(practicante.getNombres());
        System.out.println(practicante.getApellidos());
        System.out.println(practicante.getMatricula());
        System.out.println(practicante.getEmail());
        System.out.println(practicante.getContrasena());
        System.out.println(practicante.getProfesor().getEmail());
    }

    @FXML
    private void checkProject() {
        try {
            if (practicante.getProyecto() == null) {
                MainController.activate("AssignProject", "Asignar Proyecto", MainController.Sizes.MID);
            } else {
                MainController.save("Project", practicante.getProyecto());
                MainController.activate("ViewProject", "Ver Proyecto");
            }
        } catch (CustomException e) {
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void onClickBack() {
        MainController.activate("MenuCoordinador", "Menu", MainController.Sizes.MID);
    }

    @FXML
    public void eliminarPracticante() {
        if (MainController.alert(Alert.AlertType.CONFIRMATION,
                "Eliminar Practicante",
                "¿Está seguro que desea eliminar al Practicante seleccionado?")) {
            try {
                if (tblViewPracticante.getSelectionModel().getSelectedItem().eliminar()) {
                    listPracticante.remove(tblViewPracticante.getSelectionModel().getSelectedIndex());
                    MainController.alert(Alert.AlertType.INFORMATION,
                            "Practicante eliminado",
                            "Organización eliminada exitosamente");
                } else {
                    MainController.alert(
                            Alert.AlertType.INFORMATION,
                            "DBError",
                            "No se pudo establecer conexión con Base de Datos"
                    );
                }
            } catch (AssertionError e) {
                new Logger().log(e.getMessage());
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }

}