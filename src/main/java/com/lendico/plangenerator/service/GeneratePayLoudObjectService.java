package com.lendico.plangenerator.service;

import com.lendico.plangenerator.model.RequestObject;
import com.lendico.plangenerator.model.ResponseObject;
import com.lendico.plangenerator.service.calculationbasics.CalculationBasics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class GeneratePayLoudObjectService implements GeneratePayLoudObjectInterface {

    @Autowired
    CalculationBasics calculationBasics;

    @Override
    public Collection<ResponseObject> generateResposePayLoad(RequestObject requestObject) {
        return IntStream.range(0,requestObject.getDuration())
                .boxed()
                .map(monthIndex->{
                    ResponseObject responseObject=this.responseObject(requestObject,monthIndex);
                    requestObject.setLoanAmount(responseObject.getRemainingOutstandingPrincipals());
                    return  responseObject;
                }).collect(Collectors.toList());
    }

    private ResponseObject responseObject(RequestObject requestObject, int monthIndex) {
        double principle=calculationBasics.principalCalculation(requestObject.getLoanAmount(), requestObject.getNominalRate(), requestObject.getDuration(), monthIndex);
        double interest = calculationBasics.interestCalulation(requestObject.getLoanAmount(), requestObject.getNominalRate());
        double _roundedInitialOutstandingPrincipal=
                (requestObject.getLoanAmount()>principle)
                        ? BigDecimal.valueOf(requestObject.getLoanAmount()).setScale(2,BigDecimal.ROUND_HALF_DOWN).doubleValue():principle;
        return ResponseObject.builder()
                .borrowerPaymentAmount(BigDecimal.valueOf(principle+interest).setScale(2, BigDecimal.ROUND_HALF_DOWN).doubleValue())
                .initialOutstandingPrincipal(_roundedInitialOutstandingPrincipal)
                .principal(principle)
                .interest(interest)
                .date(calculationBasics.nextMonth(requestObject.getStartDate(),monthIndex))
                .remainingOutstandingPrincipals(_roundedInitialOutstandingPrincipal-principle)
                .build();
    }

}
