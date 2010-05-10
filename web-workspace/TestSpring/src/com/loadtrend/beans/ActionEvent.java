package com.loadtrend.beans;

import org.springframework.context.ApplicationEvent;

public class ActionEvent extends ApplicationEvent {
	public ActionEvent(Object source) {
		super(source);
	}
}
