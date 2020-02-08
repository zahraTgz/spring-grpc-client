package com.example.grpcclient.util;

import io.grpc.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.TimeUnit;

/**
 * @author z.Taghizadeh
 */
@Component
public class ChannelGrpc extends ManagedChannel {


    @Value("${local.grpc.in-process-server-name}")
    private String IN_PROCESS_SERVER_NAME;

    @Value("${local.grpc.port}")
    private int PORT;

    public static ManagedChannel channel;

    @PostConstruct
    public void setup() {
        channel = ManagedChannelBuilder.forAddress(IN_PROCESS_SERVER_NAME, PORT)
                .usePlaintext()
                .build();
    }

    public ChannelGrpc() {
    }

    @Override
    public <RequestT, ResponseT> ClientCall<RequestT, ResponseT> newCall(MethodDescriptor<RequestT, ResponseT> methodDescriptor,
                                                                         CallOptions callOptions) {
        return null;
    }

    @Override
    public String authority() {
        return null;
    }

    @Override
    public ManagedChannel shutdown() {
        return null;
    }

    @Override
    public boolean isShutdown() {
        return false;
    }

    @Override
    public boolean isTerminated() {
        return false;
    }

    @Override
    public ManagedChannel shutdownNow() {
        return null;
    }

    @Override
    public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
        return false;
    }

}
