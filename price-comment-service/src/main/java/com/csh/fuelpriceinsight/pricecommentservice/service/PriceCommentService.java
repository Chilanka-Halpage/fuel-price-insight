package com.csh.fuelpriceinsight.pricecommentservice.service;

import com.csh.fuelpriceinsight.pricecommentservice.model.PriceComment;

import java.util.List;

public interface PriceCommentService {
    void addComment(PriceComment comment);

    List<PriceComment> getAllComments();
}
