package org.hzero.sample.lock;

import org.hzero.sample.lock.service.impl.LengthValidator;
import org.hzero.sample.lock.service.impl.NotNullValidator;
import org.hzero.sample.lock.service.impl.ValidationService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import javax.annotation.PostConstruct;

@SpringBootApplication
public class HzeroSampleLockApplication {

    public static void main(String[] args) {
        SpringApplication.run(HzeroSampleLockApplication.class, args);
    }


}
