package com.example.grpcclient.service;

import com.example.grpcclient.InternalSystemException;
import com.example.grpcclient.mapper.BasicInfoMapper;
import com.example.grpcclient.model.BasicInfo;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.protobuf.Empty;
import com.google.protobuf.Int64Value;
import com.isc.mcb.rpc.bse.BasicInfoDataOutputList;
import com.isc.mcb.rpc.bse.BasicInfoMessage;
import com.isc.mcb.rpc.bse.BasicInfoServiceGrpc;
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
    private String inProcessServerName;

    @Value("${local.grpc.port}")
    private int port;

    private BasicInfoServiceGrpc.BasicInfoServiceFutureStub nonBlockingStub;

    private ManagedChannel managedChannel;

    @Autowired
    private BaseRpc baseRpc;

    private BasicInfoMapper basicInfoMapper = Mappers.getMapper(BasicInfoMapper.class);

    @PostConstruct
    private void init() {
        managedChannel = ManagedChannelBuilder
                .forAddress(inProcessServerName, port).usePlaintext().build();

        nonBlockingStub =
                BasicInfoServiceGrpc.newFutureStub(managedChannel);
    }


    public BasicInfo getBasicInfoById(Long id) throws InternalSystemException {

        Int64Value inputData = Int64Value.newBuilder().setValue(id).build();
        ListenableFuture<BasicInfoMessage> future = nonBlockingStub.getBasicInfoById(inputData);
        return basicInfoMapper.fromBasicInfoDataOutput((BasicInfoMessage) baseRpc.callBack(future, BasicInfoMessage.newBuilder().build()));
    }

    public List<BasicInfo> getAllBasicInfo() {

        ListenableFuture<BasicInfoDataOutputList> future = nonBlockingStub.getAllBasicInfo(Empty.newBuilder().build());
        return basicInfoMapper.fromBasicInfoDataOutputList(((BasicInfoDataOutputList) baseRpc.callBack(future, BasicInfoDataOutputList.newBuilder().build())).getItemsList());
    }

    public void saveBasicInfo(BasicInfo basicInfo) {

        ListenableFuture<Empty> future = nonBlockingStub.insertBasicInfo(basicInfoMapper.fromBasicInfo(basicInfo));
        baseRpc.callBack(future, Empty.newBuilder().build());
    }

}
