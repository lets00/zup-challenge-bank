package com.zupchallenge.bank.models;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="CLIENT")
public class Client implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id;
	
	private String name;
	private String surname;
	private String email;
	private String cnh_number;
	private String cpf;
	private String birthday_date;
	private String cep;
	private String street;
	private String neighborhood;
	private String complement;
	private String city;
	private String state;
	private String cnh_front_url;
	private String cnh_back_url;
	private boolean accept;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
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
	public String getCnh_front_url() {
		return cnh_front_url;
	}
	public void setCnh_front_url(String cnh_front_url) {
		this.cnh_front_url = cnh_front_url;
	}
	public String getCnh_back_url() {
		return cnh_back_url;
	}
	public void setCnh_back_url(String cnh_back_url) {
		this.cnh_back_url = cnh_back_url;
	}
	public boolean isAccept() {
		return accept;
	}
	public void setAccept(boolean accept) {
		this.accept = accept;
	}
}
