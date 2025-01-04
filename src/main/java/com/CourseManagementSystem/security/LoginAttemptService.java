package com.CourseManagementSystem.security;


import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class LoginAttemptService {

    private final int MAX_ATTEMPTS = 5;
    private final long LOCK_TIME_DURATION = 15 * 60 * 1000;  // 15 minutes

    private final Map<String, Integer> attemptsCache = new ConcurrentHashMap<>();
    private final Map<String, Long> lockTimeCache = new ConcurrentHashMap<>();

    public void loginFailed(String email) {
        attemptsCache.put(email, attemptsCache.getOrDefault(email, 0) + 1);
        if (attemptsCache.get(email) >= MAX_ATTEMPTS) {
            lockTimeCache.put(email, System.currentTimeMillis() + LOCK_TIME_DURATION);
        }
    }

    public void loginSucceeded(String email) {
        attemptsCache.remove(email);
        lockTimeCache.remove(email);
    }

    public boolean isBlocked(String email) {
        if (!lockTimeCache.containsKey(email)) return false;

        if (lockTimeCache.get(email) < System.currentTimeMillis()) {
            lockTimeCache.remove(email); // Unlock user
            return false;
        }
        return true;
    }

}
