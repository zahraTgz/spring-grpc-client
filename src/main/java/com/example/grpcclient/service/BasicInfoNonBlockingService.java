package com.example.grpcclient.service;

import com.example.grpcclient.InternalSystemException;
import com.example.grpcclient.mapper.BasicInfoMapper;
import com.example.grpcclient.model.BasicInfo;
import com.google.common.util.concurrent.ListenableFuture;
import com.isc.mcb.rpc.bse.*;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * @author z.Taghizadeh
 */
@Service
public class BasicInfoNonBlockingService {

    @Value("${local.grpc.in-process-server-name}")
    private String IN_PROCESS_SERVER_NAME;

    @Value("${local.grpc.port}")
    private int PORT;

    private BasicInfoServiceGrpc.BasicInfoServiceFutureStub nonBlockingStub;

    private ManagedChannel managedChannel;

    @Autowired
    private BaseRpc baseRpc;

    private BasicInfoMapper basicInfoMapper = Mappers.getMapper(BasicInfoMapper.class);

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
        return basicInfoMapper.fromBasicInfoDataOutput((BasicInfoDataOutput) baseRpc.callBack(future, BasicInfoDataOutput.newBuilder().build()));
    }

    public List<BasicInfo> getAllBasicInfo() {

        BasicInfoInput info = BasicInfoInput.newBuilder().build();
        ListenableFuture<BasicInfoDataOutputList> future = nonBlockingStub.getAllBasicInfo(info);
        return basicInfoMapper.fromBasicInfoDataOutputList(((BasicInfoDataOutputList) baseRpc.callBack(future, BasicInfoDataOutputList.newBuilder().build())).getItemsList());
    }

    public int saveBasicInfo(BasicInfo basicInfo) {

        ListenableFuture<Output> future = nonBlockingStub.insertBasicInfo(basicInfoMapper.fromBasicInfo(basicInfo));
        return (int) ((Output) baseRpc.callBack(future, Output.newBuilder().build())).getId();
    }

}
