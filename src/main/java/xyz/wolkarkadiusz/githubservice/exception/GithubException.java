package xyz.wolkarkadiusz.githubservice.exception;

/**
 * Thrown when GithubAPI returns error code.
 */
public class GithubException extends Exception {
    public GithubException() {
    }

    public GithubException(String message) {
        super(message);
    }
}
