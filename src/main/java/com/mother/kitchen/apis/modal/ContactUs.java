package com.mother.kitchen.apis.modal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class ContactUs {
	
	private String name;
	private String mobileNumber;
	private String email;
	private String selectValue;
	private String message;
}	
