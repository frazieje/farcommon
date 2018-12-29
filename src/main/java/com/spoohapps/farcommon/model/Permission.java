package com.spoohapps.farcommon.model;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use=JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = ReadDevicePermission.class, name = "readDevice"),

        @JsonSubTypes.Type(value = WriteDevicePermission.class, name = "writeDevice"),

        @JsonSubTypes.Type(value = ManagerPermission.class, name = "manager")
})
public interface Permission {

    boolean checkFor(User user);

}
