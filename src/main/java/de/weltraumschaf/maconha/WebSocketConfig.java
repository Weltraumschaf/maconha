package de.weltraumschaf.maconha;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;

/**
 * Configures web sockets.
 * <p>
 * https://spring.io/guides/gs/messaging-stomp-websocket/
 * http://clintberry.com/2013/angular-js-websocket-service/
 * https://geowarin.github.io/spring-boot-and-rethinkdb.html
 * </p>
 * <p>
 * Must not be final.
 * </p>
 */
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig extends AbstractWebSocketMessageBrokerConfigurer {

    @Override
	public void configureMessageBroker(final MessageBrokerRegistry config) {
		config.enableSimpleBroker("/topic");
//		config.setApplicationDestinationPrefixes("/app");
	}

	@Override
	public void registerStompEndpoints(final StompEndpointRegistry registry) {
		registry.addEndpoint("/jobs").withSockJS();
	}

}
