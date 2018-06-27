package com.spoohapps.farcommon.connection;

import com.spoohapps.farcommon.model.Message;

public interface PublisherConnection {
    void open();
    void close();
    void onClosed(Runnable closed);
    void publish(Message message);
    String getDescription();
}
