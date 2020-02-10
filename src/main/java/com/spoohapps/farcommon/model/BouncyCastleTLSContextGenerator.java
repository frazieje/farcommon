package com.spoohapps.farcommon.model;

import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x509.BasicConstraints;
import org.bouncycastle.asn1.x509.ExtendedKeyUsage;
import org.bouncycastle.asn1.x509.KeyPurposeId;
import org.bouncycastle.asn1.x509.KeyUsage;
import org.bouncycastle.cert.CertIOException;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;

import java.math.BigInteger;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPrivateKey;
import java.util.Date;

public class BouncyCastleTLSContextGenerator implements TLSContextGenerator {

    private final String keyType;
    private final String sigAlg;

    private static final int[] clientAuthOidData = new int[] {1, 3, 6, 1, 5, 5, 7, 3, 2};

    private static final int[] serverAuthOidData = new int[] {1, 3, 6, 1, 5, 5, 7, 3, 1};

    private static final int[] clientAuthExtensionData = new int[] {48, 10, 6, 8, 43, 6, 1, 5, 5, 7, 3, 2};

    public BouncyCastleTLSContextGenerator(String keyType, String sigAlg) {
        this.keyType = keyType;
        this.sigAlg = sigAlg;
    }

    @Override
    public TLSContext caTlsContext(int keyBits, String commonName, long validForSeconds) throws TLSContextException {

        try {

            Provider bcProvider = new BouncyCastleProvider();
            Security.addProvider(bcProvider);

            long now = System.currentTimeMillis();

            Date notBefore = new Date(now);
            Date notAfter = new Date(notBefore.getTime() + (validForSeconds * 1000));

            X500Name dnName = new X500Name("CN=" + commonName);

            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(keyType);

            keyPairGenerator.initialize(keyBits);

            KeyPair keyPair = keyPairGenerator.generateKeyPair();

            RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();

            BigInteger sn = new BigInteger(160, new SecureRandom());

            ContentSigner contentSigner = new JcaContentSignerBuilder(sigAlg).build(privateKey);

            JcaX509v3CertificateBuilder certBuilder = new JcaX509v3CertificateBuilder(dnName, sn, notBefore, notAfter, dnName, keyPair.getPublic());

            BasicConstraints basicConstraints = new BasicConstraints(true); // <-- true for CA, false for EndEntity

            certBuilder.addExtension(new ASN1ObjectIdentifier("2.5.29.19"), false, basicConstraints);

            KeyUsage keyUsage = new KeyUsage(KeyUsage.keyCertSign | KeyUsage.cRLSign);

            certBuilder.addExtension(new ASN1ObjectIdentifier("2.5.29.15"), false, keyUsage);

            X509Certificate outCert = new JcaX509CertificateConverter().setProvider(bcProvider).getCertificate(certBuilder.build(contentSigner));

            return new TLSContext(outCert, privateKey, outCert);

        } catch (CertificateException | OperatorCreationException | NoSuchAlgorithmException | CertIOException e) {
            throw new TLSContextException(e);
        }
    }

    @Override
    public TLSContext signedClientTlsContext(int keyBits, String commonName, long validForSeconds, TLSContext caContext) throws TLSContextException {

        try {

            Provider bcProvider = new BouncyCastleProvider();
            Security.addProvider(bcProvider);

            long now = System.currentTimeMillis();

            Date notBefore = new Date(now);
            Date notAfter = new Date(notBefore.getTime() + (validForSeconds * 1000));

            X500Name dnName = new X500Name("CN=" + commonName + ",O=client");

            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(keyType);

            keyPairGenerator.initialize(keyBits);

            KeyPair keyPair = keyPairGenerator.generateKeyPair();

            RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();

            BigInteger sn = new BigInteger(160, new SecureRandom());

            ContentSigner contentSigner = new JcaContentSignerBuilder(caContext.getCertificate().getSigAlgName()).build(caContext.getPrivateKey());

            X500Name caName = new X500Name(caContext.getCertificate().getSubjectDN().getName());

            JcaX509v3CertificateBuilder certBuilder = new JcaX509v3CertificateBuilder(caName, sn, notBefore, notAfter, dnName, keyPair.getPublic());

            BasicConstraints basicConstraints = new BasicConstraints(true); // <-- true for CA, false for EndEntity

            certBuilder.addExtension(new ASN1ObjectIdentifier("2.5.29.19"), false, basicConstraints);

            KeyUsage keyUsage = new KeyUsage(KeyUsage.digitalSignature | KeyUsage.keyEncipherment);

            certBuilder.addExtension(new ASN1ObjectIdentifier("2.5.29.15"), false, keyUsage);

            ExtendedKeyUsage extendedKeyUsage = new ExtendedKeyUsage(KeyPurposeId.id_kp_clientAuth);

            certBuilder.addExtension(new ASN1ObjectIdentifier("2.5.29.37"), false, extendedKeyUsage);

            X509Certificate outCert = new JcaX509CertificateConverter().setProvider(bcProvider).getCertificate(certBuilder.build(contentSigner));

            return new TLSContext(outCert, privateKey, caContext.getCertificate());

        } catch (OperatorCreationException | NoSuchAlgorithmException | CertIOException | CertificateException e) {
            throw new TLSContextException(e);
        }
    }

    @Override
    public TLSContext signedServerTlsContext(int keyBits, String commonName, long validForSeconds, TLSContext caContext) throws TLSContextException {
        try {

            Provider bcProvider = new BouncyCastleProvider();
            Security.addProvider(bcProvider);

            long now = System.currentTimeMillis();

            Date notBefore = new Date(now);
            Date notAfter = new Date(notBefore.getTime() + (validForSeconds * 1000));

            X500Name dnName = new X500Name("CN=" + commonName + ",O=server");

            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(keyType);

            keyPairGenerator.initialize(keyBits);

            KeyPair keyPair = keyPairGenerator.generateKeyPair();

            RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();

            BigInteger sn = new BigInteger(160, new SecureRandom());

            ContentSigner contentSigner = new JcaContentSignerBuilder(caContext.getCertificate().getSigAlgName()).build(caContext.getPrivateKey());

            X500Name caName = new X500Name(caContext.getCertificate().getSubjectDN().getName());

            JcaX509v3CertificateBuilder certBuilder = new JcaX509v3CertificateBuilder(caName, sn, notBefore, notAfter, dnName, keyPair.getPublic());

            BasicConstraints basicConstraints = new BasicConstraints(true); // <-- true for CA, false for EndEntity

            certBuilder.addExtension(new ASN1ObjectIdentifier("2.5.29.19"), false, basicConstraints);

            KeyUsage keyUsage = new KeyUsage(KeyUsage.digitalSignature | KeyUsage.keyEncipherment);

            certBuilder.addExtension(new ASN1ObjectIdentifier("2.5.29.15"), false, keyUsage);

            ExtendedKeyUsage extendedKeyUsage = new ExtendedKeyUsage(KeyPurposeId.id_kp_serverAuth);

            certBuilder.addExtension(new ASN1ObjectIdentifier("2.5.29.37"), false, extendedKeyUsage);

            X509Certificate outCert = new JcaX509CertificateConverter().setProvider(bcProvider).getCertificate(certBuilder.build(contentSigner));

            return new TLSContext(outCert, privateKey, caContext.getCertificate());

        } catch (OperatorCreationException | NoSuchAlgorithmException | CertIOException | CertificateException e) {
            throw new TLSContextException(e);
        }
    }

}
