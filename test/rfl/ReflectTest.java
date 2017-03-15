package rfl;

import static org.junit.Assert.*;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

import org.junit.Test;

import se.whatever.integration.AnimalType;

public class ReflectTest {
	
	@Test
	public void doStuff() throws InstantiationException, IllegalAccessException, ClassNotFoundException, IllegalArgumentException, InvocationTargetException {
		Reflect sut = new Reflect();
		
		HashMap<String, Object> hashMap = new HashMap<String, Object>();
		//get all the needed attributes from model and add them to map.
		hashMap.put("name", "Mammasson");
		
		Object o = sut.reflectTo(hashMap, AnimalType.class);
		AnimalType a = (AnimalType)o;

		assertEquals("Mammasson", a.getName());
	}
	
}
