package com.csh.fuelpriceinsight.commentsmanagerservice.service;

import com.csh.fuelpriceinsight.commentsmanagerservice.model.Comment;
import com.csh.fuelpriceinsight.commentsmanagerservice.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CommentManagerServiceImpl implements CommentManagerService {

    private final CommentRepository commentRepository;

    @RabbitListener(queues = "${rabbitmq.queue.name}")
    public void receiveMessage(Comment comment) {
        commentRepository.save(comment);
    }

    @Override
    public List<Comment> getPriceComments() {
        return commentRepository.findAll();
    }
}
