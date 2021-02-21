package Tests;

import DAO.DAOCoordinator;
import DAO.DAOProfesor;
import IDAO.IDAOProfessor;
import Models.Coordinator;
import Models.Professor;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import tools.P;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)

public class DAOCoordinatorTests {
    @Test
    public void a_signUpCoordinator() {
	    Coordinator coordinator = new Coordinator();
	    coordinator.setNombres("Angel Juan");
	    coordinator.setApellidos("Rodriguez Perez");
	    coordinator.setEmail("aj@hotmail.com");
	    coordinator.setContrasena("aj1234");
	    coordinator.setPersonalNo("N000011");
	    coordinator.setShift("Matutino");
	    DAOCoordinator daoCoordinator = new DAOCoordinator(coordinator);
	    try {
		    assertTrue(daoCoordinator.registrarse());
	    } catch (AssertionError e) {
		    System.out.println(e.getMessage());
	    }

    }

    @Test
    public void b_isRegistered() {
	    Coordinator coordinator = new Coordinator();
	    coordinator.setNombres("Angel Juan");
	    coordinator.setApellidos("Rodriguez Perez");
	    coordinator.setEmail("aj@hotmail.com");
	    coordinator.setContrasena("aj1234");
	    coordinator.setPersonalNo("N000011");
	    coordinator.setShift("Matutino");
	    DAOCoordinator daoCoordinator = new DAOCoordinator(coordinator);
	    assertTrue(daoCoordinator.estaRegistrado());
    }

    @Test
    public void c_updateProfesor() {
	    Professor roberto = new Professor();
	    DAOProfesor daoProfesor = new DAOProfesor(roberto);
	    roberto.setNombres("Alexis");
	    roberto.setApellidos("Alvarez Ortega");
	    roberto.setEmail("alexisao@hotmail.com");
	    roberto.setContrasena("alexis123");
	    roberto.setPersonalNo("N000001");
	    roberto.setShift("1");
	    try {
		    assertTrue(daoProfesor.update());
	    } catch (AssertionError e) {
		    e.printStackTrace();
	    }
    }

    @Test
    public void d_deleteProfessor() {
        try {
	        assertTrue(this.getDAOProfesor().eliminar());
        } catch (AssertionError e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void e_testGetIdShift() {
        System.out.println(getDAOProfesor().getIdShift());
        assertNotNull(getDAOProfesor().getIdShift());
    }

    @Test
    public void z_getAll() {
        for (Professor professor: IDAOProfessor.getAll()) {
	        assertNotNull(professor.getNombres());
	        P.pln(professor.getNombres());
        }
    }

    @Test
    public void z_getActive(){
	    Coordinator coordinator = null;
	    coordinator = DAOCoordinator.getActive();
	    System.out.println(coordinator);
	    assertTrue(coordinator.estaCompleto());
    }
	
	@Test
	public void isAnother() {
		assertTrue(new Coordinator().isAnother());
	}
	
	private DAOProfesor getDAOProfesor() {
		return new DAOProfesor(getInstanceProfesor());
	}
	
	private Professor getInstanceProfesor() {
		return new Professor(
			"Alexis",
			"Alvarez Ortega",
			"alexisao@hotmail.com",
			"alexis123",
			"N000001",
			"Mixto"
        );
    }
}