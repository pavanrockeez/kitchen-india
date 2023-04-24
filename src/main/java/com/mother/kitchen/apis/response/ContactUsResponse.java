package com.mother.kitchen.apis.response;

import com.mother.kitchen.apis.modal.Status;

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
public class ContactUsResponse {
	private long data;
	private Status status;
}
