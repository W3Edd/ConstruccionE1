package Models;

import DAO.DAOCoordinador;
import Exceptions.CustomException;
import java.sql.SQLException;

public class Coordinador extends Usuario {
    private String noPersonal;
    private String fechaRegistro;
    private String turno;

    public Coordinador() {
    }

    public Coordinador(String nombres, String apellidos, String email, String contrasena,
                       String noPersonal) {
        super(nombres, apellidos, email, contrasena);
        this.noPersonal = noPersonal;
    }

    public Coordinador(String nombres, String apellidos, String email, String contrasena,
                       String noPersonal, String turno) {
        super(nombres, apellidos, email, contrasena);
        this.noPersonal = noPersonal;
        this.turno = turno;
    }

    public Coordinador(Coordinador coordinador) {
        if (coordinador != null) {
            this.setNombres(coordinador.getNombres());
            this.setApellidos(coordinador.getApellidos());
            this.setEmail(coordinador.getEmail());
            this.setContrasenaLimpia(coordinador.getContrasena());
            this.setNumeroPersonal(coordinador.getNoPersonal());
        }
    }

    public String getTurno() {
        return turno;
    }

    public void setTurno(String shift) {
        this.turno = shift;
    }

    public String getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(String registrationDate) {
        this.fechaRegistro = registrationDate;
    }

    public String getNoPersonal() {
        return noPersonal;
    }

    public void setNumeroPersonal(String noPersonal) {
        if (esNoPersonal(noPersonal)) {
            this.noPersonal = noPersonal;
        } else {
            this.noPersonal = null;
        }
    }

    public static boolean esNoPersonal(String noPersonal){
        return noPersonal != null && (noPersonal.length()>0 && noPersonal.length()<33);
    }
    public boolean registrar() throws SQLException {
        return new DAOCoordinador(this).registrar();
    }

    public boolean iniciarSesion() throws SQLException {
        return new DAOCoordinador(this).iniciarSesion();
    }

    public boolean actualizar() throws SQLException {
        return new DAOCoordinador(this).actualizar();
    }

    public boolean eliminar() throws SQLException {
        return new DAOCoordinador(this).eliminar();
    }

    public boolean registrarProyecto(Proyecto proyecto) throws CustomException, SQLException {
        return proyecto.registrar();
    }

    public boolean eliminarProyecto(Proyecto proyecto) throws CustomException, SQLException {
        return proyecto.eliminarProyecto();
    }

    public boolean hayOtro() throws SQLException {
        DAOCoordinador daoCoordinador = new DAOCoordinador(this);
        return daoCoordinador.hayOtro();
    }

    public boolean registrarPracticante(Practicante practicante) throws CustomException, SQLException {
        return practicante.registrar();
    }

    public boolean eliminarPracticante(Practicante practicante) throws CustomException, SQLException {
        return practicante.eliminar();
    }

    public boolean registrarOrganizacion(Organizacion organizacion) throws SQLException {
        return organizacion.registrar();
    }

    public boolean eliminarOrganizacion(Organizacion organizacion) throws SQLException {
        return organizacion.registrar();
    }

    public boolean asignarProyecto(Practicante practicante, String projectName)
            throws CustomException, SQLException {
        return practicante.asignarProyecto(projectName);
    }

    public boolean estaRegistrado() throws SQLException {
        return new DAOCoordinador(this).estaRegistrado();
    }

    public boolean estaCompleto() {
        return super.estaCompleto() && noPersonal != null && turno != null;
    }

    public Coordinador obtenerActivo() throws SQLException {
        return DAOCoordinador.obtenerActivo();
    }

    public String toString() {
        return getNombres() + "\n" +
                getApellidos() + "\n" +
                getNoPersonal() + "\n" +
                getTurno() + "\n" +
                getEmail() + "\n";
    }
}