package com.spoohapps.farcommon.model;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use=JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = ReadDevicePermission.class, name = "readDevice"),

        @JsonSubTypes.Type(value = WriteDevicePermission.class, name = "writeDevice")
})
public interface Permission {

    boolean checkFor(User user);

}
