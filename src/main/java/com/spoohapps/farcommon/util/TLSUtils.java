package com.spoohapps.farcommon.util;

import javax.xml.bind.DatatypeConverter;
import java.io.ByteArrayInputStream;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;

public class TLSUtils {

    public static X509Certificate certificateFrom(String pemContents) throws CertificateException {
        byte[] certificateBytes = parseDERFromPEM(pemContents);
        return generateCertificateFromDER(certificateBytes);
    }

    public static RSAPrivateKey privateKeyFrom(String pkcs8pemContents) throws InvalidKeySpecException, NoSuchAlgorithmException {
        byte[] keyBytes = parseDERFromPEM(pkcs8pemContents);
        return generatePrivateKeyFromDER(keyBytes);
    }

    private static byte[] parseDERFromPEM(String section) {
        String data = section.substring(section.indexOf(lineSeparator)+1, section.lastIndexOf(lineSeparator));
        data = data.substring(0, data.lastIndexOf(lineSeparator)+1);
        return DatatypeConverter.parseBase64Binary(data);
    }

    private static RSAPrivateKey generatePrivateKeyFromDER(byte[] keyBytes) throws InvalidKeySpecException, NoSuchAlgorithmException {
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);

        KeyFactory factory = KeyFactory.getInstance("RSA");

        return (RSAPrivateKey)factory.generatePrivate(spec);
    }

    private static X509Certificate generateCertificateFromDER(byte[] certBytes) throws CertificateException {
        CertificateFactory factory = CertificateFactory.getInstance("X.509");

        return (X509Certificate)factory.generateCertificate(new ByteArrayInputStream(certBytes));
    }

    private static final String lineSeparator = System.getProperty("line.separator");
}
