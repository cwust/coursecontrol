package br.com.wust.coursecontrol.curso;

import br.com.wust.coursecontrol.abstracts.CourseControlDao;

public class CursoDao extends CourseControlDao<Curso>{

	private static final long serialVersionUID = 1L;

	@Override
	public String getDefaultOrderBy() {
		return "nome";
	}	
}
