package com.lhy.abstractfactory;


//哈利波特的魔法工厂
public class MagicFactory extends AbstractFactory {

	//交通工具：扫把
	public Vehicle createVehicle(){
		return new Broom();
	}
	
	//武器：魔法棒
	public Weapon createWeapon(){
		return new MagicStick();
	}
	//食物：毒蘑菇
	public Food createFood(){
		return new MushRoom();
	}
}
