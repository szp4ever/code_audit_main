package org.ruoyi.common.util;

import lombok.extern.slf4j.Slf4j;
import org.ruoyi.common.core.exception.base.BaseException;

import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 速率限制处理工具类
 * 用于统一处理API速率限制错误，提供等待和重试机制
 *
 * @author system
 */
@Slf4j
public class RateLimitHandler {

    private static final int DEFAULT_WAIT_SECONDS = 10;
    private static final int MAX_RETRIES = 3;
    private static final long MIN_CALL_INTERVAL_MS = 200;
    private static final Pattern RATE_LIMIT_PATTERN = Pattern.compile("(\\d+)\\s*requests?\\s*per\\s*(minute|hour|second)", Pattern.CASE_INSENSITIVE);

    /**
     * 判断异常是否为速率限制错误
     */
    public static boolean isRateLimitError(Exception e) {
        String className = e.getClass().getName();
        if (className.contains("RateLimitException")) {
            return true;
        }
        if (e instanceof BaseException) {
            BaseException be = (BaseException) e;
            String message = be.getMessage();
            if (message != null) {
                return message.contains("rate limit") 
                    || message.contains("速率限制")
                    || message.contains("RATE_LIMIT")
                    || message.contains("429")
                    || message.contains("请求过于频繁");
            }
        }
        String errorMessage = e.getMessage();
        if (errorMessage != null) {
            return errorMessage.contains("rate limit")
                || errorMessage.contains("速率限制")
                || errorMessage.contains("RATE_LIMIT")
                || errorMessage.contains("429")
                || errorMessage.contains("请求过于频繁");
        }
        return false;
    }

    /**
     * 从错误信息中提取等待时间（秒）
     * 如果无法提取，返回默认值
     */
    public static int extractWaitSeconds(Exception e) {
        String message = e.getMessage();
        if (message == null) {
            return DEFAULT_WAIT_SECONDS;
        }

        Matcher matcher = RATE_LIMIT_PATTERN.matcher(message);
        if (matcher.find()) {
            int limit = Integer.parseInt(matcher.group(1));
            String unit = matcher.group(2).toLowerCase();
            
            if ("minute".equals(unit)) {
                //每分钟limit个请求，意味着每(60/limit)秒一个请求，等待时间设为(60/limit + 5)秒以确保安全
                return Math.max(10, (60 / limit) + 5);
            } else if ("hour".equals(unit)) {
                //每小时limit个请求，等待时间设为(3600/limit + 10)秒
                return Math.max(10, (3600 / limit) + 10);
            } else if ("second".equals(unit)) {
                //每秒limit个请求，等待时间设为(1/limit + 0.5)秒，但至少1秒
                return Math.max(1, (int) Math.ceil(1.0 / limit + 0.5));
            }
        }

        if (message.contains("30 requests per minute")) {
            //30请求/分钟 = 每2秒一个请求，等待10秒
            return 10;
        }
        if (message.contains("60 requests per minute")) {
            //60请求/分钟 = 每1秒一个请求，等待6秒
            return 6;
        }

        return DEFAULT_WAIT_SECONDS;
    }

    /**
     * 等待指定时间，处理中断异常
     */
    public static void waitForRateLimit(int seconds) {
        try {
            log.info("遇到速率限制，等待{}秒后重试", seconds);
            Thread.sleep(TimeUnit.SECONDS.toMillis(seconds));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.warn("等待速率限制时被中断");
        }
    }

    /**
     * 执行带速率限制重试的操作
     *
     * @param operation 要执行的操作
     * @param operationName 操作名称（用于日志）
     * @return 操作结果
     */
    public static <T> T executeWithRetry(java.util.function.Supplier<T> operation, String operationName) {
        int attempt = 0;
        Exception lastException = null;

        while (attempt < MAX_RETRIES) {
            try {
                return operation.get();
            } catch (RuntimeException e) {
                Throwable cause = e.getCause();
                if (cause instanceof Exception && isRateLimitError((Exception) cause)) {
                    lastException = (Exception) cause;
                    attempt++;
                    if (attempt < MAX_RETRIES) {
                        int baseWaitSeconds = extractWaitSeconds((Exception) cause);
                        int waitSeconds = baseWaitSeconds * (int) Math.pow(2, attempt - 1);
                        log.warn("{}遇到速率限制（尝试{}/{}），等待{}秒后重试: {}", 
                            operationName, attempt, MAX_RETRIES, waitSeconds, cause.getMessage());
                        waitForRateLimit(waitSeconds);
                        continue;
                    } else {
                        log.error("{}达到最大重试次数（{}次），放弃重试: {}", 
                            operationName, MAX_RETRIES, cause.getMessage());
                        throw new RuntimeException(operationName + "失败：速率限制，已重试" + MAX_RETRIES + "次", cause);
                    }
                } else if (isRateLimitError(e)) {
                    lastException = e;
                    attempt++;
                    if (attempt < MAX_RETRIES) {
                        int baseWaitSeconds = extractWaitSeconds(e);
                        int waitSeconds = baseWaitSeconds * (int) Math.pow(2, attempt - 1);
                        log.warn("{}遇到速率限制（尝试{}/{}），等待{}秒后重试: {}", 
                            operationName, attempt, MAX_RETRIES, waitSeconds, e.getMessage());
                        waitForRateLimit(waitSeconds);
                        continue;
                    } else {
                        log.error("{}达到最大重试次数（{}次），放弃重试: {}", 
                            operationName, MAX_RETRIES, e.getMessage());
                        throw new RuntimeException(operationName + "失败：速率限制，已重试" + MAX_RETRIES + "次", e);
                    }
                } else {
                    throw e;
                }
            } catch (Exception e) {
                lastException = e;
                if (isRateLimitError(e)) {
                    attempt++;
                    if (attempt < MAX_RETRIES) {
                        int baseWaitSeconds = extractWaitSeconds(e);
                        int waitSeconds = baseWaitSeconds * (int) Math.pow(2, attempt - 1);
                        log.warn("{}遇到速率限制（尝试{}/{}），等待{}秒后重试: {}", 
                            operationName, attempt, MAX_RETRIES, waitSeconds, e.getMessage());
                        waitForRateLimit(waitSeconds);
                        continue;
                    } else {
                        log.error("{}达到最大重试次数（{}次），放弃重试: {}", 
                            operationName, MAX_RETRIES, e.getMessage());
                        throw new RuntimeException(operationName + "失败：速率限制，已重试" + MAX_RETRIES + "次", e);
                    }
                } else {
                    throw e;
                }
            }
        }

        if (lastException != null) {
            throw new RuntimeException(operationName + "失败", lastException);
        }
        throw new RuntimeException(operationName + "失败：未知错误");
    }

    /**
     * 添加调用间隔，避免快速连续调用
     */
    public static void addCallInterval() {
        try {
            Thread.sleep(MIN_CALL_INTERVAL_MS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
