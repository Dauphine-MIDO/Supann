package io.github.oliviercailloux.supann;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ImmutableList;

import ebx.ebx_dataservices.StandardException;
import schemas.ebx.dataservices_1.StructureType.Root.Structure;
import schemas.ebx.dataservices_1.StudentType.Root.Student;
import schemas.ebx.dataservices_1.TeacherType.Root.Teacher;

class QueryTests {
	@SuppressWarnings("unused")
	private static final Logger LOGGER = LoggerFactory.getLogger(QueryTests.class);
	private final SupannQuerier querier;

	@BeforeAll
	static void setDefaultAuthenticator() throws Exception {
		QueriesHelper.setDefaultAuthenticator();
	}

	QueryTests() {
		querier = new SupannQuerier();
	}

	@Test
	void testStructure() throws Exception {
		final Structure structure = querier.getStructure("A5IFO-902");
		assertEquals("M1 MIAGE Apprentissage", structure.getLongLabel().getValue());
		assertEquals("M1 MIAGE Apprentissage", structure.getShortLabel().getValue());
	}

	@Test
	void testTeacher() throws Exception {
		assertTrue(querier.getTeachers("lastname = 'Cailloux'").isEmpty());
		assertTrue(querier.getTeachers("lastname = 'CAILLOUX'").isEmpty());
		assertTrue(querier.getTeachers("login = 'ocailloux'").isEmpty());

		final List<Teacher> teachers = querier.getTeachers("firstname = 'Olivier'");
		final Teacher t1 = teachers.get(0);
		assertNull(t1.getSupannCivilite());
		LOGGER.info("Found: {}.", teachers.stream().map(Stringer::toString).collect(ImmutableList.toImmutableList()));

//		assertEquals("Mr", teacher.getId());
//		assertEquals("Mr", teacher.getCivilite().getValue());
//		assertEquals("Olivier", teacher.getFirstname().getValue());
//		assertEquals("Cailloux", teacher.getLastname().getValue());
//		assertEquals("ocailloux", teacher.getLogin().getValue());
//		assertEquals("olivier.cailloux@dauphine.fr", teacher.getMail().getValue());
//		assertEquals("Maitre de Conférences", teacher.getStatus().getValue());
	}

	@Test
	void testStudent() throws Exception {
		assertDoesNotThrow(() -> querier.getStudent("21802998"));
		assertDoesNotThrow(() -> querier.getStudent("22004340"));
		final Student student218 = querier.getStudent("21803737");
		LOGGER.info("Found: {}.", Stringer.toString(student218));
		final Student student219 = querier.getStudent("21906349");
		LOGGER.info("Found: {}.", Stringer.toString(student219));

//		final List<Student> students2018 = querier.getStudents("inscription/year = '2018'");
//		assertFalse(students2018.isEmpty());
//		LOGGER.info("2018: {}.", students2018.size());
//
		assertThrows(StandardException.class, () -> querier.getStudents("inscription/[year = '2018']"));
		assertThrows(StandardException.class, () -> querier.getStudents("inscription/(year = '2018')"));

//		final List<Student> students2019 = querier.getStudents("inscription/year = '2019'");
//		assertFalse(students2019.isEmpty());
//		LOGGER.info("2019: {}.", students2019.size());

		final List<Student> studentsMIDO = querier.getStudents("inscription/departement = 'MIDO'");
		assertFalse(studentsMIDO.isEmpty());
		LOGGER.info("MIDO: {}.", studentsMIDO.size());

		final List<Student> studentsA4MIA = querier.getStudents("inscription/etape = '{UAI:0750736T}A4AMIA-100'");
		assertFalse(studentsA4MIA.isEmpty());
		LOGGER.info("A4MIA: {}.", studentsA4MIA.size());

		final List<Student> studentsA3MADA = querier.getStudents("inscription/etape = '{UAI:0750736T}A3MADA-403'");
		assertFalse(studentsA3MADA.isEmpty());
		LOGGER.info("A3MADA: {}.", studentsA3MADA.size());
		final ImmutableList<String> studentsA317 = studentsA3MADA.stream()
				.filter(s -> s.getSupannEtuInscription().stream()
						.anyMatch(i -> i.getSupannEtuAnneeInscription().getValue().equals("2017")
								&& i.getSupannEtuEtape().getValue().equals("{UAI:0750736T}A3MADA-403")))
				.map(Stringer::toString).collect(ImmutableList.toImmutableList());
		LOGGER.info("Among those, in 2017: {}.", studentsA317.size());
		final ImmutableList<String> studentsA318 = studentsA3MADA.stream()
				.filter(s -> s.getSupannEtuInscription().stream()
						.anyMatch(i -> i.getSupannEtuAnneeInscription().getValue().equals("2018")
								&& i.getSupannEtuEtape().getValue().equals("{UAI:0750736T}A3MADA-403")))
				.map(Stringer::toString).collect(ImmutableList.toImmutableList());
		LOGGER.info("Among those, in 2018: {}.", studentsA318.size());

		final List<Student> students2017A3MADA = querier
				.getStudents("inscription/year = '2017' and inscription/etape = '{UAI:0750736T}A3MADA-403'");
		assertFalse(students2017A3MADA.isEmpty());
		LOGGER.info("2017 A3MADA: {}.", students2017A3MADA.size());

		final List<Student> studentsA3AMIA = querier.getStudents("inscription/etape = '{UAI:0750736T}A3AMIA-100'");
		assertFalse(studentsA3AMIA.isEmpty());
		LOGGER.info("A3AMIA: {}.", studentsA3AMIA.size());

		final List<Student> students2019A4MIA = querier
				.getStudents("inscription/year = '2019' and inscription/etape = '{UAI:0750736T}A4AMIA-100'");
		assertFalse(students2019A4MIA.isEmpty());
		LOGGER.info("2019 A4MIA: {}.", students2019A4MIA.size());

		assertTrue(querier.getStudents("firstname = 'Olivier'").isEmpty());

		final List<Student> students = querier.getStudents("firstname = 'OLIVIER'");
		assertTrue(students.size() >= 20);
	}

}
