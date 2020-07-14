package com.lhy.factory;

public class Car implements Moveable{
	@Override
	public void run() {
		System.out.println("冒着烟奔跑中...");
	}
}
