package com.vanderlei.cfp.http.exceptions;

import java.io.Serializable;

public class StandardError implements Serializable {

    private Integer status;
    private String mensagem;
    private Long timeStamp;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public Long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public StandardError(Integer status, String mensagem, Long timeStamp) {
        this.status = status;
        this.mensagem = mensagem;
        this.timeStamp = timeStamp;
    }
}
