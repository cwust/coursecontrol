package br.com.wust.coursecontrol.curso;

import java.io.Serializable;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import br.com.wust.coursecontrol.abstracts.CourseControlMB;

@ViewScoped
@ManagedBean
public class CursoMB extends CourseControlMB<Curso> implements Serializable {

	private static final long serialVersionUID = 1L;

	private List<Curso> listCursos;

	public CursoFacade getCursoFacade() {
		return (CursoFacade) super.getFacade();
	}

	public Curso getCurso() {
		return super.getEntity();
	}

	public void setCurso(Curso curso) {
		super.setEntity(curso);
	}
	
	public void saveCurso() {
		createCurso();
		//updateCurso();
	}

	public void createCurso() {
		try {
			getCursoFacade().insert(getCurso());
			closeDialog();
			displayInfoMessageToUser("Curso cadastrado com sucesso.");
			loadCursos();
			resetCurso();
		} catch (Exception e) {
			keepDialogOpen();
			displayErrorMessageToUser("Ocorreu um erro ao alterar o curso. Tente novamente mais tarde, ou entre em contato com o Administrador do Sistema.");
			e.printStackTrace();
		}
	}

	public void updateCurso() {
		try {
			getCursoFacade().update(getCurso());
			closeDialog();
			displayInfoMessageToUser("Alterado com sucesso");
			loadCursos();
			resetCurso();
		} catch (Exception e) {
			keepDialogOpen();
			displayErrorMessageToUser("Ocorreu um erro ao alterar o curso. Tente novamente mais tarde, ou entre em contato com o Administrador do Sistema.");
			e.printStackTrace();
		}
	}

	public List<Curso> getAllCursos() {
		if (listCursos == null) {
			loadCursos();
		}

		return listCursos;
	}

	private void loadCursos() {
		listCursos = getCursoFacade().listAll();
	}

	public void resetCurso() {
		resetEntity();
	}
	
	public void alterarCurso(Integer codigo) {
		setEntity(getCursoFacade().findByCodigo(codigo));
	}
	
	public void novoCurso() {
		resetCurso();
	}
	
	public void excluirCurso(Integer codigo) {
		try {
			getCursoFacade().deleteByCodigo(codigo);
			displayInfoMessageToUser("Curso excluído com sucesso.");
			loadCursos();
		} catch (Exception e) {
			displayErrorMessageToUser("Ocorreu um erro ao excluir o curso. Tente novamente mais tarde, ou entre em contato com o Administrador do Sistema.");
			e.printStackTrace();
		}
	}	
}
