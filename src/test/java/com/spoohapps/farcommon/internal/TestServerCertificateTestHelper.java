package com.spoohapps.farcommon.internal;

import com.spoohapps.farcommon.testhelpers.ProfileFileHelper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.InvalidKeySpecException;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TestServerCertificateTestHelper {

    // see https://docs.oracle.com/javase/8/docs/api/java/security/cert/X509Certificate.html#getKeyUsage--
    private static final int KEY_USAGE_CERT_SIGING = 5;

    private static final String TLS_WEB_SERVER_AUTHENTICATION = "1.3.6.1.5.5.7.3.1";

    @Test
    public void shouldCreateServerCertificate() throws CertificateException {
        X509Certificate serverCert = ProfileFileHelper.serverCertificate();
        serverCert.checkValidity();
        assertTrue(serverCert.getExtendedKeyUsage().contains(TLS_WEB_SERVER_AUTHENTICATION));
    }

    @Test
    public void shouldCreateServerCaCertificate() throws CertificateException {
        X509Certificate serverCaCert = ProfileFileHelper.serverCaCertificate();
        serverCaCert.checkValidity();
        assertTrue(serverCaCert.getKeyUsage()[KEY_USAGE_CERT_SIGING]);
    }

    @Test void shouldCreateServerPrivateKey() throws InvalidKeySpecException, NoSuchAlgorithmException {
        RSAPrivateKey serverKey = ProfileFileHelper.serverPrivateKey();
        assertNotNull(serverKey);
    }

}
