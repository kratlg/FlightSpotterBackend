package com.flightspotter.serviceImpl;

import com.flightspotter.dto.request.CommentRequest;
import com.flightspotter.dto.response.CommentResponse;
import com.flightspotter.entity.Comment;
import com.flightspotter.entity.Spot;
import com.flightspotter.entity.User;
import com.flightspotter.enums.AppMessage;
import com.flightspotter.exception.AppException;
import com.flightspotter.repository.CommentRepository;
import com.flightspotter.repository.SpotRepository;
import com.flightspotter.repository.UserRepository;
import com.flightspotter.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final SpotRepository spotRepository;
    private final UserRepository userRepository;

    @Override
    public CommentResponse addComment(UUID spotId, CommentRequest request, UUID userId) {
        Spot spot = spotRepository.findByIdAndDeletedFalse(spotId)
                .orElseThrow(() -> AppException.notFound(AppMessage.SPOT_NOT_FOUND));
        User user = userRepository.findByIdAndDeletedFalse(userId)
                .orElseThrow(() -> AppException.notFound(AppMessage.USER_NOT_FOUND));

        Comment comment = Comment.builder()
                .spot(spot)
                .user(user)
                .content(request.getContent().trim())
                .build();

        return toResponse(commentRepository.save(comment));
    }

    @Override
    public List<CommentResponse> getComments(UUID spotId) {
        return commentRepository.findBySpotIdAndDeletedFalseOrderByCreatedAtAsc(spotId)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteComment(UUID commentId, UUID userId) {
        Comment comment = commentRepository.findByIdAndDeletedFalse(commentId)
                .orElseThrow(() -> AppException.notFound(AppMessage.COMMENT_NOT_FOUND));

        if (!comment.getUser().getId().equals(userId)) {
            throw AppException.forbidden(AppMessage.COMMENT_NOT_AUTHORIZED);
        }

        comment.setDeleted(true);
        commentRepository.save(comment);
    }

    private CommentResponse toResponse(Comment c) {
        return CommentResponse.builder()
                .id(c.getId())
                .spotId(c.getSpot().getId())
                .userId(c.getUser().getId())
                .username(c.getUser().getUsername())
                .displayName(c.getUser().getDisplayName())
                .content(c.getContent())
                .createdAt(c.getCreatedAt())
                .build();
    }
}
