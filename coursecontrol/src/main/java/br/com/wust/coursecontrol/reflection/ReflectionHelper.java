package br.com.wust.coursecontrol.reflection;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class ReflectionHelper <T> {
	
	private Class<T> aClass;
	private Map<String, GetSetMethodPair> attToGetSetMethodsMap;
	
	public ReflectionHelper(Class<T> aClass) {
		this.aClass = aClass;
		attToGetSetMethodsMap = new HashMap<String, GetSetMethodPair>();
		evaluateClass();
	}

	private void evaluateClass() {
		for (String att: getAttList()) {
			attToGetSetMethodsMap.put(att, createGetSetMethodPair(att));
		}
	}

	private List<String> getAttList() {
		List<String> attList = new ArrayList<String>(); 
		for (Method method: aClass.getMethods()) {
			if ( !isInherited(method, aClass) &&
					isGetterMethod(method)) {
				attList.add(method.getName().substring(3));
			}
		}
		return attList;
	}
	
	private boolean isInherited(Method method, Class<?> aClass) {
		return (!method.getDeclaringClass().equals(aClass));
	}
	
	private boolean isGetterMethod(Method method) {
		return method.getName().startsWith("get");
	}
	
	private GetSetMethodPair createGetSetMethodPair(String att) {
		try {
			Method getter = aClass.getMethod("get" + att);
			Method setter = aClass.getMethod("set" + att, getter.getReturnType());
			
			return new GetSetMethodPair(getter, setter);
		} catch (Exception e) {
			throw new RuntimeException("Erro ao localizar metodos get e set do atributo " + att + "da classe " + aClass.getName(), e);
		}
	}
	
	public void copyAttributes(T src, T dest) {
		try {
			for (Entry<String, GetSetMethodPair> entry: attToGetSetMethodsMap.entrySet()) {
				copyAttribute(src, dest, entry.getValue());
			}
		} catch (Exception e) {
			throw new RuntimeException("Erro ao copiar atributos entre objetos da classe " + aClass.getName(), e);
		}
	}	
	
	private void copyAttribute(T src, T dest, GetSetMethodPair getSetMethodPair) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		Method getter = getSetMethodPair.getGetter();
		Method setter = getSetMethodPair.getSetter();
		
		Object value = getter.invoke(src);
		setter.invoke(dest, value);		
	}
	
	private class GetSetMethodPair {
		private Method getter;
		private Method setter;
		
		public GetSetMethodPair(Method getter, Method setter) {
			super();
			this.getter = getter;
			this.setter = setter;
		}
		public Method getGetter() {
			return getter;
		}
		public Method getSetter() {
			return setter;
		}
	}
}
