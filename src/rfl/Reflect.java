package rfl;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Reflect {

	public Object reflectTo(HashMap<String, Object> values, Class clazz) throws InstantiationException, IllegalAccessException, ClassNotFoundException, IllegalArgumentException, InvocationTargetException {

		Object newInstance = Class.forName(clazz.getName()).newInstance();
		List<Method> setters = resolveSetters(clazz, "set");
		
		for (Method m : setters) {
			String attr = m.getName().substring(3, m.getName().length()).toLowerCase();
			
			if (values.get(attr) != null) {
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
	
	public Object reflectToTransferModel(String path, HashMap<String, Object> carMap, HashMap<String, Object> engineMap)
			throws InstantiationException, IllegalAccessException, ClassNotFoundException, InvocationTargetException,
			IntrospectionException {
		
		//reflekter till transfer modellen och akumulera resultatet i en builder....
		IntegrationModelBuilder builder = new IntegrationModelBuilder();
		
		builder.carType(reflectTo(carMap, Class.forName(path + ".CarType")));
		builder.engineType(reflectTo(engineMap, Class.forName(path + ".EngineType")));
		
		return builder.build();
	}
}
