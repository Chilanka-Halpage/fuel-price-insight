package com.csh.fuelpriceinsight.commentsmanagerservice.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Data
@Document
public class Comment {
    @Id
    private String id;
    private String description;
    private String userName;
    private int userId;
    private Instant createdAt;
}
