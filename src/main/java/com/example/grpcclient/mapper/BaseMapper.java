package com.example.grpcclient.mapper;

import org.mapstruct.Mapper;

import java.util.Objects;

/**
 * @author z_taghizadeh
 */
@Mapper(componentModel = "spring")
public interface BaseMapper {

    default Boolean map(com.google.protobuf.BoolValue value) {
        if (Objects.isNull(value)) {
            return null;
        }
        return value.getValue();
    }

    default com.google.protobuf.BoolValue map(Boolean value) {
        if (Objects.isNull(value)) {
            return null;
        }
        return com.google.protobuf.BoolValue.of(value);
    }

    default String map(com.google.protobuf.StringValue value) {
        if (Objects.isNull(value)) {
            return null;
        }
        return value.getValue();
    }

    default com.google.protobuf.StringValue map(String value) {
        if (Objects.isNull(value)) {
            return null;
        }
        return com.google.protobuf.StringValue.of(value);
    }

    default Integer map(com.google.protobuf.Int32Value value) {
        if (Objects.isNull(value)) {
            return null;
        }
        return value.getValue();
    }

    default com.google.protobuf.Int32Value map(Integer value) {
        if (Objects.isNull(value)) {
            return null;
        }
        return com.google.protobuf.Int32Value.of(value);
    }

    default Long map(com.google.protobuf.Int64Value value) {
        if (Objects.isNull(value)) {
            return null;
        }
        return value.getValue();
    }

    default com.google.protobuf.Int64Value map(Long value) {
        if (Objects.isNull(value)) {
            return null;
        }
        return com.google.protobuf.Int64Value.of(value);
    }

}
