package com.lhy.factory;

//Car工厂类
public class CarFactory extends VehicleFactory{
	
	@Override
	public Moveable create() {
		//单例、多例、条件检查自己控制
		return new Car();
	}
}
