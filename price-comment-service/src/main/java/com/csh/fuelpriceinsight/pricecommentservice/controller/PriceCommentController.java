package com.csh.fuelpriceinsight.pricecommentservice.controller;

import com.csh.fuelpriceinsight.pricecommentservice.model.PriceComment;
import com.csh.fuelpriceinsight.pricecommentservice.service.PriceCommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/price-comments")
public class PriceCommentController {

    private final PriceCommentService priceCommentService;

    @PostMapping
    public ResponseEntity<String> addComment(@RequestHeader(value = "X-User-Id") String userId, @RequestHeader(value = "X-Username") String username, @RequestBody PriceComment comment) {
        comment.setUserId(userId);
        comment.setUserName(username);
        comment.setLastUpdatedAt(new Date().toInstant());
        priceCommentService.addComment(comment);
        return ResponseEntity.status(HttpStatus.CREATED).body("\"Comment Added Successfully\"");
    }

    @GetMapping
    public ResponseEntity<List<PriceComment>> getComments() {
        return ResponseEntity.ok(priceCommentService.getAllComments());
    }
}
