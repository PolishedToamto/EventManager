package com.myDomain.eventManager.entity;

import jakarta.persistence.*;
import jakarta.validation.*;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.sql.Timestamp;

@Entity
@Table(name = "events")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Start time stamp can't be empty")
    private Timestamp startTs;

    @NotNull(message = "End time stamp can't be empty")
    @Future(message = "End time stamp must be in future")
    private Timestamp endTs;

    @Pattern(regexp = "^[a-zA-Z1-9 ,-]+$", message = "please enter a valid location")
    @NotBlank(message = "Location can't be empty")
    private String location;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Timestamp getStartTs() {
        return startTs;
    }

    public void setStartTs(Timestamp startTs) {
        this.startTs = startTs;
    }

    public Timestamp getEndTs() {
        return endTs;
    }

    public void setEndTs(Timestamp endTs) {
        this.endTs = endTs;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
