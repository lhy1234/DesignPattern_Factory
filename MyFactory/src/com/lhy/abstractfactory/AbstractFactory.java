package com.lhy.abstractfactory;

//抽象工厂
public abstract class AbstractFactory {
	//生产 交通工具
	public abstract Vehicle createVehicle();
	//生产 武器
	public abstract Weapon createWeapon();
	//生产食物
	public abstract Food createFood();
}
