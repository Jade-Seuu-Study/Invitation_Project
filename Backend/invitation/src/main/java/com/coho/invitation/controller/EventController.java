package com.coho.invitation.controller;

import com.coho.invitation.dto.Event;
import com.coho.invitation.service.EventService;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/events")
@CrossOrigin(origins = "*")
public class EventController {
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    /* 로그인한 사용자의 전체 행사 목록 가져오기 */
    @GetMapping("")
    public List<Event> getEvents(){
        HttpSession session = request.getSession();
        String uid = (String) session.getAttribute("uid");

        List<Event> eventList = eventService.getEventList(uid);

        return eventList;
    }

    @GetMapping("/progressing")
    public List<Event> getEventsProgressing(){
        HttpSession session = request.getSession();
        String uid = (String) session.getAttribute("uid");

        List<Event> eventList = eventService.getEventsProgressing(uid);

        return eventList;
    }

    @GetMapping("/done")
    public List<Event> getEventsDone(){
        HttpSession session = request.getSession();
        String uid = (String) session.getAttribute("uid");

        List<Event> eventList = eventService.getEventsDone(uid);

        return eventList;
    }

    @GetMapping("/{eid}")
    public Event getEvent(@PathVariable("eid") String eid){
        HttpSession session = request.getSession();
        String uid = (String) session.getAttribute("uid");

        Event event = eventService.getEvent(eid);

        return event;
    }

    /* 행사 추가하기 */
    @PostMapping("")
    public String addEvent(@RequestBody JsonNode params){
        HttpSession session = request.getSession();
        String uid = (String) session.getAttribute("uid");
        Event event = new Event();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS");

        // 실제 event 값 설정
        event.setEid(UUID.randomUUID().toString());
        event.setEtype(params.get("type").asText());
        event.setEdate(LocalDateTime.parse(params.get("datetime").asText()),formatter);
        event.setLocation(params.get("location").asText());
        event.setEhost(params.get("host").toString());

        eventService.insertEvent(event);
        eventService.insertManage(uid,event.getEid());

        return event.getEid();
    }

    @PutMapping("")
    public String updateEvent(){
        return "";
    }

    @DeleteMapping("")
    public String deleteEvent(){
        return "";
    }

}
