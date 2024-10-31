package com.example.redisk8sdemo.config;

import io.lettuce.core.ClientOptions;
import io.lettuce.core.protocol.ProtocolVersion;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisSentinelConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    @Value("${redis.sentinel.master}")
    private String master;

    @Value("${redis.sentinel.nodes}")
    private String sentinelNodes;

    @Value("${redis.password}")
    private String password;

    @Bean
    public LettuceConnectionFactory redisConnectionFactory() {
        RedisSentinelConfiguration sentinelConfig = new RedisSentinelConfiguration();
        sentinelConfig.setMaster(master);
        
        // Parse sentinel nodes from environment variable
        String[] nodes = sentinelNodes.split(",");
        for (String node : nodes) {
            String[] hostAndPort = node.split(":");
            sentinelConfig.sentinel(hostAndPort[0], Integer.parseInt(hostAndPort[1]));
        }
        
        // Set Redis password
        sentinelConfig.setPassword(RedisPassword.of(password));
        
        // Configure Lettuce client to use RESP2
        LettuceClientConfiguration clientConfig = LettuceClientConfiguration.builder()
            .clientOptions(ClientOptions.builder()
                .protocolVersion(ProtocolVersion.RESP2)
                .build())
            .build();

        return new LettuceConnectionFactory(sentinelConfig, clientConfig);
    }

    @Bean
    public RedisTemplate<String, String> redisTemplate() {
        RedisTemplate<String, String> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory());
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(new StringRedisSerializer());
        template.setEnableTransactionSupport(true);
        template.afterPropertiesSet();
        return template;
    }
} 