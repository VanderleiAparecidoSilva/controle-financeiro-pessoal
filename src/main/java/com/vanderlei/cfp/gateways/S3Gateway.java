package com.vanderlei.cfp.gateways;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.vanderlei.cfp.exceptions.FileException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;

@Slf4j
@Service
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
      log.info("Iniciando upload");
      s3client.putObject(bucketName, fileName, inputStream, objectMetadata);
      log.info("Finalizando upload");

      return s3client.getUrl(bucketName, fileName).toURI();
    } catch (URISyntaxException e) {
      throw new FileException("Erro ao converter URL para URI");
    }
  }
}
