package pl.edu.pw.ee.rutynar.auctionsystem.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(path = "/test")
public class TestController {

    @GetMapping("/ws")
    public String websocket(){
        return "websocket";
    }

    @GetMapping("/bids-sse")
    public String bidSSE() {
        return "bids-sse";
    }

    @GetMapping("/notifications")
    public String userNotifications(){
        return "user-notifications-demo";
    }
}
