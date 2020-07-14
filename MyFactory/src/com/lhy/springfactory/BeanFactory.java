package com.lhy.springfactory;

public interface BeanFactory {

	Object getBean(String id) throws Exception;
}
