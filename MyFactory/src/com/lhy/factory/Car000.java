package com.lhy.factory;

import java.util.ArrayList;
import java.util.List;

/**
 * 交通工具Car
 * 
 */
public class Car000 {
	
	//private static Car car = new Car();
	private static List<Car000> cars = new ArrayList<>();
	
	static{
		//静态初始化cars
		cars.add(new Car000());
		cars.add(new Car000());
	}
	
	private Car000(){}
	
	public static Car000 getInstance(){
		//return car;
		//随机返回一个Car，这里就不随机了
		return cars.get(1);
	}
	
	public void run(){
		System.out.println("冒着烟奔跑中...");
	}

}
