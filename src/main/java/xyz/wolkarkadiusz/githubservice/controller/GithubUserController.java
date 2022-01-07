package xyz.wolkarkadiusz.githubservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import xyz.wolkarkadiusz.githubservice.model.GitRepo;
import xyz.wolkarkadiusz.githubservice.service.GithubUserService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class GithubUserController {
    private final GithubUserService githubUserService;

    @Autowired
    public GithubUserController(GithubUserService githubUserService){
        this.githubUserService = githubUserService;
    }

    @GetMapping("/{username}/repos")
    public List<GitRepo> getRepos(@PathVariable("username") String username){
        return githubUserService.getRepos(username);
    }

    @GetMapping("/{username}/stars")
    public Integer getStars(@PathVariable("username") String username){
        return githubUserService.getStarsCount(username);
    }

    @GetMapping("/{username}/languages")
    public Map<String, Integer> getLanguages(@PathVariable("username") String username){
        return githubUserService.getLanguages(username);
    }
}
