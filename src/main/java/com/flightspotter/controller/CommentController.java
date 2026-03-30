package com.flightspotter.controller;

import com.flightspotter.controller.base.BaseController;
import com.flightspotter.dto.common.ApiResponse;
import com.flightspotter.dto.request.CommentRequest;
import com.flightspotter.dto.response.CommentResponse;
import com.flightspotter.enums.AppMessage;
import com.flightspotter.repository.UserRepository;
import com.flightspotter.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/spots/{spotId}/comments")
@RequiredArgsConstructor
public class CommentController extends BaseController {

    private final CommentService commentService;
    private final UserRepository userRepository;

    @GetMapping
    public ResponseEntity<ApiResponse<List<CommentResponse>>> getComments(@PathVariable UUID spotId) {
        return ok(AppMessage.COMMENTS_FOUND, commentService.getComments(spotId));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<CommentResponse>> addComment(
            @PathVariable UUID spotId,
            @RequestBody @Valid CommentRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        UUID userId = getUserId(userDetails);
        return created(AppMessage.COMMENT_ADDED, commentService.addComment(spotId, request, userId));
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<ApiResponse<Void>> deleteComment(
            @PathVariable UUID spotId,
            @PathVariable UUID commentId,
            @AuthenticationPrincipal UserDetails userDetails) {
        UUID userId = getUserId(userDetails);
        commentService.deleteComment(commentId, userId);
        return ok(AppMessage.COMMENT_DELETED);
    }

    private UUID getUserId(UserDetails userDetails) {
        return userRepository.findByEmailAndDeletedFalse(userDetails.getUsername())
                .orElseThrow()
                .getId();
    }
}
