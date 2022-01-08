package xyz.wolkarkadiusz.githubservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Wrap error message.
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ErrorResponse {
    String error;
}
