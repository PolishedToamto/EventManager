package com.myDomain.eventManager.controller;

import com.myDomain.eventManager.entity.Event;
import com.myDomain.eventManager.repository.EventRepository;
import org.hibernate.annotations.Parameter;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/events")
public class EventController {

    final private EventRepository repository;

    public EventController(EventRepository repository){
        this.repository = repository;
    }

    @GetMapping
    public List<Event> getAllEvents() {
        return repository.findAll();  // Read all events
    }

    @PostMapping
    public Event createEvent(@RequestBody Event event) {
        return repository.save(event); // Save new event
    }

    @GetMapping("/{id}")
    public Event getEventById(@PathVariable Long id) {
        return repository.findById(id).orElseThrow(); // Read by ID
    }

    @GetMapping(params = "location")
    public List<Event> getEventByLocation(@RequestParam(required = false) String location){
        if(location == null){
            return repository.findAll();
        }
        else{
            return repository.findEventNative(location);
        }
    }

    @DeleteMapping("/{id}")
    public void deleteEvent(@PathVariable Long id) {
        repository.deleteById(id); // Delete by ID
    }
}
