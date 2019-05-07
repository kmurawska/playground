package spring_rabbitmq;

import org.springframework.stereotype.Component;

@Component
public class MessageReceiver {
    public void receive(String message) {
        System.out.println("Received message: " + message);
    }
}
