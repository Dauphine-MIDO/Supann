package io.github.oliviercailloux.supann;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.xml.bind.JAXBElement;

import com.google.common.collect.ImmutableList;

import schemas.ebx.dataservices_1.StudentType.Root.Student;
import schemas.ebx.dataservices_1.StudentType.Root.Student.Inscription;
import schemas.ebx.dataservices_1.TeacherType.Root.Teacher;

public class Stringer {
	public static String toString(Teacher teacher) {
		checkNotNull(teacher);
		final String pres = Stream
				.<Supplier<JAXBElement<String>>>of(teacher::getCivilite, teacher::getFirstname, teacher::getLastname)
				.map(Stringer::asOptional).map(o -> o.orElse("?")).collect(Collectors.joining(" "));
		final String rest = Stream
				.<Supplier<JAXBElement<String>>>of(teacher::getLogin, teacher::getMail, teacher::getStatus)
				.map(Stringer::asOptional).map(o -> o.orElse("?")).collect(Collectors.joining("; "));
		return teacher.getId() + " - " + pres + "; " + rest;
	}

	public static String toString(Student student) {
		checkNotNull(student);
		final String pres = Stream
				.<Supplier<JAXBElement<String>>>of(student::getCivilite, student::getFirstname, student::getLastname)
				.map(Stringer::asOptional).map(o -> o.orElse("?")).collect(Collectors.joining(" "));
		final String rest = Stream
				.<Supplier<JAXBElement<String>>>of(student::getLogin, student::getMail, student::getStatus)
				.map(Stringer::asOptional).map(o -> o.orElse("?")).collect(Collectors.joining("; "));
		return student.getId() + " - " + pres + "; " + rest + " - "
				+ student.getInscription().stream().map(Stringer::toString).collect(ImmutableList.toImmutableList());
	}

	public static String toString(Inscription inscription) {
		checkNotNull(inscription);
		final String pres = Stream
				.<Supplier<JAXBElement<String>>>of(inscription::getDepartement, inscription::getDiplome,
						inscription::getEtape)
				.map(Stringer::asOptional).map(o -> o.orElse("?")).collect(Collectors.joining(" / "));
		return asOptional(inscription::getYear).orElse("?") + ": " + pres + "; "
				+ asOptional(inscription::getInsDate).orElse("?");
	}

	public static <T> Optional<T> asOptional(Supplier<JAXBElement<T>> property) {
		checkNotNull(property);
		return Optional.ofNullable(property.get()).map(e -> e.getValue());
	}
}
