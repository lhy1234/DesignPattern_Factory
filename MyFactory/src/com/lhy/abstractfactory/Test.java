package com.lhy.abstractfactory;

public class Test {

	public static void main(String[] args) {

		
		//换一个工厂，只需要改动这一处，就可以了，换一个工厂，就把生产的系列产品都换了
		AbstractFactory factory = new DefaultFactory();// new MagicFactory(); //
		//换一个工厂
		Vehicle vehicle = factory.createVehicle();
		vehicle.run();
		Weapon weapon = factory.createWeapon();
		weapon.shoot();
		Food food = factory.createFood();
		food.printName();
		
	}

}
