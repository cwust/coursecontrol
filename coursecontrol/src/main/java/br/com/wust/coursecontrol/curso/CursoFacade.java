package br.com.wust.coursecontrol.curso;

import java.io.Serializable;

import br.com.wust.coursecontrol.abstracts.CourseControlFacade;

public class CursoFacade extends CourseControlFacade<Curso> implements Serializable{

	private static final long serialVersionUID = 1L;

	public CursoFacade() {
		super();
	}
}

