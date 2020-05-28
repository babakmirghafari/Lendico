package com.lendico.plangenerator.service;

import com.lendico.plangenerator.model.RequestObject;
import com.lendico.plangenerator.model.ResponseObject;
import com.lendico.plangenerator.service.calculationbasics.CalculationBasics;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@Slf4j
public class GeneratePayLoudObjectService implements GeneratePayLoudObjectInterface {

    @Autowired
     CalculationBasics calculationBasics;

    @Override
    public Collection<ResponseObject> generateResponsePayLoad(RequestObject requestObject) {
        return IntStream.range(0,requestObject.getDuration())
                .boxed()
                .map(monthIndex->{
                    ResponseObject responseObject=responseObject(requestObject,monthIndex);
                    requestObject.setLoanAmount(responseObject.getRemainingOutstandingPrincipals());
                    return  responseObject;
                }).collect(Collectors.toList());
    }

    private ResponseObject responseObject(RequestObject requestObject, int monthIndex) {
        double principle=calculationBasics.principalCalculation(requestObject, monthIndex);
        double interest = calculationBasics.interestCalulation(requestObject);
        Timestamp date=calculationBasics.nextMonth(requestObject.getStartDate(),monthIndex);
        double _roundedInitialOutstandingPrincipal=
                (requestObject.getLoanAmount()>principle)
                        ? BigDecimal.valueOf(requestObject.getLoanAmount()).setScale(2,BigDecimal.ROUND_HALF_DOWN).doubleValue():principle;

        return ResponseObject.builder()
                .borrowerPaymentAmount(BigDecimal.valueOf(principle+interest).setScale(2, BigDecimal.ROUND_HALF_DOWN).doubleValue())
                .initialOutstandingPrincipal(_roundedInitialOutstandingPrincipal)
                .principal(principle)
                .interest(interest)
                .date(date)
                .remainingOutstandingPrincipals(_roundedInitialOutstandingPrincipal-principle)
                .build();
    }

}
