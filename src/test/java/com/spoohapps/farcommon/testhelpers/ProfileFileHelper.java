package com.spoohapps.farcommon.testhelpers;

import com.spoohapps.farcommon.model.Profile;
import com.spoohapps.farcommon.util.TLSUtils;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.util.function.Consumer;

public class ProfileFileHelper {

    public static void writeFileContents(Path filePath, Profile profile) {
        try {
            byte[] bytes = profile.toByteArray();
            Files.write(filePath, bytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Profile getFileContents(Path filePath) {
        try (InputStream fileStream = Files.newInputStream(filePath)){
            return Profile.from(fileStream);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        return null;
    }

    public static void deleteFile(Path filePath) {
        try {
            Files.delete(filePath);
        } catch (IOException ioe) {
        }
    }

    public static X509Certificate certifcateFromPem(Path pemCertPath) throws IOException, CertificateException {
        String fileContents = new String(Files.readAllBytes(pemCertPath), StandardCharsets.UTF_8);
        return TLSUtils.certificateFrom(fileContents);
    }

    public static RSAPrivateKey privateKeyFromPem(Path pemKeyPath) throws InvalidKeySpecException, NoSuchAlgorithmException, IOException {
        String fileContents = new String(Files.readAllBytes(pemKeyPath), StandardCharsets.UTF_8);
        return TLSUtils.privateKeyFrom(fileContents);
    }

    public static InputStream streamWithProfileId(String profileId) {
        return streamOf(getProfileIdContents(profileId));
    }

    public static InputStream streamWithProfileIdAndNode(String profileId) {
        return streamOf(getProfileIdContents(profileId) + getNodeContents());
    }

    public static InputStream streamWithProfileIdNodeAndMessagingRemote(String profileId) {
        return streamOf(getProfileIdContents(profileId) + getNodeContents() + getMessagingRemoteContents());
    }

    public static InputStream streamWithProfileIdNodeAndMessagingAndAuthRemotes(String profileId) {
        return streamOf(getProfileIdContents(profileId) + getNodeContents() + getMessagingRemoteContents() + getAuthRemoteContents());
    }

    public static InputStream streamWithNode() {
        return streamOf(getNodeContents());
    }

    public static byte[] bytesWithProfileId(String profileId) {
        return byteArrayOf(getProfileIdContents(profileId));
    }

    public static byte[] bytesWithProfileIdAndNode(String profileId) {
        return byteArrayOf(getProfileIdContents(profileId) + getNodeContents());
    }

    public static byte[] bytesWithProfileIdNodeAndRemote(String profileId) {
        return byteArrayOf(getProfileIdContents(profileId) + getNodeContents() + getMessagingRemoteContents());
    }

    public static byte[] bytesWithProfileIdNodeAndMultipleRemotes(String profileId) {
        return byteArrayOf(getProfileIdContents(profileId) + getNodeContents() + getMessagingRemoteContents() + getAuthRemoteContents());
    }

    public static RSAPrivateKey nodePrivateKey() throws InvalidKeySpecException, NoSuchAlgorithmException {
        return TLSUtils.privateKeyFrom(getNodePrivateKeyContents());
    }

    public static X509Certificate nodeCertificate() throws CertificateException {
        return TLSUtils.certificateFrom(getNodeCertificateContents());
    }

    public static X509Certificate nodeCaCertificate() throws CertificateException {
        return TLSUtils.certificateFrom(getCaCertificateContents());
    }

    public static RSAPrivateKey remoteMessagingPrivateKey() throws InvalidKeySpecException, NoSuchAlgorithmException {
        return TLSUtils.privateKeyFrom(getRemoteMessagingPrivateKeyContents());
    }

    public static X509Certificate remoteMessagingCertificate() throws CertificateException {
        X509Certificate cert =  TLSUtils.certificateFrom(getRemoteMessagingCertificateContents());
        return cert;
    }

    public static X509Certificate remoteMessagingCaCertificate() throws CertificateException {
        return TLSUtils.certificateFrom(getCaCertificateContents());
    }

    public static RSAPrivateKey remoteAuthPrivateKey() throws InvalidKeySpecException, NoSuchAlgorithmException {
        return TLSUtils.privateKeyFrom(getNodePrivateKeyContents());
    }

    public static X509Certificate remoteAuthCertificate() throws CertificateException {
        X509Certificate cert =  TLSUtils.certificateFrom(getNodeCertificateContents());
        return cert;
    }

    public static X509Certificate remoteAuthCaCertificate() throws CertificateException {
        return TLSUtils.certificateFrom(getCaCertificateContents());
    }

    public static X509Certificate serverCertificate() throws CertificateException {
        return TLSUtils.certificateFrom(getServerCertificateContents());
    }

    public static X509Certificate serverCaCertificate() throws CertificateException {
        return TLSUtils.certificateFrom(getServerCaCertificateContents());
    }

    public static RSAPrivateKey serverPrivateKey() throws InvalidKeySpecException, NoSuchAlgorithmException {
        return TLSUtils.privateKeyFrom(getServerPrivateKeyContents());
    }

    private static String getProfileIdContents(String profileId) {
        StringBuilder buf = new StringBuilder();
        Consumer<String> w = getWriter(buf);
        w.accept("-----BEGIN PROFILE IDENTIFIER-----");
        w.accept(profileId);
        w.accept("-----END PROFILE IDENTIFIER-----");
        return buf.toString();
    }

    private static String getNodeContents() {
        return getNodeContents("-----BEGIN NODE-----", "-----END NODE-----");
    }

    private static String getMessagingRemoteContents() {
        return getMessagingRemoteContents("-----BEGIN REMOTE MESSAGING-----", "-----END REMOTE MESSAGING-----");
    }

    private static String getAuthRemoteContents() {
        return getNodeContents("-----BEGIN REMOTE AUTH-----", "-----END REMOTE AUTH-----");
    }

    private static String getNodeContents(String startDelimiter, String endDelimiter) {
        StringBuilder buf = new StringBuilder();
        Consumer<String> w = getWriter(buf);
        w.accept(startDelimiter);
        w.accept("-----BEGIN CLIENT CERT AND KEY-----");
        buf.append(getNodePrivateKeyContents());
        buf.append(getNodeCertificateContents());
        w.accept("-----END CLIENT CERT AND KEY-----");
        w.accept("-----BEGIN CA CERT-----");
        buf.append(getCaCertificateContents());
        w.accept("-----END CA CERT-----");
        w.accept(endDelimiter);
        return buf.toString();
    }

    private static String getMessagingRemoteContents(String startDelimiter, String endDelimiter) {
        StringBuilder buf = new StringBuilder();
        Consumer<String> w = getWriter(buf);
        w.accept(startDelimiter);
        w.accept("-----BEGIN CLIENT CERT AND KEY-----");
        buf.append(getRemoteMessagingPrivateKeyContents());
        buf.append(getRemoteMessagingCertificateContents());
        w.accept("-----END CLIENT CERT AND KEY-----");
        w.accept("-----BEGIN CA CERT-----");
        buf.append(getCaCertificateContents());
        w.accept("-----END CA CERT-----");
        w.accept(endDelimiter);
        return buf.toString();
    }

    private static String getNodePrivateKeyContents() {
        StringBuilder buf = new StringBuilder();
        Consumer<String> w = getWriter(buf);
        w.accept("-----BEGIN PRIVATE KEY-----");
        w.accept("MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQDLe6TkLUs4Lgjr");
        w.accept("B2bNU205MvZUOLfoXxe6MHj1VUbxxsY1Wua94az4gSE8nIdWcwCJSQHIT6DUz5Dt");
        w.accept("TBONfbo9C4NN6mKAs0OJDmuGylvnOeFXhySj3uQR2zXl+Cr9Rdhum38Pv8M7Doi8");
        w.accept("Ra3xhz32RU3T8wj9eXyUDFyZCg+ujj8xVDKZEKMRsVxJnpsr5kaSmw0zFz//LCa4");
        w.accept("fZetzOTxX+Hoz1SDgFhh5Raw+swOlAF9qXnixjsbt7aCvUxRbWXCKpH21kdRW2Ic");
        w.accept("4lQmHoDwNJIEHQViPyXSehKQe5AOCdFa1Au2/5sKyhnIkoUD4HLvHOL6uZZlQSP9");
        w.accept("QBhQ34inAgMBAAECggEBAMdjPu3/nAdOxJpYxFlJ+GrmDw30DfF6zKs7OCteBoh/");
        w.accept("eFVr31IMwws2rTTRRKRnSA0+Jqr7q+McCS0dMMOigU2z7FP66c6m2fSA1shbnbZz");
        w.accept("tuWnnTWeAOmmXagchzNqr2uintz10P4bfczOkmVrWkHpIxwet5543qPLgSjM/RTS");
        w.accept("uVz0RjD1autQnaWHIJFonhS3ZI6SUHVxGbfTgMK89TzMSZG9k+fU7ZlwiauicvyN");
        w.accept("TULpX4ovGJmFckm+uu+x5fSVfoDR+XD5by4owB3Sj7m/9Fs3DFPoy16TufUhr13f");
        w.accept("ONdBJgt9oumqqnZecchgQwPlTApWWWLbVTu3KKD8c3ECgYEA9sgnP4GZ6cSfGCRh");
        w.accept("6HZZIy3h5W/0u7UnWtNE1/e9Fc8m2I+yEqg9zfZhHu2CO0TO5om2B64WFcXPTl6O");
        w.accept("6cPDWwWfVmHksySybb4LQCMMl28xk4BhrBBymU3TV2Rkv7QHqz+a8xViDhzSYA2g");
        w.accept("SnMzl1j9QTG/3l+ulNlDyeSTBMMCgYEA0xVyPKsI397plb0sJ2/7yZL1ulfVNT1H");
        w.accept("pvAFH1QZvk3R+FmSd6jaL7o5DPJb2EzyKyUZ24NMOiBUhPFFQZ+0FMW7FNcsExMM");
        w.accept("N0GaoIHT1l4t+7xA8ZTAH0LGehQZqonFdvcK1SWJDPvyHLN0wnFp3Ui0vYYnTPk8");
        w.accept("UxO0FkMC3k0CgYA54FMkF7cLFivhs5aquCbLk1UpRAp3g1LJgEbjB5z24nBP1dOD");
        w.accept("gKWOCjxYzob+c3K6qo1gW7mePZgS3yZROLI2RKlLzwWd5ftatXlZ/15SnadY2oEN");
        w.accept("o4Xc4l2wX0EpnIU36mDipZ8rhCLqmAeBrmbpFdu/UHWZJ4OAMTwuu0anlQKBgCSS");
        w.accept("k691Rt1bBwe9thfDLFH5l3/I1hUaX/7JmWmbLbauTxIDmwAGjn80ecwHdehdNJxL");
        w.accept("GlbRQfTUQzChiQlcvVvYApkSyv0nELfGMx9aPzTmLntuW6Y/yqXf8PmX3/aPVlpN");
        w.accept("ZWAW188bHBDi+vjxo5EGluI7izWn/U67nDk7NRUFAoGBAK2ijXHlcyHuiFz9kjBJ");
        w.accept("tbQDk+YZ9y70FrC8vuzogseoJ7E+ncgD1rO3x/TgXBwgIX2oYUzH8vtTrZz+3yl2");
        w.accept("DlZqqVvFlQjTQtxVxqgX0Zx6JjJKI7DeK/95l0mNkbYZXMnqp0YGSVsM8Irj17bz");
        w.accept("fInwv1uLrQE8FBj2SvxfD/cI");
        w.accept("-----END PRIVATE KEY-----");
        return buf.toString();
    }

    private static String getNodeCertificateContents() {
        StringBuilder buf = new StringBuilder();
        Consumer<String> w = getWriter(buf);
        w.accept("-----BEGIN CERTIFICATE-----");
        w.accept("MIID6DCCAdCgAwIBAgIBAjANBgkqhkiG9w0BAQsFADAaMRgwFgYDVQQDDA9TcG9v");
        w.accept("aGFwcHNUZXN0Q0EwHhcNMTgwNTMwMDQzOTIzWhcNMjgwNTI3MDQzOTIzWjAkMREw");
        w.accept("DwYDVQQDDAh0ZXN0dXNlcjEPMA0GA1UECgwGY2xpZW50MIIBIjANBgkqhkiG9w0B");
        w.accept("AQEFAAOCAQ8AMIIBCgKCAQEAy3uk5C1LOC4I6wdmzVNtOTL2VDi36F8XujB49VVG");
        w.accept("8cbGNVrmveGs+IEhPJyHVnMAiUkByE+g1M+Q7UwTjX26PQuDTepigLNDiQ5rhspb");
        w.accept("5znhV4cko97kEds15fgq/UXYbpt/D7/DOw6IvEWt8Yc99kVN0/MI/Xl8lAxcmQoP");
        w.accept("ro4/MVQymRCjEbFcSZ6bK+ZGkpsNMxc//ywmuH2Xrczk8V/h6M9Ug4BYYeUWsPrM");
        w.accept("DpQBfal54sY7G7e2gr1MUW1lwiqR9tZHUVtiHOJUJh6A8DSSBB0FYj8l0noSkHuQ");
        w.accept("DgnRWtQLtv+bCsoZyJKFA+By7xzi+rmWZUEj/UAYUN+IpwIDAQABoy8wLTAJBgNV");
        w.accept("HRMEAjAAMAsGA1UdDwQEAwIFoDATBgNVHSUEDDAKBggrBgEFBQcDAjANBgkqhkiG");
        w.accept("9w0BAQsFAAOCAgEAvDniC7Vybkbz/6LDnU7+me/+hj/bx6W5lHAbAeWPmgbfTRKI");
        w.accept("8SfAHrdVT+Vc8WhhpnqYAbWs5w1OyQG58EFlHZ0s+Z99FEMggCYvLIQSqIsU56xt");
        w.accept("WttQfOgMzLovUn/VyK6cCIYd5dfyOtjL/9Zi3Sa/2vB3mZO8ugqcJxxPNFCBIJaO");
        w.accept("JsFroauD8TbyiRRLSxArMPEn2HBerpyY+DFpPBcgSfMrikjWLIwYQrzg43F//o7e");
        w.accept("/IYC5cwiCiDBiH+iPajo7GNZAk/T5VKSTXrYOA3V8nCKejs0rBRDJA0Kf8Og/cXM");
        w.accept("iqhLJFMNQscuLjbUtajLvh4fddgd98CUH2exUAVEMmKnNtliLSFZomnWpvQXf8om");
        w.accept("dt4t5+J6Ze3zshHSQZ+smRaBJb/LsmEGURmwWcqRlNWfCrTbs0YX8vrl/DQYrQw8");
        w.accept("cqUAXeAEqQNq8Zjpl1IX/xv3aUg8VBKK/yTcJ4NvCmRVWh2Nma3G+2xi3VnljVx+");
        w.accept("9kLTXbBG2nQl76WHyxNyZpkJnIUp66rGw5J1V8Mh3+JYaJ0CsBn8aG32N/gxssuZ");
        w.accept("clavGbrSDivCa1VaxHhAbFDqFlqOiO45JUHjNQpwBLj48rRn+GdX7bR3dBoAuGCX");
        w.accept("2I8D3/BDnhUp+rII88/AQzD5HJWXXKd6X5lTC7MToEgoCXdiII+S51Ksm6Y=");
        w.accept("-----END CERTIFICATE-----");
        return buf.toString();
    }

    private static String getCaCertificateContents() {
        StringBuilder buf = new StringBuilder();
        Consumer<String> w = getWriter(buf);
        w.accept("-----BEGIN CERTIFICATE-----");
        w.accept("MIIE1DCCArygAwIBAgIJAImh2ojUC0hNMA0GCSqGSIb3DQEBCwUAMBoxGDAWBgNV");
        w.accept("BAMMD1Nwb29oYXBwc1Rlc3RDQTAeFw0xODA1MzAwNDMyMTJaFw0zODA1MjUwNDMy");
        w.accept("MTJaMBoxGDAWBgNVBAMMD1Nwb29oYXBwc1Rlc3RDQTCCAiIwDQYJKoZIhvcNAQEB");
        w.accept("BQADggIPADCCAgoCggIBAMyIP8toqBFamK76woF8golfVaPjq3qtrGAlbrHNsybP");
        w.accept("XVZJ3EQLTFZI9pM2/+stuaWlO5fg7TDqCqRwUIiV8do4env9eQRvZa0zDPrLkG7E");
        w.accept("y67KlFS7Mm+Vj5c2nuVgLlzxsELLhJfZlAdFlKguTjam6vpcVAIsWMfLnJVxFoVN");
        w.accept("7fNgQNqTn/9U6OkfWjFzy22u9kwaQtdDC/kC3/lp77nVbZCnFwCS+dEaSnnqBLuX");
        w.accept("DDcjCvnaZH/mMDQbMmslXqBcD0B4+9zqgbylijQPzFAxwmuSM78b7sA7ygDpX5X+");
        w.accept("3SRy/XImjnm9ulOpnIdd32fcEPJXZFTYmP1mgNDr5y2BmXHanxx5qVQhKIKPz4KY");
        w.accept("XnQ1vN+ENJiE4XvCjXl1XnwLHrEj9hap5cTsZR+9Y8gsM17QUFCFy5afD+XyNxRQ");
        w.accept("WlPUibVCCJ9PE48QLVr+R2XAs+6rjfLMXA1TlR1t1D24a55lXMVwKafKf/YIHjI1");
        w.accept("PD3mUGPppHATfsMX1mWcfWqCvN+mrlZ4+XCp52O9YT8RNC8WcVEd5xvIc7kZj58A");
        w.accept("WkPfM/ia33mahbKv789lTOjtYzhohyHM4shJ6VfoL8JmsEq3JWK7xBGZBHYkPaDI");
        w.accept("GR6L2UJlzBHj9SIqJOs3ONyVxSYSfN0HDC6OaSnmBXyq6hVo7HWjL+xXUGUrCBcZ");
        w.accept("AgMBAAGjHTAbMAwGA1UdEwQFMAMBAf8wCwYDVR0PBAQDAgEGMA0GCSqGSIb3DQEB");
        w.accept("CwUAA4ICAQBxrmK0XWaTeH0B5qsf5N8CVDy5+bebU5oiZBiOap7JtOWXlIGZhrxI");
        w.accept("7PROnkuzk4KG84ea5gV4U+8NXp1zMgRqi9NaFBGtWFLUJ3vyOk4IfSfPTKGQhhT1");
        w.accept("rFblQuqoBGU8dn6QgbMNlokmbq03SLC+4TsrbhMZBtZGMzoLRaO8nCTRgYdJK/Mt");
        w.accept("kUuAKGLlhdGSzmDiUwYQcLlLguK46GZ6mY3tBqfRbam9xIuXiXyfdhsesTLAmz99");
        w.accept("MKPJJiDuiYL4Y0KqdyRlKqpntEMVBIzoaQI61deLMBHu6LgTy+7UeiFY+UG/ehTB");
        w.accept("JQPZUEnVYJnO6JHmGk029HFg0tPowfv9tM7/I9gZKaIfC3bVweLnH5HjBw9M+CuI");
        w.accept("cuj1oX2MUTR3QCvZ6M7m4Ff945BdOEiA8n4ipRiz2JpjuMIGI+3JwbjqWT5ZAFoc");
        w.accept("3tvFD4yScbRUDx3S7gH11ThOLouI7Jgl28Qm0/a8E3UmkjnWqnb4Itv/IY9BE1GI");
        w.accept("d8+8R/mrlQ6qifUnn0WrSyh+Y1G7mOrNoZsvA0XR7twVNHr9hkfbgFJoD9xTsweK");
        w.accept("GxWaosg++cYKX22IQDO6o7X8WM0FVuH3bZla4CssyJ3HVZptI2wtWMHMbyvhJxkx");
        w.accept("Tjy2QViZVcY6O2frarkxPdSLhP2CrHn3QaVZnjvbhZ9+PXU6lUwyjQ==");
        w.accept("-----END CERTIFICATE-----");
        return buf.toString();
    }

    private static String getRemoteMessagingPrivateKeyContents() {
        StringBuilder buf = new StringBuilder();
        Consumer<String> w = getWriter(buf);
        w.accept("-----BEGIN PRIVATE KEY-----");
        w.accept("MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQDAsHFfNpQlNA32");
        w.accept("H0Iss68jwssY5qiQOjxvK5QoymxLcOZnzTjhL4ugp9/kWSH0lnXvG/SOqcIU5/Ga");
        w.accept("zMBXVJZ9a7dW+Lj/743sFf9wL21H2v1KB1IbNwr7ZaIHgKqgZ5GS/rPH4WaGGAig");
        w.accept("K2bfWdFfCehunRWj3IPtD8sZfoSexd+lwgM4rmM6QGBOBpiOKTSQh0wbMalPsdVj");
        w.accept("hVqdhYhNz4qZsc0QoSDEiMYCn9qIDktHAHDtRUXeA2j9eX1psqxzjJT3NzZFSfa5");
        w.accept("Yc8vYV0WVAcPx8udEXlgB8bmTS6jsU6xYogd5+O0Td0GttEhHYhh8gnwYYBluj1o");
        w.accept("s2iLpuhhAgMBAAECggEBAIFNZaao71zPw5anfzaUFTEAJF2/Wtn92lQXgEKnI2i0");
        w.accept("iEibtFGYIDBctqd7EIga92TO85tQW0bAJqbkQyXXQ21CfTNFkI9eZ3RHhrP3f3O6");
        w.accept("hkHJC9XrZ9kvaD10oAsntM5ZULcOkEfnlNI2jb/Pe+o0sd6YVs8wyO6prHadKc2I");
        w.accept("e2laUh+T4UKHXjoGlk2qiv3PHoeF6P5V2PF1dhVzHX++4cN1/gybIerMrSFLs/DK");
        w.accept("GRrwEFXWuNzhSEtXiwDys9JKV4FTYJJee3qM3PHb/JoJ86v8onz4Y1QP1ewTVB9p");
        w.accept("snq96fz3P3AXCy2lVF/F1RXWEWT1MAYAbKL8ZmBTPuECgYEA9YPq4CN7QGCOLWpB");
        w.accept("F2QgZrruuHaXEVChlohYGM6STIzbsilai5CxZOrDp0CgSwqPeSvK/6M4MmjjEF/l");
        w.accept("I9/NsuP7aM680s26C4Wn9RvPV9qsqbRbBuzyXHSlS2RQxjNL6/S63SVT7tcL9/E4");
        w.accept("4B8KJYaLAFJXJ201ezZF6jcjiS0CgYEAyOsB2vLMDSWfbVw9drVCI1IkuS9vM0dD");
        w.accept("bcQ9RIn8iqjQ6P31MlOVocLL6G9/NbniGCDEVNAjvT/czZG9hrIhrDdLirHQ2BCj");
        w.accept("nkO1bkb6E1rXB4r+ORa+Buy9dF8iNYALT4xNe7XTfE06qF2uo/0Xd7LPpabNuJHJ");
        w.accept("DESbVRzRtIUCgYEAkjbsDGxVHLPkOJvNBBc3TLcLMIQ/16oDcdjlTnDHpBtHQ8Vp");
        w.accept("DTEw1H8T4mmUjNxPMcSo8rHL6AmbIMdbeY/xuxYXRgXJYmnu4KnKqyjSxOmQZsjo");
        w.accept("ZSl79R/qyQmHKWKnVnC7ULTIVbdwg0r/qkoSCuMjqR9glDRv418hKCmKWkkCgYA6");
        w.accept("6GRn3I4lQKWou8Wtm2Fj1766qQSyhS9o7IOGJ+rqgKqX1XXhpo4VmnaMlLw4dWKr");
        w.accept("7leCAnCoGSRVBWkLKAklZCT66j2wS9idG38DcT37FMQ9CuyUsm1OvHSjvQzboMow");
        w.accept("LZO+NMZoCICtQqJkpF0QFFY7XFeuyNeoiCj+4G8/vQKBgDjrjtmRgllMTXteSyrt");
        w.accept("hrku4Uax3PKtpVvsXfgojrO5//UgU4tVSmnCcKOwuGDF15vYdK9hYo5OfeHHTemp");
        w.accept("t7SZSv/8fxWz5XUZplfX946YXY3gVGyaW9adqpvDUliJidBWs1l3FbnrlxtZ4U0F");
        w.accept("PW1cVC+YJMvLAMqUnnQ79EAD");
        w.accept("-----END PRIVATE KEY-----");
        return buf.toString();
    }

    private static String getRemoteMessagingCertificateContents() {
        StringBuilder buf = new StringBuilder();
        Consumer<String> w = getWriter(buf);
        w.accept("-----BEGIN CERTIFICATE-----");
        w.accept("MIID7TCCAdWgAwIBAgIBBDANBgkqhkiG9w0BAQsFADAaMRgwFgYDVQQDDA9TcG9v");
        w.accept("aGFwcHNUZXN0Q0EwHhcNMTgxMTAzMTczODQxWhcNMjgxMDMxMTczODQxWjApMRYw");
        w.accept("FAYDVQQDDA1kZXZpY2Vjb250cm9sMQ8wDQYDVQQKDAZjbGllbnQwggEiMA0GCSqG");
        w.accept("SIb3DQEBAQUAA4IBDwAwggEKAoIBAQDAsHFfNpQlNA32H0Iss68jwssY5qiQOjxv");
        w.accept("K5QoymxLcOZnzTjhL4ugp9/kWSH0lnXvG/SOqcIU5/GazMBXVJZ9a7dW+Lj/743s");
        w.accept("Ff9wL21H2v1KB1IbNwr7ZaIHgKqgZ5GS/rPH4WaGGAigK2bfWdFfCehunRWj3IPt");
        w.accept("D8sZfoSexd+lwgM4rmM6QGBOBpiOKTSQh0wbMalPsdVjhVqdhYhNz4qZsc0QoSDE");
        w.accept("iMYCn9qIDktHAHDtRUXeA2j9eX1psqxzjJT3NzZFSfa5Yc8vYV0WVAcPx8udEXlg");
        w.accept("B8bmTS6jsU6xYogd5+O0Td0GttEhHYhh8gnwYYBluj1os2iLpuhhAgMBAAGjLzAt");
        w.accept("MAkGA1UdEwQCMAAwCwYDVR0PBAQDAgWgMBMGA1UdJQQMMAoGCCsGAQUFBwMCMA0G");
        w.accept("CSqGSIb3DQEBCwUAA4ICAQB59RBO9WbDmJltWvLQ7a5+X+x8RSI3/9fB5qtnDItl");
        w.accept("DZWodPXbidrPug4QPxG2wpwGIByMCOetdOkzq+wZ/3t3T7VlXggcDYQMN19znyF2");
        w.accept("ZLQ+MUU+TCXwcmmk2H4P8PEZ8lphr+LRBnzyaDwfot/aKrZxjsA+lg+1gjySkw6o");
        w.accept("zqb/fmvSHdIGXhqGN7emoy6BJcS7+1pZ0eHY0TcfFrQWQ+5s3Ip6Tgym471WV2eH");
        w.accept("Z6jGMM5vLUuJa0IQToqGbscTOPa6SMhqm2gZ6F/RLL77BNCHyZnzwfCbdzR3AY6F");
        w.accept("Fxp9Bch8ceGr1hQz/WNJcgkHe0cYqK1vrENtQGPG05fmWCPxip+rg49z824cdGSi");
        w.accept("qEWH2CwYf0oumnPmLutMKzCSTSsv7dFdSpemu+Iih7bNRY898KVFZfcPisggpffW");
        w.accept("4YY+fhElm7zEURCfB1WlNaXAjP3b32euNNlafkvA9sVnClmzfN8DUwKr9C8jQXuc");
        w.accept("tAQl49Qf7y4ljchwIdz9YhEkEvXemNNWPa1a/28CGeUvFiga3ACKWEwwIU3fXwD3");
        w.accept("Uh288GSU1ueySrsJogobo2aKE0jfApFCv+HT7HVpjbw+yBb6OtHo9r6zn+lKNWib");
        w.accept("Km3TPg9yr9fU7jn6qQ34SrDZ1xnGgQwIofReqTG8vazFa+jW4zrEfFb4vNv94Uut");
        w.accept("PQ==");
        w.accept("-----END CERTIFICATE-----");
        return buf.toString();
    }

    private static String getServerCaCertificateContents() {
        StringBuilder buf = new StringBuilder();
        Consumer<String> w = getWriter(buf);
        w.accept("-----BEGIN CERTIFICATE-----");
        w.accept("MIIE1DCCArygAwIBAgIJAImh2ojUC0hNMA0GCSqGSIb3DQEBCwUAMBoxGDAWBgNV");
        w.accept("BAMMD1Nwb29oYXBwc1Rlc3RDQTAeFw0xODA1MzAwNDMyMTJaFw0zODA1MjUwNDMy");
        w.accept("MTJaMBoxGDAWBgNVBAMMD1Nwb29oYXBwc1Rlc3RDQTCCAiIwDQYJKoZIhvcNAQEB");
        w.accept("BQADggIPADCCAgoCggIBAMyIP8toqBFamK76woF8golfVaPjq3qtrGAlbrHNsybP");
        w.accept("XVZJ3EQLTFZI9pM2/+stuaWlO5fg7TDqCqRwUIiV8do4env9eQRvZa0zDPrLkG7E");
        w.accept("y67KlFS7Mm+Vj5c2nuVgLlzxsELLhJfZlAdFlKguTjam6vpcVAIsWMfLnJVxFoVN");
        w.accept("7fNgQNqTn/9U6OkfWjFzy22u9kwaQtdDC/kC3/lp77nVbZCnFwCS+dEaSnnqBLuX");
        w.accept("DDcjCvnaZH/mMDQbMmslXqBcD0B4+9zqgbylijQPzFAxwmuSM78b7sA7ygDpX5X+");
        w.accept("3SRy/XImjnm9ulOpnIdd32fcEPJXZFTYmP1mgNDr5y2BmXHanxx5qVQhKIKPz4KY");
        w.accept("XnQ1vN+ENJiE4XvCjXl1XnwLHrEj9hap5cTsZR+9Y8gsM17QUFCFy5afD+XyNxRQ");
        w.accept("WlPUibVCCJ9PE48QLVr+R2XAs+6rjfLMXA1TlR1t1D24a55lXMVwKafKf/YIHjI1");
        w.accept("PD3mUGPppHATfsMX1mWcfWqCvN+mrlZ4+XCp52O9YT8RNC8WcVEd5xvIc7kZj58A");
        w.accept("WkPfM/ia33mahbKv789lTOjtYzhohyHM4shJ6VfoL8JmsEq3JWK7xBGZBHYkPaDI");
        w.accept("GR6L2UJlzBHj9SIqJOs3ONyVxSYSfN0HDC6OaSnmBXyq6hVo7HWjL+xXUGUrCBcZ");
        w.accept("AgMBAAGjHTAbMAwGA1UdEwQFMAMBAf8wCwYDVR0PBAQDAgEGMA0GCSqGSIb3DQEB");
        w.accept("CwUAA4ICAQBxrmK0XWaTeH0B5qsf5N8CVDy5+bebU5oiZBiOap7JtOWXlIGZhrxI");
        w.accept("7PROnkuzk4KG84ea5gV4U+8NXp1zMgRqi9NaFBGtWFLUJ3vyOk4IfSfPTKGQhhT1");
        w.accept("rFblQuqoBGU8dn6QgbMNlokmbq03SLC+4TsrbhMZBtZGMzoLRaO8nCTRgYdJK/Mt");
        w.accept("kUuAKGLlhdGSzmDiUwYQcLlLguK46GZ6mY3tBqfRbam9xIuXiXyfdhsesTLAmz99");
        w.accept("MKPJJiDuiYL4Y0KqdyRlKqpntEMVBIzoaQI61deLMBHu6LgTy+7UeiFY+UG/ehTB");
        w.accept("JQPZUEnVYJnO6JHmGk029HFg0tPowfv9tM7/I9gZKaIfC3bVweLnH5HjBw9M+CuI");
        w.accept("cuj1oX2MUTR3QCvZ6M7m4Ff945BdOEiA8n4ipRiz2JpjuMIGI+3JwbjqWT5ZAFoc");
        w.accept("3tvFD4yScbRUDx3S7gH11ThOLouI7Jgl28Qm0/a8E3UmkjnWqnb4Itv/IY9BE1GI");
        w.accept("d8+8R/mrlQ6qifUnn0WrSyh+Y1G7mOrNoZsvA0XR7twVNHr9hkfbgFJoD9xTsweK");
        w.accept("GxWaosg++cYKX22IQDO6o7X8WM0FVuH3bZla4CssyJ3HVZptI2wtWMHMbyvhJxkx");
        w.accept("Tjy2QViZVcY6O2frarkxPdSLhP2CrHn3QaVZnjvbhZ9+PXU6lUwyjQ==");
        w.accept("-----END CERTIFICATE-----");
        return buf.toString();
    }

    private static String getServerCertificateContents() {
        StringBuilder buf = new StringBuilder();
        Consumer<String> w = getWriter(buf);
        w.accept("-----BEGIN CERTIFICATE-----");
        w.accept("MIID6DCCAdCgAwIBAgIBATANBgkqhkiG9w0BAQsFADAaMRgwFgYDVQQDDA9TcG9v");
        w.accept("aGFwcHNUZXN0Q0EwHhcNMTgwNTMwMDQzNjIzWhcNMjgwNTI3MDQzNjIzWjAkMREw");
        w.accept("DwYDVQQDDAhlcGljdXJ1czEPMA0GA1UECgwGc2VydmVyMIIBIjANBgkqhkiG9w0B");
        w.accept("AQEFAAOCAQ8AMIIBCgKCAQEAsu3u1z5N1Xs41MdSjA1b6F+dm/UoD82z+Mn8LU/2");
        w.accept("UipEiC5Ld2yCXoqpxZ9afH+0OZOTH2Jkxada70VryHo3NSzwQ+jQh2Tt4mKH44wS");
        w.accept("RFOWhT/OAmncpKoWo0GeLVjr6u1YxtzcNSJc2GMDmJa+uNQQ8B5Kg6cSThg6oFTS");
        w.accept("Y/KB1cX6yLvev0XJWqOtBFKGAm8l7/xajVm1hhqMmddtvp4Lfrjl3RIUNZGw5tjf");
        w.accept("TFrBLR9+8BbnLQhCo0Et5zkEz4RGrZu/GaOxcOTcy5apDZJ+vIY7syE7nXzQraSC");
        w.accept("VnwYDYYDXkVRZ7V5iIUuuwr4KqJpXYOxVdFnLMPtr6xwUQIDAQABoy8wLTAJBgNV");
        w.accept("HRMEAjAAMAsGA1UdDwQEAwIFoDATBgNVHSUEDDAKBggrBgEFBQcDATANBgkqhkiG");
        w.accept("9w0BAQsFAAOCAgEAIWkd5vKE8ovITWoP/px533a2KRTkyztvWl/gyDNhGeymzjS+");
        w.accept("38xeKFMD6wbGkCavwuWsjKIQVvS6wAN4PWXNLB0d1qA0eO90Mp6aeFmX9dbcItpc");
        w.accept("OCO9X3pFPQHFyDTefc4NaMSIySkY8pDNg+ARhZcI+JdUnUEyvq7bjq058ngceTVV");
        w.accept("m2h9lKmmZoMSPsPfaKz8bOTkKZbs51ini8sblB9UO/qN+ya9CCA3BAm8vtVMnkzM");
        w.accept("IWRajQ6bW6JelTlnZ2x81tXMk8h0Oj71rS2agD4K82sYH0UvosEBYSPIDKJKhnH0");
        w.accept("ElE7LAO8DRWN5g95ZG1rRVsNB0APCmZ2w0VVYFQsjHLZ61xsctP794ZRil5nKlkS");
        w.accept("9+UozRZrl5oCLoFJT7xbKokOKmOVDPrv78SfsuBNw3lToeeM1zPQlZScU2nQiZCc");
        w.accept("Ddhx3cOyOotk6CrbQfkppUBkWrkgwtjMsNo6AGpamVqyDgxVHh07yOP47PUTSnR3");
        w.accept("pX+7gTzS7a7BTOOqhgccFigr7zd7E+MgXl1IY4tpqmK5l3HONRnSb4eqxNymlGt3");
        w.accept("Q7sQYPR1LtLbVLhYT54sr3U7S67gJ1ov6NOTempbGE4vKv6Km8IUHR2iqnfi5ypj");
        w.accept("SVoZCdgl/STZLk2+6X7OfmJNtK/v2nV6tpIxTArSx5MOD5CLrlWZK5lcmjY=");
        w.accept("-----END CERTIFICATE-----");
        return buf.toString();
    }

    private static String getServerPrivateKeyContents() {
        StringBuilder buf = new StringBuilder();
        Consumer<String> w = getWriter(buf);
        w.accept("-----BEGIN PRIVATE KEY-----");
        w.accept("MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCy7e7XPk3VezjU");
        w.accept("x1KMDVvoX52b9SgPzbP4yfwtT/ZSKkSILkt3bIJeiqnFn1p8f7Q5k5MfYmTFp1rv");
        w.accept("RWvIejc1LPBD6NCHZO3iYofjjBJEU5aFP84CadykqhajQZ4tWOvq7VjG3Nw1IlzY");
        w.accept("YwOYlr641BDwHkqDpxJOGDqgVNJj8oHVxfrIu96/Rclao60EUoYCbyXv/FqNWbWG");
        w.accept("GoyZ122+ngt+uOXdEhQ1kbDm2N9MWsEtH37wFuctCEKjQS3nOQTPhEatm78Zo7Fw");
        w.accept("5NzLlqkNkn68hjuzITudfNCtpIJWfBgNhgNeRVFntXmIhS67Cvgqomldg7FV0Wcs");
        w.accept("w+2vrHBRAgMBAAECggEAFzWf1pHBXOC39XnJbltJQbItao55XNts7eeda29E31GW");
        w.accept("RCflKB+OJZ9M+FJ8VUvnX9SRr+9+bNkRhZn2qjGYObCTlet8KSPha40mXVDNCcnh");
        w.accept("63TkTxHWShG6EAEx+GXhbD7PVjRN6CKF8pedXsym/qibbdowBjXEvRaNfECkbcxO");
        w.accept("b7tmNb1RfMLt4ZI8KZOmrhzNxfDwdv0bYHNayDlurn3DjCRrvtAo2+mjDCLjYU7d");
        w.accept("DFTd52KcQwVpminl0Ce/jJApeI0Jbhggq8bIXuo+SyO4X5Nd81ZWYtst/zE3LygW");
        w.accept("JLPfxQ8AqSWuQStVLwE4RzOCLzCCYZAajv8Cvs+dsQKBgQDoMxLSpTP64cQyAAWb");
        w.accept("FBlrBI41PaaCldGhAEQMYjAknzvqH6h/nhUhpQ0jdhXGj+rarq4mZu4k4KOgEVzf");
        w.accept("lBPzPjoiYw1UkI2OWGGga5KjCPrOc/Dq4+qa3qKR+NDDwVrFWH85eGkO4CfXBNkd");
        w.accept("rjM81S4+wFETJfU6/ezDDx4jNQKBgQDFRQzHMoMBOV8ue0mrEwKud7StCbR64uqS");
        w.accept("iJYcIXunM4veCf4fnxXFe0W8VcBuWDXE+JaPTkVJpG0rLiOL2KdhmfifMg7p4lPw");
        w.accept("NK+PMqztX4GwmxqOApwHykdiBN0wX15Li9wpuRZrLdVDoRslm6Gy1DXg/oFwgG5I");
        w.accept("Vi63S6dALQKBgQC61aW2osFBhHm2dbgNAU42gzLd+h5KArzrXGhkwnjUbt/p0dFl");
        w.accept("5lBAgysgNnrtC45Aqb9tHPNP7wUZ3f6p0999oLtsQeFPAwUz3T+osgYKc0ZD6pyW");
        w.accept("lA+Iz81lwRxR9vnPwxaTqqR2GL1V0QvWSv3lW+9bBKn3y1oYPotMpJRuZQKBgGuW");
        w.accept("mDx/6QEe6DFyniAGczzyHoENPPiUj7ixc6UOAeGT9GF587YNTpNxAvyPjC7iPxkZ");
        w.accept("NwwbdSCP49ZcCJj55XgOwJEafRXEEWF9ao5f5f5kS1glj5sKA00gXkQwQO5j5Qqw");
        w.accept("0beTsY1s5tZGpIajVdIxeN0oETJpaTxcfIgNY79VAoGABUN+yfyscgnFlJ9+/CLv");
        w.accept("+BS9RKcYof1btEjO7J6vqPrVkE3qFwdU6iD3VItWTqsPvpW0S9E+D6c1LbNqaGp5");
        w.accept("vLBhIh8v3R4yyv6UpA8zdvXqBdOUTU/dkwtjyBxYkIO5id1vWzwCRpR38kg3qtrk");
        w.accept("sBAjrEUejYTq5D5CLxStHag=");
        w.accept("-----END PRIVATE KEY-----");
        return buf.toString();
    }

    private static Consumer<String> getWriter(StringBuilder buf) {
        return l -> buf.append(l).append(lineSeparator);
    }

    private static InputStream streamOf(String contents) {
        return new BufferedInputStream(new ByteArrayInputStream(byteArrayOf(contents)));
    }

    private static byte[] byteArrayOf(String contents) {
        return contents.getBytes(StandardCharsets.UTF_8);
    }

    private static final String lineSeparator = System.getProperty("line.separator");
}
