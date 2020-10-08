package com.zupchallenge.bank.resources;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.Period;

import javax.validation.Valid;

import com.zupchallenge.bank.models.Address;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

	private HttpHeaders mountHeader(String route, String token) {
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.set("location", "http://localhost:8080/" + route);
		responseHeaders.set("token", token);
		return responseHeaders;
	}
	
	@PostMapping("/client-info")
	public ResponseEntity<String> createBasicInformation(@Valid @RequestBody UserInfo user) {
		Client clientsByCpf = cr.findByCpf(user.getCpf());
		Client clientsByEmail = cr.findByEmail(user.getEmail());
		if(clientsByCpf == null && clientsByEmail == null) {
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
					HttpHeaders responseHeaders = mountHeader("address", user.getCpf());
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

	@PostMapping("/address")
	public ResponseEntity<String> createAddress(@Valid @RequestBody Address address, @RequestHeader("token") String jwt) {
		Client clientsByCpf = cr.findByCpf(jwt);
		if (clientsByCpf != null) {
			clientsByCpf.setCep(address.getCep());
			clientsByCpf.setStreet(address.getStreet());
			clientsByCpf.setCity(address.getCity());
			clientsByCpf.setState(address.getState());
			clientsByCpf.setNeighborhood(address.getNeighborhood());
			clientsByCpf.setComplement(address.getComplement());
			cr.save(clientsByCpf);

			HttpHeaders responseHeaders = mountHeader("cnh", jwt);
			return ResponseEntity.created(null).headers(responseHeaders).build();
		} else {
			return ResponseEntity.badRequest().build();
		}
	}
}
