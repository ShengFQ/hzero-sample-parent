package org.hzero.sample.lock.service;

/**
 * ClassName: AbsValidator
 * Description: 抽象类
 *
 * @author shengfq
 * @date: 2024/7/16 10:43 下午
 */
public abstract class AbsValidator implements Validator{
    protected Validator validator;
    @Override
    public void setNext(Validator validator) {
        this.validator=validator;
    }

    @Override
    public abstract boolean validate(String input) ;
}
