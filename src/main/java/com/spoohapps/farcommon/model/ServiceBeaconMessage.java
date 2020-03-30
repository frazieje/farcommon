package com.spoohapps.farcommon.model;

public class ServiceBeaconMessage {

    private String serviceName;
    private String profileId;
    private int apiPort;
    private int authApiPort;
    private String replicationRemoteHost;
    private int replicationRemotePort;

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getProfileId() {
        return profileId;
    }

    public void setProfileId(String profileId) {
        this.profileId = profileId;
    }

    public int getApiPort() {
        return apiPort;
    }

    public void setApiPort(int apiPort) {
        this.apiPort = apiPort;
    }

    public int getAuthApiPort() {
        return authApiPort;
    }

    public void setAuthApiPort(int authApiPort) {
        this.authApiPort = authApiPort;
    }

    public String getReplicationRemoteHost() {
        return replicationRemoteHost;
    }

    public void setReplicationRemoteHost(String replicationRemoteHost) {
        this.replicationRemoteHost = replicationRemoteHost;
    }

    public int getReplicationRemotePort() {
        return replicationRemotePort;
    }

    public void setReplicationRemotePort(int replicationRemotePort) {
        this.replicationRemotePort = replicationRemotePort;
    }

}
