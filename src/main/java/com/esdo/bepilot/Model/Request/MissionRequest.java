package com.esdo.bepilot.Model.Request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MissionRequest {

    private Long id;

    private String missionKey;

    @NotBlank
    private String name;

    @NotBlank
    private String communication;

    @NotBlank
    private String missionType;

    @NotNull
    private Integer quantity;

    @NotNull
    private Integer quantityMade;

    @NotBlank
    private String keyWord;

    @NotBlank
    private String link;

    private Long customerId;

    private Long missionDetails;

    @NotBlank
    private BigDecimal priceUnit;

    @NotBlank
    private BigDecimal moneyReceived;
}
