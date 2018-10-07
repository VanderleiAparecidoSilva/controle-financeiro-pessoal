package com.vanderlei.cfp.http.exceptions;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import com.vanderlei.cfp.exceptions.*;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class ControllerExceptionHandler {

  @Autowired private MessageSource messageSource;

  @ExceptionHandler(ObjectDuplicatedException.class)
  public ResponseEntity<StandardError> objectDuplicated(
      ObjectDuplicatedException e, HttpServletRequest request) {

    String mensagemUsuario =
        messageSource.getMessage(
            "mensagem.objeto.duplicado", null, LocaleContextHolder.getLocale());

    StandardError standardError =
        new StandardError(
            System.currentTimeMillis(),
            HttpStatus.CONFLICT.value(),
            mensagemUsuario,
            ExceptionUtils.getRootCauseMessage(e),
            request.getRequestURI());
    return ResponseEntity.status(HttpStatus.CONFLICT).body(standardError);
  }

  @ExceptionHandler(UnrecognizedPropertyException.class)
  public ResponseEntity<StandardError> unrecognized(
      UnrecognizedPropertyException e, HttpServletRequest request) {
    String mensagemUsuario =
        messageSource.getMessage("mensagem.dado.invalido", null, LocaleContextHolder.getLocale());

    StandardError standardError =
        new StandardError(
            System.currentTimeMillis(),
            HttpStatus.BAD_REQUEST.value(),
            mensagemUsuario,
            ExceptionUtils.getRootCauseMessage(e),
            request.getRequestURI());
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(standardError);
  }

  @ExceptionHandler(ObjectNotFoundException.class)
  public ResponseEntity<StandardError> objectNotFound(
      ObjectNotFoundException e, HttpServletRequest request) {
    String mensagemUsuario =
        messageSource.getMessage(
            "mensagem.objeto.nao.encontrado", null, LocaleContextHolder.getLocale());

    StandardError standardError =
        new StandardError(
            System.currentTimeMillis(),
            HttpStatus.NOT_FOUND.value(),
            mensagemUsuario,
            ExceptionUtils.getRootCauseMessage(e),
            request.getRequestURI());
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(standardError);
  }

  @ExceptionHandler(DataIntegrityException.class)
  public ResponseEntity<StandardError> dataIntegrity(
      DataIntegrityException e, HttpServletRequest request) {
    String mensagemUsuario =
        messageSource.getMessage(
            "mensagem.integridade.dados", null, LocaleContextHolder.getLocale());

    StandardError standardError =
        new StandardError(
            System.currentTimeMillis(),
            HttpStatus.BAD_REQUEST.value(),
            mensagemUsuario,
            ExceptionUtils.getRootCauseMessage(e),
            request.getRequestURI());
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(standardError);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<StandardError> methodArgumentNotValidException(
      MethodArgumentNotValidException e, HttpServletRequest request) {
    String mensagemUsuario =
        messageSource.getMessage("mensagem.erro.validacao", null, LocaleContextHolder.getLocale());

    ValidationError validationError =
        new ValidationError(
            System.currentTimeMillis(),
            HttpStatus.UNPROCESSABLE_ENTITY.value(),
            mensagemUsuario,
            ExceptionUtils.getRootCauseMessage(e),
            request.getRequestURI());
    for (FieldError fieldError : e.getBindingResult().getFieldErrors()) {
      validationError.addError(
          StringUtils.capitalize(fieldError.getField()), fieldError.getDefaultMessage());
    }

    return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(validationError);
  }

  @ExceptionHandler(AuthorizationException.class)
  public ResponseEntity<StandardError> authorization(
      AuthorizationException e, HttpServletRequest request) {
    String mensagemUsuario =
        messageSource.getMessage("mensagem.acesso.negado", null, LocaleContextHolder.getLocale());

    StandardError standardError =
        new StandardError(
            System.currentTimeMillis(),
            HttpStatus.FORBIDDEN.value(),
            mensagemUsuario,
            ExceptionUtils.getRootCauseMessage(e),
            request.getRequestURI());
    return ResponseEntity.status(HttpStatus.FORBIDDEN).body(standardError);
  }

  @ExceptionHandler(FileException.class)
  public ResponseEntity<StandardError> file(FileException e, HttpServletRequest request) {
    String mensagemUsuario =
        messageSource.getMessage("mensagem.erro.arquivo", null, LocaleContextHolder.getLocale());

    StandardError standardError =
        new StandardError(
            System.currentTimeMillis(),
            HttpStatus.BAD_REQUEST.value(),
            mensagemUsuario,
            ExceptionUtils.getRootCauseMessage(e),
            request.getRequestURI());
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(standardError);
  }

  @ExceptionHandler(AmazonServiceException.class)
  public ResponseEntity<StandardError> amazonService(
      AmazonServiceException e, HttpServletRequest request) {
    String mensagemUsuario =
        messageSource.getMessage(
            "mensagem.erro.amazon.service", null, LocaleContextHolder.getLocale());

    HttpStatus code = HttpStatus.valueOf(e.getErrorCode());
    StandardError standardError =
        new StandardError(
            System.currentTimeMillis(),
            code.value(),
            mensagemUsuario,
            ExceptionUtils.getRootCauseMessage(e),
            request.getRequestURI());
    return ResponseEntity.status(code).body(standardError);
  }

  @ExceptionHandler(AmazonClientException.class)
  public ResponseEntity<StandardError> amazonClient(
      AmazonClientException e, HttpServletRequest request) {
    String mensagemUsuario =
        messageSource.getMessage(
            "mensagem.erro.amazon.client", null, LocaleContextHolder.getLocale());

    StandardError standardError =
        new StandardError(
            System.currentTimeMillis(),
            HttpStatus.BAD_REQUEST.value(),
            mensagemUsuario,
            ExceptionUtils.getRootCauseMessage(e),
            request.getRequestURI());
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(standardError);
  }

  @ExceptionHandler(AmazonS3Exception.class)
  public ResponseEntity<StandardError> amazonS3(AmazonS3Exception e, HttpServletRequest request) {
    String mensagemUsuario =
        messageSource.getMessage("mensagem.erro.amazon.s3", null, LocaleContextHolder.getLocale());

    StandardError standardError =
        new StandardError(
            System.currentTimeMillis(),
            HttpStatus.BAD_REQUEST.value(),
            mensagemUsuario,
            ExceptionUtils.getRootCauseMessage(e),
            request.getRequestURI());
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(standardError);
  }
}