package com.spoohapps.farcommon.model;

import com.spoohapps.farcommon.testhelpers.ProfileFileHelper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.spec.InvalidKeySpecException;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class WhenReadingRemoteMessagingProfileFromStreamTests {

    Profile profile;

    private static final String expectedProfileId = "a2a48c93";

    @BeforeAll
    public void setup() {
        profile = Profile.from(ProfileFileHelper.streamWithProfileIdNodeAndMessagingRemote(expectedProfileId));
    }

    @Test
    public void shouldHaveTheCorrectProfileId() {
        assertEquals(expectedProfileId, profile.getId());
    }

    @Test
    public void shouldHaveTheCorrectNodeCertificate() throws CertificateException {
        assertArrayEquals(profile.getNodeContext().getCertificate().getEncoded(), ProfileFileHelper.nodeCertificate().getEncoded());
    }

    @Test
    public void shouldHaveTheCorrectNodePrivateKey() throws InvalidKeySpecException, NoSuchAlgorithmException {
        assertArrayEquals(profile.getNodeContext().getPrivateKey().getEncoded(), ProfileFileHelper.nodePrivateKey().getEncoded());
    }

    @Test
    public void shouldHaveTheCorrectNodeCaCertificate() throws CertificateException {
        assertArrayEquals(profile.getNodeContext().getCaCertificate().getEncoded(), ProfileFileHelper.nodeCaCertificate().getEncoded());
    }

    @Test
    public void shouldHaveTheCorrectRemoteCertificate() throws CertificateException {
        assertArrayEquals(profile.getRemoteMessageContext().getCertificate().getEncoded(), ProfileFileHelper.remoteMessagingCertificate().getEncoded());
    }

    @Test
    public void shouldHaveTheCorrectRemotePrivateKey() throws InvalidKeySpecException, NoSuchAlgorithmException {
        assertArrayEquals(profile.getRemoteMessageContext().getPrivateKey().getEncoded(), ProfileFileHelper.remoteMessagingPrivateKey().getEncoded());
    }

    @Test
    public void shouldHaveTheCorrectRemoteCaCertificate() throws CertificateException {
        assertArrayEquals(profile.getRemoteMessageContext().getCaCertificate().getEncoded(), ProfileFileHelper.remoteMessagingCaCertificate().getEncoded());
    }

    @Test
    public void shouldHaveTheCorrectBytes() {
        assertArrayEquals(profile.toByteArray(), ProfileFileHelper.bytesWithProfileIdNodeAndRemote(expectedProfileId));
    }

}
