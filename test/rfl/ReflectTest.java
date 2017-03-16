package rfl;

import static org.junit.Assert.*;

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

import org.junit.Test;

import model.Car;
import model.Engine;
import model.Engine.ENGINETYPE;
import se.whatever.integration.CarType;
import se.whatever.integration.EngineType;

public class ReflectTest {
	
	@Test
	public void reflectFrom() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, IntrospectionException {
		//modell :-)
		Car car = new Car();
		car.setName("OBK 032");
		
		Engine engine = new Engine();
		engine.setType(Engine.ENGINETYPE.GASOLINE);
		car.setEngine(engine);
		
		Reflect sut = new Reflect();
		
		//hämta data från modell.
		HashMap<String, Object> carMap = sut.reflectFrom(car);
		
		assertNotNull(carMap.get("name"));
		assertEquals(carMap.get("name"), "OBK 032");
	}

	@Test
	public void reflectTo_car() throws InstantiationException, IllegalAccessException, ClassNotFoundException, IllegalArgumentException, InvocationTargetException, IntrospectionException {
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
	public void reflectTo_engine() throws InstantiationException, IllegalAccessException, ClassNotFoundException, IllegalArgumentException, InvocationTargetException, IntrospectionException {
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
	public void reflectTo_chain() throws InstantiationException, IllegalAccessException, ClassNotFoundException, IllegalArgumentException, InvocationTargetException, IntrospectionException {
		//modell :-)
		Car car = new Car();
		car.setName("OBK 032");
		
		Engine engine = new Engine();
		engine.setType(Engine.ENGINETYPE.GASOLINE);
		car.setEngine(engine);
		
		Reflect sut = new Reflect();

		//hämta data från modell.
		HashMap<String, Object> carMap = sut.reflectFrom(car);
		HashMap<String, Object> engineMap = sut.reflectFrom(car.getEngine());
		
		//skapa integrationsmodell.
		Object o = sut.reflectTo(carMap, CarType.class);
		CarType carType = (CarType)o;
		assertEquals("OBK 032", carType.getName());
		
		o = sut.reflectTo(engineMap, EngineType.class);
		EngineType engineType = (EngineType)o;
		assertEquals("GASOLINE", engineType.getType());
	}
	
	
	@Test
	public void build() throws InstantiationException, IllegalAccessException, ClassNotFoundException, IllegalArgumentException, InvocationTargetException, IntrospectionException {
		//modell :-)
		Car car = new Car();
		car.setName("OBK 032");
		
		Engine engine = new Engine();
		engine.setType(Engine.ENGINETYPE.GASOLINE);
		car.setEngine(engine);
		
		Reflect sut = new Reflect();

		//hämta data från modell.
		HashMap<String, Object> carMap = sut.reflectFrom(car);
		HashMap<String, Object> engineMap = sut.reflectFrom(car.getEngine());
		
		//skapa integrationsmodell.
		Object o = sut.reflectTo(carMap, CarType.class);
		CarType carType = (CarType)o;
		assertEquals("OBK 032", carType.getName());
		
		o = sut.reflectTo(engineMap, EngineType.class);
		EngineType engineType = (EngineType)o;
		assertEquals("GASOLINE", engineType.getType());
		

		
		
		IntegrationModelBuilder builder = new IntegrationModelBuilder();
		builder.carType(carType);
		builder.engineType(engineType);
		
		//bygger det som faktiskt är gemensamt... vilket borde vara det mesta.
		Object transferModell = builder.build(CarType.class);
		
		assertNotNull(transferModell);

		CarType result = (CarType)transferModell;
		assertNotNull(result);
		assertTrue(result.getName().equals("OBK 032"));
		
		assertNotNull(result.getEngineType());
		assertTrue(result.getEngineType().getType().equals("GASOLINE"));
		
		
		//TODO: ta hand om det som faktiskt inte är gemensamt.
	}
	
	
}
