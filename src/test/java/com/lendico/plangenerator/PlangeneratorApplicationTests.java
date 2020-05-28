package com.lendico.plangenerator;

import com.lendico.plangenerator.web.GeneratePlanController;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class PlangeneratorApplicationTests extends AbstractTestClass {

    @Autowired
    private GeneratePlanController generatePlanController;

    @Test
    void contextLoads() {
        Assert.assertNotNull(generatePlanController);
    }

}
