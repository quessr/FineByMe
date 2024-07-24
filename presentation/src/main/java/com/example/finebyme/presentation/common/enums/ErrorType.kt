package com.example.finebyme.presentation.common.enums

enum class ErrorType(val message: String) {
    NETWORK_ERROR("Network error. Please try again."),
    BAD_REQUEST("Bad Request: The request was unacceptable, often due to missing a required parameter."),
    UNAUTHORIZED("Unauthorized: Invalid Access Token."),
    FORBIDDEN("Forbidden: Missing permissions to perform request."),
    NOT_FOUND("Not Found: The requested resource doesn’t exist."),
    SERVER_ERROR("Server Error: Something went wrong on our end. Please try again later."),
    NO_INTERNET("No internet connection. Please check your connection."),
    UNKNOWN_ERROR("An unknown error occurred. Please try again.")
}