package com.lendico.plangenerator.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data

@NoArgsConstructor
public class RequestObject {

    private Double loanAmount;
    private Float nominalRate;
    private Integer duration;
    private Timestamp startDate;
}
