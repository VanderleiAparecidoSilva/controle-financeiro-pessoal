package com.vanderlei.cfp.config.springfox;

import com.google.common.collect.Lists;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ResponseMessageBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ResponseMessage;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.UiConfiguration;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.List;

import static org.springframework.http.HttpStatus.*;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
  @Bean
  public Docket gatewayApi() {
    return new Docket(DocumentationType.SWAGGER_2)
        .select()
        .apis(RequestHandlerSelectors.any())
        .paths(PathSelectors.regex("/api/.*"))
        .build()
        .pathMapping("/")
        .useDefaultResponseMessages(false)
        .globalResponseMessage(RequestMethod.POST, defaultErrorsMessage())
        .apiInfo(apiInfo());
  }

  private List<ResponseMessage> defaultErrorsMessage() {
    return Lists.newArrayList(
        new ResponseMessageBuilder()
            .code(NOT_FOUND.value())
            .message("Dados não encontrados")
            .build(),
        new ResponseMessageBuilder()
            .code(INTERNAL_SERVER_ERROR.value())
            .message("Erro interno no controle financeiro pessoal")
            .build(),
        new ResponseMessageBuilder()
            .code(BAD_REQUEST.value())
            .message("Dado inválido / json")
            .build(),
        new ResponseMessageBuilder()
            .code(BAD_GATEWAY.value())
            .message("Comunicação entre o controle financeiro pessoal e o upstream server falhou")
            .build());
  }

  private ApiInfo apiInfo() {
    return new ApiInfoBuilder()
        .title("Controle Financeiro Pessoal")
        .description("By Vanderlei")
        .version("1.0")
        .build();
  }

  @Bean
  public UiConfiguration uiConfig() {
    return new UiConfiguration(null);
  }
}
