package com.smart.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data	
@AllArgsConstructor
@NoArgsConstructor
public class EmailRequest {

	private String subject;
	private String message;
	private String to;
	

}
