package com.loadtrend.action;

import java.util.HashMap;
import java.util.Map;

import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;

public class RegisterAction extends SimpleFormController
{
	protected ModelAndView onSubmit( Object cmd, BindException ex ) throws Exception
	{
		Map rsMap = new HashMap();
		rsMap.put( "registerinfo", cmd );
		return new ModelAndView( this.getSuccessView(), rsMap );
	}
}
