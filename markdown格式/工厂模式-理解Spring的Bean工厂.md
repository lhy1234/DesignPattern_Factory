##         工厂模式-理解Spring的Bean工厂  

接面向对象里面 “老张开车去东北”的场景。

封装“老张开车去东北”里面的交通工具，封装交通工具Car

###	只给司机一辆车（单例、多例）

顺带讲解单例

要求只能有一辆车，别人不能new Car，只有Car自己能控制newCar的逻辑。私有化构造方法，别人就不能new了。

```java
/**
 * 交通工具Car
 * 
 */
public class Car {
	
	//private static Car car = new Car();
	private Car(){}
	
	public static Car getInstance(){
		return new Car();
	}
	
	public void run(){
		System.out.println("冒着烟奔跑中...");
	}

}

```

工厂就是自主生产自己的产品，不再依赖于new。比如你想new我家的一个抽屉，你想拿钱就拿钱， 肯定不行。

但是我要给你提供一个方法：getChouTi(); 我就能在get方法里面做各种各样的限制了。

比如返回Car的getInstance方法，可以做逻辑判断。

```java
/**
 * 交通工具Car
 */
public class Car {

    private Car(){}

    public static Car getInstance(){
        if(有驾照){
            return new Car();
        }
        return null;
    }
    public void run(){
        System.out.println("冒着烟奔跑中...");
    }
}

```

再回到上面的要求，只有一辆车，这么做：自己new一个Car，调用getInstance时候，返回这个Car  。

```java
public class Car {
	
	private static Car car = new Car();
	private Car(){}
	
	public static Car getInstance(){
		return car;
	}
	
	public void run(){
		System.out.println("冒着烟奔跑中...");
	}

}

```

测试

getInstance两次看是不是一辆车：

```java
public static void main(String[] args) {
        Car car1 = Car.getInstance();
        Car car2 = Car.getInstance();
        System.err.println(car1 == car2);
    }
```

打印：true

打印true，说明是一辆车

这个模式叫 单例，又有人叫这个getInstance方法叫静态工厂方法。

任何方法，里面控制了产生对象的逻辑，都可以叫工厂方法。

### 多例

如果Car类里面返回的不是一个Car，里面有一个List装了一堆的Car，getInstance的时候随机返回一个，这个又有人起了个名字 叫---多例

```java
public class Car {
	
	//private static Car car = new Car();
	private static List<Car> cars = new ArrayList<>();
	
	static{
		//静态初始化cars
		cars.add(new Car());
		cars.add(new Car());
	}
	
	private Car(){}
	
	public static Car getInstance(){
		//return car;
		//随机返回一个Car，这里就不随机了
		return cars.get(1);
	}
	
	public void run(){
		System.out.println("冒着烟奔跑中...");
	}

}

```

JDBC连接池，里面装的Connection，就是多例。  

### 任意定制交通工具的类型和生产过程

自然就想起了多态，抽取一个接口：Moveable，然后让Car实现Moveable接口：

```java
public interface Moveable {
	void run();
}
```

Car的实现：

```java
public class Car implements Moveable{
	@Override
	public void run() {
		System.out.println("冒着烟奔跑中...");
	}
}
```

飞机的实现：

```java
public class Plane implements Moveable{

	@Override
	public void run() {
		System.out.println("扇着翅膀飞呀飞...");
	}
}
```

测试类：

调用的时候，父类引用指向子类对象，多态，我new谁，就调用的是谁，很随意就换了交通工具：

```java
Moveable m = new Car();
m.run();
m = new Plane();
m.run();
打印结果：
冒着烟奔跑中...
扇着翅膀飞呀飞...

```

还有其他任何交通工具 比如交通工具是哈利波特的扫帚，就直接实现Moveable接口，你就可以直接new Broom(); 了 。

现在存在的问题就是，可以任意的new 交通工具，构造方法是公开的。现在想对任意交通工具的生产过程也能够定制的话。有了上面单例的思路，现在第一个想到的还是，在交通工具类里面写一个静态的方法控制new 的过程。这里比如飞机，把产生飞机的过程单独一个类拿出来，比如叫飞机工厂PlaneFactory：

```java
//飞机工厂类
public class PlaneFactory {
	public Plane createPlane(){
		//单例、多例、条件检查自己控制
		return new Plane();
	}
}
```

测试 ：

```java
//飞机工厂
		PlaneFactory factory = new PlaneFactory();
		Moveable m = factory.createPlane();
		m.run();

打印结果：
扇着翅膀飞呀飞...
```

如果现在想有一个Car工厂，那么很简单，就是这样：

```java
//Car工厂类
public class CarFactory {
	public Car createPlane(){
		//单例、多例、条件检查自己控制
		return new Car();
	}
}
```

测试代码：

以前是开着飞机，现在想换成开Car，需要把飞机工厂换成了Car工厂，调用他的createCar方法。这样太别扭了。

```java
//飞机工厂
		//PlaneFactory factory = new PlaneFactory();
		//换成Car工厂,整个工厂方法都得换
		CarFactory factory = new CarFactory();
		Moveable m = factory.createCar();
		m.run();
```

有没有什么办法，从飞机换成Car的时候，只换工厂的实现就行呢？自然就想起了多态，有多态就得有父类、子类。所以工厂类需要有一个父类。Factory本来是产生交通工具的，抽象出一个产生交通工具的工厂：

```java
//交通工具工厂
public abstract class VehicleFactory {

	//具体生成什么交通工具由子类决定，这里是抽象的。
	public abstract Moveable create();
}
```

这时候让CarFactory和PlaneFactory去继承VehicleFactory ：

```java
//Car工厂类
public class CarFactory extends VehicleFactory{
	
	@Override
	public Moveable create() {
		//单例、多例、条件检查自己控制
		return new Car();
	}
}
//飞机工厂类
public class PlaneFactory extends VehicleFactory {
	
	@Override
	public Moveable create() {
		//单例、多例、条件检查自己控制
		return new Plane();
	}
}
```

换了工厂的实现，就可以换交通工具了，比如你加了一个哈利波特的魔法扫帚，需要加一个Broom类实现Moveable接口，和一个BroomFactory工厂类继承VehicleFactory就可以了。

![](D:\Z_lhy\学习\markdown文档\工厂模式\1.png)

![](D:\Z_lhy\学习\markdown文档\工厂模式\2.png)





```java
//扫帚
public class Broom implements Moveable{

	@Override
	public void run() {
		System.out.println("扫帚摇着尾巴呼呼呼...");
	}
}

//扫帚工厂类
public class BroomFactory extends VehicleFactory {
	
	@Override
	public Moveable create() {
		//单例、多例、条件检查自己控制
		return new Broom();
	}
}

```

此时测试代码就成了这样子：

```java
//机车工厂，new飞机工厂实例
		VehicleFactory factory = new PlaneFactory();
		Moveable m = factory.create();
		m.run();
		//换成Car工厂
		factory = new CarFactory();
		m = factory.create();
		m.run();
		//换成扫帚工厂
		factory = new BroomFactory();
		m = factory.create();
		m.run();
打印结果；
扇着翅膀飞呀飞...
冒着烟奔跑中...
扫帚摇着尾巴呼呼呼...

```

在某一个维度上有了可扩展了，不仅可以控制产生交通工具的类型，还可以控制产生交通工具的生产过程。需要改的只有一个地方：要是需要换交通工具，站在客户角度，需要改的只有交通工具的工厂，其他地方都不用动。如果用了配置文件的话，代码都不用该，只改配置文件即可。后面再说。

关于抽象类和接口的选择：

比如上面的交通工具工厂，是一个抽象类，也可以设计成接口，么问题。

假如这个概念在我们脑子是确确实实存在的，就用抽象类，

假如这个概念只是某些方面的特性：比如会飞的，会跑的，就用接口

假如两个概念模糊的时候，不知道选择哪个的时候，就用接口，原因是，从实现了这个接口后，还能从其它的抽象类继承，更灵活。

### 抽象工厂

看一下JDK里面，先看getInstance方法，有一大堆，不同类里面有同样的方法getInstance。

这些大多数都是静态的工厂方法，是不是单例不一定，得看具体的实现。

![](D:\Z_lhy\学习\markdown文档\工厂模式\3.png)

各种各样的Factory也很多。像下面的加密的key，就不适合new，用一个工厂去实现它，产生的时候可以实现各种各种算法，检测各种资质，new的话构造方法只能是写死的，用工厂的话，实现一个子类的时候还可以控制生产过程，更灵活。

![](D:\Z_lhy\学习\markdown文档\工厂模式\4.png)

总而言之，getInstance和Factory，JDK里面很常用。下面开始说抽象工厂。

回到最原始的状态，我们有一辆车Car。

### 控制一系列的产品（车、武器、食品补给）  

现在让这个人，开着车，拿着AK47，吃着苹果。 意思就是，这是一些列的产品，要控制这一些列的产品的生产。

比如你要装修，海尔整体厨房，有微波炉，油烟机，洗衣机，电磁炉。一系列的产品。

```java
public class Car{
	
	public void run() {
		System.out.println("冒着烟奔跑中...");
	}
}

public class AK47 {

	public void shoot(){
		System.out.print("哒哒哒....");
	}
}
public class Apple {
	public void getName(){
		System.out.println("Apple..."); 
	}
}

```

测试类：

```java
Car car = new Car();
		car.run();
		AK47 ak = new AK47();
		ak.shoot();
		Apple apple = new Apple();
		apple.getName();
打印：
冒着烟奔跑中...
哒哒哒....
Apple...
```

产生这一系列的产品，需要有一个默认的工厂：

```java
//默认的工厂
public class DefaultFactory {

	public Car createCar(){
		return new Car();
	}
	public AK47 createAK47(){
		return new AK47();
	}
	public Apple createApple(){
		return new Apple();
	}
}

```

此时的测试程序，只要new出来一个默认工厂，就可以生产这一系列的产品了：

```java
DefaultFactory factory = new DefaultFactory();
		Car car = factory.createCar();
		car.run();
		AK47 ak = factory.createAK47();
		ak.shoot();
		Apple apple = factory.createApple();
		apple.getName();
打印：
冒着烟奔跑中...
哒哒哒....
Apple...

```

当需要吧这一系列产品全换掉的话，把这个工厂换掉就可以了：

这里新建一个工厂，魔法工厂：

```java
//哈利波特的魔法工厂
public class MagicFactory {

	//交通工具：扫把
	public Broom createBroom(){
		return new Broom();
	}
	
	//武器：魔法棒
	public MagicStick createMagicStick(){
		return new MagicStick();
	}
	//食物：毒蘑菇
	public MushRoom createMushRoom(){
		return new MushRoom();
	}
}

//扫帚
public class Broom{

	public void run() {
		System.out.println("扫帚摇着尾巴呼呼呼...");
	}
}


//武器：魔法棒
public class MagicStick {
}

//食物：毒蘑菇
public class MushRoom {

}

```

但是此时，站在客户的角度，想把工厂从DefaultFactory换到MagicFactory：  

![](D:\Z_lhy\学习\markdown文档\工厂模式\5.png)

有了之前的经验，这里自然就联想到，工厂创建的不能是具体的类，要是一个接口/或者是一个抽象类，比如你的Car，工厂创建的不能是Car，要是Car的父类，这样在换工厂的时候，下面的代码才可以不用改动。所以，我们要建一个抽象工厂，然后让DefaultFactory、MagicFactory都去继承/实现这个工厂。而且工厂的返回值，都是抽象类或者接口。

````java
//抽象工厂
public abstract class AbstractFactory {
	//生产 交通工具
	public abstract Vehicle createVehicle();
	//生产 武器
	public abstract Weapon createWeapon();
	//生产食物
	public abstract Food createFood();
}


//交通工具
public abstract class Vehicle {
	//实现由子类决定
	public abstract void run();
}

//食物
public abstract class Food {
	public abstract void printName();
}


//武器
public abstract class Weapon {
	//
	public abstract void shoot();
}
````

产品类：都继承产品的抽象类

```java
public class Car extends Vehicle{
	@Override
	public void run() {
		System.out.println("冒着烟奔跑中...");
	}
}
//扫帚
public class Broom extends Vehicle{

	@Override
	public void run() {
		System.out.println("扫帚摇着尾巴呼呼呼...");
	}
}
//食物：毒蘑菇
public class MushRoom extends Food {

	@Override
	public void printName() {
		System.out.println("mushroom");
	}

}

public class Apple extends Food {
	@Override
	public void printName() {
		System.out.println("apple");
	}
}

public class AK47 extends Weapon{

	public void shoot(){
		System.out.println("哒哒哒....");
	}
}

//武器：魔法棒
public class MagicStick extends Weapon {
	@Override
	public void shoot() {
		System.out.println("fire hu hu hu ...");
	}

}

```

魔法工厂继承抽象工厂：

```java
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

```

DefaultFactory继承抽象工厂：

```java
//默认的工厂
public class DefaultFactory extends AbstractFactory{

	@Override
	public Food createFood() {
		return new Apple();
	}

	@Override
	public Vehicle createVehicle() {
		return new Car();
	}

	@Override
	public Weapon createWeapon() {
		return new AK47();
	}
}

```

最终形成的类结构是这样的：



![](D:\Z_lhy\学习\markdown文档\工厂模式\6.png)



测试程序：

```java
//换一个工厂，只需要改动这一处，就可以了，换一个工厂，就把生产的系列产品都换了
		AbstractFactory factory =  new MagicFactory(); //new DefaultFactory();
		//换一个工厂
		Vehicle vehicle = factory.createVehicle();
		vehicle.run();
		Weapon weapon = factory.createWeapon();
		weapon.shoot();
		Food food = factory.createFood();
		food.printName();
	
DefaultFactory打印：
冒着烟奔跑中...
哒哒哒....
apple
```

**把工厂换为****MagicFactory

AbstractFactory factory =  new DefaultFactory();

打印：

扫帚摇着尾巴呼呼呼...

fire hu hu hu ...

mushroom

抽象工厂：生产一系列的产品，如果你想换掉一系列的产品，或者你想在这一系列的产品上进行扩展，以及想对这一些列产品的生成过程进行控制，用抽象工厂。

### 抽象工厂和普通工厂的优缺点：

**普通工厂：**

可以在产品的维度上进行扩展，可以产生新的产品，可以产生新的产品的工厂。也就是说可以在产品的维度进行扩展。

在普通工厂想要产生产品系列，就会特别麻烦。产生一个产品，就会出现一个产品的Factory。就会出现”工厂泛滥”。

 

**抽象工厂：**

能换工厂，生产新的产品系列，但是不能产生新的产品品种，新添加一个产品，就要在抽象工厂里面加 createXXX(); 方法，所有子类都要实现这个方法。要改动的地方太多。

 

有没有一种工厂，结合普通工厂和抽象工厂的有点呢？

既可以随意添加产品品种，又很方便的添加产品系列？ 没有。

Spring提供了一种方案，Spring的Bean工厂。

Spring说，你就不要这么Moveable m = **new** Car(); 就行new Car了，你给配置到配置文件里。

测试java读取properties配置文件反射生成对象

![](D:\Z_lhy\学习\markdown文档\工厂模式\7.png)

```properties
spring.properties：

VehicleType=com.lhy.springfactory.Car
```

测试代码：

```java
public static void main(String[] args) throws Exception{
	Properties props = new Properties();
		props.load(Test.class.getClassLoader().getResourceAsStream("com/lhy/springfactory/spring.properties"));

		String vehicleTypeName = props.getProperty("VehicleType");
		System.out.println(vehicleTypeName);
		//反射生成对象
		Object o = Class.forName(vehicleTypeName).newInstance();
		Moveable m = (Moveable)o;
		m.run();
	}
打印结果：
com.lhy.springfactory.Car
冒着烟奔跑中...

```

把配置文件换成或者Trian:

```properties
VehicleType=com.lhy.springfactory.Train
```

执行测试代码，打印：

com.lhy.springfactory.Train

小火车呜呜呜...  



可以看到，只是改了配置文件，就可以动态控制生成的类了。代码都不用动。Spring就是这样的思路。

最简单的使用Spring：

引入必须的jar包，



![](D:\Z_lhy\学习\markdown文档\工厂模式\8.png)

Spring要求的配置文件：

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
           http://www.springframework.org/schema/beans/spring-beans-3.0.xsd">

  <bean id="v" class="com.bjsxt.spring.factory.Train">
  </bean>
  
  <!--  //v=com.bjsxt.spring.factory.Car  -->


</beans>

```

测试程序：  

```java
package com.bjsxt.spring.factory;

import java.io.IOException;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Test {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws Exception {
		BeanFactory f = new ClassPathXmlApplicationContext("applicationContext.xml");
		Object o = f.getBean("v");
		Moveable m = (Moveable)o;
		m.run();
	}

}
配置文件配置的火车类，打印：
小火车呜呜呜...
换成Car，打印：
冒着烟奔跑中...

```

## 下面来模拟Spring的Bean工厂

Spring的BeanFactory ，就是一个容器，是用一个map实现的，就是从配置文件读取 <bean id=*"v"* class=*"com.bjsxt.spring.factory.Train"*/> 这样的配置，遍历解析这样的xml配置，以id为key，class后的类全限定名用反射生成的对象为value，放到这个map中去。当用的时候，直接map.get(id); 获取这个Bean对象。

ClassPathXmlApplicationContext是BeanFactory的一种实现。这里模拟这种实现。

​        这里模拟Spring的Bean工厂：  

```java
public interface BeanFactory {

	Object getBean(String id);
}

```

ClassPathXmlApplicationContext：

```java
public class ClassPathXmlApplicationContext implements BeanFactory{
	//存放一个个Bean对象的容器，
	private Map<String, Object> container = new HashMap<String, Object>();
	
	// 构造方法找到配置文件，读取xml配置文件
	public ClassPathXmlApplicationContext(String fileName) throws Exception{
		SAXBuilder sb = new SAXBuilder();
		Document doc = sb.build(this.getClass().getClassLoader()
				.getResourceAsStream(fileName));
		Element root = doc.getRootElement();
		List list = XPath.selectNodes(root, "/beans/bean");
		System.out.println(list.size());

		for (int i = 0; i < list.size(); i++) {
			Element bean = (Element) list.get(i);
			String id = bean.getAttributeValue("id");
			String clazz = bean.getAttributeValue("class");
			Object o = Class.forName(clazz).newInstance();
			container.put(id, o);
			System.out.println(id + " " + clazz);
		}
	}

	//读取配置文件，读取id为传进来的id的Bean，实例化
	@Override
	public Object getBean(String id) {
		return container.get(id);
	}

	
}

```

测试程序：

```java
public static void main(String[] args) throws Exception{
		
		BeanFactory f = new ClassPathXmlApplicationContext("com/lhy/springfactory/applicationContext.xml");
		Object o = f.getBean("v");
		Moveable m = (Moveable)o;
		m.run();
		
		Train trian = (Train)f.getBean("trian");
		trian.run();
		
	}

```

applicationContext.xml:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans>
  <bean id="v" class="com.lhy.springfactory.Car"/>
	<bean id="trian" class="com.lhy.springfactory.Train"/>
  <!--  //v=com.bjsxt.spring.factory.Car  -->
</beans>

```

打印：

2

v com.lhy.springfactory.Car

trian com.lhy.springfactory.Train

冒着烟奔跑中...

小火车呜呜呜...

 

这样，就把类配置在了配置文件里，更灵活了！