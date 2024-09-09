package dev.nathanlively.adapter.out.ai;

import org.springframework.ai.chat.client.advisor.VectorStoreChatMemoryAdvisor;
import org.springframework.ai.vectorstore.VectorStore;

import java.lang.reflect.Field;

public class CustomVectorStoreChatMemoryAdvisor extends VectorStoreChatMemoryAdvisor {

    public CustomVectorStoreChatMemoryAdvisor(VectorStore vectorStore) {
        super(vectorStore);
    }

//    @Override
//    public AdvisedRequest adviseRequest(AdvisedRequest unfulfilledRequest, Map<String, Object> context) {
//        String advisedSystemText = unfulfilledRequest.systemText() + System.lineSeparator() + getSystemTextAdvise();
//
//        var searchRequest = SearchRequest.query(unfulfilledRequest.userText())
//                .withTopK(this.doGetChatMemoryRetrieveSize(context))
//                .withFilterExpression(
//                        getDocumentMetadataConversationId() + "=='" + this.doGetConversationId(context) + "'");
//
//        List<Document> documents = this.getChatMemoryStore().similaritySearch(searchRequest);
//
//        String longTermMemory = documents.stream()
//                .map(Content::getContent)
//                .collect(Collectors.joining(System.lineSeparator()));
//
//        Map<String, Object> advisedSystemParams = new HashMap<>(unfulfilledRequest.systemParams());
//        advisedSystemParams.put("long_term_memory", longTermMemory);
//
//        AdvisedRequest advisedRequest = AdvisedRequest.from(unfulfilledRequest)
//                .withSystemText(advisedSystemText)
//                .withSystemParams(advisedSystemParams)
//                .build();
//
//        UserMessage userMessage = new UserMessage(unfulfilledRequest.userText(), unfulfilledRequest.media());
//        this.getChatMemoryStore().write(toDocuments(List.of(userMessage), this.doGetConversationId(context)));
//
//        return advisedRequest;
//    }

    private String getSystemTextAdvise() {
        try {
            Field field = VectorStoreChatMemoryAdvisor.class.getDeclaredField("systemTextAdvise");
            field.setAccessible(true);
            return (String) field.get(this);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("Could not access systemTextAdvise field", e);
        }
    }

    private String getDocumentMetadataConversationId() {
        try {
            Field field = VectorStoreChatMemoryAdvisor.class.getDeclaredField("DOCUMENT_METADATA_CONVERSATION_ID");
            field.setAccessible(true);
            return (String) field.get(null); // static fields are accessed register null target
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("Could not access DOCUMENT_METADATA_CONVERSATION_ID field", e);
        }
    }
}
