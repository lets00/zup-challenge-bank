package com.zupchallenge.bank.resources;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.Period;

import javax.validation.Valid;

import com.zupchallenge.bank.models.*;
import com.zupchallenge.bank.services.auth.JwtService;
import com.zupchallenge.bank.services.email.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.zupchallenge.bank.repository.ProposalRepository;

@RestController
@RequestMapping(value="/v1/")
public class ProposalResource {
	@Autowired
	ProposalRepository cr;

	@Autowired
	EmailService emailService;

	@Autowired
	JwtService token;

	@Value("${app.host.url}")
	private String HOST_URL;
	
	private boolean hasLegalAge(String date) throws DateTimeException {
		LocalDate birthday = LocalDate.parse(date);
		LocalDate actual = LocalDate.now();
		int diff_year = Period.between(birthday, actual).getYears();
		return diff_year >=18;
	}

	private HttpHeaders mountHeader(String route, String token) {
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.set("location", HOST_URL + "/v1/" + route);
		responseHeaders.set("token", token);
		return responseHeaders;
	}
	
	@PostMapping("/client-info")
	public ResponseEntity<JsonMessage> createBasicInformation(@Valid @RequestBody UserInfo user) {
		Proposal clientsByCpf = cr.findByCpf(user.getCpf());
		Proposal clientsByEmail = cr.findByEmail(user.getEmail());
		if(clientsByCpf == null && clientsByEmail == null) {
			try {
				if (hasLegalAge(user.getBirthday_date())) {
					// Create a client and put user data on it
					Proposal proposal = new Proposal();
					proposal.setName(user.getName());
					proposal.setSurname(user.getSurname());
					proposal.setEmail(user.getEmail());
					proposal.setCnh_number(user.getCnh_number());
					proposal.setCpf(user.getCpf());
					proposal.setBirthday_date(user.getBirthday_date());

					cr.save(proposal);
					// Response with address route
					HttpHeaders responseHeaders = mountHeader("address", user.getCpf());
					return ResponseEntity.created(null).headers(responseHeaders).build();
				}
				else {
					JsonMessage message = new JsonMessage("You haven't Legal age (>=18 years old)");
					return ResponseEntity.badRequest().body(message);
				}
			} catch (DateTimeException e_date) {
				JsonMessage message = new JsonMessage("Incorrect Date");
				return ResponseEntity.badRequest().body(message);
			}
		} else {
			JsonMessage message = new JsonMessage("CPF or Email already registred");
			return ResponseEntity.badRequest().body(message);
		}
	}

	@PostMapping("/address")
	public ResponseEntity<JsonMessage> createAddress(@Valid @RequestBody Address address, @RequestHeader("token") String jwt) {
		Proposal clientsByCpf = cr.findByCpf(jwt);
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

	@PostMapping("/cnh")
	public ResponseEntity<JsonMessage> createCNH(@Valid @RequestBody CNH cnh, @RequestHeader("token") String jwt) {
		Proposal proposalByCpf = cr.findByCpf(jwt);
		if (proposalByCpf != null) {
			if (proposalByCpf.getCep() != null) {
				proposalByCpf.setCnh_front_url(cnh.getCnh_front_url());
				proposalByCpf.setCnh_back_url(cnh.getCnh_back_url());
				cr.save(proposalByCpf);
				HttpHeaders responseHeaders = mountHeader("proposal", jwt);
				return ResponseEntity.created(null).headers(responseHeaders).build();
			} else {
				return ResponseEntity.unprocessableEntity().build();
			}
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@GetMapping("/proposal")
	public ResponseEntity<Proposal> createCNH(@RequestHeader("token") String jwt) {
		Proposal proposalByCpf = cr.findByCpf(jwt);
		if (proposalByCpf != null) {
			if (proposalByCpf.getCnh_front_url() != null) {
				return ResponseEntity.ok(proposalByCpf);
			} else {
				return ResponseEntity.unprocessableEntity().build();
			}
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@PostMapping("/proposal")
	public ResponseEntity<JsonMessage> acceptOrReject(@RequestHeader("token") String jwt, @RequestParam("accept") String accept) {
		Proposal proposalByCpf = cr.findByCpf(jwt);
		if (proposalByCpf != null) {
			JsonMessage msg;
			String emailMsg;
			if (accept.equals("true")) {
				// send a email to create account
				msg = new JsonMessage("Proposal accept. I'll send a email with respective accept process");
				emailMsg = "Create your account!";
			} else {
				// send a email begging to create account
				msg = new JsonMessage("Proposal declined");
				emailMsg = "Create your account, please. I begging you!";
			}
			String tk = token.generateToken(jwt);
			String accountLink = HOST_URL + "/v1/account/create?token=" + tk;
			emailService.sendEmail(proposalByCpf.getEmail(), emailMsg, accountLink);
			return ResponseEntity.ok(msg);
		} else {
			return ResponseEntity.notFound().build();
		}
	}
}
