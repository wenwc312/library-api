package org.example.library_api.service;

import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class LoginAttemptService {
    private static final int MAX_ATTEMPTS = 5;
    private static final Duration LOCK_TIMEOUT = Duration.ofMinutes(15);

    private final Map<String, Integer> failCounts = new ConcurrentHashMap<>();
    private final Map<String, Instant> lockUntil = new ConcurrentHashMap<>();

    // 是否在鎖定中
    public boolean isLocked(String ip){
        Instant until = lockUntil.get(ip);
        if(until == null){
            return false;
        }
        if(Instant.now().isAfter(until)){
            // 鎖定時間已過，重新計算
            lockUntil.remove(ip);
            failCounts.remove(ip);
            return false;
        }
        return true;
    }

    // 記錄一次失敗，回傳剩餘次數
    public int recordFailure(String ip){
        int count = failCounts.merge(ip,1, Integer::sum);
        if(count >= MAX_ATTEMPTS){
            lockUntil.put(ip,Instant.now().plus(LOCK_TIMEOUT));
            return 0;
        }
        return MAX_ATTEMPTS-count;
    }

    // 登入成功，清除紀錄
    public void recordSuccess(String ip){
        failCounts.remove(ip);
        lockUntil.remove(ip);
    }

}
