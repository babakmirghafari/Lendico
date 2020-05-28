package com.lendico.plangenerator.web;

import com.lendico.plangenerator.model.RequestObject;
import com.lendico.plangenerator.model.ResponseObject;
import com.lendico.plangenerator.service.GeneratePayLoudObjectInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/generate-plan")
public class GeneratePlanController {

    @Autowired
    private GeneratePayLoudObjectInterface generatePayLoudObjectInterface;

    @PostMapping
    public Collection<ResponseObject> generateplan(@RequestBody RequestObject requestObejct) {
        return generatePayLoudObjectInterface.generateResponsePayLoad(requestObejct);
    }
}
