package xyz.wolkarkadiusz.githubservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.wolkarkadiusz.githubservice.model.GitRepo;
import xyz.wolkarkadiusz.githubservice.repository.GitReposRepository;

import java.util.List;

@Service
public class GithubUserService {
    private final GitReposRepository gitReposRepository;

    @Autowired
    public GithubUserService(GitReposRepository gitReposRepository){
        this.gitReposRepository = gitReposRepository;
    }

    public List<GitRepo> getRepos(String username){
        return gitReposRepository.findByUsername(username);
    }

    public Integer getStarsCount(String username){
        return gitReposRepository.findByUsername(username).stream().reduce(0, (sum, gitRepo) -> sum + gitRepo.getStargazers_count(), Integer::sum);
    }
}
