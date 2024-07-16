package org.hzero.sample.lock.service.impl;

import org.hzero.sample.lock.service.Validator;
import org.springframework.stereotype.Service;

@Service
public class ValidationService {
    private Validator validatorChain;

    public void setValidatorChain(Validator validatorChain) {
        this.validatorChain = validatorChain;
    }

    public boolean validate(String input) {
        return validatorChain.validate(input);
    }
}
