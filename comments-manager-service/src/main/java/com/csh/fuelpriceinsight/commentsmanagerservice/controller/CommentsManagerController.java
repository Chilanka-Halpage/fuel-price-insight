package com.csh.fuelpriceinsight.commentsmanagerservice.controller;

import com.csh.fuelpriceinsight.commentsmanagerservice.model.Comment;
import com.csh.fuelpriceinsight.commentsmanagerservice.service.CommentManagerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/comments-manager")
public class CommentsManagerController {
    private final CommentManagerService commentManagerService;

    @GetMapping("/price")
    public ResponseEntity<List<Comment>> getPriceComments() {
        return ResponseEntity.ok().body(commentManagerService.getPriceComments());
    }

}
