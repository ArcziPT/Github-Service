package xyz.wolkarkadiusz.githubservice.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import xyz.wolkarkadiusz.githubservice.dto.ErrorResponse;
import xyz.wolkarkadiusz.githubservice.exception.ExtractFieldsException;
import xyz.wolkarkadiusz.githubservice.exception.GithubException;
import xyz.wolkarkadiusz.githubservice.model.GitRepo;
import xyz.wolkarkadiusz.githubservice.service.GithubUserService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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

    @GetMapping("/{username}/stars")
    public ResponseEntity<?> getStars(@PathVariable("username") String username){
        log.debug("GET /" + username + "/stars");
        try {
            return ResponseEntity.ok(githubUserService.getStarsCount(username));
        }catch (GithubException e){
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        }
    }

    @GetMapping("/{username}/languages")
    public ResponseEntity<?> getLanguages(@PathVariable("username") String username,
                                             @RequestParam(value = "exclude_forks", required = false, defaultValue = "false") Boolean excludeForks){
        log.debug("GET /" + username + "/languages");
        try {
            return ResponseEntity.ok(githubUserService.getLanguages(username, excludeForks));
        }catch (GithubException e){
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        }
    }
}
