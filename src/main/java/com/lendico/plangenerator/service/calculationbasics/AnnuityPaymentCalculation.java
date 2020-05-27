package com.lendico.plangenerator.service.calculationbasics;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class AnnuityPaymentCalculation {

    public Double annuityCalculation(double loanAmount,float nominalRate,int duration,int monthIndex){

        double ratePerMonth=nominalRate/12/100;
        double _calculatedValue=(loanAmount*(ratePerMonth))/(1-(Math.pow((1+ratePerMonth),-(duration-monthIndex))));
        double calculatedValue=BigDecimal.valueOf(_calculatedValue).setScale(2,BigDecimal.ROUND_HALF_EVEN).doubleValue();
        return calculatedValue;
    }
}
