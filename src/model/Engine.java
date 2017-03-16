package model;

public class Engine {
	
	private ENGINETYPE type; 
	
	public ENGINETYPE getType() {
		return type;
	}

	public void setType(ENGINETYPE type) {
		this.type = type;
	}

	public enum ENGINETYPE {
	    GASOLINE, DISEL 
	}
}


