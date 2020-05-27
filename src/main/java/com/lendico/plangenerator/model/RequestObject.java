package com.lendico.plangenerator.model;

import lombok.Data;
import java.sql.Timestamp;

@Data
public class RequestObject {

    private Double loanAmount;
    private Float nominalRate;
    private Integer duration;
    private Timestamp startDate;
}
