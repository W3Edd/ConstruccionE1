package View.administrador.controller;

import IDAO.Turno;
import Models.Coordinador;
import View.MainController;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import tools.Logger;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class AdministrarCoordinadorController implements Initializable {
    @FXML
    private TableView<Coordinador> tableViewCoordinator;
    @FXML
    private TableColumn<Coordinador, String> clmnEmail;
    @FXML
    private TableColumn<Coordinador, String> clmnNames;
    @FXML
    private TableColumn<Coordinador, String> clmnLastNames;

    @FXML
    private Label lblNames;
    @FXML
    private Label lblLastnames;
    @FXML
    private Label lblEmail;
    @FXML
    private Label lblPersonalNo;
    @FXML
    private Label lblShift;
    @FXML
    private Label lblRegistrationDate;

    @FXML
    private JFXButton btnDelete;
    @FXML
    private JFXButton btnRegister;
    @FXML
    private JFXButton btnUpdate;
    @FXML
    private JFXTextField txtEmail;
    @FXML
    private JFXPasswordField pwdPassword;
    @FXML
    private JFXTextField txtNames;
    @FXML
    private JFXTextField txtLastNames;
    @FXML
    private JFXTextField txtNoPersonal;
    @FXML
    private JFXComboBox<String> cmbShift;

    private Coordinador coordinador;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        coordinador = new Coordinador();
        try {
            if (coordinador.hayOtro()) {
                coordinador = coordinador.obtenerActivo();
                llenarDetallesCoordinador(coordinador);
                enableEdit();
            } else {
                enableRegister();
            }
            ObservableList<String> listShift = FXCollections.observableArrayList();
            Turno.llenarTurno(listShift);
            cmbShift.setItems(listShift);

        } catch (SQLException e) {
            Logger.staticLog(e, true);
        }
    }

    @FXML
    public void registrar() {
        coordinador = new Coordinador();
        this.instanceCoordinator(coordinador);
        if (MainController.alert(Alert.AlertType.CONFIRMATION, "¿Estás seguro que deseas agregar?",
                "Pulse aceptar para continuar")) {
            if (coordinador.estaCompleto()) {
                try {
                    if (!coordinador.estaRegistrado()) {
                        if (coordinador.registrar()) {
                            MainController.alert(
                                    Alert.AlertType.INFORMATION,
                                    "Coordinador registrado correctamente",
                                    "Pulse aceptar para continuar"
                            );
                            llenarDetallesCoordinador(coordinador);
                            enableEdit();
                        } else {
                            MainController.alert(
                                    Alert.AlertType.WARNING,
                                    "Ocurrio un error al conectarse a la base de datos",
                                    "Pulse aceptar para continuar"
                            );
                        }
                    } else {
                        MainController.alert(Alert.AlertType.WARNING,
                                "La cuenta ya se encuentra registrada",
                                "Pulse aceptar para continuar"
                        );
                    }
                } catch (SQLException sqlException) {
                    MainController.alert(
                            Alert.AlertType.ERROR,
                            "Ocurrio un error al conectarse a la base de datos",
                            "Pulse aceptar para continuar"
                    );
                    Logger.staticLog(sqlException, true);
                }
            } else {
                MainController.alert(
                        Alert.AlertType.WARNING,
                        "Campos incorrectos o incompletos",
                        "Pulse aceptar para continuar"
                );
            }
        }
    }

    @FXML
    public void update() {
        coordinador = new Coordinador();
        this.instanceCoordinator(coordinador);
        try {
            if (MainController.alert(Alert.AlertType.CONFIRMATION, "¿Está seguro?", "") &&
                    coordinador.estaCompleto()) {
                instanceCoordinator(coordinador);
                if (coordinador.actualizar()) {
                    MainController.alert(
                            Alert.AlertType.INFORMATION,
                            "Coordinador registrado exitosamente",
                            "Pulse aceptar para continua"
                    );
                } else {
                    MainController.alert(
                            Alert.AlertType.ERROR,
                            "No se pudo actualizar al coordinador",
                            "Pulse aceptar para continuar"
                    );
                }
            } else {
                MainController.alert(
                        Alert.AlertType.WARNING,
                        "LLene todos los campos correctamente",
                        "Pulse aceptar para continuar"
                );
            }
        } catch (AssertionError e) {
            new Logger().log(e.getMessage());
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
            Logger.staticLog(sqlException, true);
        }
    }

    @FXML
    public void eliminar() {
        if (MainController.alert(Alert.AlertType.CONFIRMATION, "¿Está seguro?", "")) {
            try {
                if (coordinador.eliminar()) {
                    MainController.alert(
                            Alert.AlertType.INFORMATION,
                            "Acción realizada exitosamente",
                            "Pulse aceptar para continuar"
                    );
                    enableRegister();
                    cleanFormCoordinator();
                    limpiarDetallesCoordinador();
                } else {
                    MainController.alert(
                            Alert.AlertType.ERROR,
                            "Error al conectarse a la base de datos",
                            "Pulse aceptar para continuar"
                    );
                }
            } catch (SQLException sqlException) {
                sqlException.printStackTrace();
                Logger.staticLog(sqlException, true);
                MainController.alert(
                        Alert.AlertType.WARNING,
                        "Error al conectarse a la base de datos",
                        "Pulse aceptar para continuar"
                );
            }
        }
    }

    @FXML
    public void onBackArrowClicked(MouseEvent event) {
        MainController.activate("MenuAdministrador", "Menú Administrador", MainController.Sizes.MID);
    }

    private void instanceCoordinator(Coordinador coordinador) {
        coordinador.setEmail(txtEmail.getText());
        coordinador.setContrasena(pwdPassword.getText());
        coordinador.setNombres(txtNames.getText());
        coordinador.setApellidos(txtLastNames.getText());
        coordinador.setNumeroPersonal(txtNoPersonal.getText());
        coordinador.setTurno(cmbShift.getValue());
    }

    private void cleanFormCoordinator() {
        txtEmail.setText(null);
        txtNames.setText(null);
        txtLastNames.setText(null);
        txtNoPersonal.setText(null);
    }

    private void llenarDetallesCoordinador(Coordinador coordinador) {
        lblNames.setText(coordinador.getNombres());
        lblLastnames.setText(coordinador.getApellidos());
        lblEmail.setText(coordinador.getEmail());
        lblPersonalNo.setText(coordinador.getNoPersonal());
        lblShift.setText(coordinador.getTurno());
        lblRegistrationDate.setText(coordinador.getFechaRegistro());
    }

    private void limpiarDetallesCoordinador(){
        lblNames.setText(null);
        lblLastnames.setText(null);
        lblEmail.setText(null);
        lblPersonalNo.setText(null);
        lblShift.setText(null);
        lblRegistrationDate.setText(null);
    }

    private void enableRegister() {
        txtEmail.setDisable(false);
        pwdPassword.setDisable(false);

        btnDelete.setDisable(true);
        btnUpdate.setDisable(true);
        btnRegister.setDisable(false);
    }

    private void enableEdit() {
        txtEmail.setDisable(true);
        pwdPassword.setDisable(true);

        btnDelete.setDisable(false);
        btnUpdate.setDisable(false);
        btnRegister.setDisable(true);
    }

}
