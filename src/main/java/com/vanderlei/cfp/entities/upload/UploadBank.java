package com.vanderlei.cfp.entities.upload;

import com.vanderlei.cfp.entities.Usuario;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;

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

  private String tipo;

  @NotNull private Usuario usuario;
}
