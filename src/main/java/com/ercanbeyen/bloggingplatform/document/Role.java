package com.ercanbeyen.bloggingplatform.document;

import com.ercanbeyen.bloggingplatform.constant.RoleName;
import lombok.Builder;
import lombok.Data;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Builder
@Data
@Document
public class Role {
    @Id
    private String id;
    private RoleName roleName;
}
