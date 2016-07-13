package de.weltraumschaf.maconha;

import static de.weltraumschaf.maconha.controller.ApiController.BASE_URI_PATH;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;

/**
 */
@Configuration
@EnableWebSocketMessageBroker
public final class WebSocketConfig extends AbstractWebSocketMessageBrokerConfigurer {

    @Override
	public void configureMessageBroker(final MessageBrokerRegistry config) {
		config.enableSimpleBroker("/topic");
		config.setApplicationDestinationPrefixes("/app");
	}

	@Override
	public void registerStompEndpoints(final StompEndpointRegistry registry) {
		registry.addEndpoint(BASE_URI_PATH +"/jobs").withSockJS();
	}

}
