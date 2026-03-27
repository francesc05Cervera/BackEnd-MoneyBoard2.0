package com.MoneyBoard2.Request;

public class UserCreateRequest 
{	
	private String username; 
	private String email; 
	private String password; 
	
	public UserCreateRequest(String user, String mail, String pass) 
	{
		this.username = user; 
		this.email = mail;
		this.password = pass;
	}
	
	public UserCreateRequest() { }

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	} 
	
	

}
