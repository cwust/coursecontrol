package br.com.wust.coursecontrol.emf;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class CourseControlEntityManagerFactory {
	private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("coursecontrol");

	public static EntityManagerFactory getEmf() {
		return emf;
	}
}
