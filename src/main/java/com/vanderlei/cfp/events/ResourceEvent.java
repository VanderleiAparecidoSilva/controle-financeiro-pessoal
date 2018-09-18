package com.vanderlei.cfp.events;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import javax.servlet.http.HttpServletResponse;

@Getter
public class ResourceEvent extends ApplicationEvent {

  private HttpServletResponse response;

  private String id;

  public ResourceEvent(Object source, HttpServletResponse response, String id) {
    super(source);
    this.response = response;
    this.id = id;
  }
}
