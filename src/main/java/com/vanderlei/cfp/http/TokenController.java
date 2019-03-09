package com.vanderlei.cfp.http;

import com.vanderlei.cfp.http.mapping.UrlMapping;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;

@Profile("oauth-security")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(UrlMapping.TOKENS)
public class TokenController {

  @Value("${seguranca.enable-https}")
  private Boolean isEnableHttps;

  static final String TAG_CONTROLLER = "tokens-controller";

  @DeleteMapping("/revoke")
  public void revoke(HttpServletRequest req, HttpServletResponse resp) {
    log.info(LocalDateTime.now() + " - " + isEnableHttps.toString());
    Cookie cookie = new Cookie("refreshToken", null);
    cookie.setHttpOnly(true);
    cookie.setSecure(isEnableHttps);
    cookie.setPath(req.getContextPath() + "/oauth/token");
    cookie.setMaxAge(0);

    resp.addCookie(cookie);
    resp.setStatus(HttpStatus.NO_CONTENT.value());
  }
}
