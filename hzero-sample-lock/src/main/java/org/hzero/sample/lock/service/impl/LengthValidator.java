package org.hzero.sample.lock.service.impl;

import org.hzero.sample.lock.service.Validator;

public class LengthValidator implements Validator {
    private int min;
    private int max;
    private Validator next;

    public LengthValidator(int min, int max) {
        this.min = min;
        this.max = max;
    }

    @Override
    public void setNext(Validator validator) {
        this.next = validator;
    }

    @Override
    public boolean validate(String input) {
        if (input.length() < min || input.length() > max) {
            System.out.println("Input length must be between " + min + " and " + max + ".");
            return false;
        }
        if (next != null) {
            next.validate(input);
        }
        return true;
    }
}
