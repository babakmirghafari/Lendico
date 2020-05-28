package com.lendico.plangenerator;

import com.lendico.plangenerator.model.ResponseObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.stream.IntStream;

public class GeneratePayLoudObjectServiceTest extends AbstractTestClass {

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    /**
     * check requestObject is not null
     * check responseObject Collection size as same as requestObject duration
     */
    @Test
    public void  generateResponsePayLoadTest(){
        Collection<ResponseObject> responseObjectCollection=generatePayLoudObjectInterface.generateResponsePayLoad(requestObject);
        Assert.assertNotNull(requestObject);
        Assert.assertTrue(responseObjectCollection.size()==requestObject.getDuration());

    }

    /**
     * check responseObject Initialization
     */
    @Test
    public void responseObjectInitializationTest(){
        IntStream.range(0,requestObject.getDuration())
                .boxed()
                .forEach(monthIndex->{
                    double principal=calculationBasics.principalCalculation(requestObject, monthIndex);
                    double interest = calculationBasics.interestCalulation(requestObject);
                    double borrowerPaymentAmount= BigDecimal.valueOf(principal+interest).setScale(2, BigDecimal.ROUND_HALF_DOWN).doubleValue();
                    Timestamp date=calculationBasics.nextMonth(requestObject.getStartDate(),monthIndex);
                    double _roundedInitialOutstandingPrincipal=
                            (requestObject.getLoanAmount()>principal)
                                    ? BigDecimal.valueOf(requestObject.getLoanAmount()).setScale(2,BigDecimal.ROUND_HALF_DOWN).doubleValue():principal;
                    ResponseObject responseObject=generatePayLoudObjectInterface.responseObject(requestObject,monthIndex);

                    /**
                     * check true initialization, each calculated value should be set to valid field,
                     * for example if you set principal to borrowerPaymentAmount this Test return fail .
                     */
                    Assert.assertTrue(responseObject.getBorrowerPaymentAmount().equals(borrowerPaymentAmount));
                    Assert.assertTrue(responseObject.getRemainingOutstandingPrincipals().equals(_roundedInitialOutstandingPrincipal-principal));
                    Assert.assertTrue(responseObject.getDate().equals(date));
                    Assert.assertTrue(responseObject.getInitialOutstandingPrincipal().equals(_roundedInitialOutstandingPrincipal));
                    Assert.assertTrue(responseObject.getInterest().equals(interest));
                    Assert.assertTrue(responseObject.getPrincipal().equals(principal));

                    /**
                     * check last month installment's remainingOutstandingPrincipals equal to 0.0
                     */
                    if(monthIndex.equals(requestObject.getDuration()-1))
                        Assert.assertTrue(responseObject.getRemainingOutstandingPrincipals().equals(0.0));

                    /**
                     * check if principal exceed initialOutstandingPrincipal, then principal should be set as initialOutstandingPrincipal
                     */
                    if(principal>responseObject.getInitialOutstandingPrincipal())
                        Assert.assertSame(principal,responseObject.getInitialOutstandingPrincipal());

                    /**
                     * set new remainingOutstandingPrincipals to requestObject
                     */
                    requestObject.setLoanAmount(responseObject.getRemainingOutstandingPrincipals());

                });
    }

    /**
     * check All calculated basics, for first month
     * because of , base on Annuity formula i first month return valid calculations , it repeated for all month
     * and because of in this {@link #responseObjectInitializationTest()} we check last month remainingOutstandingPrincipal,
     * with these two Test we can guaranty calculated data validity
     */
    @Test
    public void dataCalculationTest(){
        /**
         * All element calculated for first month
         */
            double ratePerMonth=requestObject.getNominalRate()/12/100;
            double calculatedValue=(requestObject.getLoanAmount()*(ratePerMonth))/(1-(Math.pow((1+ratePerMonth),-(requestObject.getDuration()-0))));
            double principal=calculationBasics.principalCalculation(requestObject, 0);
            double interest = calculationBasics.interestCalulation(requestObject);
            double borrowerPaymentAmount= BigDecimal.valueOf(principal+interest).setScale(2, BigDecimal.ROUND_HALF_DOWN).doubleValue();
            double annuity=BigDecimal.valueOf(calculatedValue).setScale(2,BigDecimal.ROUND_HALF_EVEN).doubleValue();
            Timestamp date=calculationBasics.nextMonth(requestObject.getStartDate(),0);
        /**
         * check calculated data base on requestObject initialization
         */
        Assert.assertEquals(borrowerPaymentAmount,219.36,0.0);
                Assert.assertEquals(date,Timestamp.valueOf("2018-01-01 00:00:00.0"));
                Assert.assertEquals(principal,198.53,0.0);
                Assert.assertEquals(interest,20.83,0.0);
                Assert.assertEquals(annuity,219.36,0.0);


    }

}
