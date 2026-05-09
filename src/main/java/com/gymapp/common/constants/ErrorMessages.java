package com.gymapp.common.constants;

public final class ErrorMessages {

    public static final String INVALID_CREDENTIALS = "Invalid username or password";
    public static final String EMAIL_ALREADY_EXISTS = "Email is already registered";
    public static final String USERNAME_ALREADY_EXISTS = "Username is already taken";
    public static final String USER_NOT_FOUND = "User not found";
    public static final String ACCESS_DENIED = "Access denied";
    public static final String TOKEN_EXPIRED = "Token expired";
    public static final String INVALID_TOKEN = "Invalid token";
    public static final String CURRENT_PASSWORD_INCORRECT = "Current password is incorrect";
    public static final String NEW_PASSWORD_MUST_BE_DIFFERENT = "New password must be different from current password";
    public static final String RESET_TOKEN_INVALID_OR_EXPIRED = "Reset token is invalid or expired";
    public static final String DB_CONSTRAINT_VIOLATION = "Request violates database constraints";
    public static final String SOMETHING_WENT_WRONG = "Something went wrong";
    public static final String INVALID_PAYMENT_SIGNATURE = "Invalid payment signature";
    public static final String NO_SUBSCRIPTION_FOUND_FOR_USER_ID = "No subscription found for user id: ";
    public static final String USER_NOT_FOUND_WITH_ID = "User not found with id: ";
    public static final String SUBSCRIPTION_NOT_FOUND_WITH_ID = "Subscription not found with id: ";
    public static final String PAYMENT_NOT_FOUND_FOR_ORDER_ID = "Payment not found for order id: ";
    public static final String ACTIVE_SUBSCRIPTION_ALREADY_EXISTS = "Active subscription already exists";
    public static final String ONLY_ACTIVE_SUBSCRIPTIONS_CAN_BE_CANCELED = "Only active subscriptions can be canceled";
    public static final String CANNOT_CHANGE_OWN_ROLE = "You cannot change your own role";
    public static final String CANNOT_DEMOTE_LAST_ADMIN = "Cannot demote the last active admin user";

    private ErrorMessages() {
    }
}


