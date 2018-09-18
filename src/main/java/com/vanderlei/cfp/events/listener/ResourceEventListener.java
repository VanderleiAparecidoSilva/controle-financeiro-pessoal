package com.vanderlei.cfp.events.listener;

import com.vanderlei.cfp.events.ResourceEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletResponse;
import java.net.URI;

@Component
public class ResourceEventListener implements ApplicationListener<ResourceEvent> {

  @Override
  public void onApplicationEvent(ResourceEvent resourceEvent) {
    HttpServletResponse response = resourceEvent.getResponse();
    String id = resourceEvent.getId();

    addHeaderLocation(response, id);
  }

  private void addHeaderLocation(HttpServletResponse response, String id) {
    URI uri =
        ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(id).toUri();
    response.setHeader("Location", uri.toASCIIString());
  }
}
