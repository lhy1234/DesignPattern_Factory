package com.lhy.factory;

public class Test {

	public static void main(String[] args) {


		//测试Car
//		Car000 car = Car000.getInstance();
//		Car000 car2 = Car000.getInstance();
//		System.err.println(car == car2); //true
		
//		Moveable m = new Car();
//		m.run();
//		m = new Plane();
//		m.run();
		
		//机车工厂，new飞机工厂实例
		VehicleFactory factory = new PlaneFactory();
		Moveable m = factory.create();
		m.run();
		//换成Car工厂
		factory = new CarFactory();
		m = factory.create();
		m.run();
		//换成扫帚工厂
		factory = new BroomFactory();
		m = factory.create();
		m.run();
		
	}

}
