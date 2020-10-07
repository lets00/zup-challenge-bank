package com.zupchallenge.bank.models;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

public class UserInfo {
	
	@NotBlank(message = "name is mandatory")
	private String name;
	
	@NotBlank(message = "surname is mandatory")
	private String surname;
	
	@NotBlank(message = "email is mandatory")
	@Email(message = "Malformed email")
	private String email;
	
	@NotBlank(message = "cnh_number is mandatory")
	@Pattern(regexp = "^[0-9]{11}$", message = "Malformed CNH")
	private String cnh_number;
	
	@NotBlank(message = "cpf is mandatory")
	@Pattern(regexp = "^[0-9]{11}$", message = "Malformed CPF")
	private String cpf;
	
	@NotBlank(message = "birthday_date is mandatory")
	@Pattern(regexp = "^[0-9]{4}-[0-9]{2}-[0-9]{2}$", message = "Malformed data. It must be: YYYY-MM-DD. Ex: 2020-10-07")
	private String birthday_date;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSurname() {
		return surname;
	}
	public void setSurname(String surname) {
		this.surname = surname;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getCnh_number() {
		return cnh_number;
	}
	public void setCnh_number(String cnh_number) {
		this.cnh_number = cnh_number;
	}
	public String getCpf() {
		return cpf;
	}
	public void setCpf(String cpf) {
		this.cpf = cpf;
	}
	public String getBirthday_date() {
		return birthday_date;
	}
	public void setBirthday_date(String birthday_date) {
		this.birthday_date = birthday_date;
	}
	
}
