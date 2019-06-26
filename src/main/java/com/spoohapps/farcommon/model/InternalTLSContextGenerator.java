package com.spoohapps.farcommon.model;

import sun.security.util.ObjectIdentifier;
import sun.security.x509.*;

import java.math.BigInteger;
import java.security.*;

import java.security.interfaces.RSAPrivateKey;
import java.util.Date;
import java.util.Vector;

@SuppressWarnings("sunapi")
public class InternalTLSContextGenerator implements TLSContextGenerator {

    private final String keyType;
    private final String sigAlg;
    private final String providerName;

    private static final int[] clientAuthOidData = new int[] {1, 3, 6, 1, 5, 5, 7, 3, 2};

    private static final int[] serverAuthOidData = new int[] {1, 3, 6, 1, 5, 5, 7, 3, 1};

    private static final int[] clientAuthExtensionData = new int[] {48, 10, 6, 8, 43, 6, 1, 5, 5, 7, 3, 2};

    public InternalTLSContextGenerator(String keyType, String sigAlg, String providerName) {
        this.keyType = keyType;
        this.sigAlg = sigAlg;
        this.providerName = providerName;
    }

    @Override
    public TLSContext caTlsContext(int keyBits, String commonName, long validForSeconds) throws TLSContextException {

        try {

            X500Name distinguishedName = new X500Name("CN=" + commonName);

            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(keyType);

            keyPairGenerator.initialize(keyBits);

            KeyPair keypair = keyPairGenerator.generateKeyPair();

            AlgorithmId algorithmId = new AlgorithmId(AlgorithmId.sha256WithRSAEncryption_oid);

            RSAPrivateKey privateKey = (RSAPrivateKey) keypair.getPrivate();

            X509CertInfo certInfo = new X509CertInfo();

            Date notBefore = new Date();
            Date notAfter = new Date(notBefore.getTime() + (validForSeconds * 1000));

            BigInteger sn = new BigInteger(64, new SecureRandom());

            certInfo.set(X509CertInfo.SERIAL_NUMBER, new CertificateSerialNumber(sn));

            certInfo.set(X509CertInfo.VALIDITY, new CertificateValidity(notBefore, notAfter));

            certInfo.set(X509CertInfo.SUBJECT, distinguishedName);

            certInfo.set(X509CertInfo.ISSUER, distinguishedName);

            certInfo.set(X509CertInfo.KEY, new CertificateX509Key(keypair.getPublic()));

            certInfo.set(X509CertInfo.VERSION, new CertificateVersion(CertificateVersion.V3));

            certInfo.set(X509CertInfo.ALGORITHM_ID, new CertificateAlgorithmId(algorithmId));

            CertificateExtensions extensions = new CertificateExtensions();

            BasicConstraintsExtension basicConstraints = new BasicConstraintsExtension(true, -1);

            extensions.set(BasicConstraintsExtension.NAME, new BasicConstraintsExtension(false, basicConstraints.getValue()));

            KeyUsageExtension keyUsageExtension = new KeyUsageExtension(new byte[8]);

            keyUsageExtension.set(KeyUsageExtension.CRL_SIGN, true);
            keyUsageExtension.set(KeyUsageExtension.KEY_CERTSIGN, true);

            extensions.set(KeyUsageExtension.NAME, new KeyUsageExtension(false, keyUsageExtension.getValue()));

            certInfo.set(X509CertInfo.EXTENSIONS, extensions);

            X509CertImpl outCert = new X509CertImpl(certInfo);

            outCert.sign(privateKey, sigAlg);

            return new TLSContext(outCert, privateKey, outCert);

        } catch (Exception e) {
            throw new TLSContextException(e);
        }
    }

    @Override
    public TLSContext signedClientTlsContext(int keyBits, String commonName, long validForSeconds, TLSContext caContext) throws TLSContextException {

        try {

            X500Name distinguishedName = new X500Name("CN=" + commonName + ",O=client");

            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(keyType);

            keyPairGenerator.initialize(keyBits);

            KeyPair keypair = keyPairGenerator.generateKeyPair();

            AlgorithmId algorithmId = new AlgorithmId(AlgorithmId.sha256WithRSAEncryption_oid);

            RSAPrivateKey privateKey = (RSAPrivateKey) keypair.getPrivate();

            X509CertInfo certInfo = new X509CertInfo();

            Date notBefore = new Date();
            Date notAfter = new Date(notBefore.getTime() + (validForSeconds * 1000));

            BigInteger sn = new BigInteger(64, new SecureRandom());

            certInfo.set(X509CertInfo.SERIAL_NUMBER, new CertificateSerialNumber(sn));

            certInfo.set(X509CertInfo.VALIDITY, new CertificateValidity(notBefore, notAfter));

            certInfo.set(X509CertInfo.SUBJECT, distinguishedName);

            certInfo.set(X509CertInfo.ISSUER, caContext.getCertificate().getSubjectDN());

            certInfo.set(X509CertInfo.KEY, new CertificateX509Key(keypair.getPublic()));

            certInfo.set(X509CertInfo.VERSION, new CertificateVersion(CertificateVersion.V3));

            certInfo.set(X509CertInfo.ALGORITHM_ID, new CertificateAlgorithmId(algorithmId));

            CertificateExtensions extensions = new CertificateExtensions();

            BasicConstraintsExtension basicConstraints = new BasicConstraintsExtension(false, -1);

            extensions.set(BasicConstraintsExtension.NAME, new BasicConstraintsExtension(true, basicConstraints.getValue()));

            KeyUsageExtension keyUsageExtension = new KeyUsageExtension(new byte[8]);

            keyUsageExtension.set(KeyUsageExtension.DIGITAL_SIGNATURE, true);
            keyUsageExtension.set(KeyUsageExtension.KEY_ENCIPHERMENT, true);

            extensions.set(KeyUsageExtension.NAME, new KeyUsageExtension(true, keyUsageExtension.getValue()));

            Vector<ObjectIdentifier> oids = new Vector<>();
            oids.add(new ObjectIdentifier(clientAuthOidData));

            ExtendedKeyUsageExtension extendedKeyUsageExtension = new ExtendedKeyUsageExtension(true, oids);

            extensions.set(ExtendedKeyUsageExtension.NAME, new ExtendedKeyUsageExtension(true, extendedKeyUsageExtension.getValue()));

            certInfo.set(X509CertInfo.EXTENSIONS, extensions);

            X509CertImpl outCert = new X509CertImpl(certInfo);

            outCert.sign(caContext.getPrivateKey(), caContext.getCertificate().getSigAlgName());

            return new TLSContext(outCert, privateKey, caContext.getCertificate());

        } catch (Exception e) {
            throw new TLSContextException(e);
        }
    }

    @Override
    public TLSContext signedServerTlsContext(int keyBits, String commonName, long validForSeconds, TLSContext caContext) throws TLSContextException {

        try {

            X500Name distinguishedName = new X500Name("CN=" + commonName + ",O=server");

            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(keyType);

            keyPairGenerator.initialize(keyBits);

            KeyPair keypair = keyPairGenerator.generateKeyPair();

            AlgorithmId algorithmId = new AlgorithmId(AlgorithmId.sha256WithRSAEncryption_oid);

            RSAPrivateKey privateKey = (RSAPrivateKey) keypair.getPrivate();

            X509CertInfo certInfo = new X509CertInfo();

            Date notBefore = new Date();
            Date notAfter = new Date(notBefore.getTime() + (validForSeconds * 1000));

            BigInteger sn = new BigInteger(64, new SecureRandom());

            certInfo.set(X509CertInfo.SERIAL_NUMBER, new CertificateSerialNumber(sn));

            certInfo.set(X509CertInfo.VALIDITY, new CertificateValidity(notBefore, notAfter));

            certInfo.set(X509CertInfo.SUBJECT, distinguishedName);

            certInfo.set(X509CertInfo.ISSUER, caContext.getCertificate().getSubjectDN());

            certInfo.set(X509CertInfo.KEY, new CertificateX509Key(keypair.getPublic()));

            certInfo.set(X509CertInfo.VERSION, new CertificateVersion(CertificateVersion.V3));

            certInfo.set(X509CertInfo.ALGORITHM_ID, new CertificateAlgorithmId(algorithmId));

            CertificateExtensions extensions = new CertificateExtensions();

            BasicConstraintsExtension basicConstraints = new BasicConstraintsExtension(false, -1);

            extensions.set(BasicConstraintsExtension.NAME, new BasicConstraintsExtension(true, basicConstraints.getValue()));

            KeyUsageExtension keyUsageExtension = new KeyUsageExtension(new byte[8]);

            keyUsageExtension.set(KeyUsageExtension.DIGITAL_SIGNATURE, true);
            keyUsageExtension.set(KeyUsageExtension.KEY_ENCIPHERMENT, true);

            extensions.set(KeyUsageExtension.NAME, new KeyUsageExtension(true, keyUsageExtension.getValue()));

            Vector<ObjectIdentifier> oids = new Vector<>();
            oids.add(new ObjectIdentifier(serverAuthOidData));

            ExtendedKeyUsageExtension extendedKeyUsageExtension = new ExtendedKeyUsageExtension(true, oids);

            extensions.set(ExtendedKeyUsageExtension.NAME, new ExtendedKeyUsageExtension(true, extendedKeyUsageExtension.getValue()));

            certInfo.set(X509CertInfo.EXTENSIONS, extensions);

            X509CertImpl outCert = new X509CertImpl(certInfo);

            outCert.sign(caContext.getPrivateKey(), caContext.getCertificate().getSigAlgName());

            return new TLSContext(outCert, privateKey, caContext.getCertificate());

        } catch (Exception e) {
            throw new TLSContextException(e);
        }
    }

}
