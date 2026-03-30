package com.flightspotter.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CommentRequest {

    @NotBlank(message = "Comment cannot be empty.")
    @Size(max = 500, message = "Comment must be 500 characters or less.")
    private String content;
}
