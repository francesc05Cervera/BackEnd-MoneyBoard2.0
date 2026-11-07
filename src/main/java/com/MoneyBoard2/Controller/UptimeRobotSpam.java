package com.MoneyBoard2.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Deprecated 
@RestController
@RequestMapping("api/spam")
public class UptimeRobotSpam 
{

	
	@GetMapping
	public ResponseEntity<Object> spamMethod()
	{	
		return ResponseEntity.ok("Hi! This is a bluf"); 
	}
}
