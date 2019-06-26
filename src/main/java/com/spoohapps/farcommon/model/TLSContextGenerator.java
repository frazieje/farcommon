package com.spoohapps.farcommon.model;

public interface TLSContextGenerator {

    TLSContext caTlsContext(int keyBits, String commonName, long validForSeconds) throws TLSContextException;

    TLSContext signedClientTlsContext(int keyBits, String commonName, long validForSeconds, TLSContext caContext) throws TLSContextException;

    TLSContext signedServerTlsContext(int keyBits, String commonName, long validForSeconds, TLSContext caContext) throws TLSContextException;
}

