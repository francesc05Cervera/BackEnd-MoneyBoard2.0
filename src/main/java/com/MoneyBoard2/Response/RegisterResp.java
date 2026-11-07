package com.MoneyBoard2.Response;

public class RegisterResp 
{
    private String message;

    public RegisterResp(String message) 
    {
        this.message = message;
    }

    public String getMessage() 
    {
        return message;
    }
    
    public void setMessage(String message) 
    {
        this.message = message;
    }
    
}
