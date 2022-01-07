package xyz.wolkarkadiusz.githubservice.repository;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;
import xyz.wolkarkadiusz.githubservice.model.GitRepo;

import java.util.*;

@Repository
public class GitReposRepository {
    private final RestTemplate restTemplate;
    private final String baseUrl;

    @Autowired
    public GitReposRepository(RestTemplate restTemplate,
                              @Value("${github-service.url}") String baseUrl){
        this.restTemplate = restTemplate;
        this.baseUrl = baseUrl;
    }

    public List<GitRepo> findByUsername(String username){
        var response = restTemplate.getForEntity(baseUrl + "/users/" + username + "/repos", GitRepo[].class);
        if(response.hasBody() && response.getBody() != null)
            return new ArrayList<GitRepo>(Arrays.asList(response.getBody()));

        return new ArrayList<>();
    }

    public Map<String, Integer> getRepoLanguages(String username, String repo){
        var response = restTemplate.getForEntity(baseUrl + "/repos/" + username + "/" + repo + "/languages", JsonNode.class);
        if(response.hasBody() && response.getBody() != null){
            var json = response.getBody();

            Map<String, Integer> languages = new HashMap<>();
            json.fields().forEachRemaining(e -> languages.put(e.getKey(), e.getValue().asInt()));
            return languages;
        }

        return new HashMap<>();
    }
}
