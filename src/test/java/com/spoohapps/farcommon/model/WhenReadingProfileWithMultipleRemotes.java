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
public class WhenReadingProfileWithMultipleRemotes {

    Profile profile;

    private static final String expectedProfileId = "a2a48c93";

    @BeforeAll
    public void setup() {
        profile = Profile.from(ProfileFileHelper.streamWithProfileIdNodeAndMessagingAndAuthRemotes(expectedProfileId));
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
    public void shouldHaveTheCorrectRemoteMessagingCertificate() throws CertificateException {
        assertArrayEquals(profile.getRemoteMessageContext().getCertificate().getEncoded(), ProfileFileHelper.remoteMessagingCertificate().getEncoded());
    }

    @Test
    public void shouldHaveTheCorrectRemoteMessagingPrivateKey() throws InvalidKeySpecException, NoSuchAlgorithmException {
        assertArrayEquals(profile.getRemoteMessageContext().getPrivateKey().getEncoded(), ProfileFileHelper.remoteMessagingPrivateKey().getEncoded());
    }

    @Test
    public void shouldHaveTheCorrectRemoteMessagingCaCertificate() throws CertificateException {
        assertArrayEquals(profile.getRemoteMessageContext().getCaCertificate().getEncoded(), ProfileFileHelper.remoteMessagingCaCertificate().getEncoded());
    }

    @Test
    public void shouldHaveTheCorrectRemoteAuthCertificate() throws CertificateException {
        assertArrayEquals(profile.getRemoteAuthContext().getCertificate().getEncoded(), ProfileFileHelper.remoteAuthCertificate().getEncoded());
    }

    @Test
    public void shouldHaveTheCorrectRemoteAuthPrivateKey() throws InvalidKeySpecException, NoSuchAlgorithmException {
        assertArrayEquals(profile.getRemoteAuthContext().getPrivateKey().getEncoded(), ProfileFileHelper.remoteAuthPrivateKey().getEncoded());
    }

    @Test
    public void shouldHaveTheCorrectRemoteAuthCaCertificate() throws CertificateException {
        assertArrayEquals(profile.getRemoteAuthContext().getCaCertificate().getEncoded(), ProfileFileHelper.remoteAuthCaCertificate().getEncoded());
    }

    @Test
    public void shouldEqualOtherProfile() throws CertificateException, InvalidKeySpecException, NoSuchAlgorithmException {
        TLSContext nodeContext = new TLSContext(ProfileFileHelper.nodeCertificate(), ProfileFileHelper.nodePrivateKey(), ProfileFileHelper.nodeCaCertificate());
        TLSContext remoteMessagingContext = new TLSContext(ProfileFileHelper.remoteMessagingCertificate(), ProfileFileHelper.remoteMessagingPrivateKey(), ProfileFileHelper.remoteMessagingCaCertificate());
        TLSContext remoteAuthContext = new TLSContext(ProfileFileHelper.remoteAuthCertificate(), ProfileFileHelper.remoteAuthPrivateKey(), ProfileFileHelper.remoteAuthCaCertificate());

        Profile p = Profile.from()
                .setId(expectedProfileId)
                .setNodeHost(ProfileFileHelper.nodeHost())
                .setNodePort(ProfileFileHelper.nodePort())
                .setNodeContext(nodeContext)
                .setRemoteMessageHost(ProfileFileHelper.remoteMessagingHost())
                .setRemoteMessagePort(ProfileFileHelper.remoteMessagingPort())
                .setRemoteMessageContext(remoteMessagingContext)
                .setRemoteAuthHost(ProfileFileHelper.nodeHost())
                .setRemoteAuthPort(ProfileFileHelper.nodePort())
                .setRemoteAuthContext(remoteAuthContext)
                .build();

        assertEquals(profile, p);
    }

    @Test
    public void hashCodeShouldEqualOtherProfile() throws CertificateException, InvalidKeySpecException, NoSuchAlgorithmException {

        TLSContext nodeContext = new TLSContext(ProfileFileHelper.nodeCertificate(), ProfileFileHelper.nodePrivateKey(), ProfileFileHelper.nodeCaCertificate());
        TLSContext remoteMessagingContext = new TLSContext(ProfileFileHelper.remoteMessagingCertificate(), ProfileFileHelper.remoteMessagingPrivateKey(), ProfileFileHelper.remoteMessagingCaCertificate());
        TLSContext remoteAuthContext = new TLSContext(ProfileFileHelper.remoteAuthCertificate(), ProfileFileHelper.remoteAuthPrivateKey(), ProfileFileHelper.remoteAuthCaCertificate());

        Profile p = Profile.from()
                .setId(expectedProfileId)
                .setNodeHost(ProfileFileHelper.nodeHost())
                .setNodePort(ProfileFileHelper.nodePort())
                .setNodeContext(nodeContext)
                .setRemoteMessageHost(ProfileFileHelper.remoteMessagingHost())
                .setRemoteMessagePort(ProfileFileHelper.remoteMessagingPort())
                .setRemoteMessageContext(remoteMessagingContext)
                .setRemoteAuthHost(ProfileFileHelper.nodeHost())
                .setRemoteAuthPort(ProfileFileHelper.nodePort())
                .setRemoteAuthContext(remoteAuthContext)
                .build();

        assertEquals(profile.hashCode(), p.hashCode());
    }

}
