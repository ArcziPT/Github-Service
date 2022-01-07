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

import java.util.Arrays;

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
}
