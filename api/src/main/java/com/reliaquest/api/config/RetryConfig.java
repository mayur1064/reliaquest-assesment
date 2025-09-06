package com.reliaquest.api.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.RetryContext;
import org.springframework.retry.RetryListener;
import org.springframework.retry.annotation.EnableRetry;

// Exponential Backoff for Retry to not overwhelm the API when 429
@Configuration
@EnableRetry
@Slf4j
public class RetryConfig {

    @Bean
    public RetryListener retryLogger() {
        return new RetryListener() {
            @Override
            public <T, E extends Throwable> boolean open(RetryContext context, RetryCallback<T, E> callback) {
                return true;
            }

            @Override
            public <T, E extends Throwable> void close(
                    RetryContext context, RetryCallback<T, E> callback, Throwable throwable) {}

            @Override
            public <T, E extends Throwable> void onError(
                    RetryContext context, RetryCallback<T, E> callback, Throwable throwable) {
                log.warn("Retry attempt #" + context.getRetryCount() + " failed due to: " + throwable.getMessage());
            }
        };
    }
}
