package xyz.wolkarkadiusz.githubservice.repository;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import xyz.wolkarkadiusz.githubservice.exception.GithubException;
import xyz.wolkarkadiusz.githubservice.model.GitRepo;

import java.util.*;

/**
 * Repository, which uses Redis to cache GithubAPI responses.
 */
@Repository
@Slf4j
public class GitReposCacheableRepository {
    private final RestTemplate restTemplate;
    private final String baseUrl;

    @Autowired
    public GitReposCacheableRepository(RestTemplate restTemplate,
                                       @Value("${github-service.url}") String baseUrl){
        this.restTemplate = restTemplate;
        this.baseUrl = baseUrl;
    }

    @Cacheable("userReposCache")
    public List<GitRepo> findByUsername(String username, Integer per_page, Integer page) throws GithubException {
        log.info("findByUsername(" + username + ", " + per_page + ", " + page + ")");
        var urlTemplate = UriComponentsBuilder.fromHttpUrl(baseUrl + "/users/" + username + "/repos")
                .queryParam("per_page", per_page)
                .queryParam("page", page);

        try {
            var response = restTemplate.getForEntity(urlTemplate.encode().toUriString(), GitRepo[].class);
            if(response.hasBody() && response.getBody() != null)
                return new ArrayList<GitRepo>(Arrays.asList(response.getBody()));

            return new ArrayList<>();
        }catch (HttpClientErrorException e){
            throw new GithubException(e.getMessage());
        }
    }

    @Cacheable("repoLanguagesCache")
    public Map<String, Integer> getRepoLanguages(String username, String repo) throws GithubException {
        log.info("getRepoLanguage(" + username + ", " + repo + ")");
        try{
            var response = restTemplate.getForEntity(baseUrl + "/repos/" + username + "/" + repo + "/languages", JsonNode.class);
            if(response.hasBody() && response.getBody() != null){
                var json = response.getBody();

                Map<String, Integer> languages = new HashMap<>();
                json.fields().forEachRemaining(e -> languages.put(e.getKey(), e.getValue().asInt()));
                return languages;
            }

            return new HashMap<>();
        }catch (HttpClientErrorException e){
            throw new GithubException(e.getMessage());
        }
    }
}
