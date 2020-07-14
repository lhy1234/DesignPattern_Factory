package com.lhy.factory;


//扫帚工厂类
public class BroomFactory extends VehicleFactory {
	
	@Override
	public Moveable create() {
		//单例、多例、条件检查自己控制
		return new Broom();
	}
}
