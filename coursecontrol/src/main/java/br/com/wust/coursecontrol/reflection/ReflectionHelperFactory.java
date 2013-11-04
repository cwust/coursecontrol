package br.com.wust.coursecontrol.reflection;

import java.util.HashMap;
import java.util.Map;

public class ReflectionHelperFactory {
	private static ReflectionHelperFactory instance;

	private Map<Class<?>, ReflectionHelper<?>> map;
	
	public static ReflectionHelperFactory getInstance() {
		if (instance == null) {
			instance = new ReflectionHelperFactory();
		}
		return instance;
	}
	
	private ReflectionHelperFactory() {
		map = new HashMap<Class<?>, ReflectionHelper<?>>();
	}
	
	public <T> ReflectionHelper<T> getReflectionHelper(Class<T> aClass) {
		@SuppressWarnings("unchecked")
		ReflectionHelper<T> reflectionHelper = (ReflectionHelper<T>) map.get(aClass);
		if (reflectionHelper == null) {
			reflectionHelper = new ReflectionHelper<T>(aClass);
			map.put(aClass, reflectionHelper);
		}
		return reflectionHelper;
	}
}
