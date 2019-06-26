package com.spoohapps.farcommon.model;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.cert.CertificateEncodingException;
import java.util.*;
import java.util.AbstractMap.SimpleEntry;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Profile {

    private static final int PROFILE_ID_LENGTH = 8;

    private String id;

    private TLSContext nodeContext = new TLSContext();

    private TLSContext remoteMessageContext = new TLSContext();

    private TLSContext remoteAuthContext = new TLSContext();

    private static final String lineSeparator = System.getProperty("line.separator");

    private Profile() {

    }

    public static Profile from(String id, TLSContext nodeContext, TLSContext remoteMessageContext, TLSContext remoteAuthContext) {
        Profile p = from(id);

        if (nodeContext != null) {
            if (!nodeContext.hasValue())
                throw new IllegalArgumentException("Could not create profile. Incomplete TLS credentials for the node client.");
            p.nodeContext = nodeContext;
        }

        if (remoteMessageContext != null) {
            if (!remoteMessageContext.hasValue())
                throw new IllegalArgumentException("Could not create profile. Incomplete TLS credentials for the remote messaging client.");
            p.remoteMessageContext = remoteMessageContext;
        }

        if (remoteAuthContext != null) {
            if (!remoteAuthContext.hasValue())
                throw new IllegalArgumentException("Could not create profile. Incomplete TLS credentials for the remote auth client.");
            p.remoteAuthContext = remoteAuthContext;
        }

        return p;
    }

    public static Profile from(String id) {
        if (id == null)
            throw new IllegalArgumentException("Profile id must not be null");

        Profile p = new Profile();

        String lower = id.toLowerCase();

        verifyProfileId(lower);

        p.id = id;

        return p;
    }

    public static Profile from(InputStream profileStream) {

        if (profileStream == null)
            throw new IllegalArgumentException("Error reading profile from stream: null stream");

        Profile p = new Profile();
        try {
            readSection(new InputStreamReader(profileStream), p.sectionDelimiters);
        } catch (IOException e) {
            e.printStackTrace();
            throw new IllegalArgumentException("Error reading profile: " + e.getMessage());
        }

        if (p.id != null
                || (p.getNodeContext() != null && p.getNodeContext().hasValue())
                || (p.getRemoteMessageContext() != null && p.getRemoteMessageContext().hasValue())
                || (p.getRemoteMessageContext() != null && p.getRemoteMessageContext().hasValue()))
            return p;

        return null;
    }

    private static void readSection(Reader readerImpl, Map<String, Map.Entry<String, Consumer<String>>> delimiters) throws IOException {
        String str;
        String start = null;
        String end = null;
        StringBuilder buf = new StringBuilder();
        BufferedReader reader = new BufferedReader(readerImpl);
        while ((str = reader.readLine()) != null) {
            if (delimiters.containsKey(str)) {
                start = str;
                end = delimiters.get(str).getKey();
            }
            buf.append(str).append(lineSeparator);
            if (str.equals(end)) {
                delimiters.get(start).getValue().accept(buf.toString());
                buf.delete(0, buf.length());
            }
        }
    }

    private void readNode(String section) {
        String node = removeFirstAndLastLines(section);
        try {
            readSection(new StringReader(node), getSectionDetailDelimiters(this::readNodePrivateKey, this::readNodeCertificate, this::readNodeCaCertificate));
        } catch (IOException e) {
            e.printStackTrace();
            throw new IllegalArgumentException("Improper profile format: could not read node section");
        }
    }

    private void readRemoteMessaging(String section) {
        String api = removeFirstAndLastLines(section);
        try {
            readSection(new StringReader(api), getSectionDetailDelimiters(this::readRemoteMessagePrivateKey, this::readRemoteMessageCertificate, this::readRemoteMessageCaCertificate));
        } catch (IOException e) {
            e.printStackTrace();
            throw new IllegalArgumentException("Improper profile format: could not read remote messaging section");
        }
    }

    private void readRemoteAuth(String section) {
        String api = removeFirstAndLastLines(section);
        try {
            readSection(new StringReader(api), getSectionDetailDelimiters(this::readRemoteAuthPrivateKey, this::readRemoteAuthCertificate, this::readRemoteAuthCaCertificate));
        } catch (IOException e) {
            e.printStackTrace();
            throw new IllegalArgumentException("Improper profile format: could not read remote auth section");
        }
    }

    private void readClientCertAndKey(String section, Consumer<String> readKey, Consumer<String> readCert) {
        String data = removeFirstAndLastLines(section);
        try {
            readSection(new StringReader(data), getKeyAndCertDelimiters(readKey, readCert));
        } catch (IOException e) {
            e.printStackTrace();
            throw new IllegalArgumentException("Improper profile format: could not read client section");
        }
    }

    private void readCaCert(String section, Consumer<String> readCert) {
        String data = removeFirstAndLastLines(section);
        try {
            readSection(new StringReader(data), getKeyAndCertDelimiters(null, readCert));
        } catch (IOException e) {
            e.printStackTrace();
            throw new IllegalArgumentException("Improper profile format: could not read ca certificate");
        }
    }


    private void readProfileId(String section) {
        if (section == null || section.length() == 0)
            throw new IllegalArgumentException("Improper profile format: no profile id found");

        String data = removeFirstAndLastLines(section);

        String lower = data.toLowerCase().trim();

        verifyProfileId(lower);

        id = lower;
    }

    static void verifyProfileId(String id) {
        if (id.length() != PROFILE_ID_LENGTH)
            throw new IllegalArgumentException("Profile id should be " + PROFILE_ID_LENGTH + " characters long.");
        for (int i = 0; i < id.length(); i++) {
            if (!profileHexString.contains(id.substring(i, i+1))) {
                throw new IllegalArgumentException("Improper profile format: profile must only contain characters 0-9 and a-f");
            }
        }
    }

    private void readRemoteMessagePrivateKey(String privateKey) {
        try {
            remoteMessageContext.setPrivateKey(privateKey);
        } catch (TLSContextException e) {
            throw new IllegalArgumentException("Problem reading remote messaging client key: " + e.getMessage());
        }
    }

    private void readRemoteMessageCertificate(String certificate) {
        try {
            remoteMessageContext.setCertificate(certificate);
        } catch (TLSContextException e) {
            throw new IllegalArgumentException("Problem reading remote messaging certificate: " + e.getMessage());
        }
    }

    private void readRemoteMessageCaCertificate(String certificate) {
        try {
            remoteMessageContext.setCaCertificate(certificate);
        } catch (TLSContextException e) {
            throw new IllegalArgumentException("Problem reading remote messaging ca certificate: " + e.getMessage());
        }
    }

    private void readRemoteAuthPrivateKey(String privateKey) {
        try {
            remoteAuthContext.setPrivateKey(privateKey);
        } catch (TLSContextException e) {
            throw new IllegalArgumentException("Problem reading remote auth client key: " + e.getMessage());
        }
    }

    private void readRemoteAuthCertificate(String certificate) {
        try {
            remoteAuthContext.setCertificate(certificate);
        } catch (TLSContextException e) {
            throw new IllegalArgumentException("Problem reading remote auth certificate: " + e.getMessage());
        }
    }

    private void readRemoteAuthCaCertificate(String certificate) {
        try {
            remoteAuthContext.setCaCertificate(certificate);
        } catch (TLSContextException e) {
            throw new IllegalArgumentException("Problem reading remote auth ca certificate: " + e.getMessage());
        }
    }

    private void readNodePrivateKey(String privateKey) {
        try {
            nodeContext.setPrivateKey(privateKey);
        } catch (TLSContextException e) {
            throw new IllegalArgumentException("Problem reading node private key: " + e.getMessage());
        }
    }

    private void readNodeCertificate(String certificate) {
        try {
            nodeContext.setCertificate(certificate);
        } catch (TLSContextException e) {
            throw new IllegalArgumentException("Problem reading node certificate: " + e.getMessage());
        }
    }

    private void readNodeCaCertificate(String certificate) {
        try {
            nodeContext.setCaCertificate(certificate);
        } catch (TLSContextException e) {
            throw new IllegalArgumentException("Problem reading node ca certificate: " + e.getMessage());
        }
    }

    public byte[] toByteArray() {
        StringBuilder buf = new StringBuilder();
        Base64.Encoder encoder = Base64.getMimeEncoder(64, lineSeparator.getBytes(StandardCharsets.UTF_8));

        if (id != null && id.length() > 0) {
            buf.append(profileIdStartDelimiter).append(lineSeparator);
            buf.append(id).append(lineSeparator);
            buf.append(profileIdEndDelimiter).append(lineSeparator);
        }

        if (nodeContext != null && nodeContext.hasValue()) {
            writeSection(encoder, buf, nodeContext, nodeStartDelimiter, nodeEndDelimiter);
        }

        if (remoteMessageContext != null && remoteMessageContext.hasValue()) {
            writeSection(encoder, buf, remoteMessageContext, remoteMessageStartDelimiter, remoteMessageEndDelimiter);
        }

        if (remoteAuthContext != null && remoteAuthContext.hasValue()) {
            writeSection(encoder, buf, remoteAuthContext, remoteAuthStartDelimiter, remoteAuthEndDelimiter);
        }

        return buf.toString().getBytes(StandardCharsets.UTF_8);
    }

    private void writeSection(Base64.Encoder encoder, StringBuilder buf, TLSContext context, String startDelimiter, String endDelimiter) {
        if (context.hasValue()) {
            try {
                String cert = encoder.encodeToString(context.getCertificate().getEncoded());
                String privateKey = encoder.encodeToString(context.getPrivateKey().getEncoded());
                String cacert = encoder.encodeToString(context.getCaCertificate().getEncoded());

                buf.append(startDelimiter).append(lineSeparator);

                buf.append(clientCertAndKeyStartDelimiter).append(lineSeparator);

                buf.append(privateKeyStartDelimiter).append(lineSeparator);
                buf.append(privateKey).append(lineSeparator);
                buf.append(privateKeyEndDelimiter).append(lineSeparator);

                buf.append(certificateStartDelimiter).append(lineSeparator);
                buf.append(cert).append(lineSeparator);
                buf.append(certificateEndDelimiter).append(lineSeparator);

                buf.append(clientCertAndKeyEndDelimiter).append(lineSeparator);

                buf.append(caCertStartDelimiter).append(lineSeparator);

                buf.append(certificateStartDelimiter).append(lineSeparator);
                buf.append(cacert).append(lineSeparator);
                buf.append(certificateEndDelimiter).append(lineSeparator);

                buf.append(caCertEndDelimiter).append(lineSeparator);

                buf.append(endDelimiter).append(lineSeparator);

            } catch (CertificateEncodingException ignored) {}
        }
    }

    private static String removeFirstAndLastLines(String target) {
        String data = target.substring(target.indexOf(lineSeparator)+1, target.lastIndexOf(lineSeparator));
        return data.substring(0, data.lastIndexOf(lineSeparator)+1);
    }

    @Override
    public String toString() {
        return new String(toByteArray(), StandardCharsets.UTF_8);
    }

    @Override
    public boolean equals(Object other) {
        Profile otherProfile = (Profile)other;

        if (other == null)
            return false;

        return Arrays.equals(toByteArray(), otherProfile.toByteArray());
    }

    @Override
    public int hashCode() {
        return Objects.hash(toString());
    }

    public String getId() {
        return id;
    }

    public TLSContext getNodeContext() {
        return nodeContext;
    }

    public TLSContext getRemoteMessageContext() {
        return remoteMessageContext;
    }

    public TLSContext getRemoteAuthContext() {
        return remoteAuthContext;
    }

    private final static String profileHexString = "0123456789abcdefghijklmnopqrstuv";

    private static final String profileIdStartDelimiter = "-----BEGIN PROFILE IDENTIFIER-----";
    private static final String profileIdEndDelimiter = "-----END PROFILE IDENTIFIER-----";

    private static final String nodeStartDelimiter = "-----BEGIN NODE-----";
    private static final String nodeEndDelimiter = "-----END NODE-----";

    private static final String remoteMessageStartDelimiter = "-----BEGIN REMOTE MESSAGING-----";
    private static final String remoteMessageEndDelimiter = "-----END REMOTE MESSAGING-----";

    private static final String remoteAuthStartDelimiter = "-----BEGIN REMOTE AUTH-----";
    private static final String remoteAuthEndDelimiter = "-----END REMOTE AUTH-----";

    private static final String clientCertAndKeyStartDelimiter = "-----BEGIN CLIENT CERT AND KEY-----";
    private static final String clientCertAndKeyEndDelimiter = "-----END CLIENT CERT AND KEY-----";

    private static final String caCertStartDelimiter = "-----BEGIN CA CERT-----";
    private static final String caCertEndDelimiter = "-----END CA CERT-----";

    private static final String privateKeyStartDelimiter = "-----BEGIN PRIVATE KEY-----";
    private static final String privateKeyEndDelimiter = "-----END PRIVATE KEY-----";
    private static final String certificateStartDelimiter = "-----BEGIN CERTIFICATE-----";
    private static final String certificateEndDelimiter = "-----END CERTIFICATE-----";

    private Map<String, Map.Entry<String, Consumer<String>>> sectionDelimiters =
            Collections.unmodifiableMap(Stream.of(
                    getEntry(profileIdStartDelimiter,
                            profileIdEndDelimiter,
                            this::readProfileId),
                    getEntry(nodeStartDelimiter,
                            nodeEndDelimiter,
                            this::readNode),
                    getEntry(remoteMessageStartDelimiter,
                            remoteMessageEndDelimiter,
                            this::readRemoteMessaging),
                    getEntry(remoteAuthStartDelimiter,
                            remoteAuthEndDelimiter,
                            this::readRemoteAuth)
            ).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)));

    private Map<String, Map.Entry<String, Consumer<String>>> getSectionDetailDelimiters(Consumer<String> readKey, Consumer<String> readCert, Consumer<String> readCaCert) {
        return Collections.unmodifiableMap(Stream.of(
                getEntry(clientCertAndKeyStartDelimiter,
                        clientCertAndKeyEndDelimiter,
                        str -> readClientCertAndKey(str, readKey, readCert)),
                getEntry(caCertStartDelimiter,
                        caCertEndDelimiter,
                        str -> readCaCert(str, readCaCert))
        ).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)));
    }

    private Map<String, Map.Entry<String, Consumer<String>>> getKeyAndCertDelimiters(Consumer<String> readKey, Consumer<String> readCert) {
        return Collections.unmodifiableMap(Stream.of(
                getEntry(privateKeyStartDelimiter,
                        privateKeyEndDelimiter,
                        readKey),
                getEntry(certificateStartDelimiter,
                        certificateEndDelimiter,
                        readCert)
        ).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)));
    }

    private static Map.Entry<String, Map.Entry<String, Consumer<String>>> getEntry(String startDelimiter, String endDelimiter, Consumer<String> parser) {
        return new SimpleEntry<>(startDelimiter, new SimpleEntry<>(endDelimiter, parser));
    }
}