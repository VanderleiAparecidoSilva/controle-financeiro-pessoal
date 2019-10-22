package com.vanderlei.cfp.entities.upload;

import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "uploadBank")
@Getter
public class UploadBank {

  @Id private String id;

  private String descricao;

  private String centroCustoPrimario;

  private String centroCustoSecundario;

  private Integer diaVencimento;

  private String contaBancaria;

  private Integer linhaInicialArquivo;
}
