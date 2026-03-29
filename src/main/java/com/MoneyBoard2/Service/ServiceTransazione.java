package com.MoneyBoard2.Service;

import com.MoneyBoard2.Model.SezioneRisparmio;
import com.MoneyBoard2.Request.CreateTransactionRequest;

public class ServiceTransazione 
{
	
	public static boolean validateCreation(SezioneRisparmio sezione, CreateTransactionRequest transazione)
	{
		boolean entrata = transazione.isEntrata(); 
		
		if(!entrata)
		{
			if(transazione.getImporto() > sezione.getSaldo())
			{
				return false; 
			}
		}
		
		return true; 
	}

}
