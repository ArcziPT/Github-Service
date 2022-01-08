package xyz.wolkarkadiusz.githubservice.controller;

import com.fasterxml.jackson.core.util.InternCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import xyz.wolkarkadiusz.githubservice.dto.ErrorResponse;
import xyz.wolkarkadiusz.githubservice.exception.ExtractFieldsException;
import xyz.wolkarkadiusz.githubservice.exception.GithubException;
import xyz.wolkarkadiusz.githubservice.model.GitRepo;
import xyz.wolkarkadiusz.githubservice.service.GithubUserService;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/users")
public class GithubUserController {
    private final GithubUserService githubUserService;

    @Autowired
    public GithubUserController(GithubUserService githubUserService){
        this.githubUserService = githubUserService;
    }

    /**
     * Returns list of repositories for specified user with (optional) additional fields.
     * Always includes fields:
     *  - name
     *  - stars
     * @param username - username
     * @param fields - additional fields to include (GitRepo: html_url, forks_count, fork, description)
     * @param excludeForks - ignore forked repositories
     * @param perPage - how many repositories to return
     * @param page - which page
     * @return list of repositories
     */
    @GetMapping("/{username}/repos")
    public ResponseEntity<?> getRepos(@PathVariable("username") String username,
                                      @RequestParam(value = "fields", required = false, defaultValue = "") List<String> fields,
                                      @RequestParam(value = "exclude_forks", required = false, defaultValue = "false") Boolean excludeForks,
                                      @RequestParam(value = "per_page", required = false, defaultValue = "50") Integer perPage,
                                      @RequestParam(value = "page", required = false, defaultValue = "1") Integer page){
        log.debug("GET /" + username + "/repos");
        try{
            var repos = new ArrayList<>();
            for(GitRepo repo : githubUserService.getRepos(username, excludeForks, perPage, page)){
                repos.add(repo.toMap(fields));
            }
            return ResponseEntity.ok(repos);
        }catch (ExtractFieldsException | GithubException e){
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        }
    }

    /**
     * Returns sum of stars in all repositories for specified user.
     * @param username - user
     * @return sum of stars
     */
    @GetMapping("/{username}/stars")
    public ResponseEntity<?> getStars(@PathVariable("username") String username){
        log.debug("GET /" + username + "/stars");
        try {
            return ResponseEntity.ok(githubUserService.getStarsCount(username));
        }catch (GithubException e){
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        }
    }

    /**
     * Returns every language used in any repository with number of bytes sorted desc.
     * @param username - user
     * @param excludeForks - ignore forked repositories
     * @param count - number of results to return
     * @return language frequency map sorted desc
     */
    @GetMapping("/{username}/languages")
    public ResponseEntity<?> getLanguages(@PathVariable("username") String username,
                                          @RequestParam(value = "exclude_forks", required = false, defaultValue = "false") Boolean excludeForks,
                                          @RequestParam(value = "count", required = false, defaultValue = "1") Integer count){
        log.debug("GET /" + username + "/languages");
        if(count <= 0)
            return ResponseEntity.badRequest().body(new ErrorResponse("Count must be greater than 0"));

        try {
            var languages = githubUserService.getLanguages(username, excludeForks);
            List<Map.Entry<String, Integer>> result  = languages.entrySet().stream().sorted(Map.Entry.comparingByValue()).collect(Collectors.toList());
            Collections.reverse(result);
            count = Math.min(result.size(), count);
            return ResponseEntity.ok(result.subList(0, count));
        }catch (GithubException e){
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        }
    }
}
