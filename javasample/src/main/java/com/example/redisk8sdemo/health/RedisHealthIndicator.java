package com.example.redisk8sdemo.health;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class RedisHealthIndicator implements HealthIndicator {

    private final RedisTemplate<String, String> redisTemplate;

    public RedisHealthIndicator(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public Health health() {
        try {
            RedisConnection connection = redisTemplate.getConnectionFactory().getConnection();
            String result = connection.ping();
            connection.close();
            
            if ("PONG".equals(result)) {
                return Health.up()
                    .withDetail("redis", "Redis is responding")
                    .build();
            } else {
                return Health.down()
                    .withDetail("redis", "Redis is not responding correctly")
                    .build();
            }
        } catch (Exception e) {
            return Health.down()
                .withDetail("redis", "Redis connection failed")
                .withDetail("error", e.getMessage())
                .build();
        }
    }
} 