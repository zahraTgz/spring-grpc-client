package com.example.grpcclient.service;

import com.example.grpcclient.InternalSystemException;
import com.example.grpcclient.model.BasicInfo;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.MoreExecutors;
import com.isc.mcb.rpc.bse.*;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * @author z.Taghizadeh
 */
@Component
public class BasicInfoNonBlockingService {

    @Value("${local.grpc.in-process-server-name}")
    private String IN_PROCESS_SERVER_NAME;

    @Value("${local.grpc.port}")
    private int PORT;

    private BasicInfoServiceGrpc.BasicInfoServiceFutureStub nonBlockingStub;

    private ManagedChannel managedChannel;


    @PostConstruct
    private void init() {
        managedChannel = ManagedChannelBuilder
                .forAddress(IN_PROCESS_SERVER_NAME, PORT).usePlaintext().build();

        nonBlockingStub =
                BasicInfoServiceGrpc.newFutureStub(managedChannel);
    }


    public BasicInfo getBasicInfoById(Long id) throws InternalSystemException {

        BasicInfoInput info = BasicInfoInput.newBuilder().setId(id).build();
        ListenableFuture<BasicInfoDataOutput> future = nonBlockingStub.getBasicInfoById(info);

        BasicInfo basicInfo = new BasicInfo();
        Futures.addCallback(future, new FutureCallback<BasicInfoDataOutput>() {
            @Override
            public void onSuccess(BasicInfoDataOutput result) {
                basicInfo.setId(result.getId());
                basicInfo.setCode((int) result.getCode());
                basicInfo.setName(result.getName());
                basicInfo.setEnglishName(result.getEnglishName());
                basicInfo.setActive(result.getIsActive());
            }

            @Override
            public void onFailure(Throwable t) {
                throw new InternalSystemException(((StatusRuntimeException) t).getStatus().getDescription());
            }
        }, MoreExecutors.directExecutor());
        try {
            future.get();
            managedChannel.shutdown();

        } catch (ExecutionException | InterruptedException e) {
            throw new InternalSystemException(e);
        }
        return basicInfo;
    }


    public List<BasicInfo> getAllBasicInfo() {

        BasicInfoInput info = BasicInfoInput.newBuilder().build();
        ListenableFuture<BasicInfoDataOutputList> future = nonBlockingStub.getAllBasicInfo(info);

        List<BasicInfo> basicInfoList = new ArrayList<>();
        Futures.addCallback(future, new FutureCallback<BasicInfoDataOutputList>() {
            @Override
            public void onSuccess(BasicInfoDataOutputList result) {

                for (BasicInfoDataOutput o : result.getItemsList()) {
                    BasicInfo basicInfo = new BasicInfo();
                    basicInfo.setId(o.getId());
                    basicInfo.setCode((int) o.getCode());
                    basicInfo.setEnglishName(o.getEnglishName());
                    basicInfo.setName(o.getName());
                    basicInfo.setActive(o.getIsActive());
                    basicInfoList.add(basicInfo);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                throw new InternalSystemException(((StatusRuntimeException) t).getStatus().getDescription());

            }
        }, MoreExecutors.directExecutor());
        try {
            future.get();
        } catch (ExecutionException | InterruptedException e) {
            throw new InternalSystemException(e);
        }
        managedChannel.shutdown();
        return basicInfoList;
    }

    public int saveBasicInfo(BasicInfo basicInfo) {

        BasicInfoInput info = BasicInfoInput.newBuilder()
                .setCode(basicInfo.getCode())
                .setName(basicInfo.getName())
                .setEnglishName(basicInfo.getEnglishName())
                .setIsActive(basicInfo.getActive())
                .build();
        ListenableFuture<Output> future = nonBlockingStub.insertBasicInfo(info);

        final Integer[] resultCode = new Integer[1];
        Futures.addCallback(future, new FutureCallback<Output>() {

            @Override
            public void onSuccess(Output result) {
                resultCode[0] = ((int) result.getErrorCode());
            }

            @Override
            public void onFailure(Throwable t) {
                throw new InternalSystemException(((StatusRuntimeException) t).getStatus().getDescription());
            }
        }, MoreExecutors.directExecutor());
        try {
            future.get();
            managedChannel.shutdown();
        } catch (ExecutionException | InterruptedException e) {
            throw new InternalSystemException(e);
        }

        return resultCode[0];
    }

}
