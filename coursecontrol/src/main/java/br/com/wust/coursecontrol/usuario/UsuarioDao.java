package br.com.wust.coursecontrol.usuario;

import br.com.wust.coursecontrol.abstracts.CourseControlDao;

public class UsuarioDao extends CourseControlDao<Usuario>{

	private static final long serialVersionUID = 1L;

	@Override
	public String getDefaultOrderBy() {
		return "login";
	}	
	
	@Override
	public String getDefaultFetchs() {
		return "join fetch usuario.pessoa pessoa";
	}
	
	
	
}
