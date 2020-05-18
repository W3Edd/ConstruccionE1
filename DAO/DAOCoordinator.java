package DAO;

import Connection.DBConnection;
import IDAO.IDAOCoordinator;
import IDAO.IDAOUser;
import Models.Coordinator;
import Models.Professor;

public class DAOCoordinator implements IDAOCoordinator {
	private Coordinator coordinator;
	private final DBConnection connection = new DBConnection();
	
	public DAOCoordinator(Coordinator coordinator) {
		this.coordinator = coordinator;
	}
	
	@Override
	public boolean signUp() {
		assert this.coordinator.isComplete() : "Coordinator isnt complete: DAOCoordinator.signUp()";
		assert !this.isRegistered() : "Coordinator is already registered: DAOCoordinator.signUp()";
		assert !this.isAnother() : "There is another coordinator: DAOCoordinator.signUp()";
		boolean signedUp = false;
		String query =
			"INSERT INTO Usuario (nombres, apellidos, correoElectronico, contrasena, status)" +
				"VALUES (?, ?, ?, ?, ?)";
		String[] values =
			{this.coordinator.getNames(),
				this.coordinator.getLastnames(),
				this.coordinator.getEmail(),
				this.coordinator.getPassword(), "1"};
		if(this.connection.sendQuery(query, values)){
			query = "INSERT INTO Coordinador (idUsuario, noPersonal, fechaRegistro) VALUES " +
					"((SELECT idUsuario FROM Usuario WHERE correoElectronico = ?),?,(SELECT CURRENT_DATE))";
			values = new String[]{this.coordinator.getEmail(), this.coordinator.getPersonalNo()};
			signedUp = this.connection.sendQuery(query, values);
		}

		return signedUp;
	}

	public boolean update(){
		assert coordinator != null : "Coordinator is null : update()";
		assert coordinator.isComplete() : "Coordinator is not complete : update()";
		assert this.isActive() : "Coordinator has not a status active on the system";
		boolean updated = false;
		String query = "UPDATE Usuario SET nombres = ?, apellidos = ? WHERE correoElectronico = ?";
		String[] values = {this.coordinator.getNames(), this.coordinator.getLastnames(),
				this.coordinator.getEmail()};
		if (this.connection.sendQuery(query, values)) {
			query = "UPDATE Coordinador SET noPersonal = ? WHERE idUsuario = ?";
			values = new String[]{this.coordinator.getPersonalNo(), this.getIdCoordinator()};
			if (this.connection.sendQuery(query, values)) {
				updated = true;
			}
		}
		return updated;
	}
	
	@Override
	public boolean isRegistered() {
		String query = "SELECT COUNT(Usuario.idUsuario) AS TOTAL FROM Usuario " +
			"INNER JOIN Coordinador ON Usuario.idUsuario = Coordinador.idUsuario " +
			"WHERE correoElectronico = ?";
		String[] values = {this.coordinator.getEmail()};
		String[] names = {"TOTAL"};
		return this.connection.select(query, values, names)[0][0].equals("1");
	}
	
	@Override
	public boolean logIn() {
		assert this.isRegistered() : "Coordinator is not registered: DAOCoordinator.logIn()";
		String query = "SELECT COUNT(idUsuario) AS TOTAL FROM Coordinador " +
			"WHERE correoElectronico = ? AND contrasena = ?";
		String[] values = {this.coordinator.getEmail(), this.coordinator.getPassword()};
		String[] names = {"TOTAL"};
		return this.connection.select(query, values, names)[0][0].equals("1");
	}
	
	@Override
	public boolean delete() {
		assert this.coordinator != null : "Coordinator is null: DAOCoordinator.delete()";
		assert this.isRegistered() : "Coordinator is not registered: DAOCoordinator.delete()";
		assert this.isActive() : "Coordinator is not active: DAOCoordinator.delete()";
		String query = "UPDATE Usuario SET status = 0 WHERE correoElectronico = ?";
		String[] values = {this.coordinator.getEmail()};
		return this.connection.sendQuery(query, values);
	}
	
	public boolean isActive() {
		assert this.coordinator != null : "Coordinator is null: DAOCoordinator.isActive()";
		assert this.coordinator.getEmail() != null :
			"Coordinator email is null: DAOCoordinator.isActive()";
		assert this.isRegistered() : "Coordinator is not registered: DAOCoordinator.isActive()";
		String query = "SELECT status FROM Usuario WHERE correoElectronico = ?";
		String[] values = {this.coordinator.getEmail()};
		String[] names = {"status"};
		return this.connection.select(query, values, names)[0][0].equals("1");
	}
	
	@Override
	public boolean reactive() {
		assert this.coordinator != null : "Coordinator is null: DAOCoordinator.reactive()";
		assert this.isRegistered() : "Coordinator is not registered: DAOCoordinator.reactive()";
		assert this.isActive() : "Coordinator is not active: DAOCoordinator.reactive()";
		String query = "UPDATE Usuario SET status = 1 WHERE correoElectronico = ?";
		String[] values = {this.coordinator.getEmail()};
		return this.connection.sendQuery(query, values);
	}
	
	public boolean isAnother() {
		String query = "SELECT COUNT (Coordinador.idUsuario) AS TOTAL FROM Coordinador " +
			"INNER JOIN Usuario ON Coordinador.idUsuario = Usuario.idUsuario WHERE Usuario.status = 1";
		String[] names = {"TOTAL"};
		String[][] resultQuery = this.connection.select(query, null, names);
		return Integer.parseInt(resultQuery[0][0]) > 0;
	}

	public static Coordinator[] getAll() {
		String query =
				"SELECT nombres, apellidos, correoElectronico, contrasena, noPersonal, status, fechaRegistro FROM " +
						"Usuario INNER JOIN Coordinador ON Usuario.idUsuario = Coordinador.idUsuario";
		String[] names =
				{"nombres", "apellidos", "correoElectronico", "contrasena", "noPersonal", "status", "fechaRegistro"};
		String[][] responses = new DBConnection().select(query, null, names);
		Coordinator[] coordinators = new Coordinator[responses.length];
		for (int i = 0; i < responses.length; i++) {
			coordinators[i] = new Coordinator(
					responses[i][0],
					responses[i][1],
					responses[i][2],
					responses[i][3],
					responses[i][4]
			);
		}
		return coordinators;
	}

	private String getIdCoordinator() {
		String query = "SELECT idUsuario AS idCoordinator FROM Usuario WHERE correoElectronico = ?";
		String[] values = {this.coordinator.getEmail()};
		String[] names = {"idCoordinator"};
		return this.connection.select(query, values, names)[0][0];
	}
	
}