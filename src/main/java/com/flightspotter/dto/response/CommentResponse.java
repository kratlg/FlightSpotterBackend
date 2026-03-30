package com.flightspotter.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class CommentResponse {
    private UUID id;
    private UUID spotId;
    private UUID userId;
    private String username;
    private String displayName;
    private String content;
    private LocalDateTime createdAt;
}
