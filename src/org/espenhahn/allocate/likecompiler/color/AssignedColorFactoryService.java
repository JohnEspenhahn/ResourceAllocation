package org.espenhahn.allocate.likecompiler.color;

public class AssignedColorFactoryService {
	
	// default
	private static AssignedColorFactory factory = new AssignedColorFactoryImpl();
	
	public static void setFactory(AssignedColorFactory f) {
		factory = f;
	}
	
	public static AssignedColorFactory getFactory() {
		return factory;
	}

}
