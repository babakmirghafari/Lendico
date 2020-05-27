package com.lendico.plangenerator.model;

import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;

@Data
@Builder
public class ResponseObject {

    private Double borrowerPaymentAmount;
    private Timestamp date;
    private Double initialOutstandingPrincipal;
    private Double interest;
    private Double principal;
    private Double remainingOutstandingPrincipals;
}
