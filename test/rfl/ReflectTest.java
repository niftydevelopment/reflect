package rfl;

import static org.junit.Assert.*;

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

import org.junit.Test;

import model.Car;
import model.Engine;
import se.whatever.integration.CarType;
import se.whatever.integration.EngineType;

public class ReflectTest {
	
	@Test
	public void reflectFrom() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, IntrospectionException {
		Car car = buildModel();
		
		Reflect sut = new Reflect();
		
		//hämta data från modell.
		HashMap<String, Object> carMap = sut.reflectFrom(car);
		
		assertNotNull(carMap.get("name"));
		assertEquals(carMap.get("name"), "OBK 032");
	}

	@Test
	public void reflectTo_carType() throws InstantiationException, IllegalAccessException, ClassNotFoundException, IllegalArgumentException, InvocationTargetException, IntrospectionException {
		//modell :-)
		Car car = new Car();
		car.setName("OBK 032");
		
		Reflect sut = new Reflect();

		//hämta data från modell.
		HashMap<String, Object> carMap = sut.reflectFrom(car);
		
		//skapa integrationsmodell.
		Object o = sut.reflectTo(carMap, CarType.class);
		CarType carType = (CarType)o;
		assertEquals("OBK 032", carType.getName());
		
	}
	
	@Test
	public void reflectTo_engineType() throws InstantiationException, IllegalAccessException, ClassNotFoundException, IllegalArgumentException, InvocationTargetException, IntrospectionException {
		//modell :-)
		Engine engine = new Engine();
		engine.setType(Engine.ENGINETYPE.GASOLINE);
		
		Reflect sut = new Reflect();

		//hämta data från modell.
		HashMap<String, Object> engineMap = sut.reflectFrom(engine);
		
		assertEquals((String)engineMap.get("type"), "GASOLINE");
		
		//skapa integrationsmodell.
		Object o = sut.reflectTo(engineMap, EngineType.class);
		EngineType engineType = (EngineType)o;
		assertEquals("GASOLINE", engineType.getType());
	}	
	
	@Test
	public void reflectToTransferModel() throws InstantiationException, IllegalAccessException, ClassNotFoundException, IllegalArgumentException, InvocationTargetException, IntrospectionException {
		//bygg modell
		Car car = buildModel();
		
		//reflektera från modellen...
		Reflect sut = new Reflect();
		HashMap<String, Object> carMap = sut.reflectFrom(car);
		HashMap<String, Object> engineMap = sut.reflectFrom(car.getEngine());
		//tolka om ex: namn -> name om det behövs.

		
		//ett namespace blir kort... de andra, nope!
		CarType result = 
				(CarType)sut.reflectToTransferModel("se.whatever.integration", carMap, engineMap);

		se.whatever.integration.v2.CarType result2 = 
				(se.whatever.integration.v2.CarType)
				sut.reflectToTransferModel("se.whatever.integration.v2", carMap, engineMap);

		se.whatever.integration.v3.CarType result3 = 
				(se.whatever.integration.v3.CarType)
				sut.reflectToTransferModel("se.whatever.integration.v3", carMap, engineMap);

		
		//test it
		assertTrue(result.getName().equals("OBK 032"));
		assertTrue(result.getEngineType().getType().equals("GASOLINE"));				
		
		//test it
		assertTrue(result2.getName().equals("OBK 032"));
		assertTrue(result2.getEngineType().getType().equals("GASOLINE"));
		
		//test it
		assertTrue(result3.getName().equals("OBK 032"));
		assertTrue(result3.getEngineType().getType().equals("GASOLINE"));
	}

	private Car buildModel() {
		//modell :-)
		Car car = new Car();
		car.setName("OBK 032");
		
		Engine engine = new Engine();
		engine.setType(Engine.ENGINETYPE.GASOLINE);
		car.setEngine(engine);
		return car;
	}	
	
}
