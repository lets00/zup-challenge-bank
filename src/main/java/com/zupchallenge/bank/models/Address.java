package com.zupchallenge.bank.models;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

public class Address {
	
	@NotBlank(message = "cep is mandatory")
	@Pattern(regexp = "^[0-9]{8}$", message = "Malformed CEP")
	private String cep;
	
	@NotBlank(message = "street is mandatory")
	private String street;

	@NotBlank(message = "neighborhood is mandatory")
	private String neighborhood;
	
	@NotBlank(message = "complement is mandatory")
	private String complement;
	
	@NotBlank(message = "city is mandatory")
	private String city;
	
	@NotBlank(message = "state is mandatory")
	private String state;
	
	public String getCep() {
		return cep;
	}
	public void setCep(String cep) {
		this.cep = cep;
	}
	public String getStreet() {
		return street;
	}
	public void setStreet(String street) {
		this.street = street;
	}
	public String getNeighborhood() {
		return neighborhood;
	}
	public void setNeighborhood(String neighborhood) {
		this.neighborhood = neighborhood;
	}
	public String getComplement() {
		return complement;
	}
	public void setComplement(String complement) {
		this.complement = complement;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
}
