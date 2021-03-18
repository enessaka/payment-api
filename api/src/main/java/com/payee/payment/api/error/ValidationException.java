package com.payee.payment.api.error;



public class ValidationException extends Exception {

    public ValidationException(String s){
        super(s);
    }

    public ValidationException(Exception s){
        super(s);
    }

}
