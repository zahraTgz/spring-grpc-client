package com.example.grpcclient.service;

import com.example.grpcclient.InternalSystemException;
import com.example.grpcclient.mapper.BasicInfoMapper;
import com.example.grpcclient.model.BasicInfo;
import com.google.protobuf.Int64Value;
import com.isc.mcb.rpc.bse.BasicInfoMessage;
import com.isc.mcb.rpc.bse.BasicInfoServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @author z.Taghizadeh
 */
@Component
public class BasicInfoBlockingService {

    @Value("${local.grpc.in-process-server-name}")
    private String inProcessServerName;

    @Value("${local.grpc.port}")
    private int port;

    private BasicInfoServiceGrpc.BasicInfoServiceBlockingStub blockingStub;

    private BasicInfoMapper basicInfoMapper = Mappers.getMapper(BasicInfoMapper.class);

    @PostConstruct
    private void init() {
        ManagedChannel managedChannel = ManagedChannelBuilder
                .forAddress(inProcessServerName, port).usePlaintext().build();

        blockingStub =
                BasicInfoServiceGrpc.newBlockingStub(managedChannel);
    }

    public BasicInfo getBasicInfoById(Long id) throws InternalSystemException {
        try {
            Int64Value inputData = Int64Value.newBuilder().setValue(id).build();
            BasicInfoMessage result = blockingStub.getBasicInfoById(inputData);
            return basicInfoMapper.fromBasicInfoDataOutput(result);
        } catch (Exception e) {
            throw new InternalSystemException(((StatusRuntimeException) e).getStatus().getDescription());
        }
    }

}
