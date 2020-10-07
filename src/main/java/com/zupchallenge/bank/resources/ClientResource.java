package com.zupchallenge.bank.resources;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zupchallenge.bank.models.UserInfo;
import com.zupchallenge.bank.models.Client;
import com.zupchallenge.bank.repository.ClientRepository;

@RestController
@RequestMapping(value="/v1/")
public class ClientResource {
	@Autowired
	ClientRepository cr;
	
	private boolean hasLegalAge(String date) throws DateTimeException {
		LocalDate birthday = LocalDate.parse(date);
		LocalDate actual = LocalDate.now();
		int diff_year = Period.between(birthday, actual).getYears();
		return diff_year >=18;
	}
	
	@PostMapping("/client-info")
	public ResponseEntity<String> createBasicInformation(@Valid @RequestBody UserInfo user) {
		List<Client> clientsByCpf = cr.findByCpf(user.getCpf());
		List<Client> clientsByEmail = cr.findByEmail(user.getEmail());
		if(clientsByCpf.size() == 0 && clientsByEmail.size() == 0) {
			try {
				if (hasLegalAge(user.getBirthday_date())) {
					// Create a client and put user data on it

					Client client = new Client();
					client.setName(user.getName());
					client.setSurname(user.getSurname());
					client.setEmail(user.getEmail());
					client.setCnh_number(user.getCnh_number());
					client.setCpf(user.getCpf());
					client.setBirthday_date(user.getBirthday_date());

					cr.save(client);
					// Response with address route
					HttpHeaders responseHeaders = new HttpHeaders();
					responseHeaders.set("location", "http://localhost:8080/address?cpf=" + user.getCpf());
					return ResponseEntity.created(null).headers(responseHeaders).build();
				}
				else {
					return ResponseEntity.badRequest().body("You haven't Legal age (>=18 years old)");
				}
			} catch (DateTimeException e_date) {
				return ResponseEntity.badRequest().body("Incorrect Date");
			}

		} else {
			return ResponseEntity.badRequest().body("CPF or Email already registred");
		}
	}

}
