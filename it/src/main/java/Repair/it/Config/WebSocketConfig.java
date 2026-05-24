//package Repair.it.Config;
//
//
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.socket.config.annotation.EnableWebSocket;
//import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
//import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
//import Repair.it.Handlers.GarageRequestHandler;
//@Configuration
//@EnableWebSocket
//public class WebSocketConfig implements WebSocketConfigurer {
//
//    private final GarageRequestHandler garageRequestHandler;
//
//    public WebSocketConfig(GarageRequestHandler garageRequestHandler) {
//        this.garageRequestHandler = garageRequestHandler;
//    }
//
//    @Override
//    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
//        registry.addHandler(garageRequestHandler, "/ws/garage")
//                .setAllowedOrigins("*");
//    }
//}