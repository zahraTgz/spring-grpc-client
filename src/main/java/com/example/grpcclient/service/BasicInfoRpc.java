package com.example.grpcclient.service;

import com.example.grpcclient.InternalSystemException;
import com.example.grpcclient.model.BasicInfo;
import com.example.grpcclient.util.ChannelGrpc;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.MoreExecutors;
import com.isc.mcb.rpc.bse.*;
import io.grpc.StatusRuntimeException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * @author z.Taghizadeh
 */
@Service
public class BasicInfoRpc {


    public BasicInfo getBasicInfoById(Long id) throws InternalSystemException {
        BasicInfoServiceGrpc.BasicInfoServiceFutureStub stub = BasicInfoServiceGrpc.newFutureStub(ChannelGrpc.channel);

        BasicInfoInput info = BasicInfoInput.newBuilder()
                .setId(id)
                .build();

        ListenableFuture<BasicInfoDataOutput> future = stub.getBasicInfoById(info);

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
        } catch (ExecutionException e) {
            throw new InternalSystemException(e);
        } catch (InterruptedException e) {
            throw new InternalSystemException(e);
        }
        return basicInfo;
    }


    public List<BasicInfo> getAllBasicInfo() {
        BasicInfoServiceGrpc.BasicInfoServiceFutureStub stub = BasicInfoServiceGrpc.newFutureStub(ChannelGrpc.channel);

        BasicInfoInput info = BasicInfoInput.newBuilder()
                .build();

        ListenableFuture<BasicInfoDataOutputList> future = stub.getAllBasicInfo(info);

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
        } catch (ExecutionException e) {
            throw new InternalSystemException(e);
        } catch (InterruptedException e) {
            throw new InternalSystemException(e);
        }
        return basicInfoList;
    }

    public int saveBasicInfo(BasicInfo basicInfo) {

        BasicInfoInput info = BasicInfoInput.newBuilder()
                .setCode(basicInfo.getCode())
                .setName(basicInfo.getName())
                .setEnglishName(basicInfo.getEnglishName())
                .setIsActive(basicInfo.getActive())
                .build();

        BasicInfoServiceGrpc.BasicInfoServiceFutureStub stub = BasicInfoServiceGrpc.newFutureStub(ChannelGrpc.channel);

        ListenableFuture<Output> future = stub.insertBasicInfo(info);
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
        } catch (ExecutionException e) {
            throw new InternalSystemException(e);
        } catch (InterruptedException e) {
            throw new InternalSystemException(e);
        }
        return resultCode[0];
    }


}
