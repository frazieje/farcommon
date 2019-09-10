package com.spoohapps.farcommon.model;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;
import java.util.Base64;
import java.util.Calendar;

import static org.junit.jupiter.api.Assertions.*;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Disabled
public class CertificateGenerationTests {

    private TLSContextGenerator generator;

    private TLSContext caCertContext;

    private TLSContext clientContext;

    private String expectedCaCommonName;
    private String expectedCaOrganization;
    private int expectedCaKeyBits;
    private int expectedCaCertValidForSeconds;
    private String expectedCaKeyType;
    private String expectedSignatureAlgorithm;
    private long expectedCaCertificateExpirationTimeInMillis;
    private long expectedCaCertificateStartTimeInMillis;
    private int expectedClientCertValidForSeconds;
    private int expectedClientKeyBits;
    private String expectedClientCommonName;

    @BeforeAll
    public void setup() throws TLSContextException {

        expectedCaKeyType = "RSA";

        expectedSignatureAlgorithm = "SHA256withRSA";

        generator = new InternalTLSContextGenerator(expectedCaKeyType, expectedSignatureAlgorithm, null);

        expectedCaCommonName = "SpoohappsGatewayMQCA";

        expectedCaOrganization = null;

        expectedCaKeyBits = 4096;

        expectedCaCertValidForSeconds = 24 * 60 * 60;

        expectedClientCertValidForSeconds = 12 * 60 * 60;

        caCertContext = generator.caTlsContext(expectedCaKeyBits, expectedCaCommonName, expectedCaCertValidForSeconds);

        Calendar c = Calendar.getInstance();

        expectedCaCertificateStartTimeInMillis = c.getTimeInMillis();

        c.add(Calendar.SECOND, expectedCaCertValidForSeconds);

        expectedCaCertificateExpirationTimeInMillis = c.getTimeInMillis();

        expectedClientKeyBits = 2048;
        expectedClientCommonName = "someClient@domain.com";
        clientContext = generator.signedClientTlsContext(expectedClientKeyBits, expectedClientCommonName, expectedClientCertValidForSeconds, caCertContext);



    }

    @Test
    public void shouldGenerateCaCertificate() throws IOException, CertificateEncodingException {
        assertNotNull(caCertContext.getCertificate());
    }

    @Test
    public void shouldGenerateCaPrivateKey() {
        assertNotNull(caCertContext.getPrivateKey());
    }

    @Test
    public void shouldGenerateCaCaCertificate() {
        assertNotNull(caCertContext.getCaCertificate());
    }

    @Test
    public void shouldGenerateSelfSignedRootCaCertificate() {
        assertEquals(caCertContext.getCertificate(), caCertContext.getCaCertificate());
    }

    @Test
    public void shouldGenerateCaPrivateKeyWithCorrectAlgorithm() {
        assertEquals(expectedCaKeyType, caCertContext.getPrivateKey().getAlgorithm());
    }

    @Test
    public void shouldGenerateCaPrivateKeyOfCorrectLength() {
        assertEquals(expectedCaKeyBits, caCertContext.getPrivateKey().getModulus().bitLength());
    }

    @Test
    public void shouldGenerateCaCertificateWithCorrectSigningAlgorithm() {
        assertEquals(expectedSignatureAlgorithm, caCertContext.getCertificate().getSigAlgName());
    }

    @Test
    public void shouldGenerateCaCertificateWithCorrectCommonName() {

        String[] dnParts = caCertContext.getCertificate().getSubjectDN().getName().split(",");

        String actualCN = null;

        for (String part : dnParts) {
            if (part.startsWith("CN=")) {
                actualCN = part.substring(3);
            }
        }

        assertEquals(expectedCaCommonName, actualCN);
    }

    @Test
    public void shouldGenerateCaCertificateWithCorrectOrganizationalUnit() {

        String[] dnParts = caCertContext.getCertificate().getSubjectDN().getName().split(",");

        String actualCN = null;

        for (String part : dnParts) {
            if (part.startsWith("O=")) {
                actualCN = part.substring(2);
            }
        }

        assertEquals(expectedCaOrganization, actualCN);
    }

    @Test
    public void shouldGenerateCaCertificateWithCorrectExpirationTime() {
        long timeDiffMillis =
                Math.abs(expectedCaCertificateExpirationTimeInMillis - caCertContext.getCertificate().getNotAfter().getTime());

        assertTrue(timeDiffMillis <= 5000);
    }

    @Test
    public void shouldGenerateCaCertificateWithCorrectStartTime() {
        long timeDiffMillis =
                Math.abs(expectedCaCertificateStartTimeInMillis - caCertContext.getCertificate().getNotBefore().getTime());

        assertTrue(timeDiffMillis <= 5000);
    }

    @Test
    public void shouldGenerateCaCertificateWithBasicConstraints() throws IOException, CertificateEncodingException {
        writeToPemFile(caCertContext.getCertificate());

        int test = caCertContext.getCertificate().getBasicConstraints();

    }








    @Test
    public void shouldGenerateSignedClientCertificate() throws IOException, CertificateEncodingException {
        assertNotNull(clientContext.getCertificate());
        writeToPemFile(clientContext.getCertificate());
    }

    @Test
    public void shouldGenerateClientPrivateKey() {
        assertNotNull(clientContext.getPrivateKey());
    }

    @Test
    public void shouldGenerateClientCaCertificate() {
        assertNotNull(clientContext.getCaCertificate());
    }

    @Test
    public void shouldGenerateClientCertSignedWithCaCertificate() {
        assertEquals(clientContext.getCaCertificate(), caCertContext.getCertificate());
    }




    private void writeToPemFile(X509Certificate cert) throws IOException, CertificateEncodingException {
        StringBuilder sb = new StringBuilder();

        String lineSeparator = System.getProperty("line.separator");

        Base64.Encoder encoder = Base64.getMimeEncoder(64, lineSeparator.getBytes(StandardCharsets.UTF_8));

        sb.append("-----BEGIN CERTIFICATE-----").append(lineSeparator);
        sb.append(encoder.encodeToString(cert.getEncoded())).append(lineSeparator);
        sb.append("-----END CERTIFICATE-----").append(lineSeparator);

        String writePath = "~/testcert.pem";

        Files.write(Paths.get(writePath.replaceFirst("^~", System.getProperty("user.home"))), sb.toString().getBytes(StandardCharsets.UTF_8));

        assertNotNull(cert);
    }
}
