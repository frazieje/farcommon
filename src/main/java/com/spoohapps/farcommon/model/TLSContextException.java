package com.spoohapps.farcommon.model;

public class TLSContextException extends Exception {
    public TLSContextException(Exception inner) {
        super(inner);
    }
}
