package com.loadtrend.beans;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class LoginAction implements ApplicationContextAware {
	
	private ApplicationContext applicationContext;

	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.applicationContext = applicationContext;
	}

	public int login(String username, String password) {
		ActionEvent event = new ActionEvent(username);
		this.applicationContext.publishEvent(event);
		return 0;
	}
}
