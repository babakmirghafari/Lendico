package com.lendico.plangenerator;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lendico.plangenerator.model.RequestObject;
import com.lendico.plangenerator.service.GeneratePayLoudObjectInterface;
import com.lendico.plangenerator.service.calculationbasics.CalculationBasics;
import com.lendico.plangenerator.web.GeneratePlanController;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.sql.Timestamp;

import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

/**
 * All share needed objects and configuration you can find here,
 * each Test class need this share objects and configuration just extend from this abstract class
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class AbstractTestClass {

    MockMvc mockMvc;

    @Autowired
    protected GeneratePlanController generatePlanController;

    @Autowired
    protected GeneratePayLoudObjectInterface generatePayLoudObjectInterface;

    @Autowired
    protected CalculationBasics calculationBasics;

    static final String requestUrl="http://localhost:8080/generate-plan";
    RequestObject requestObject;

    @Before
    protected void setUp() throws Exception {
        this.mockMvc = standaloneSetup(this.generatePlanController).build();
        requestObject=new RequestObject();
        requestObject.setStartDate(Timestamp.valueOf("2018-01-01 00:00:00"));
        requestObject.setNominalRate(Float.valueOf("5.0"));
        requestObject.setDuration(24);
        requestObject.setLoanAmount(5000.00);
    }

    protected  static String writeObjectAsJasonString(final RequestObject requestObject) {
        try {
            return new ObjectMapper().writeValueAsString(requestObject);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
