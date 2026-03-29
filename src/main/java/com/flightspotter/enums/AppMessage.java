package com.flightspotter.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AppMessage {

    // Auth
    REGISTER_SUCCESS("Registration successful. Welcome to FlightSpotter!"),
    LOGIN_SUCCESS("Login successful."),
    LOGOUT_SUCCESS("Logout successful."),
    INVALID_CREDENTIALS("Invalid email or password."),
    EMAIL_ALREADY_EXISTS("An account with this email already exists."),
    USERNAME_ALREADY_EXISTS("This username is already taken."),
    TOKEN_EXPIRED("Session expired. Please log in again."),
    TOKEN_INVALID("Invalid authentication token."),
    UNAUTHORIZED("You must be logged in to perform this action."),
    FORBIDDEN("You do not have permission to perform this action."),

    // User
    USER_NOT_FOUND("User not found."),
    USER_FOUND("User retrieved successfully."),
    USER_UPDATED("Profile updated successfully."),
    USER_DELETED("Account deleted successfully."),

    // Spot
    SPOT_CREATED("Aircraft spotted successfully! Added to your collection."),
    SPOT_FOUND("Spot retrieved successfully."),
    SPOTS_FOUND("Spots retrieved successfully."),
    SPOT_UPDATED("Spot updated successfully."),
    SPOT_DELETED("Spot removed from collection."),
    SPOT_NOT_FOUND("Spot not found."),
    SPOT_NOT_AUTHORIZED("You can only modify your own spots."),

    // Flight
    FLIGHTS_FETCHED("Live flight data retrieved successfully."),
    FLIGHT_DETAIL_FETCHED("Aircraft details retrieved successfully."),
    FLIGHT_API_ERROR("Unable to fetch live flight data. Please try again."),
    AIRCRAFT_NOT_FOUND("Aircraft information not found for this transponder code."),

    // File
    FILE_UPLOAD_SUCCESS("Image uploaded successfully."),
    FILE_UPLOAD_ERROR("Failed to upload image. Please try again."),
    FILE_TYPE_INVALID("Only image files are allowed (JPG, PNG, WEBP)."),
    FILE_SIZE_EXCEEDED("File size exceeds the 10MB limit."),

    // Generic
    SUCCESS("Operation completed successfully."),
    NOT_FOUND("Requested resource not found."),
    VALIDATION_ERROR("Validation failed. Please check your input."),
    INTERNAL_ERROR("An unexpected error occurred. Please try again later.");

    private final String message;
}
