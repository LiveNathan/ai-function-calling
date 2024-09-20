package dev.nathanlively.security;

import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.concurrent.Callable;
import java.util.concurrent.Executor;

public class DelegatingSecurityContextExecutor implements Executor {
    private final Executor delegate;

    public DelegatingSecurityContextExecutor(Executor delegate) {
        this.delegate = delegate;
    }

    @Override
    public void execute(Runnable task) {
        SecurityContext context = SecurityContextHolder.getContext();
        Callable<Void> callable = () -> {
            try {
                SecurityContextHolder.setContext(context);
                task.run();
            } finally {
                SecurityContextHolder.clearContext();
            }
            return null;
        };
        delegate.execute(() -> {
            try {
                callable.call();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }
}
