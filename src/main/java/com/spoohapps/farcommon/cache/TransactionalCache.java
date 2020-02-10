package com.spoohapps.farcommon.cache;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface TransactionalCache<T> extends Cache<T> {

    CompletableFuture<Boolean> beginTransaction();

    CompletableFuture<Boolean> abortTransaction();

    CompletableFuture<List<Object>> executeTransaction();

}
