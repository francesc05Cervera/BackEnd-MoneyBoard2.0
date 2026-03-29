package com.MoneyBoard2.Service;

import com.MoneyBoard2.Request.LoginRequest;
import com.MoneyBoard2.Request.UserCreateRequest;

public class ServiceUtente 
{
	
	public static boolean validateInputLogin(LoginRequest login)
	{
		String email = login.getEmail();
		String password = login.getPassword();
		
		if(email.isBlank() || password.isBlank())
			return false;
		else
			return true;
	}
	
	public static boolean validateInputCreate(UserCreateRequest request)
	{
		String email = request.getEmail();
		String username = request.getUsername();
		String password = request.getPassword();
		
		if(email.isBlank() || username.isBlank() || password.isBlank())
		{
			return false;
		}
		
		return true;
	}

}
