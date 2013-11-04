package br.com.wust.coursecontrol.abstracts;

import br.com.wust.coursecontrol.reflection.ReflectionHelper;
import br.com.wust.coursecontrol.reflection.ReflectionHelperFactory;


public abstract class CourseControlEntity <T extends CourseControlEntity<?>> {
	public abstract Integer getCodigo();
	
	@SuppressWarnings("unchecked")
	private ReflectionHelper<T> getReflectionHelper() {
		return (ReflectionHelper<T>) ReflectionHelperFactory.getInstance().getReflectionHelper(this.getClass());
	}
	
	@Override
	public boolean equals(Object obj) {
		if (isCodigoPrenchido() && isSameClass(obj)) {
			@SuppressWarnings("unchecked")
			CourseControlEntity<T> other = (CourseControlEntity<T>) obj;
			return getCodigo().equals(other.getCodigo());
		} else {
			return false;
		}
	}
	
	public boolean isCodigoPrenchido() {
		return this.getCodigo() != null;
	}
	
	public boolean isSameClass(Object obj) {
		return obj.getClass().equals(this.getClass());
	}
	
	@SuppressWarnings("unchecked")
	public void copyAttributes(T another) {
		getReflectionHelper().copyAttributes((T) this, another);
	}
	
	
}
