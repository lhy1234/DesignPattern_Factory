package com.lhy.factory;


//扫帚
public class Broom implements Moveable{
	@Override
	public void run() {
		System.out.println("扫帚摇着尾巴呼呼呼...");
	}
}
