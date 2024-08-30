package dev.nathanlively.adapter.out.ai;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.AdvisedRequest;
import org.springframework.ai.chat.client.RequestResponseAdvisor;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.model.Generation;

import java.util.List;
import java.util.Map;

public class LoggingAdvisor implements RequestResponseAdvisor {
    private static final Logger log = LoggerFactory.getLogger(LoggingAdvisor.class);

    @Override
    public AdvisedRequest adviseRequest(AdvisedRequest request, Map<String, Object> context) {
//        log.info("Request: {}", unfulfilledRequest);
        return request;
    }

//    @Override
//    public Flux<ChatResponse> adviseResponse(Flux<ChatResponse> fluxResponse, Map<String, Object> context) {
//        return fluxResponse
//                .publishOn(Schedulers.boundedElastic())
//                .doFinally(signalType -> {
//                    // Collect all responses for logging after the flux completes
//                    fluxResponse.collectList()
//                            .doOnNext(responses -> log.info("Consolidated Response: {}", formatResponses(responses)))
//                            .subscribe();
//                });
//    }

    private String formatResponses(List<ChatResponse> responses) {
        StringBuilder sb = new StringBuilder("Consolidated Chat Responses: ");
        for (ChatResponse response : responses) {
            for (Generation generation : response.getResults()) {
                sb.append(generation.getOutput().getContent());
            }
        }
        return sb.toString();
    }
}
