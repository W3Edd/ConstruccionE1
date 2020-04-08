package DAO;

import Connection.DBConnection;
import IDAO.IDAOPracticante;
import Models.Practicante;
import Models.Proyecto;

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

	/**
	 * Update the Database with the current PRACTICANTE
	 *
	 * @return true => updated | false => something went wrong
	 */
	@Override
	public boolean update() {
		boolean updated = false;
		if (this.isRegistered()) {
			String query = "UPDATE Usuario SET nombres = ?, apellidos = ?, correoElectronico = ?, "
				+ "contrasena = ? WHERE correoElectronico = ?";
			String[] values = {this.practicante.getNombres(), this.practicante.getApellidos(),
				this.practicante.getCorreoElectronico(), this.practicante.getContrasena(),
				this.practicante.getCorreoElectronico()};
			if (this.connection.preparedQuery(query, values)) {
				query = "UPDATE Practicante SET Matricula = ? WHERE idUsuario = (SELECT " +
					"idUsuario" + " " + "FROM Usuario WHERE correoElectronico = ?)";
				values = new String[] {this.practicante.getMatricula(),
					this.practicante.getCorreoElectronico()};
				if (this.connection.preparedQuery(query, values)) {
					updated = true;
				}
			}
		}
		return updated;
	}

	/**
	 * Delete the current PRACTICANTE from the Database
	 *
	 * @return true => deleted | false => not deleted
	 */
	@Override
	public boolean delete() {
		boolean deleted = false;
		if (this.practicante != null && this.isRegistered()) {
			String query = "UPDATE Usuario SET status = 0 WHERE correoElectronico = ?";
			String[] values = {this.practicante.getCorreoElectronico()};
			if (this.connection.preparedQuery(query, values)) {
				deleted = true;
			}
		}
		return deleted;
	}

	/**
	 * Log the current PRACTICANTE
	 * Verifies if the current instance is registered<br/>
	 * and has the correct credentials to log in
	 *
	 * @return true => registered and correct credentials<br/>
	 * false => not registered or incorrect credentials
	 */
	@Override
	public boolean logIn() {
		boolean loggedIn = false;
		String query = "SELECT COUNT(idUsuario) AS TOTAL FROM Usuario WHERE correoElectronico = ? "
			+ "AND contrasena = ? AND status = 1";
		String[] values = {this.practicante.getCorreoElectronico(),
			this.practicante.getContrasena()};
		String[] names = {"TOTAL"};
		if (this.isRegistered()) {
			if (this.connection.select(query, values, names)[0][0].equals("1")) {
				loggedIn = true;
			}
		}
		return loggedIn;
	}

	/**
	 * Register the current instance to the database
	 * <p>
	 * Verifies that the current instance is not already registered,<br/>
	 * if not, saves it to the database.
	 *
	 * @return true => registered | false => couldn't register
	 */
	@Override
	public boolean signUp() {
		boolean signedUp = false;
		String query =
			"INSERT INTO Usuario (nombres, apellidos, correoElectronico, contrasena, status) "
				+ "VALUES (?, ?, ?, ?, 1)";
		String[] values = {this.practicante.getNombres(), this.practicante.getApellidos(),
			this.practicante.getCorreoElectronico(), this.practicante.getContrasena()};
		if (!this.isRegistered()) {
			if (this.connection.preparedQuery(query, values)) {
				query = "INSERT INTO Practicante (idUsuario, matricula) VALUES " +
					"((SELECT idUsuario FROM Usuario WHERE correoElectronico = ?), ?)";
				values = new String[] {this.practicante.getCorreoElectronico(),
					this.practicante.getMatricula()};
				if (this.connection.preparedQuery(query, values)) {
					signedUp = true;
				}
			}
		}
		return signedUp;
	}

	/**
	 * Verifies the existence of the current PRACTICANTE against the database<br/>
	 * Checks if the email of the current PRACTICANTE
	 * is already registered in the database
	 *
	 * @return true => registered | false => not registered
	 */
	@Override
	public boolean isRegistered() {
		boolean isRegistered = false;
		if (this.practicante != null && this.practicante.getCorreoElectronico() != null) {
			String query = "SELECT COUNT(idUsuario) AS TOTAL " +
				"FROM Usuario WHERE correoElectronico = ? AND status = 1";
			String[] values = {this.practicante.getCorreoElectronico()};
			String[] names = {"TOTAL"};
			if (this.practicante != null && this.practicante.getCorreoElectronico() != null) {
				if (this.connection.select(query, values, names)[0][0].equals("1")) {
					query = "SELECT COUNT(Practicante.idUsuario) AS TOTAL FROM Practicante " +
						"INNER JOIN Usuario ON Practicante.idUsuario = Usuario.idUsuario " +
						"WHERE Usuario.correoElectronico = ?";
					if (this.connection.select(query, values, names)[0][0].equals("1")) {
						isRegistered = true;
					}
				}
			}
		}
		return isRegistered;
	}

	/**
	 * Returns an array of all PRACTICANTE from DB
	 *
	 * @return Array of PRACTICANTE<br/>
	 * If there are no PRACTICANTEs registered, returns null
	 */
	public static Practicante[] getAll() {
		Practicante[] practicantes;
		DBConnection connection = new DBConnection();
		String query = "SELECT nombres, apellidos, correoElectronico, contrasena, matricula " +
			"FROM Usuario INNER JOIN Practicante ON Usuario.idUsuario = Practicante.idUsuario " +
			"WHERE Usuario.status = 1";
		String[] names = {"nombres", "apellidos", "correoElectronico", "contrasena", "matricula"};
		String[][] results = connection.select(query, null, names);
		practicantes = new Practicante[results.length];
		for (int i = 0; i < results.length; i++) {
			practicantes[i] = new Practicante(results[i][0], results[i][1], results[i][2],
				results[i][3], results[i][4]);
		}
		return practicantes;
	}

	/**
	 * Returns an instance of Practicante by its email
	 *
	 * @param practicante Instance of PRACTICANTE with email
	 * @return Instace of PRACTICANTE completed from the DB<br/>
	 * If the provided PRACTICANTE is not registered, it will return null
	 */
	public static Practicante get(Practicante practicante) {
		DBConnection connection = new DBConnection();
		Practicante returnPracticante = null;
		if (practicante != null) {
			if (new DAOPracticante(practicante).isRegistered()) {
				String query = "SELECT nombres, apellidos, correoElectronico, contrasena, " +
					"matricula FROM Usuario INNER JOIN Practicante " +
					"ON Usuario.idUsuario = Practicante.idUsuario " +
					"WHERE Usuario.correoElectronico = ?";
				String[] values = {practicante.getCorreoElectronico()};
				String[] names = {"nombres", "apellidos", "correoElectronico", "contrasena",
					"matricula"};
				String[][] results = connection.select(query, values, names);
				returnPracticante = new Practicante(results[0][0], results[0][1], results[0][2],
					results[0][3], results[0][4]);
			}
		}
		return returnPracticante;
	}

	public boolean selectProyect(String projectName) {
		return this.selectProyect(new DAOProyecto().loadProyecto(projectName));
	}

	/**
	 * Saves the selection of a project from the current PRACTICANTE in the database
	 *
	 * @param proyecto Instance of PROYECTO to relate in the database
	 * @return true => selection registered<br/>
	 * false => not registered
	 */
	public boolean selectProyect(Proyecto proyecto) {
		boolean selected = false;
		if (this.practicante != null && this.practicante.isComplete() && this.isRegistered() &&
			proyecto != null && proyecto.isComplete() && new DAOProyecto(proyecto).isRegistered()) {
			String query = "SELECT COUNT(idUsuario) AS TOTAL FROM SeleccionProyecto " +
				"WHERE idUsuario = (SELECT idUsuario FROM Usuario WHERE correoElectronico = ?)";
			String[] values = {this.practicante.getCorreoElectronico()};
			String[] names = {"TOTAL"};
			int selectedProyects =
				Integer.parseInt(this.connection.select(query, values, names)[0][0]);
			if (selectedProyects < 3) {
				query = "INSERT INTO SeleccionProyecto (idProyecto, idUsuario) VALUES " + "(" +
					"(SELECT idProyecto FROM Proyecto WHERE nombre = ? AND status = 1), " +
					"(SELECT idUsuario FROM Usuario WHERE correoElectronico = ?))";
				values = new String[] {proyecto.getNombre(),
					this.practicante.getCorreoElectronico()};
				if (this.connection.preparedQuery(query, values)) {
					selected = true;
				}
			}
		}
		return selected;
	}

	/**
	 * Returns an array of the selected PROYECTO from the current PRACTICANTE
	 *
	 * @return Array of PROYECTO
	 */
	public Proyecto[] getProyects() {
		Proyecto[] proyectos = null;
		if (this.practicante != null && this.practicante.isComplete() && this.isRegistered()) {
			String query = "SELECT nombre " + "FROM Proyecto INNER JOIN SeleccionProyecto ON " +
				"Proyecto.idProyecto = SeleccionProyecto.idProyecto " +
				"WHERE SeleccionProyecto.idUsuario = " +
				"(SELECT idUsuario FROM Usuario WHERE correoElectronico = ?) " +
				"AND Proyecto.status = 1";
			String[] values = {this.practicante.getCorreoElectronico()};
			String[] names = {"nombre"};
			String[][] results = this.connection.select(query, values, names);
			if (results.length > 0) {
				DAOProyecto daoProyecto = new DAOProyecto();
				proyectos = new Proyecto[results.length];
				for (int i = 0; i < results.length; i++) {
					proyectos[i] = daoProyecto.loadProyecto(results[i][0]);
				}
			}
		}
		return proyectos;
	}

	public boolean deleteSelectedProyect(String projectName) {
		boolean deleted = false;
		if (this.practicante != null && this.practicante.getCorreoElectronico() != null &&
			this.isRegistered() && projectName != null) {
			DAOProyecto daoProyecto = new DAOProyecto(projectName);
			if (daoProyecto.isRegistered()) {
				boolean isSelected = false;
				for (Proyecto proyecto: this.getProyects()) {
					if (proyecto != null && proyecto.getNombre().equals(projectName)) {
						isSelected = true;
						break;
					}
				}
				if (isSelected) {
					String query = "DELETE FROM SeleccionProyecto WHERE idUsuario = " +
						"(SELECT idUsuario FROM Usuario WHERE correoElectronico = ?) " +
						"AND idProyecto = " +
						"(SELECT idProyecto FROM Proyecto WHERE nombre = ? AND " + "status = 1)";
					String[] values = {this.practicante.getCorreoElectronico(), projectName};
					if (this.connection.preparedQuery(query, values)) {
						deleted = true;
					}
				}
			}
		}
		return deleted;
	}

}