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
		
		// ���ע���û����Ƿ�Ϸ�
		if ( regInfo.getUsername().length() < 4 )
		{
			errors.rejectValue( "username", "less4chars", null, "�û������ȱ�����ڵ���4����ĸ��" );
		}
		
		/*
		 * ����û����Ƿ��Ѿ����� if (UserDAO.getUser(regInfo.getUsername()) != null) {
		 * errors.rejectValue("username", "existed", null, "�û��Ѵ��ڣ�"); }
		 */
		if ( regInfo.getPassword1().length() < 6 )
		{
			errors.rejectValue( "password1", "less6chars", null, "���볤�ȱ�����ڵ���6����ĸ" );
		}
		
		if ( !regInfo.getPassword2().equals( regInfo.getPassword1() ) )
		{
			errors.rejectValue( "password2", "notsame", null, "������������벻һ�£�" );
		}
	}
}