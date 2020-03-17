package com.example.grpcclient.mapper;

import com.example.grpcclient.model.BasicInfo;
import com.isc.mcb.rpc.bse.BasicInfoDataOutput;
import com.isc.mcb.rpc.bse.BasicInfoInput;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;

import java.util.List;

/**
 * @author z.Taghizadeh
 */
@Mapper(nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface BasicInfoMapper {

    BasicInfo fromBasicInfoDataOutput(BasicInfoDataOutput basicInfoDataOutput);

    List<BasicInfo> fromBasicInfoDataOutputList(List<BasicInfoDataOutput> basicInfoDataOutputList);

    BasicInfoInput fromBasicInfo(BasicInfo basicInfo);

}
