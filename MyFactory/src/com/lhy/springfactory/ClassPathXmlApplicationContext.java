package com.lhy.springfactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import org.jdom2.xpath.XPath;



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
