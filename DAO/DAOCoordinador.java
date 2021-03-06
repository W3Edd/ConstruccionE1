package DAO;

import Connection.ConexionBD;
import IDAO.IDAOCoordinador;
import IDAO.Turno;
import Models.Coordinador;

import java.sql.SQLException;

public class DAOCoordinador implements IDAOCoordinador, Turno {
	private Coordinador coordinador;
	private final ConexionBD conexion = new ConexionBD();
	
	public DAOCoordinador(Coordinador coordinador) {
		this.coordinador = coordinador;
	}
	
	@Override
	public boolean registrar() throws SQLException {
		assert this.coordinador.estaCompleto() :
			"Coordinador incompleto: DAOCoordinador.registrarSP()";
		assert !this.estaRegistrado() : "Coordinador ya registrado: DAOCoordinador.registrarSP()";
		assert !this.hayOtro() : "Ya hay otro coordinador: DAOCoordinador.registrarSP()";
		boolean registrado;
		String query =
			"CALL SPI_registrarCoordinador(?, ?, ?, ?, ?, ?)";
		String[] valores =
			{this.coordinador.getNombres(),
				this.coordinador.getApellidos(),
				this.coordinador.getEmail(),
				this.coordinador.getContrasena(),
				this.coordinador.getNoPersonal(),
				this.coordinador.getTurno()};
		registrado = this.conexion.ejecutarSP(query, valores);
		return registrado;
	}
	
	@Override
	public boolean actualizar() throws SQLException {
		assert coordinador != null : "Coordinador es nulo: DAOCoordinador.actualizar()";
		assert coordinador.estaCompleto() : "Coordinador imcompleto: DAOCoordinador.actualizar()";
		assert this.estaActivo() : "Coordinador inactivo: DAOCoordinador.actualizar()";
		
		boolean actualizado;
		String query = "CALL SPA_actualizarCoordinador(?, ?, ?, ?, ?)";
		String[] valores = {
			this.coordinador.getEmail(),
			this.coordinador.getNombres(),
			this.coordinador.getApellidos(),
			this.coordinador.getNoPersonal(),
			this.coordinador.getTurno()
		};
		actualizado = this.conexion.ejecutarSP(query, valores);
		return actualizado;
	}
	
	@Override
	public boolean estaRegistrado() throws SQLException {
		assert this.coordinador != null : "Coordinador es nulo: DAOCoordinador.estaRegistrado()";
		assert this.coordinador.getEmail() != null :
			"Email de coordinador es nulo: DAOCoordinador.estaRegistrado()";
		String query = "SELECT COUNT(MiembroFEI.idMiembro) AS TOTAL FROM MiembroFEI " +
			"INNER JOIN Coordinador ON MiembroFEI.idMiembro = Coordinador.idMiembro " +
			"WHERE correoElectronico = ?";
		String[] valores = {this.coordinador.getEmail()};
		String[] columnas = {"TOTAL"};
		String[][] resultados = this.conexion.seleccionar(query, valores, columnas);
		return resultados != null && resultados[0][0].equals("1");
	}
	
	@Override
	public boolean iniciarSesion() throws SQLException {
		assert this.estaRegistrado() : "Coordinador no registrado: DAOCoordinador.iniciarSesion()";
		
		String query = "SELECT COUNT(MiembroFEI.idMiembro) AS TOTAL " +
			"FROM Coordinador INNER JOIN MiembroFEI " +
			"ON Coordinador.idMiembro = MiembroFEI.idMiembro " +
			"WHERE correoElectronico = ? AND contrasena = ? AND estaActivo = 1";
		String[] valores = {this.coordinador.getEmail(), this.coordinador.getContrasena()};
		String[] columnas = {"TOTAL"};
		String[][] resultados = this.conexion.seleccionar(query, valores, columnas);
		return resultados != null && resultados[0][0].equals("1");
	}
	
	@Override
	public boolean eliminar() throws SQLException {
		assert this.coordinador != null : "Coordinador es nulo: DAOCoordinador.eliminar()";
		assert this.estaRegistrado() : "Coordinador no registrado: DAOCoordinador.eliminar()";
		assert this.estaActivo() : "Coordinador inactivo: DAOCoordinador.eliminar()";
		String query = "CALL SPA_eliminarCoordinador(?)";
		String[] valores = {this.coordinador.getEmail()};
		return this.conexion.ejecutarSP(query, valores);
	}
	
	public boolean estaActivo() throws SQLException {
		assert this.coordinador != null : "Coordinador es nulo: DAOCoordinador.estaActivo()";
		assert this.coordinador.getEmail() != null :
			"Email de coordinador es nulo: DAOCoordinador.estaActivo()";
		assert this.estaRegistrado() : "Coordinador no registrado: DAOCooridnador.estaActivo()";
		
		String query = "SELECT estaActivo FROM MiembroFEI WHERE correoElectronico = ?";
		String[] valores = {this.coordinador.getEmail()};
		String[] columnas = {"estaActivo"};
		String[][] resultados = this.conexion.seleccionar(query, valores, columnas);
		return resultados != null && resultados[0][0].equals("1");
	}
	
	@Override
	public boolean reactivar() throws SQLException {
		assert this.coordinador != null : "Coordinador es nulo: DAOCoordinador.reactivar()";
		assert this.estaRegistrado() : "Coordinador no registrado: DAOCoordinador.reactivar()";
		assert this.estaActivo() : "Coordinador inactivo: DAOCoordinador.reactivar()";
		
		String query = "UPDATE MiembroFEI SET estaActivo = 1 WHERE correoElectronico = ?";
		String[] valores = {this.coordinador.getEmail()};
		return this.conexion.ejecutar(query, valores);
	}
	
	public boolean hayOtro() throws SQLException {
		String query = "SELECT COUNT (Coordinador.idMiembro) AS TOTAL FROM Coordinador " +
			"INNER JOIN MiembroFEI ON Coordinador.idMiembro = MiembroFEI.idMiembro WHERE MiembroFEI.estaActivo = 1";
		String[] columnas = {"TOTAL"};
		String[][] resultados = this.conexion.seleccionar(query, null, columnas);
		return resultados != null && Integer.parseInt(resultados[0][0]) > 0;
	}
	
	@Override
	public String getTurno() throws SQLException {
		assert this.coordinador != null : "Coordinador es nulo: DAOCoordinador.getTurno()";
		assert this.coordinador.getEmail() != null :
			"Email de coordinador es nulo: DAOCoordinador.getTurno()";
		String query = "SELECT turno FROM Turno " +
			"INNER JOIN Coordinador ON Turno.idTurno = Coordinador.turno " +
			"INNER JOIN MiembroFEI ON Coordinador.idMiembro = MiembroFEI.idMiembro " +
			"WHERE MiembroFEI.correoElectronico = ?";
		String[] valores = {this.coordinador.getEmail()};
		String[] columnas = {"turno"};
		String[][] resultados = this.conexion.seleccionar(query, valores, columnas);
		return resultados != null ? resultados[0][0] : "";
	}
	
	public static Coordinador obtenerActivo() throws SQLException {
		Coordinador coordinador = new Coordinador();
		String query =
			"SELECT nombres, apellidos, correoElectronico, contrasena, noPersonal, fechaRegistro, " +
				"Turno.turno FROM " +
				"MiembroFEI INNER JOIN Coordinador " +
				"ON MiembroFEI.idMiembro = Coordinador.idMiembro " +
				"INNER JOIN Turno " +
				"ON Turno.idTurno = Coordinador.turno WHERE estaActivo = 1";
		String[] columnas = {
			"nombres", "apellidos", "correoElectronico", "contrasena", "noPersonal",
			"fechaRegistro", "turno"
		};
		String[][] resultados = new ConexionBD().seleccionar(query, null, columnas);
		if (resultados != null && resultados.length > 0) {
			coordinador.setNombres(resultados[0][0]);
			coordinador.setApellidos(resultados[0][1]);
			coordinador.setEmail(resultados[0][2]);
			coordinador.setContrasena(resultados[0][3]);
			coordinador.setNumeroPersonal(resultados[0][4]);
			coordinador.setFechaRegistro(resultados[0][5]);
			coordinador.setTurno(resultados[0][6]);
		}
		return coordinador;
	}
	
	private String getIdCoordinador() throws SQLException {
		String query =
			"SELECT idMiembro AS idCoordinator FROM MiembroFEI WHERE correoElectronico = ?";
		String[] valores = {this.coordinador.getEmail()};
		String[] columnas = {"idCoordinator"};
		return this.conexion.seleccionar(query, valores, columnas)[0][0];
	}
}
