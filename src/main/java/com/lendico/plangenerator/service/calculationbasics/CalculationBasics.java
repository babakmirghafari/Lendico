package com.lendico.plangenerator.service.calculationbasics;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

@Component
public class CalculationBasics {

    @Autowired
    AnnuityPaymentCalculation annuityPaymentCalculation;

    public double borrowerPaymentAmount(double principleCalculation,double interest) {

        double borrowerPaymentAmount = principleCalculation + interest;
        return BigDecimal.valueOf(borrowerPaymentAmount).setScale(2, BigDecimal.ROUND_HALF_DOWN).doubleValue();
    }

    public double principleCalculation(double initialOutstandingPrincipal, float nominalRate, int duration,int monthIndex) {
        double anuuity = this.annuity(initialOutstandingPrincipal, nominalRate, duration,monthIndex);
        double interest = this.interestCalulation(initialOutstandingPrincipal, nominalRate);
        double principle = BigDecimal.valueOf(anuuity - interest).setScale(2, BigDecimal.ROUND_HALF_DOWN).doubleValue();
        return principle;
    }

    public double interestCalulation(double initialOutstandingPrincipal, float nominalRate) {
        double interest = BigDecimal.valueOf((nominalRate / 100 * 30 * initialOutstandingPrincipal) / 360).setScale(2, BigDecimal.ROUND_HALF_DOWN).doubleValue();
        return interest;
    }

    public double annuity(double initialOutstandingPrincipal, float nominalRate, int duration,int monthIndex) {
        return annuityPaymentCalculation.annuityCalculation(initialOutstandingPrincipal, nominalRate, duration,monthIndex);
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
