package rfl;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import model.Car;
import se.whatever.integration.CarType;

public class Reflect {

	public Object reflectTo(HashMap<String, Object> values, Class clazz) throws InstantiationException, IllegalAccessException, ClassNotFoundException, IllegalArgumentException, InvocationTargetException {

		Object newInstance = Class.forName(clazz.getName()).newInstance();
		List<Method> setters = resolveSetters(clazz, "set");
		
		for (Method m : setters) {
			String attr = m.getName().substring(3, m.getName().length()).toLowerCase();
			
			System.out.println("----------------->");
			if (values.get(attr) != null) {
				
				System.out.println("attributet:" + attr);
				System.out.println("Som är av datatyp:" + attr.getClass().getName());
				System.out.println("skall sättas till:" + values.get(attr));
				
				System.out.println("på classen:" + newInstance.getClass().getName());
				System.out.println("via metoden:" + m.getName());
				
				m.invoke(newInstance, values.get(attr));				
			} else {
				System.out.println("attributet:" + attr + " saknar värde att reflektera");
			}

		}
		
		return newInstance;
	}

	private List<Method> resolveSetters(Class clazz, String suffix) {
		Class curClass = clazz;
		Method[] allMethods = curClass.getMethods();
		List<Method> setters = new ArrayList<Method>();
		for(Method method : allMethods) {
		    if(method.getName().startsWith(suffix)) {
		        setters.add(method);
		    }
		}
		return setters;
	}

	public HashMap<String, Object> reflectFrom(Object object) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, IntrospectionException {
		HashMap<String, Object> values = new HashMap<String, Object>();
		
		
		for (PropertyDescriptor pd : Introspector.getBeanInfo(object.getClass()).getPropertyDescriptors()) {
			if (pd.getReadMethod() != null && !"class".equals(pd.getName())) {
				String name = pd.getReadMethod().getName();
				name = name.substring(3).toLowerCase();
				
				Object result = pd.getReadMethod().invoke(object);
				if (result == null) {
					continue;
				}

				if (result.getClass().isEnum()) { //TODO: mycket sånt här behövs nog!
					values.put(name, result.toString());
				} else {
					values.put(name, result);	
				}
				
			}
		}
		
		return values;
	}
	
}
