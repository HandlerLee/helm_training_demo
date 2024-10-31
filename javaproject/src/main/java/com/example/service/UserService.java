package com.example.service;

import com.example.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    
    private final RedisTemplate<String, Object> redisTemplate;
    private static final String KEY_PREFIX = "user:";
    
    public void saveUser(User user) {
        redisTemplate.opsForValue().set(KEY_PREFIX + user.getId(), user);
    }
    
    public User getUser(String id) {
        return (User) redisTemplate.opsForValue().get(KEY_PREFIX + id);
    }
    
    public void deleteUser(String id) {
        redisTemplate.delete(KEY_PREFIX + id);
    }
} 