package com.spoohapps.farcommon.model;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use=JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = ReadProfilePermission.class, name = "readAnyDevice"),

        @JsonSubTypes.Type(value = WriteProfilePermission.class, name = "writeAnyDevice")
})
public interface Permission {

    boolean checkFor(User user);

}
