package br.com.wust.coursecontrol.abstracts;

import java.io.Serializable;
import java.util.List;

import br.com.wust.coursecontrol.convention.ConventionUtils;

public class CourseControlFacade  <T extends CourseControlEntity<T>> implements Serializable{

	private static final long serialVersionUID = 1L;

	private CourseControlDao<T> dao;

	public CourseControlFacade() {
		this.dao = ConventionUtils.getInstance().createDaoForFacade(this);
	}

	public void insert(T entity) {
		dao.beginTransaction();
		dao.save(entity);
		dao.commitAndCloseTransaction();
	}

	public void update(T entity) {
		dao.beginTransaction();
		T persistedEntity= dao.find(entity.getCodigo());
		persistedEntity.copyAttributes(entity);
		dao.update(persistedEntity);
		dao.commitAndCloseTransaction();
	}

	public T findByCodigo(int codigo) {
		dao.beginTransaction();
		T entity = dao.find(codigo);
		dao.closeTransaction();
		return entity;
	}

	public List<T> listAll() {
		dao.beginTransaction();
		List<T> result = dao.findAll();
		dao.closeTransaction();
		return result;
	}
 
    public void deleteByCodigo(Integer codigo) {
        dao.beginTransaction();
        dao.deleteByCodigo(codigo);
        dao.commitAndCloseTransaction();
    }
}

