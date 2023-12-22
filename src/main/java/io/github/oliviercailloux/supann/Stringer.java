package io.github.oliviercailloux.supann;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.xml.bind.JAXBElement;

import com.google.common.collect.ImmutableList;

import schemas.ebx.dataservices_1.StudentType.Root.Student;
import schemas.ebx.dataservices_1.StudentType.Root.Student.SupannEtuInscription;
import schemas.ebx.dataservices_1.TeacherType.Root.Teacher;

public class Stringer {
	public static String toString(Teacher teacher) {
		checkNotNull(teacher);
		final String pres = Stream
				.<Supplier<JAXBElement<String>>>of(teacher::getSupannCivilite, teacher::getGivenName, teacher::getSurname)
				.map(Stringer::asOptional).map(o -> o.orElse("?")).collect(Collectors.joining(" "));
		final String rest = Stream
				.<Supplier<JAXBElement<String>>>of(teacher::getSupannAliasLogin, teacher::getMail, teacher::getDisplayName)
				.map(Stringer::asOptional).map(o -> o.orElse("?")).collect(Collectors.joining("; "));
		return teacher.getUid() + " - " + pres + "; " + rest;
	}

	public static String toString(Student student) {
		checkNotNull(student);
		final String pres = Stream
				.<Supplier<JAXBElement<String>>>of(student::getSupannCivilite, student::getGivenName, student::getSurname)
				.map(Stringer::asOptional).map(o -> o.orElse("?")).collect(Collectors.joining(" "));
		final String rest = Stream
				.<Supplier<JAXBElement<String>>>of(student::getSupannAliasLogin, student::getMail, student::getDisplayName)
				.map(Stringer::asOptional).map(o -> o.orElse("?")).collect(Collectors.joining("; "));
		return student.getUuid() + " - " + pres + "; " + rest + " - "
				+ student.getSupannEtuInscription().stream().map(Stringer::toString).collect(ImmutableList.toImmutableList());
	}

	public static String toString(SupannEtuInscription inscription) {
		checkNotNull(inscription);
		final String pres = Stream
				.<Supplier<JAXBElement<String>>>of(inscription::getSupannEntiteAffectation, inscription::getSupannEtuDiplome,
						inscription::getSupannEtuEtape)
				.map(Stringer::asOptional).map(o -> o.orElse("?")).collect(Collectors.joining(" / "));
		return asOptional(inscription::getSupannEtuAnneeInscription).orElse("?") + ": " + pres;
	}

	public static <T> Optional<T> asOptional(Supplier<JAXBElement<T>> property) {
		checkNotNull(property);
		return Optional.ofNullable(property.get()).map(e -> e.getValue());
	}
}
