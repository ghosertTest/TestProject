package com.loadtrend.beans;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;

public class ActionListener implements ApplicationListener {
	
	public void onApplicationEvent(ApplicationEvent event) {
		if (event instanceof ActionEvent) {
			System.out.println(event.toString());
		}
	}
}
