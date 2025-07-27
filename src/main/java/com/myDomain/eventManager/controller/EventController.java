package com.myDomain.eventManager.controller;

import com.myDomain.eventManager.entity.Event;
import com.myDomain.eventManager.repository.EventRepository;
import org.apache.coyote.Response;
import org.hibernate.annotations.Parameter;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<List<Event>> getAllEvents() {
        return ResponseEntity.ok(repository.findAll());  // Read all events
    }

    @PostMapping
    public ResponseEntity<Event> createEvent(@RequestBody Event event) {
        Event e = repository.save(event); // Save new event
        return ResponseEntity.status(HttpStatus.CREATED).body(e);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Event> getEventById(@PathVariable Long id) {
        return repository.findById(id).map(e->ResponseEntity.ok(e)).orElseGet(()->ResponseEntity.notFound().build());
    }

    @GetMapping(params = "location")
    public ResponseEntity<List<Event>> getEventByLocation(@RequestParam(required = false) String location){
        if(location == null){
            return getAllEvents();
        }
        else{
            return ResponseEntity.ok(repository.findEventNative(location));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long id) {
        if(repository.existsById(id)){
            repository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        else return ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Event> updateEvent(@PathVariable Long id, @RequestBody Event e){
        return repository.findById(id)
                .map(event -> {

                    event.setLocation(e.getLocation());
                    event.setStartTs(e.getStartTs());
                    event.setEndTs(e.getEndTs());
                    Event saved = repository.save(event);
                    return ResponseEntity.ok(saved);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
