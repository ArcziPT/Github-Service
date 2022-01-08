package xyz.wolkarkadiusz.githubservice;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import xyz.wolkarkadiusz.githubservice.exception.GithubException;
import xyz.wolkarkadiusz.githubservice.model.GitRepo;
import xyz.wolkarkadiusz.githubservice.repository.GitReposRepository;
import xyz.wolkarkadiusz.githubservice.service.GithubUserService;

import java.util.AbstractMap;
import java.util.Arrays;
import java.util.Map;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {GitReposRepository.class, GithubUserService.class})
public class GithubUserServiceTest {
    @MockBean
    private GitReposRepository gitReposRepository;

    @Autowired
    private GithubUserService githubUserService;

    @Test
    public void getReposShouldFilterForkedRepos() throws GithubException {
        var repos = Arrays.asList(
                new GitRepo("test1", null, null, 0, 0, false),
                new GitRepo("test2", null, null, 0, 0, true),
                new GitRepo("test3", null, null, 0, 0, false),
                new GitRepo("test4", null, null, 0, 0, false),
                new GitRepo("test5", null, null, 0, 0, false),
                new GitRepo("test6", null, null, 0, 0, true),
                new GitRepo("test7", null, null, 0, 0, true),
                new GitRepo("test8", null, null, 0, 0, false),
                new GitRepo("test9", null, null, 0, 0, false)
        );

        var filtered = Arrays.asList(
                new GitRepo("test1", null, null, 0, 0, false),
                new GitRepo("test3", null, null, 0, 0, false),
                new GitRepo("test4", null, null, 0, 0, false),
                new GitRepo("test5", null, null, 0, 0, false),
                new GitRepo("test8", null, null, 0, 0, false),
                new GitRepo("test9", null, null, 0, 0, false)
        );

        Mockito.when(gitReposRepository.findByUsername(Mockito.anyString(), Mockito.anyInt(), Mockito.anyInt())).thenReturn(repos);

        var result = githubUserService.getRepos("username", false, 10, 1);
        Assert.assertArrayEquals(repos.toArray(), result.toArray());

        result = githubUserService.getRepos("username", true, 10, 1);
        Assert.assertArrayEquals(filtered.toArray(), result.toArray());
    }

    @Test
    public void getStarsCountShouldSumStarsInAllRepos() throws GithubException {
        var repos = Arrays.asList(
                new GitRepo("test1", null, null, 1, 0, false),
                new GitRepo("test2", null, null, 2, 0, true),
                new GitRepo("test3", null, null, 3, 0, false),
                new GitRepo("test4", null, null, 4, 0, false),
                new GitRepo("test5", null, null, 5, 0, false),
                new GitRepo("test6", null, null, 6, 0, true),
                new GitRepo("test7", null, null, 7, 0, true),
                new GitRepo("test8", null, null, 8, 0, false),
                new GitRepo("test9", null, null, 9, 0, false)
        );

        Mockito.when(gitReposRepository.getAllRepos(Mockito.anyString())).thenReturn(repos);
        var result = githubUserService.getStarsCount("username");

        Assert.assertEquals(45, result.intValue());
    }

    @Test
    public void getLanguagesShouldCorrectlyMergeLanguageFrequencyMaps() throws GithubException {
        var repos = Arrays.asList(
                new GitRepo("test1", null, null, 1, 0, false),
                new GitRepo("test2", null, null, 2, 0, true),
                new GitRepo("test3", null, null, 3, 0, false)
        );

        Map<String, Integer> languagesTest1 = Map.ofEntries(
                new AbstractMap.SimpleEntry<>("C++", 3000),
                new AbstractMap.SimpleEntry<>("Java", 8000),
                new AbstractMap.SimpleEntry<>("Python", 2000)
        );

        Map<String, Integer> languagesTest2 = Map.ofEntries(
                new AbstractMap.SimpleEntry<>("C++", 10000),
                new AbstractMap.SimpleEntry<>("Lua", 1500)
        );

        Map<String, Integer> languagesTest3 = Map.ofEntries(
                new AbstractMap.SimpleEntry<>("Java", 15000),
                new AbstractMap.SimpleEntry<>("Python", 7000)
        );

        Map<String, Integer> languagesResult = Map.ofEntries(
                new AbstractMap.SimpleEntry<>("Java", 23000),
                new AbstractMap.SimpleEntry<>("Python", 9000),
                new AbstractMap.SimpleEntry<>("C++", 13000),
                new AbstractMap.SimpleEntry<>("Lua", 1500)
        );

        Map<String, Integer> languagesResultExcludeForks = Map.ofEntries(
                new AbstractMap.SimpleEntry<>("Java", 23000),
                new AbstractMap.SimpleEntry<>("Python", 9000),
                new AbstractMap.SimpleEntry<>("C++", 3000)
        );

        Mockito.when(gitReposRepository.getAllRepos(Mockito.anyString())).thenReturn(repos);
        Mockito.when(gitReposRepository.getRepoLanguages(Mockito.anyString(), Mockito.eq("test1"))).thenReturn(languagesTest1);
        Mockito.when(gitReposRepository.getRepoLanguages(Mockito.anyString(), Mockito.eq("test2"))).thenReturn(languagesTest2);
        Mockito.when(gitReposRepository.getRepoLanguages(Mockito.anyString(), Mockito.eq("test3"))).thenReturn(languagesTest3);

        var result = githubUserService.getLanguages("username", false);
        Assert.assertEquals(languagesResult, result);

        result = githubUserService.getLanguages("username", true);
        Assert.assertEquals(languagesResultExcludeForks, result);
    }
}
