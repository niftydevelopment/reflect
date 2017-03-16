package rfl;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class IntegrationModelBuilder {

	private Object carType;
	private Object engineType;
	
	public void carType(Object carType) {
		this.carType = carType;
	}
	
	public void engineType(Object engineType) {
		this.engineType = engineType;
	}

	
	public Object build(Class carClazz) throws IntrospectionException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException, ClassNotFoundException {
	
		//vi har inget interface, men vi har ett väldigt likt api.
		Method setEngineMethod = getMethod(carType, "setEngineType");
		setEngineMethod.invoke(carType, engineType);
		
		return carType;
	}
	
	private Method getMethod(Object object, String methodName) throws IntrospectionException {
		for (PropertyDescriptor pd : Introspector.getBeanInfo(object.getClass()).getPropertyDescriptors()) {
			if (pd.getWriteMethod() != null && !"class".equals(pd.getName())) {
				if (pd.getWriteMethod().getName().equals(methodName)) {
					return pd.getWriteMethod();	
				}
			}
		}
		throw new IllegalArgumentException("Det attrubutet finns inte");
	}
	
}
