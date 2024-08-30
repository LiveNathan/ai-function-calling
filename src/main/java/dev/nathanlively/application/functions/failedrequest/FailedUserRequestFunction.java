package dev.nathanlively.application.functions.failedrequest;

import dev.nathanlively.application.port.RequestRepository;
import dev.nathanlively.domain.UnfulfilledUserRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Function;

public class FailedUserRequestFunction implements Function<UnfulfilledUserRequest, String> {
    private static final Logger log = LoggerFactory.getLogger(FailedUserRequestFunction.class);
    private final RequestRepository requestRepository;

    public FailedUserRequestFunction(RequestRepository requestRepository) {
        this.requestRepository = requestRepository;
    }

    @Override
    public String apply(UnfulfilledUserRequest unfulfilledUserRequest) {
        log.info("Storing failed user unfulfilledRequest: {}", unfulfilledUserRequest);
        return requestRepository.save(unfulfilledUserRequest);
    }
}
