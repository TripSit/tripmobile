package me.tripsit.mobile.error;

public interface ConnectionErrorHandler extends ErrorHandler {
    /**
     * Handle connection error and return true if the user wants to retry
     * @return Returns true if the user wants to retry the connection
     */
    public boolean handleConnectionError();
}
