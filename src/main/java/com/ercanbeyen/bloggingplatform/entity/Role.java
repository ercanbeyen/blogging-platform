package com.ercanbeyen.bloggingplatform.entity;

import com.ercanbeyen.bloggingplatform.constant.enums.RoleName;
import lombok.Builder;
import lombok.Data;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Builder
@Data
@Document
public class Role {
    @MongoId(FieldType.OBJECT_ID)
    private String id;
    private RoleName roleName;
}
