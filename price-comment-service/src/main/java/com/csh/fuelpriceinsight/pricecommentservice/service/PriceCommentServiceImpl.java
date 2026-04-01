package com.csh.fuelpriceinsight.pricecommentservice.service;

import com.csh.fuelpriceinsight.pricecommentservice.model.PriceComment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
public class PriceCommentServiceImpl implements PriceCommentService {

    private final RabbitTemplate rabbitTemplate;
    private final WebClient commentManagerServiceWebClient;

    @Value("${rabbitmq.exchange.name}")
    private String exchangeName;

    @Value("${rabbitmq.routing.key}")
    private String routingKey;

    @Override
    public void addComment(PriceComment comment) {
        rabbitTemplate.convertAndSend(exchangeName, routingKey, comment);
    }

    @Override
    public List<PriceComment> getAllComments() {
        try {
            return commentManagerServiceWebClient.get().
                    uri("/api/comments-manager/price")
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .bodyToFlux(PriceComment.class)
                    .collectList()
                    .block();
        }catch (Exception e){
            log.error("Error in retrieving comments from comments manager service: {} ", e.getMessage());
            throw new RuntimeException("Error in retrieving comments from comments manager service: " + e.getMessage());
        }
    }
}
