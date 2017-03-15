package rfl;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import se.whatever.integration.AnimalType;

public class Reflect {

	public Object reflectTo(HashMap<String, Object> values, Class<AnimalType> clazz) throws InstantiationException, IllegalAccessException, ClassNotFoundException, IllegalArgumentException, InvocationTargetException {

		Object newInstance = Class.forName(clazz.getName()).newInstance();
		List<Method> setters = resolveSetters(clazz);
		
		for (Method m : setters) {
			String attr = m.getName().substring(3, m.getName().length()).toLowerCase();
			System.out.println(attr);
			m.invoke(newInstance, values.get(attr));
		}
		
		return newInstance;
	}

	private List<Method> resolveSetters(Class<AnimalType> clazz) {
		Class curClass = clazz;
		Method[] allMethods = curClass.getMethods();
		List<Method> setters = new ArrayList<Method>();
		for(Method method : allMethods) {
		    if(method.getName().startsWith("set")) {
		        setters.add(method);
		    }
		}
		return setters;
	}
	
}
