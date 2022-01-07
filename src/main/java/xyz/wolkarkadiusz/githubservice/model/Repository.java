package xyz.wolkarkadiusz.githubservice.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class Repository {
    String name;
    String html_url;
    String description;

    Integer stargazers_count;
    Integer forks_counts;
}
