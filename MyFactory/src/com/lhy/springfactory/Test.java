package com.lhy.springfactory;


import java.util.Properties;



public class Test {
	
//	Properties props = new Properties();
//	props.load(Test.class.getClassLoader().getResourceAsStream("com/lhy/springfactory/spring.properties"));
//
//	String vehicleTypeName = props.getProperty("VehicleType");
//	System.out.println(vehicleTypeName);
//	//反射生成对象
//	Object o = Class.forName(vehicleTypeName).newInstance();
//	Moveable m = (Moveable)o;
//	m.run();

	public static void main(String[] args) throws Exception{
		
		BeanFactory f = new ClassPathXmlApplicationContext("com/lhy/springfactory/applicationContext.xml");
		Object o = f.getBean("v");
		Moveable m = (Moveable)o;
		m.run();
		
		Train trian = (Train)f.getBean("trian");
		trian.run();
		
	}

}
