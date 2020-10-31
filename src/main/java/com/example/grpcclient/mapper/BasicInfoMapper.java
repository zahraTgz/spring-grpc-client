package com.example.grpcclient.mapper;

import com.example.grpcclient.model.BasicInfo;
import com.isc.mcb.rpc.bse.BasicInfoMessage;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;

import java.util.List;

/**
 * @author z.Taghizadeh
 */
@Mapper(nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        uses = {BaseMapper.class})
public interface BasicInfoMapper {

    BasicInfo fromBasicInfoDataOutput(BasicInfoMessage basicInfoDataOutput);

    List<BasicInfo> fromBasicInfoDataOutputList(List<BasicInfoMessage> basicInfoDataOutputList);

    BasicInfoMessage fromBasicInfo(BasicInfo basicInfo);

}
