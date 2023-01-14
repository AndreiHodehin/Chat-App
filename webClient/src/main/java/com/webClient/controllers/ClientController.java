package com.webClient.controllers;

import com.webClient.client.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;


@RestController
@RequestMapping()
public class ClientController {

    Client client;
    @Autowired
    public ClientController(Client client){
        this.client = client;
    }
    @GetMapping(value = "/message")
    public ModelAndView printMessage(ModelAndView modelAndView, HttpServletResponse response) {

        response.setIntHeader("Refresh",8);
        String conversation = client.toString();
        modelAndView.addObject("conv",conversation);
        String nick = client.getNickName();
        modelAndView.addObject("nick",nick);
        modelAndView.setViewName("/result.jsp");

        return modelAndView;
    }
    @PostMapping(value = "/nickname")
    public ModelAndView insertNick(@ModelAttribute("nickname") String nick, ModelAndView modelAndView) {
        client.setNickName(nick);
        if(!client.isConnected()){
        client.getConnection();
        }
        modelAndView.setViewName("redirect:/message");
        return modelAndView;
    }
    @PostMapping(value = "/send")
    public ModelAndView sendMessage(@ModelAttribute("message") String message, ModelAndView modelAndView) {
        if(!message.isEmpty()){
        client.sendMessage(message);
        }
        modelAndView.setViewName("redirect:/message");
        return modelAndView;
    }
}
