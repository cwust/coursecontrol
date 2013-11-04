package br.com.wust.coursecontrol.convention;

import javax.persistence.EntityManager;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.Metamodel;

import br.com.wust.coursecontrol.abstracts.CourseControlDao;
import br.com.wust.coursecontrol.abstracts.CourseControlEntity;
import br.com.wust.coursecontrol.abstracts.CourseControlFacade;
import br.com.wust.coursecontrol.abstracts.CourseControlMB;
import br.com.wust.coursecontrol.emf.CourseControlEntityManagerFactory;

public class ConventionUtils {
	public static final String BASE_PACKAGE = "br.com.wust.coursecontrol";
	public static final String MB_SUFIX = "MB";
	public static final String FACADE_SUFIX = "Facade";
	public static final String DAO_SUFIX = "Dao";
	
	private BidirectionalMap<String, Class<? extends CourseControlMB<?>>> mapMBClass;
	private BidirectionalMap<String, Class<? extends CourseControlFacade<?>>> mapFacadeClass;
	private BidirectionalMap<String, Class<? extends CourseControlDao<?>>> mapDaoClass;
	private BidirectionalMap<String, Class<? extends CourseControlEntity<?>>> mapEntityClass;
	
	private static ConventionUtils instance;
	
	private ConventionUtils() {
		initialize();
	}
	
	public static ConventionUtils getInstance() {
		if (instance == null) {
			instance = new ConventionUtils();
		}
		return instance;
	}
	
	private void initialize() {
		mapMBClass = new BidirectionalMap<String, Class<? extends CourseControlMB<?>>>();
		mapFacadeClass = new BidirectionalMap<String, Class<? extends CourseControlFacade<?>>>();
		mapDaoClass = new BidirectionalMap<String, Class<? extends CourseControlDao<?>>>();
		mapEntityClass = new BidirectionalMap<String, Class<? extends CourseControlEntity<?>>>();
		
		EntityManager em = CourseControlEntityManagerFactory.getEmf().createEntityManager();
		
		try {
			Metamodel metamodel = em.getMetamodel();
			for (EntityType<?> entityType: metamodel.getEntities()) {
				mapMBClass.put(entityType.getName(), getMBClassFor(entityType.getName()));
				mapFacadeClass.put(entityType.getName(), getFacadeClassFor(entityType.getName()));
				mapDaoClass.put(entityType.getName(), getDaoClassFor(entityType.getName()));
				mapEntityClass.put(entityType.getName(), getEntityClassFor(entityType.getName()));
			}
		} finally {
			em.close();
		}
	}

	private String getPackageForEntity(String entityName) {
		return String.format("%s.%s", BASE_PACKAGE, entityName.toLowerCase());
		
	}

	@SuppressWarnings("unchecked")
	private Class<? extends CourseControlMB<?>> getMBClassFor(String entityName) {
		String className = String.format("%s.%s%s",
				getPackageForEntity(entityName),
				entityName,
				MB_SUFIX);
		try {
			return (Class<? extends CourseControlMB<?>>) Class.forName(className);
		} catch (ClassNotFoundException cnfe) {
			return null;
		} catch (Exception e) {
			throw new RuntimeException("O MB da entidade " + entityName + " nao esta declarado no pacote esperado. Esperava-se " + className, e);
		}
	}

	@SuppressWarnings("unchecked")
	private Class<? extends CourseControlFacade<?>> getFacadeClassFor(String entityName) {
		String className = String.format("%s.%s%s",
				getPackageForEntity(entityName),
				entityName,
				FACADE_SUFIX);
		try {
			return (Class<? extends CourseControlFacade<?>>) Class.forName(className);
		} catch (ClassNotFoundException cnfe) {
			return null;
		} catch (Exception e) {
			throw new RuntimeException("O facade da entidade " + entityName + " nao esta declarado no pacote esperado. Esperava-se " + className, e);
		}
	}

	@SuppressWarnings("unchecked")
	private Class<? extends CourseControlDao<?>> getDaoClassFor(String entityName) {
		String className = String.format("%s.%s%s",
				getPackageForEntity(entityName),
				entityName,
				DAO_SUFIX);
		try {
			return (Class<? extends CourseControlDao<?>>) Class.forName(className);
		} catch (ClassNotFoundException cnfe) {
			return null;
		} catch (Exception e) {
			throw new RuntimeException("O dao da entidade " + entityName + " nao esta declarado no pacote esperado. Esperava-se " + className, e);
		}
	}

	@SuppressWarnings("unchecked")
	private Class<? extends CourseControlEntity<?>> getEntityClassFor(String entityName) {
		String className = String.format("%s.%s",
				getPackageForEntity(entityName),
				entityName);
		try {
			return (Class<? extends CourseControlEntity<?>>) Class.forName(className);
		} catch (Exception e) {
			throw new RuntimeException("A entidade " + entityName + " nao esta declarada no pacote esperado. Esperava-se " + className, e);
		}
	}

	@SuppressWarnings("unchecked")
	public <M extends CourseControlMB<E>, F extends CourseControlFacade<E>, E extends CourseControlEntity<E>> Class<F> getFacadeClassForMBClass(Class<M> mbClass) {
		String entityName = mapMBClass.inverseGet(mbClass);
		return (Class<F>) mapFacadeClass.get(entityName);				
	}
	
	
	public <M extends CourseControlMB<E>, F extends CourseControlFacade<E>, E extends CourseControlEntity<E>> F createFacadeForMb(M mb) {
		@SuppressWarnings("unchecked")
		Class<F> facadeClass = (Class<F>) getFacadeClassForMBClass(mb.getClass());
		
		try {
			return facadeClass.newInstance();
		} catch (Exception e) {
			throw new RuntimeException("Erro ao criar facade para o MB " + mb.getClass().getName(), e);
		}
		
	}
	
	@SuppressWarnings("unchecked")
	public <F extends CourseControlFacade<E>, D extends CourseControlDao<E>, E extends CourseControlEntity<E>> Class<D> getDaoClassForFacadeClass(Class<F> facadeClass) {
		String entityName = mapFacadeClass.inverseGet(facadeClass);
		return (Class<D>) mapDaoClass.get(entityName);				
	}
	
	
	public <F extends CourseControlFacade<E>, D extends CourseControlDao<E>, E extends CourseControlEntity<E>> D createDaoForFacade(F facade) {
		@SuppressWarnings("unchecked")
		Class<D> daoClass = (Class<D>) getDaoClassForFacadeClass(facade.getClass());
		
		try {
			return daoClass.newInstance();
		} catch (Exception e) {
			throw new RuntimeException("Erro ao criar dao para o facade " + facade.getClass().getName(), e);
		}
		
	}

	@SuppressWarnings("unchecked")
	public <D extends CourseControlDao<E>, E extends CourseControlEntity<E>> Class<E> getEntityClassForDao(Class<D> daoClass) {
		String entityName = mapDaoClass.inverseGet(daoClass);
		return (Class<E>) mapEntityClass.get(entityName);
	}

	@SuppressWarnings("unchecked")
	public <M extends CourseControlMB<E>, E extends CourseControlEntity<E>> Class<E> getEntityClassForMb(Class<M> mbClass) {
		String entityName = mapMBClass.inverseGet(mbClass);
		return (Class<E>) mapEntityClass.get(entityName);
	}		

}
