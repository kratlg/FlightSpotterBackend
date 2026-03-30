package com.flightspotter.service;

import com.flightspotter.dto.request.CommentRequest;
import com.flightspotter.dto.response.CommentResponse;

import java.util.List;
import java.util.UUID;

public interface CommentService {
    CommentResponse addComment(UUID spotId, CommentRequest request, UUID userId);
    List<CommentResponse> getComments(UUID spotId);
    void deleteComment(UUID commentId, UUID userId);
}
