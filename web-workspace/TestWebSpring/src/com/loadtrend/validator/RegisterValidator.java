package com.loadtrend.validator;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.loadtrend.info.RegisterInfo;

public class RegisterValidator implements Validator
{
	public boolean supports( Class clazz )
	{
		return RegisterInfo.class.isAssignableFrom(clazz);
	}

	public void validate( Object obj, Errors errors )
	{
		RegisterInfo regInfo = (RegisterInfo) obj;
		
		// 检查注册用户名是否合法
		if ( regInfo.getUsername().length() < 4 )
		{
			errors.rejectValue( "username", "less4chars", null, "用户名长度必须大于等于4个字母！" );
		}
		
		/*
		 * 检查用户名是否已经存在 if (UserDAO.getUser(regInfo.getUsername()) != null) {
		 * errors.rejectValue("username", "existed", null, "用户已存在！"); }
		 */
		if ( regInfo.getPassword1().length() < 6 )
		{
			errors.rejectValue( "password1", "less6chars", null, "密码长度必须大于等于6个字母" );
		}
		
		if ( !regInfo.getPassword2().equals( regInfo.getPassword1() ) )
		{
			errors.rejectValue( "password2", "notsame", null, "两次输入的密码不一致！" );
		}
	}
}