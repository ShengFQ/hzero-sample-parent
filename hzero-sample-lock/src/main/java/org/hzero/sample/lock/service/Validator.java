package org.hzero.sample.lock.service;

public interface Validator {
    void setNext(Validator validator);
    boolean validate(String input);
}
