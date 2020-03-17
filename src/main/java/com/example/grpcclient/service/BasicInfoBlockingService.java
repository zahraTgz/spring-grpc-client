package com.example.grpcclient.service;

import com.example.grpcclient.InternalSystemException;
import com.example.grpcclient.mapper.BasicInfoMapper;
import com.example.grpcclient.model.BasicInfo;
import com.isc.mcb.rpc.bse.BasicInfoDataOutput;
import com.isc.mcb.rpc.bse.BasicInfoInput;
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
    private String IN_PROCESS_SERVER_NAME;

    @Value("${local.grpc.port}")
    private int PORT;

    private BasicInfoServiceGrpc.BasicInfoServiceBlockingStub blockingStub;

    private BasicInfoMapper basicInfoMapper = Mappers.getMapper(BasicInfoMapper.class);

    @PostConstruct
    private void init() {
        ManagedChannel managedChannel = ManagedChannelBuilder
                .forAddress(IN_PROCESS_SERVER_NAME, PORT).usePlaintext().build();

        blockingStub =
                BasicInfoServiceGrpc.newBlockingStub(managedChannel);
    }

    public BasicInfo getBasicInfoById(Long id) throws InternalSystemException {
        try {

            BasicInfoInput info = BasicInfoInput.newBuilder().setId(id).build();
            BasicInfoDataOutput result = blockingStub.getBasicInfoById(info);
            return basicInfoMapper.fromBasicInfoDataOutput(result);
        } catch (Exception e) {
            throw new InternalSystemException(((StatusRuntimeException) e).getStatus().getDescription());
        }
    }

}
