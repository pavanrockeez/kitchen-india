package com.mother.kitchen.apis.dao;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.mother.kitchen.apis.modal.ContactUs;
import com.mother.kitchen.apis.modal.UserEmailMobileNumber;

@Repository
public class ContactUsDao {
	
	@Autowired
	private NamedParameterJdbcTemplate namedParameterJDBCTemplate;
	
	private final String query= "INSERT INTO contact_us(name, mobile_number,email,select_value,message)"
			+ "values(:name,:mobileNumber,:email,:selectValue,:message)";
	
	private final String saveEmailQuery = "INSERT INTO save_email(email, mobile_number) "
			+ "values(:email, :mobileNumber)";
	
	public long saveContacUs(ContactUs req) {
		return namedParameterJDBCTemplate.update(query, new BeanPropertySqlParameterSource(req));	
	}
	
	public long saveUserEmailAndMobileNumber(UserEmailMobileNumber req) {
		return namedParameterJDBCTemplate.update(saveEmailQuery, new BeanPropertySqlParameterSource(req));
	}
}
