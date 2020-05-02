package DAO;

import Connection.DBConnection;
import Exceptions.CustomException;
import IDAO.IDAOProfessor;
import Models.Professor;

public class DAOProfessor implements IDAOProfessor {
	private Professor professor;
	private DBConnection connection = new DBConnection();
	
	public DAOProfessor(Professor professor) {
		this.professor = professor;
	}
	
	public Professor getProfessor() {
		return professor;
	}
	
	public void setProfessor(Professor professor) {
		this.professor = professor;
	}
	
	@Override
	public boolean update() throws CustomException {
		boolean updated = false;
		String query = "UPDATE Usuario SET nombres = ?, apellidos = ?, correoElectronico = ?, "
			+ "contrasena = ? WHERE correoElectronico = ?";
		String[] values = {this.professor.getNames(), this.professor.getLastnames(),
			this.professor.getEmail(), this.professor.getPassword(),
			this.professor.getEmail()};
		if (this.connection.sendQuery(query, values)) {
			query = "UPDATE Profesor SET noPersonal = ?, turno = ? WHERE idUsuario = (SELECT " +
				"idUsuario" + " " + "FROM Usuario WHERE correoElectronico = ?)";
			values = new String[]{this.professor.getPersonalNo(), String.valueOf(this.professor.getShift()),
				this.professor.getEmail()};
			if (this.connection.sendQuery(query, values)) {
				updated = true;
			}
		}
		return updated;
	}
	
	@Override
	public boolean delete() throws CustomException {
		boolean deleted = false;
		if (this.professor != null && this.isRegistered()) {
			if (this.isActive()) {
				String query = "UPDATE Usuario SET status = 0 WHERE correoElectronico = ?";
				String[] values = {this.professor.getEmail()};
				if (this.connection.sendQuery(query, values)) {
					deleted = true;
				}
			} else {
				deleted = true;
			}
		}
		return deleted;
	}
	
	public boolean reactive() throws CustomException {
		boolean reactivated = false;
		if (!this.isActive()) {
			String query = "UPDATE Usuario SET status = 1 WHERE correoElectronico = ?";
			String[] values = {this.professor.getEmail()};
			if (this.connection.sendQuery(query, values)) {
				reactivated = true;
			}
		}
		return reactivated;
	}
	
	@Override
	public boolean logIn() throws CustomException {
		boolean loggedIn = false;
		String query = "SELECT COUNT(idUsuario) AS TOTAL FROM Usuario WHERE correoElectronico = ? " +
			"AND contrasena = ?";
		String[] values = {this.professor.getEmail(), this.professor.getPassword()};
		String[] names = {"TOTAL"};
		if (this.isRegistered() && this.isActive()) {
			if (this.connection.select(query, values, names)[0][0].equals("1")) {
				loggedIn = true;
			}
		}
		return loggedIn;
	}
	
	@Override
	public boolean signUp() throws CustomException {
		boolean signedUp = false;
		if (this.professor.isComplete()) {
			String query = "INSERT INTO Usuario (nombres, apellidos, correoElectronico, contrasena, status)" +
				"VALUES (?, ?, ?, ?, ?)";
			String[] values = {this.professor.getNames(), this.professor.getLastnames(),
				this.professor.getEmail(), this.professor.getPassword(), "1"};
			if (this.connection.sendQuery(query, values)) {
				query = "INSERT INTO Profesor (idUsuario, fechaRegistro, noPersonal, turno) VALUES " +
					"((SELECT idUsuario FROM Usuario WHERE correoElectronico = ?), (SELECT CURRENT_DATE), ?, " +
						"(SELECT idTurno FROM Turno WHERE turno = ?))";
				values = new String[]{this.professor.getEmail(), this.professor.getPersonalNo(), this.professor.getShift()};
				if (this.connection.sendQuery(query, values)) {
					signedUp = true;
				}
			}
		}
		return signedUp;
	}
	
	public boolean isActive() throws CustomException {
		boolean isActive = false;
		if (this.professor != null && this.professor.getEmail() != null &&
			this.isRegistered()) {
			String query = "SELECT status FROM Usuario WHERE correoElectronico = ?";
			String[] values = {this.professor.getEmail()};
			String[] names = {"status"};
			isActive = this.connection.select(query, values, names)[0][0].equals("1");
		}
		return isActive;
	}
	
	@Override
	public boolean isRegistered() throws CustomException {
		assert this.professor != null;
		assert this.professor.getEmail() != null;
		String query = "SELECT COUNT(Usuario.idUsuario) AS TOTAL FROM Usuario INNER JOIN Profesor " +
			"ON Usuario.idUsuario = Profesor.idUsuario WHERE correoElectronico = ?";
		String[] values = {this.professor.getEmail()};
		String[] names = {"TOTAL"};
		return this.connection.select(query, values, names)[0][0].equals("1");
	}
	
	public static Professor[] getAll() {
		return new Professor[0];
	}
	
	public static Professor[] get(Professor professor) {
		return null;
	}
}
