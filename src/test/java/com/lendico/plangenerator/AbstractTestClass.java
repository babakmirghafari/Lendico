package com.lendico.plangenerator;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lendico.plangenerator.model.RequestObject;
import com.lendico.plangenerator.web.GeneratePlanController;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.sql.Timestamp;

import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@SpringBootTest
@RunWith(SpringRunner.class)
public class AbstractTestClass {

    MockMvc mockMvc;

    @Autowired
    GeneratePlanController generatePlanController;

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
