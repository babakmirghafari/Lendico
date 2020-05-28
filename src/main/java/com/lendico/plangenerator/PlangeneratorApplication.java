package com.lendico.plangenerator;

import com.lendico.plangenerator.service.GeneratePayLoudObjectInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PlangeneratorApplication {

    public static void main(String[] args) {
        SpringApplication.run(PlangeneratorApplication.class, args);
    }

}
