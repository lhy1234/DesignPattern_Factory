package com.lhy.factory;

//飞机工厂类
public class PlaneFactory extends VehicleFactory {
	
	@Override
	public Moveable create() {
		//单例、多例、条件检查自己控制
		return new Plane();
	}
}
