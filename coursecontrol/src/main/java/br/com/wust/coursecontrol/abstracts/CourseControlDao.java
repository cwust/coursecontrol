package br.com.wust.coursecontrol.abstracts;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import br.com.wust.coursecontrol.convention.ConventionUtils;
import br.com.wust.coursecontrol.emf.CourseControlEntityManagerFactory;

public abstract class CourseControlDao<T extends CourseControlEntity<T>> implements Serializable {
	private static final long serialVersionUID = 1L;
	private String defaultSelect;

	private EntityManager em;

	private Class<T> entityClass;

	@SuppressWarnings("unchecked")
	public CourseControlDao() {
		this.entityClass = (Class<T>) ConventionUtils.getInstance().getEntityClassForDao(this.getClass());
		
	}

	public EntityManagerFactory getEmf() {
		return CourseControlEntityManagerFactory.getEmf();
	}
	
	public void beginTransaction() {
		em =  getEmf().createEntityManager();

		em.getTransaction().begin();
	}

	public void commit() {
		em.getTransaction().commit();
	}

	public void rollback() {
		em.getTransaction().rollback();
	}

	public void closeTransaction() {
		em.close();
	}

	public void commitAndCloseTransaction() {
		commit();
		closeTransaction();
	}

	public void flush() {
		em.flush();
	}

	public void joinTransaction() {
		em = getEmf().createEntityManager();
		em.joinTransaction();
	}

	public void save(T entity) {
		em.persist(entity);
	}

	protected void delete(Object id, Class<T> classe) {
		T entityToBeRemoved = em.getReference(classe, id);

		em.remove(entityToBeRemoved);
	}

	public T update(T entity) {
		return em.merge(entity);
	}

	@SuppressWarnings("unchecked")
	public T find(int codigo) {
		StringBuilder sb = new StringBuilder(getDefaultSelect());
		sb.append(" WHERE ");
		sb.append(entityClass.getSimpleName().toLowerCase());
		sb.append(".codigo = :codigo");
		
		Query query = em.createQuery(sb.toString());
		query.setParameter("codigo", codigo);
		
		return (T) query.getSingleResult();
	}

	public T findReferenceOnly(int entityID) {
		return em.getReference(entityClass, entityID);
	}

	@SuppressWarnings("unchecked")
	public List<T> findAll() {
		StringBuilder sb = new StringBuilder(getDefaultSelect());
		
		sb.append(" ");
		
		String orderBy = getDefaultOrderBy(); 
		if (orderBy != null) {
			sb.append("order by ").append(orderBy);			
		}
		
		Query query = em.createQuery(sb.toString());
		return query.getResultList();
	}
	
	private String getDefaultSelect() {
		if (defaultSelect == null) {
			createDefaultSelect();
		}
		return defaultSelect;
	}
	
	private void createDefaultSelect() {
		StringBuilder sb = new StringBuilder();
		sb.append("from ").append(entityClass.getSimpleName());
		sb.append(" ").append(entityClass.getSimpleName().toLowerCase()); 
		//alias
		
		sb.append(" ");
		
		String fetchs = getDefaultFetchs(); 
		if (fetchs != null) {
			sb.append(fetchs);			
		}		
		
		defaultSelect = sb.toString();
	}

	@SuppressWarnings("unchecked")
	protected T findOneResult(String namedQuery, Map<String, Object> parameters) {
		T result = null;

		try {
			Query query = em.createNamedQuery(namedQuery);

			// Method that will populate parameters if they are passed not null
			// and empty
			if (parameters != null && !parameters.isEmpty()) {
				populateQueryParameters(query, parameters);
			}

			result = (T) query.getSingleResult();

		} catch (NoResultException e) {
			System.out
					.println("No result found for named query: " + namedQuery);
		} catch (Exception e) {
			System.out.println("Error while running query: " + e.getMessage());
			e.printStackTrace();
		}

		return result;
	}

	private void populateQueryParameters(Query query,
			Map<String, Object> parameters) {
		for (Entry<String, Object> entry : parameters.entrySet()) {
			query.setParameter(entry.getKey(), entry.getValue());
		}
	}
	
	public String getDefaultOrderBy() {
		return null;
	}

	public String getDefaultFetchs() {
		return null;
	}

	public void deleteByCodigo(Integer codigo) {
		delete(codigo, entityClass);
	}
}