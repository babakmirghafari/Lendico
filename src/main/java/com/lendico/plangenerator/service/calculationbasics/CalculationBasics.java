package com.lendico.plangenerator.service.calculationbasics;

import com.lendico.plangenerator.model.RequestObject;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

/***
 * This class is responsible for calculate All basic calculation parameters
 * (principal,interest,Annuity,next month)
 * user send desired  parameter for pre-calculation via controller class {@link com.lendico.plangenerator.web.GeneratePlanController }
 * to ResponseObject Generator service {@link com.lendico.plangenerator.service.GeneratePayLoudObjectService}
 * and this service fill all calculated needed fields with this class methods
 */
@Component
public class CalculationBasics {

    /**
     *This method responsible for calculate principal for each month with these parameters:
     * @param requestObject=this object chang during calculation , for each month loanAmount changed according this formula
     *                     new month loanAmount(initialOutstandingPrincipal)=previous month loanAmount- previous month principal(remainingOutstandingPrincipals)
     * @param monthIndex =index for each month start from 0 to desired duration
     * @return= a double value calculated base on principal formula: 
     *                     calculated Annuity {@link #annuityCalculation(RequestObject, int)}-calculated interest {@link #interestCalulation(RequestObject)}
     */
    public double principalCalculation(RequestObject requestObject, int monthIndex){
        double anuuity = annuityCalculation(requestObject,monthIndex);
        double interest = interestCalulation(requestObject);
        /**
         * using BigDecimal.ROUND_HALF_DOWN for round principal calculated value
         */
        return  BigDecimal.valueOf(anuuity - interest).setScale(2, BigDecimal.ROUND_HALF_DOWN).doubleValue();
    }

    /**
     *This method responsible for calculate interest for each month with this parameter:
     * @param requestObject=this object chang during calculation , for each month loanAmount changed according this formula
     *      *                     new month loanAmount(initialOutstandingPrincipal)=previous month loanAmount- previous month principal(remainingOutstandingPrincipals)
     * @return a double value calculated base on interest calculation formula
     */
    public  double interestCalulation(RequestObject requestObject) {
        /**
         * each month has 30 days and a year has 360 days ( base on task document).
         * requestObject.getNominalRate() / 100 statement calculated suitable value for put in formula
                                              (for example : nominal rate=5,suitable value=5/100=0.05)
         * using BigDecimal.ROUND_HALF_DOWN for round interest calculated value.
         */
        return BigDecimal.valueOf((requestObject.getNominalRate() / 100 * 30 * requestObject.getLoanAmount()) / 360).setScale(2, BigDecimal.ROUND_HALF_DOWN).doubleValue();
    }

    /**
     *This method responsible for calculate annuity for each month with these parameters :
     * @param requestObject=this object chang during calculation , for each month loanAmount changed according this formula
     *      *      *                     new month loanAmount(initialOutstandingPrincipal)=previous month loanAmount- previous month principal(remainingOutstandingPrincipals)
     * @param monthIndex= index for each month start from 0 to desired duration
     * @return a double base on annuity formula
     */
    public double annuityCalculation(RequestObject requestObject,int monthIndex){
        /**
         * calculate nominal rate for each month (user desired nominal rate is for a year, but in annuity formula we have to use annuity for month)
         */
        double ratePerMonth=requestObject.getNominalRate()/12/100;
        /**
         * annuity formula for each month
                                      (requestObject.getDuration()-monthIndex)-->calculate power for related month
                                      (for example user desired month 24,related month or month index  3,calculated value=21)
         */
        double calculatedValue=(requestObject.getLoanAmount()*(ratePerMonth))/(1-(Math.pow((1+ratePerMonth),-(requestObject.getDuration()-monthIndex))));
        /**
         * using BigDecimal.ROUND_HALF_DOWN for round annuity calculated value.
         */
        return BigDecimal.valueOf(calculatedValue).setScale(2,BigDecimal.ROUND_HALF_EVEN).doubleValue();
    }

    /**
     * This method responsible for calculated next month
     * @param startDate= user desired start date
     * @param monthIndex= index for each month start from 0 to desired duration
     * @return = a Timestamp value base on month index
     */
    public Timestamp nextMonth(Timestamp startDate, int monthIndex) {
        Calendar cal = GregorianCalendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
        Date _date = new Date();
        _date.setTime(startDate.getTime());
        cal.setTime(startDate);
        /**
         * Generate Timestamp base on month index
                             (for example : monthIndex=3 , startDate=2018-01-01T00:00:00.000+00:00,calculated Timestamp="2018-03-31T23:00:00.000+00:00")
                             monthIndex 0 return startDate
         */
        cal.set(Calendar.MONTH, cal.get(Calendar.MONTH) + monthIndex);
        return Timestamp.valueOf(df.format(cal.getTime()));
    }
}
