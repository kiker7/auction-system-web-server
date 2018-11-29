package pl.edu.pw.ee.rutynar.auctionsystem.errors;

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import java.lang.reflect.Method;

public class AsyncExceptionHandler implements AsyncUncaughtExceptionHandler {

    @Override
    public void handleUncaughtException(Throwable throwable, Method method, Object... objects) {
        System.out.println("Exception message - " + throwable.getMessage());
        System.out.println("Method name - " + method.getName());
        for (Object object : objects)
            System.out.println("Parameter vlaue - " + object);
    }
}
