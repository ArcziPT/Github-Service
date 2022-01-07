package xyz.wolkarkadiusz.githubservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.wolkarkadiusz.githubservice.model.GitRepo;
import xyz.wolkarkadiusz.githubservice.repository.GitReposRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public Map<String, Integer> getLanguages(String username){
        var repoNamesStream = getRepos(username).stream().map(GitRepo::getName);
        var languagesStream = repoNamesStream.map(repoName -> gitReposRepository.getRepoLanguages(username, repoName));

        return languagesStream.reduce(new HashMap<String, Integer>(), ((sumMap, languagesMap) -> {
            for(Map.Entry<String, Integer> entry : languagesMap.entrySet()){
                var key = entry.getKey();
                sumMap.put(key, sumMap.getOrDefault(key, 0) + entry.getValue());
            }
            return sumMap;
        }));
    }
}
