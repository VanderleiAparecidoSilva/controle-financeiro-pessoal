package com.vanderlei.cfp.http.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class EmailDataContract implements Serializable {

  @NotEmpty
  @Email(message = "Email inválido")
  private String email;

  public EmailDataContract() {}

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }
}
