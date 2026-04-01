package com.csh.fuelpriceinsight.commentsmanagerservice.repository;

import com.csh.fuelpriceinsight.commentsmanagerservice.model.Comment;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CommentRepository extends MongoRepository<Comment, String> {

}
