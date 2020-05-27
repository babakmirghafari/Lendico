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

    static double principal=0.0;
    static double initialOutstandingPrincipal=0.0;
    static double interest=0.0;

    @Override
    public Collection<ResponseObject> generateResposePayLoad(RequestObject requestObject) {

        List<ResponseObject> responseObjectList=IntStream.range(0,requestObject.getDuration()).boxed().map(monthIndex->{

            if (monthIndex==0)
                initialOutstandingPrincipal=requestObject.getLoanAmount();
            else
                initialOutstandingPrincipal-=principal;

            double _roundedInitialOutstandingPrincipal=
                    (initialOutstandingPrincipal>principal)
                    ?BigDecimal.valueOf(initialOutstandingPrincipal).setScale(2,BigDecimal.ROUND_HALF_DOWN).doubleValue():principal;
            principal=calculationBasics.principleCalculation(_roundedInitialOutstandingPrincipal,requestObject.getNominalRate(),requestObject.getDuration(),monthIndex);
            interest=calculationBasics.interestCalulation(_roundedInitialOutstandingPrincipal,requestObject.getNominalRate());
            double borrowerPaymentAmount=calculationBasics.borrowerPaymentAmount(principal,interest);
            ResponseObject responseObject=ResponseObject.builder()
                    .borrowerPaymentAmount(borrowerPaymentAmount)
                    .date(calculationBasics.nextMonth(requestObject.getStartDate(),monthIndex))
                    .initialOutstandingPrincipal(_roundedInitialOutstandingPrincipal)
                    .interest(interest)
                    .principal(principal)
                    .remainingOutstandingPrincipals(_roundedInitialOutstandingPrincipal-principal)
                    .build();
            return  responseObject;
        }).collect(Collectors.toList());

        return responseObjectList;
    }


}
