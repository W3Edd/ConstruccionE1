package Tests;

import DAO.DAOProfesor;
import IDAO.IDAOProfesor;
import Models.Profesor;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import tools.P;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)

public class DAOProfesorTests {
	@Test
	public void a_signUpProfesor() {
		Profesor alexis = new Profesor();
		DAOProfesor daoProfesor = new DAOProfesor(alexis);
		alexis.setNombres("Octavio");
		alexis.setApellidos("Ocharan");
		alexis.setEmail("ocha@hotmail.com");
		alexis.setContrasena("ocha1234");
		alexis.setNumeroPersonal("N000002");
		alexis.setTurno("1");
		try {
			assertTrue(daoProfesor.registrar());
		} catch (SQLException throwables) {
			throwables.printStackTrace();
		}
	}
	
	@Test
	public void b_isRegistered() {
		Profesor alexis = new Profesor();
		alexis.setNombres("Alexis");
		alexis.setApellidos("Alvarez");
		alexis.setEmail("a@b.c");
		alexis.setContrasena("alexis123");
		alexis.setNumeroPersonal("N12345678");
		alexis.setTurno("1");
		DAOProfesor daoProfesor = new DAOProfesor(alexis);
		try {
			assertTrue(daoProfesor.estaRegistrado());
		} catch (SQLException throwables) {
			throwables.printStackTrace();
		}
	}
	
	@Test
	public void c_updateProfesor() {
		Profesor roberto = new Profesor();
		DAOProfesor daoProfesor = new DAOProfesor(roberto);
		roberto.setNombres("Alexis");
		roberto.setApellidos("Alvarez Ortega");
		roberto.setEmail("alexisao@hotmail.com");
		roberto.setContrasena("alexis123");
		roberto.setNumeroPersonal("N000001");
		roberto.setTurno("1");
		try {
			assertTrue(daoProfesor.update());
		} catch (AssertionError e) {
			e.printStackTrace();
		} catch (SQLException throwables) {
			throwables.printStackTrace();
		}
	}
	
	@Test
	public void d_deleteProfessor() {
		try {
			assertTrue(this.getDAOProfesor().eliminar());
		} catch (AssertionError e) {
			System.out.println(e.getMessage());
		} catch (SQLException throwables) {
			throwables.printStackTrace();
		}
	}

	@Test
	public void estaActivo(){
		try {
			assertTrue(this.getDAOProfesor().estaRegistrado());
		} catch (AssertionError e) {
			System.out.println(e.getMessage());
		} catch (SQLException throwables) {
			throwables.printStackTrace();
		}
	}

	@Test
	public void obtenerProfesores(){
		try {
			Profesor[] profesores = IDAOProfesor.obtenerProfesores();
			for (Profesor profesor: profesores) {
				System.out.println(profesor);
			}
			assertNotNull(profesores);
		} catch (AssertionError e) {
			System.out.println(e.getMessage());
		} catch (SQLException throwables) {
			throwables.printStackTrace();
		}
	}
	
	@Test
	public void e_testGetIdShift() {
		try {
			System.out.println(getDAOProfesor().getIdShift());
			assertNotNull(getDAOProfesor().getIdShift());
		} catch (SQLException throwables) {
			throwables.printStackTrace();
		}

	}
	
	@Test
	public void z_getAll() {
		try {
			for (Profesor profesor : IDAOProfesor.obtenerProfesores()) {
				assertNotNull(profesor.getNombres());
				P.pln(profesor.getNombres());
			}
		} catch (Exception exception) {

		}
	}
	
	private DAOProfesor getDAOProfesor() {
		return new DAOProfesor(getInstanceProfesor());
	}
	
	private Profesor getInstanceProfesor() {
		return new Profesor(
			"Alexis",
			"Alvarez Ortega",
			"alexisao@hotmail.com",
			"alexis123",
			"N000001",
			"Mixto"
		);
	}
}
