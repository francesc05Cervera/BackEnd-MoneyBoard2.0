package com.MoneyBoard2.Service;

import com.MoneyBoard2.Model.SezioneRisparmio;
import java.util.*; 
import com.MoneyBoard2.Model.Transazione; 

public class ServiceSezione 
{
	
	public static boolean validateDelete(SezioneRisparmio sezione)
	{
		List<Transazione> list = new ArrayList<Transazione>(); 
		
		list = sezione.getTransazioni(); 
		
		if(list.size() == 0 || list == null)
		{
			return true; 
		}
		
		return false; 
	}

	public static Double aggiornaSaldo(boolean entrata, Double importo, Double saldo)
	{
		if(entrata)
		{
			Double saldo_nuovo = saldo + importo; 
			
			if(saldo_nuovo > saldo)
			{
				return saldo_nuovo;
			}
		}
		else 
		{
				Double saldo_nuovo = saldo - importo;
				
				if(saldo_nuovo < 0)
				{
					return -1.00; 
				}
				
				return saldo_nuovo;
		}
		
		return saldo;
	}
}
