package com.lendico.plangenerator.service.calculationbasics;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

@Component
public class CalculationBasics {

    public double principalCalculation(double initialOutstandingPrincipal, float nominalRate, int duration, int monthIndex){
        double anuuity = this.annuityCalculation(initialOutstandingPrincipal, nominalRate, duration,monthIndex);
        double interest = interestCalulation(initialOutstandingPrincipal, nominalRate);
        double principal = BigDecimal.valueOf(anuuity - interest).setScale(2, BigDecimal.ROUND_HALF_DOWN).doubleValue();
        return principal;
    }
    public static double interestCalulation(double initialOutstandingPrincipal, float nominalRate) {
        double interest = BigDecimal.valueOf((nominalRate / 100 * 30 * initialOutstandingPrincipal) / 360).setScale(2, BigDecimal.ROUND_HALF_DOWN).doubleValue();
        return interest;
    }

//    public double annuity(double initialOutstandingPrincipal, float nominalRate, int duration,int monthIndex) {
//        return this.annuityCalculation(initialOutstandingPrincipal, nominalRate, duration,monthIndex);
//    }

    public Double annuityCalculation(double loanAmount,float nominalRate,int duration,int monthIndex){

        double ratePerMonth=nominalRate/12/100;
        double _calculatedValue=(loanAmount*(ratePerMonth))/(1-(Math.pow((1+ratePerMonth),-(duration-monthIndex))));
        double calculatedValue=BigDecimal.valueOf(_calculatedValue).setScale(2,BigDecimal.ROUND_HALF_EVEN).doubleValue();
        return calculatedValue;
    }

    public Timestamp nextMonth(Timestamp currentDate, int monthIndex) {
        Calendar cal = GregorianCalendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
        Date _date = new Date();
        _date.setTime(currentDate.getTime());
        cal.setTime(currentDate);
        cal.set(Calendar.MONTH, cal.get(Calendar.MONTH) + monthIndex);
        String nextMonthAsString = df.format(cal.getTime());
        return Timestamp.valueOf(nextMonthAsString);
    }
}
