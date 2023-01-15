package com.ercanbeyen.bloggingplatform.document;

import com.ercanbeyen.bloggingplatform.constant.RoleName;
import lombok.Builder;
import lombok.Data;

import org.springframework.data.annotation.Id;
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
