package xyz.wolkarkadiusz.githubservice.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import xyz.wolkarkadiusz.githubservice.exception.GithubException;
import xyz.wolkarkadiusz.githubservice.model.GitRepo;

import java.util.*;

/**
 * Wrapper for caching repository.
 */
@Repository
public class GitReposRepository {
    private final GitReposCacheableRepository gitReposCacheableRepository;

    @Autowired
    public GitReposRepository(GitReposCacheableRepository gitReposCacheableRepository){
        this.gitReposCacheableRepository = gitReposCacheableRepository;
    }

    public List<GitRepo> findByUsername(String username, Integer per_page, Integer page) throws GithubException {
        return gitReposCacheableRepository.findByUsername(username, per_page, page);
    }

    public List<GitRepo> getAllRepos(String username) throws GithubException {
        var repos = new ArrayList<GitRepo>();

        final int per_page = 50;
        int page = 1;
        while(true){
            var newRepos = findByUsername(username, per_page, page);
            repos.addAll(newRepos);
            page++;

            if(newRepos.size() < per_page)
                break;
        }

        return repos;
    }

    public Map<String, Integer> getRepoLanguages(String username, String repo) throws GithubException {
        return gitReposCacheableRepository.getRepoLanguages(username, repo);
    }
}
