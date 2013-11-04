package br.com.wust.coursecontrol.abstracts;

import java.util.List;

import org.primefaces.context.RequestContext;

import br.com.wust.coursecontrol.convention.ConventionUtils;
import br.com.wust.coursecontrol.util.JSFMessageUtil;

public class CourseControlMB <T extends CourseControlEntity<T>> {
	private static final String KEEP_DIALOG_OPENED = "KEEP_DIALOG_OPENED";
	private Class<T> entityClass;
	private T entity;
	private List<T> entitiesList;
	private CourseControlFacade<T> facade;
	 
	private JSFMessageUtil messageUtil;

    public CourseControlMB() {
        messageUtil = new JSFMessageUtil();
    }
 
	public JSFMessageUtil getMessageUtil() {
		return messageUtil;
	}
	
    protected void displayErrorMessageToUser(String message) {
    	getMessageUtil().sendErrorMessageToUser(message);
    }
 
    protected void displayInfoMessageToUser(String message) {
    	getMessageUtil().sendInfoMessageToUser(message);
    }
 
    protected void closeDialog(){
        getRequestContext().addCallbackParam(KEEP_DIALOG_OPENED, false);
    }
 
    protected void keepDialogOpen(){
        getRequestContext().addCallbackParam(KEEP_DIALOG_OPENED, true);
    }
 
    protected RequestContext getRequestContext(){
        return RequestContext.getCurrentInstance();
    }
    
    public T getEntity() {
		if (entity == null) {
			resetEntity();
		}
		return entity;
    }
    
    public T newEntity() {
    	try {
			return getEntityClass().newInstance();
		} catch (Exception e) {
			throw new RuntimeException("Erro ao instanciar entidade do ManagedBean " + getClass().getName(), e);
		}
    }
    
	@SuppressWarnings("unchecked")
	private Class<T> getEntityClass() {
		if (entityClass == null) {
			entityClass = ConventionUtils.getInstance().getEntityClassForMb(this.getClass()); 
		}
		return entityClass;
		
	}

    
    public void setEntity(T entity) {
    	this.entity = entity;
    }
    
    public void resetEntity() {
    	setEntity(newEntity());
    }
    
	public List<T> getAllEntities() {
		if (entitiesList == null) {
			loadCursos();
		}

		return entitiesList;
	}
	
	public CourseControlFacade<T> getFacade() {
		if (facade == null) {
			createFacade();
		}
		return facade;
	}

	private void createFacade() {
		facade = ConventionUtils.getInstance().createFacadeForMb(this);
	}
	
	private void loadCursos() {
		entitiesList = getFacade().listAll();
	}
}
