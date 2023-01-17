package com.appsdeveloperblog.photoapp.api.users.ui.model;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class CreateUserRequestModel {

	@NotNull(message="First Name cannot be null ")
	@Size(min=2,message="First Name must not be less than two characters")
	private String firstname;
	@NotNull(message="Last Name cannot be null ")
	@Size(min=2,message="Last Name must not be less than two characters")
	private String lastname;
	@NotNull(message="password cannot be null ")
	@Size(min=8,max=16,message="password must be equal or gretaer than 8 characters")
	private String password;
	@NotNull(message="email cannot be null ")
	@Email
	private String email;
	
	public String getFirstname() {
		return firstname;
	}
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}
	public String getLastname() {
		return lastname;
	}
	public void setLastname(String lastname) {
		this.lastname = lastname;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
	
}
