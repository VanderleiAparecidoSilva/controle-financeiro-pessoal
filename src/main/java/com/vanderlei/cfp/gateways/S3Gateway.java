package com.vanderlei.cfp.gateways;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.vanderlei.cfp.exceptions.FileException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDateTime;

@Slf4j
@Service
@Profile("!test")
public class S3Gateway {

  @Autowired private AmazonS3 s3client;

  @Value("${s3.bucket}")
  private String bucketName;

  public URI uploadFile(final MultipartFile multipartFile) {
    try {
      String fileName = multipartFile.getOriginalFilename();
      InputStream inputStream = multipartFile.getInputStream();
      String contentType = multipartFile.getContentType();

      return uploadFile(inputStream, fileName, contentType);
    } catch (IOException e) {
      throw new FileException("Erro de IO: " + e.getMessage());
    }
  }

  public URI uploadFile(
      final InputStream inputStream, final String fileName, final String contentType) {
    try {
      ObjectMetadata objectMetadata = new ObjectMetadata();
      objectMetadata.setContentType(contentType);
      log.info(LocalDateTime.now() + " - Iniciando upload");
      s3client.putObject(bucketName, fileName, inputStream, objectMetadata);
      log.info(LocalDateTime.now() + " - Finalizando upload");

      return s3client.getUrl(bucketName, fileName).toURI();
    } catch (URISyntaxException e) {
      throw new FileException(LocalDateTime.now() + " - Erro ao converter URL para URI");
    }
  }
}
