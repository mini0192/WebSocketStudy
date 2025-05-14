package com.test;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Configuration
@Slf4j
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    // 메시지 라우팅 관리
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // "/topic/{}" 경로로 구독 가능
        // Example: /topic/hello 경로 구독 가능
        registry.enableSimpleBroker("/topic");

        // "/app/{}" 경로로 메시지 전송 가능
        // Example: /app/world 경로로 메시지 전송 가능
        registry.setApplicationDestinationPrefixes("/app");
    }

    // WebSocket 연결을 위한 엔드포인트 관리
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // 클라이언트가 WebSocket에 연결할 수 있는 엔드포인트를 등록
        // Example: http://localhost:8080/websocket으로 WebSocket에 연결
        registry.addEndpoint("/websocket")
                // 모든 도메인에서의 요청을 허용 (CORS 설정)
                .setAllowedOriginPatterns("*")
                // SockJS를 사용해 WebSocket을 지원하지 않는 브라우저도 사용 가능
                .withSockJS();
    }

    // 소켓이 연결 되었을 때의 EventListener
    @EventListener
    public void handleWebSocketConnectListener(SessionConnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        log.info("WebSocket Connected: sessionId={}", headerAccessor.getSessionId());
    }

    // 소켓이 연결이 해제 되었을 때의 EventListener
    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        log.info("WebSocket Disconnected: sessionId={}", headerAccessor.getSessionId());
    }
}
