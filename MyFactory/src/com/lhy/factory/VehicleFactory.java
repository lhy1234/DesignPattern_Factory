package com.lhy.factory;

//交通工具工厂
public abstract class VehicleFactory {

	//具体生成什么交通工具由子类决定，这里是抽象的。
	public abstract Moveable create();
}
