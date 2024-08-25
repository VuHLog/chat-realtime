package com.vuhlog.chat.log;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.Date;

@Component
@Slf4j
public class WebSocketEventListener {
    @Autowired
    private SimpMessageSendingOperations messagingTemplate;

    @EventListener(SessionConnectEvent.class)
    public void handleWebsocketConnectListener(SessionConnectEvent event) {
        log.debug("Session connected : {}", new Date().toString());
    }

    @EventListener(SessionDisconnectEvent.class)
    public void handleWebsocketDisconnectListener(SessionDisconnectEvent event) {
        log.debug("Session closed : {}", new Date().toString());
    }
}
