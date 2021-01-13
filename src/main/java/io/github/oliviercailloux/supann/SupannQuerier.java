package io.github.oliviercailloux.supann;

import static com.google.common.base.Verify.verify;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Iterables;

import ebx.ebx_dataservices.EbxDataservices;
import ebx.ebx_dataservices.EbxDataservicesService;
import ebx.ebx_dataservices.StandardException;
import io.github.oliviercailloux.xml_utils.XmlUtils;
import schemas.ebx.dataservices_1.ObjectFactory;
import schemas.ebx.dataservices_1.SelectStructureRequestType;
import schemas.ebx.dataservices_1.SelectStructureResponseType;
import schemas.ebx.dataservices_1.SelectStudentRequestType;
import schemas.ebx.dataservices_1.SelectStudentResponseType;
import schemas.ebx.dataservices_1.SelectTeacherRequestType;
import schemas.ebx.dataservices_1.SelectTeacherResponseType;
import schemas.ebx.dataservices_1.StructureType.Root.Structure;
import schemas.ebx.dataservices_1.StudentType.Root.Student;
import schemas.ebx.dataservices_1.TeacherType.Root.Teacher;

public class SupannQuerier {
	@SuppressWarnings("unused")
	private static final Logger LOGGER = LoggerFactory.getLogger(SupannQuerier.class);

	private final EbxDataservices dataservices;

	public SupannQuerier() {
		dataservices = new EbxDataservicesService().getEbxDataservices();
	}

	public List<Structure> getStructures(String predicate) throws StandardException {
		final SelectStructureRequestType request = new SelectStructureRequestType();
		request.setBranch("pvRefSupann");
		request.setInstance("RefSupann");
		request.setPredicate(predicate);
		LOGGER.debug("Request: {}.", XmlUtils.toXml(new ObjectFactory().createSelectStructure(request)));
		final SelectStructureResponseType result = dataservices.selectStructureOperation(request);
		LOGGER.debug("Result: {}.", XmlUtils.toXml(new ObjectFactory().createSelectStructureResponse(result)));
		return result.getData().getRoot().getStructure();
	}

	public Structure getStructure(String code) throws StandardException {
		final String predicate = "code = '" + code + "'";
		final List<Structure> structures = getStructures(predicate);
		verify(structures.size() == 1, "" + structures.size());
		final Structure structure = Iterables.getOnlyElement(structures);
		verify(structure.getCode().equals(code));
		return structure;
	}

	public List<Teacher> getTeachers(String predicate) throws StandardException {
		final SelectTeacherRequestType request = new SelectTeacherRequestType();
		request.setBranch("pvRefSupann");
		request.setInstance("RefSupann");
		request.setPredicate(predicate);
		LOGGER.debug("Request: {}.", XmlUtils.toXml(new ObjectFactory().createSelectTeacher(request)));
		final SelectTeacherResponseType result = dataservices.selectTeacherOperation(request);
		LOGGER.debug("Result: {}.", XmlUtils.toXml(new ObjectFactory().createSelectTeacherResponse(result)));
		return result.getData().getRoot().getTeacher();
	}

	public Teacher getTeacher(String id) throws StandardException {
		final String predicate = "id = '" + id + "'";
		final List<Teacher> teachers = getTeachers(predicate);
		verify(teachers.size() == 1, "" + teachers.size());
		final Teacher teacher = Iterables.getOnlyElement(teachers);
		verify(teacher.getId().equals(id));
		return teacher;
	}

	public List<Student> getStudents(String predicate) throws StandardException {
		final SelectStudentRequestType request = new SelectStudentRequestType();
		request.setBranch("pvRefSupann");
		request.setInstance("RefSupann");
		request.setPredicate(predicate);
		LOGGER.debug("Request: {}.", XmlUtils.toXml(new ObjectFactory().createSelectStudent(request)));
		final SelectStudentResponseType result = dataservices.selectStudentOperation(request);
		LOGGER.debug("Result: {}.", XmlUtils.toXml(new ObjectFactory().createSelectStudentResponse(result)));
		return result.getData().getRoot().getStudent();
	}

	public Student getStudent(String id) throws StandardException {
		final String predicate = "id = '" + id + "'";
		final List<Student> students = getStudents(predicate);
		verify(students.size() == 1, id + " ⇒ " + students.size());
		final Student student = Iterables.getOnlyElement(students);
		verify(student.getId().equals(id));
		return student;
	}
}
