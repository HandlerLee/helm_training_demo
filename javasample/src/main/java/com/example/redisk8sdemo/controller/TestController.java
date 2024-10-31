package com.example.redisk8sdemo.controller;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class TestController {

    private final RedisTemplate<String, String> redisTemplate;

    public TestController(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @PostMapping("/value")
    public void setValue(@RequestParam String key, @RequestParam String value) {
        redisTemplate.opsForValue().set(key, value);
    }

    @GetMapping("/value/{key}")
    public String getValue(@PathVariable String key) {
        return redisTemplate.opsForValue().get(key);
    }
} 