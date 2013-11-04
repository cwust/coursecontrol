package br.com.wust.coursecontrol.usuario;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import br.com.wust.coursecontrol.abstracts.CourseControlMB;

@ViewScoped
@ManagedBean
public class UsuarioMB extends CourseControlMB<Usuario> implements Serializable {

	private static final long serialVersionUID = 1L;
	
	public static final String CADASTRO_USUARIOS_PAGE = "/pages/cadastroUsuarios";
	
	public String editarUsuario() {
		//setEntity(getFacade().findByCodigo(codigo));
		System.out.println("cpf=" + getEntity().getPessoa().getCpf());
		
		
		return CADASTRO_USUARIOS_PAGE;
	}
	
	public String novoUsuario() {
		setEntity(new Usuario());
		return CADASTRO_USUARIOS_PAGE;
	}

	public Integer getCodigo() {
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		
		this.codigo = codigo;
		System.out.println("XXXXXXXXXXXXXXXXXXXXsws--->"+codigo );
	}

	@ManagedProperty(value="#{codigoUsuario}")
	private Integer codigo;
}
