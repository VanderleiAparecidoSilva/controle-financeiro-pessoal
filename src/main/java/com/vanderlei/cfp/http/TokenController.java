package com.vanderlei.cfp.http;

import com.vanderlei.cfp.config.property.CFPApiProperty;
import com.vanderlei.cfp.http.mapping.UrlMapping;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Profile("oauth-security")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(UrlMapping.TOKENS)
public class TokenController {

  static final String TAG_CONTROLLER = "tokens-controller";

  @Autowired private CFPApiProperty cfpApiProperty;

  @DeleteMapping("/revoke")
  public void revoke(HttpServletRequest req, HttpServletResponse resp) {
    Cookie cookie = new Cookie("refreshToken", null);
    cookie.setHttpOnly(true);
    cookie.setSecure(cfpApiProperty.getSeguranca().isEnableHttps());
    cookie.setPath(req.getContextPath() + "/oauth/token");
    cookie.setMaxAge(0);

    resp.addCookie(cookie);
    resp.setStatus(HttpStatus.NO_CONTENT.value());
  }
}
