package Models;

import DAO.DAOOrganizacion;
import javafx.collections.ObservableList;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class Organizacion {
	private String nombre;
	private String telefono;
	private String sector;
	private final Map<String, String> direccion = new HashMap<>();
	
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	public String getTelefono() {
		return telefono;
	}
	
	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}
	
	public String getSector() {
		return sector;
	}
	
	public void setSector(String sector) {
		this.sector = sector;
	}
	
	public void setDireccion(String calle, String numero, String colonia, String localidad) {
		this.direccion.clear();
		this.direccion.put("calle", calle);
		this.direccion.put("numero", numero);
		this.direccion.put("colonia", colonia);
		this.direccion.put("localidad", localidad);
	}
	
	public String getNombre() {
		return nombre;
	}
	
	public Map<String, String> getDireccion() {
		return this.direccion;
	}
	
	public boolean registrar() throws SQLException {
		return new DAOOrganizacion(this).registrar();
	}
	
	public boolean estaCompleto() {
		return this.nombre != null &&
			!this.direccion.isEmpty() &&
			this.sector != null &&
			this.telefono != null;
	}
	
	public boolean estaRegistrado() throws SQLException {
		return new DAOOrganizacion(this).estaRegistrado();
	}
	
	public boolean eliminar() throws SQLException {
		return new DAOOrganizacion(this).eliminar();
	}
	
	public boolean estaActivo() throws SQLException {
		return new DAOOrganizacion(this).estaActivo();
	}
	
	public boolean reactivar() throws SQLException {
		return new DAOOrganizacion(this).reactivar();
	}
	
	public static boolean llenarTablaOrganizacion(ObservableList<Organizacion> listaOrganizacion) throws SQLException {
		boolean lleno = false;
		DAOOrganizacion organizacionAuxiliar = new DAOOrganizacion(new Organizacion());
		if (organizacionAuxiliar.llenarTablaOrganizacion(listaOrganizacion)) {
			lleno = true;
		}
		return lleno;
	}
	
	public static boolean llenarSector(ObservableList<String> listaOrganizacion) throws SQLException {
		boolean lleno = false;
		DAOOrganizacion daoOrg = new DAOOrganizacion(new Organizacion());
		if (daoOrg.llenarSector(listaOrganizacion)) {
			lleno = true;
		}
		return lleno;
	}
	
	public static boolean llenarNombres(ObservableList<String> listaOrganizacion) throws SQLException {
		DAOOrganizacion daoOrganization = new DAOOrganizacion(new Organizacion());
		return daoOrganization.llenarNombresOrganizaciones(listaOrganizacion);
	}
	
	public static boolean llenarTodosNombres(ObservableList<String> listaOrganizacion) throws SQLException {
		DAOOrganizacion daoOrganization = new DAOOrganizacion(new Organizacion());
		return daoOrganization.llenarNombresOrganizaciones(listaOrganizacion);
	}
	
	public String getId() throws SQLException {
		return new DAOOrganizacion(this).getId();
	}
	
	public static Organizacion obtenerPorNombre(String name) throws SQLException {
		return DAOOrganizacion.obtenerPorNombre(name);
	}
	
	public boolean actualizar(String nombreAntiguo) throws SQLException {
		return new DAOOrganizacion(this).actualizar(nombreAntiguo);
	}
	
	@Override
	public String toString() {
		return "Organizacion{" +
			"nombre='" + nombre + '\'' +
			", telefono='" + telefono + '\'' +
			", sector='" + sector + '\'' +
			", direccion=" + direccion +
			'}';
	}
}
