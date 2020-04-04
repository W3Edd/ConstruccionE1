package DAO;

import Connection.DBConnection;
import IDAO.IDAOPracticante;
import Models.Practicante;

public class DAOPracticante implements IDAOPracticante {
	private Practicante practicante;
	private DBConnection connection = new DBConnection();

	public DAOPracticante(Practicante practicante) {
		this.practicante = practicante;
	}

	public Practicante getPracticante() {
		return practicante;
	}

	public void setPracticante(Practicante practicante) {
		this.practicante = practicante;
	}

	@Override
	public boolean update() {
		boolean updated = false;
		if (this.isRegistered()) {
			String query = "UPDATE Usuario SET nombres = ?, apellidos = ?, correoElectronico = ?, " +
					"contrasena = ? WHERE correoElectronico = ?";
			String[] values = {this.practicante.getNombres(), this.practicante.getApellidos(),
					this.practicante.getCorreoElectronico(), this.practicante.getContrasena(),
					this.practicante.getCorreoElectronico()};
			if (this.connection.preparedQuery(query, values)) {
				query = "UPDATE Practicante SET Matricula = ? WHERE idUsuario = (SELECT idUsuario " +
						"FROM Usuario WHERE correoElectronico = ?)";
				values = new String[]{this.practicante.getMatricula(),
						this.practicante.getCorreoElectronico()};
				if (this.connection.preparedQuery(query, values)) {
					updated = true;
				}
			}
		}
		return updated;
	}

	@Override
	public boolean delete() {
		boolean deleted = false;
		String query = "DELETE FROM Practicante WHERE idUsuario = (SELECT idUsuario FROM Usuario " +
				"WHERE correoElectronico = ?)";

		String[] values = {this.practicante.getCorreoElectronico()};
		if (this.isRegistered()) {
			if (this.connection.preparedQuery(query, values)) {
				query = "DELETE FROM Usuario WHERE correoElectronico = ?";
				if (this.connection.preparedQuery(query, values)) {
					deleted = true;
				}
			}
		}
		return deleted;
	}

	@Override
	public boolean logIn() {
		boolean loggedIn = false;
		String query = "SELECT COUNT(idUsuario) AS TOTAL FROM Usuario WHERE correoElectronico = ? " +
				"AND contrasena = ? AND status = 1";
		String[] values = {this.practicante.getCorreoElectronico(), this.practicante.getContrasena()};
		String[] names = {"TOTAL"};
		if (this.isRegistered()) {
			if (this.connection.select(query, values, names)[0][0].equals("1")) {
				loggedIn = true;
			}
		}
		return loggedIn;
	}

	@Override
	public boolean signUp() {
		boolean signedUp = false;
		String query = "INSERT INTO Usuario (nombres, apellidos, correoElectronico, contrasena, status) " +
				"VALUES (?, ?, ?, ?, 1)";
		String[] values = {this.practicante.getNombres(), this.practicante.getApellidos(),
				this.practicante.getCorreoElectronico(), this.practicante.getContrasena()};
		if (!this.isRegistered()) {
			if (this.connection.preparedQuery(query, values)) {
				query = "INSERT INTO Practicante (idUsuario, matricula) VALUES " +
						"((SELECT idUsuario FROM Usuario WHERE correoElectronico = ?), ?)";
				values = new String[]{this.practicante.getCorreoElectronico(),
						this.practicante.getMatricula()};
				if (this.connection.preparedQuery(query, values)) {
					signedUp = true;
				}
			}
		}
		return signedUp;
	}

	@Override
	public boolean isRegistered() {
		boolean isRegistered = false;
		String query = "SELECT COUNT(idUsuario) AS TOTAL FROM Usuario WHERE correoElectronico = ?";
		String[] values = {this.practicante.getCorreoElectronico()};
		String[] names = {"TOTAL"};
		if (this.practicante != null && this.practicante.isComplete()) {
			if (this.connection.select(query, values, names)[0][0].equals("1")) {
				query = "SELECT COUNT(Practicante.idUsuario) AS TOTAL FROM Practicante INNER JOIN Usuario " +
						"ON Practicante.idUsuario = Usuario.idUsuario " +
						"WHERE Usuario.correoElectronico = ?";
				if (this.connection.select(query, values, names)[0][0].equals("1")) {
					isRegistered = true;
				}
			}
		}
		return isRegistered;
	}

	public static Practicante[] getAll() {
		return new Practicante[0];
	}

	public static Practicante get(Practicante practicante) {
		return null;
	}
}
