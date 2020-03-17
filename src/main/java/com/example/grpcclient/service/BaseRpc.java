package com.example.grpcclient.service;

import com.example.grpcclient.InternalSystemException;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.MoreExecutors;
import io.grpc.StatusRuntimeException;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutionException;

@Component
public class BaseRpc<T> {

    public Object callBack(ListenableFuture future, T t) {
        Futures.addCallback(future, new FutureCallback<T>() {
            @Override
            public void onSuccess(T t) {
                assert t.equals(t);
            }

            @Override
            public void onFailure(Throwable t) {
                throw new InternalSystemException(((StatusRuntimeException) t).getStatus().getDescription());
            }
        }, MoreExecutors.directExecutor());
        try {
            return future.get();
        } catch (ExecutionException e) {
            throw new InternalSystemException(e);
        } catch (InterruptedException e) {
            throw new InternalSystemException(e);
        }
    }

}
