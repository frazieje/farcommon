package com.spoohapps.farcommon.model;

import com.spoohapps.farcommon.testhelpers.ProfileFileHelper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.spec.InvalidKeySpecException;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class WhenWritingRemoteProfileToByteArrayTests {

    Profile profile;

    private static final String expectedProfileId = "a2a48c93";

    @BeforeAll
    public void setup() throws InvalidKeySpecException, NoSuchAlgorithmException, CertificateException {

        TLSContext nodeContext = new TLSContext();
        nodeContext.setPrivateKey(ProfileFileHelper.nodePrivateKey());
        nodeContext.setCertificate(ProfileFileHelper.nodeCertificate());
        nodeContext.setCaCertificate(ProfileFileHelper.nodeCaCertificate());

        TLSContext apiContext = new TLSContext();
        apiContext.setPrivateKey(ProfileFileHelper.remotePrivateKey());
        apiContext.setCertificate(ProfileFileHelper.remoteCertificate());
        apiContext.setCaCertificate(ProfileFileHelper.remoteCaCertificate());

        profile = Profile.from(expectedProfileId, nodeContext, apiContext);
    }

    @Test
    public void shouldWriteCorrectBytes() {
        assertArrayEquals(ProfileFileHelper.bytesWithProfileIdNodeAndRemote(expectedProfileId), profile.toByteArray());
    }

}
