package org.hzero.sample.lock.service.impl;

import org.hzero.sample.lock.service.AbsValidator;
import org.hzero.sample.lock.service.Validator;
import org.springframework.stereotype.Component;

/**
 * 传统java原生责任链
 * */
public class NotNullValidator extends AbsValidator {
    private Validator next;

    @Override
    public void setNext(Validator validator) {
        this.next = validator;
    }

    @Override
    public boolean validate(String input) {
        if (input == null || input.trim().isEmpty()) {
            System.out.println("Input cannot be null or empty.");
            return false;
        }
        if (next != null) {
            next.validate(input);
        }
        return true;
    }
}
