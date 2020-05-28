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

/**
 * This class responsible for provide responseObject
 */
@Service
@Slf4j
public class GeneratePayLoudObjectService implements GeneratePayLoudObjectInterface {

    @Autowired
     CalculationBasics calculationBasics;

    /**
     * This method provide a Collection of ResponseObject
     * @param requestObject
     * @return
     */
    @Override
    public Collection<ResponseObject> generateResponsePayLoad(RequestObject requestObject) {
        /**
         * create a loop started with 0 and ended with requestObject.getDuration()
         * this loop create responseObject for each month (for example if user desired duration = 24, then this loop create 24 responseObject )
         */
        return IntStream.range(0,requestObject.getDuration())
                .boxed()
                .map(monthIndex->{
                    /**
                     * create ResponseObject for each month with this method{@link #responseObject(RequestObject, int)}
                     * monthIndex is a index for each month (started with 0 for first month and ended with (requestObject.getDuration()-1) for last month )
                     */
                    ResponseObject responseObject=responseObject(requestObject,monthIndex);
                    /**
                     * after each ResponseObject created, remainingOutstandingPrincipals and initialOutstandingPrincipal should be change for
                     * next month. in first month initialOutstandingPrincipal is user desired loanAmount (for example 5000) ,
                     * but for next month initialOutstandingPrincipal is previous remainingOutstandingPrincipals
                     * (for example after first month calculation, remainingOutstandingPrincipals=4807.65).This value should be set for next month initialOutstandingPrincipal
                     * this line set previous month remainingOutstandingPrincipals to requestObject loanAmount and send it as new initialOutstandingPrincipal with
                     * requestObject. because of we don't need previous values for requestObject(previous month responseObject created), we don't care
                     * about changed values.
                     */
                    requestObject.setLoanAmount(responseObject.getRemainingOutstandingPrincipals());
                    return  responseObject;
                }).collect(Collectors.toList());
    }

    @Override
    public ResponseObject responseObject(RequestObject requestObject, int monthIndex) {
        /**
         * calculate principal with {@link CalculationBasics#principalCalculation(RequestObject, int)}
         */
        double principle=calculationBasics.principalCalculation(requestObject, monthIndex);
        /**
         * calculate interest with {@link CalculationBasics#interestCalulation(RequestObject)}
         */
        double interest = calculationBasics.interestCalulation(requestObject);
        /**
         * generate next month Timestamp with {@link CalculationBasics#nextMonth(Timestamp, int)}
         */
        Timestamp date=calculationBasics.nextMonth(requestObject.getStartDate(),monthIndex);
        /**
         * check if principal exceeds the initialOutstandingPrincipal ( @see{@link #generateResponsePayLoad(RequestObject)} comments)
         * principal value set to initialOutstandingPrincipal , else initialOutstandingPrincipal used for calculation
         * (normally it happen in last month)
         *
         * after this check rounded initialOutstandingPrincipal with BigDecimal.ROUND_HALF_DOWN
         */
        double _roundedInitialOutstandingPrincipal=
                (requestObject.getLoanAmount()>principle)

                        ? BigDecimal.valueOf(requestObject.getLoanAmount()).setScale(2,BigDecimal.ROUND_HALF_DOWN).doubleValue():principle;
        /**
         * build ResponseObject and send it to {@link #generateResponsePayLoad(RequestObject)} for create Collection<ResponseObject>
         */
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
