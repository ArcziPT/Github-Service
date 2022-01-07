package xyz.wolkarkadiusz.githubservice.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class GitRepo {
    String name;
    String html_url;
    String description;

    Integer stargazers_count;
    Integer forks_count;
}
