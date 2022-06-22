package com.agussuhardi.restapisimulator.vo;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;


@Data
public class LogsVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @NotNull(message = "id can not null")
    private String id;

    @NotNull(message = "uri can not null")
    private String uri;

    @NotNull(message = "method can not null")
    private String method;

    @NotNull(message = "headers can not null")
    private String headers;

    @NotNull(message = "params can not null")
    private String params;

    @NotNull(message = "body can not null")
    private String body;

    private BigDecimal createdAt;

    private BigDecimal updatedAt;

}
