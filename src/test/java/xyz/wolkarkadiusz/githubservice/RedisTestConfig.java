package xyz.wolkarkadiusz.githubservice;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;
import redis.embedded.RedisServer;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@TestConfiguration
public class RedisTestConfig {
    private final RedisServer redisServer;

    public RedisTestConfig(@Value("${spring.redis.port:6370}") String redisPort) {
        System.out.println(redisPort);
        this.redisServer = new RedisServer(Integer.parseInt(redisPort));
    }

    @PostConstruct
    public void postConstruct() {
        redisServer.start();
    }

    @PreDestroy
    public void preDestroy() {
        redisServer.stop();
    }
}
