package com.lhy.abstractfactory;

public class Car extends Vehicle{
	@Override
	public void run() {
		System.out.println("冒着烟奔跑中...");
	}
}
