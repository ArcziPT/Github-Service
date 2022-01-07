package xyz.wolkarkadiusz.githubservice.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;
import xyz.wolkarkadiusz.githubservice.model.GitRepo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Repository
public class GitReposRepository {
    private final RestTemplate restTemplate;
    private final String baseUrl;

    @Autowired
    public GitReposRepository(RestTemplate restTemplate,
                              @Value("${github-service.url:https://api.github.com}") String baseUrl){
        this.restTemplate = restTemplate;
        this.baseUrl = baseUrl;
    }

    public List<GitRepo> findByUsername(String username){
        var response = restTemplate.getForEntity(baseUrl + "/users/" + username + "/repos", GitRepo[].class);
        if(response.hasBody() && response.getBody() != null)
            return new ArrayList<GitRepo>(Arrays.asList(response.getBody()));

        return new ArrayList<>();
    }
}
