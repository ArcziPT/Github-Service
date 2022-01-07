package xyz.wolkarkadiusz.githubservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.wolkarkadiusz.githubservice.exception.GithubException;
import xyz.wolkarkadiusz.githubservice.model.GitRepo;
import xyz.wolkarkadiusz.githubservice.repository.GitReposRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class GithubUserService {
    private final GitReposRepository gitReposRepository;

    @Autowired
    public GithubUserService(GitReposRepository gitReposRepository){
        this.gitReposRepository = gitReposRepository;
    }

    public List<GitRepo> getRepos(String username, Boolean excludeForks, Integer per_page, Integer page) throws GithubException {
        return gitReposRepository.findByUsername(username, per_page, page)
                    .stream().filter(repo -> !excludeForks || !repo.getFork())
                    .collect(Collectors.toList());
    }

    public Integer getStarsCount(String username) throws GithubException {
        return gitReposRepository.getAllRepos(username).stream().reduce(0, (sum, gitRepo) -> sum + gitRepo.getStargazers_count(), Integer::sum);
    }

    public Map<String, Integer> getLanguages(String username, Boolean excludeForks) throws GithubException {
        var repoNames = gitReposRepository.getAllRepos(username).stream()
                .filter(repo -> !excludeForks || !repo.getFork())
                .map(GitRepo::getName)
                .collect(Collectors.toList());

        var languages = new ArrayList<Map<String, Integer>>();
        for(String repoName : repoNames)
            languages.add(gitReposRepository.getRepoLanguages(username, repoName));

        return languages.stream().reduce(new HashMap<String, Integer>(), ((sumMap, languagesMap) -> {
            for(Map.Entry<String, Integer> entry : languagesMap.entrySet()){
                var key = entry.getKey();
                sumMap.put(key, sumMap.getOrDefault(key, 0) + entry.getValue());
            }
            return sumMap;
        }));
    }
}
