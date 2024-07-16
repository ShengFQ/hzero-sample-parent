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


    @Bean
    public ValidationService validationService() {
        ValidationService service = new ValidationService();
        NotNullValidator notNullValidator = new NotNullValidator();
        LengthValidator lengthValidator = new LengthValidator(5, 10);

        notNullValidator.setNext(lengthValidator);
        service.setValidatorChain(notNullValidator);
        return service;
    }

    @PostConstruct
    public void testValidationService() {
        ValidationService service = validationService();
        boolean isValid = service.validate("test"); // This will pass
        isValid = service.validate(""); // This will fail due to NotNullValidator
        isValid = service.validate("thisisaverylongstringthatdoesnotfitthelengthrequirement"); // This will fail due to LengthValidator
    }

}
