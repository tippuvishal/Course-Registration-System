package com.example.entity;

import jakarta.persistence.*;

import java.time.DayOfWeek;
import java.time.LocalTime;

@Embeddable
public class TimeSlot {

    @Enumerated(EnumType.STRING)
    private DayOfWeek day;

    private LocalTime startTime;

    private LocalTime endTime;

    public TimeSlot() {
    }

    public TimeSlot(DayOfWeek day,
                    LocalTime startTime,
                    LocalTime endTime) {

        this.day = day;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public DayOfWeek getDay() {
        return day;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public boolean overlaps(TimeSlot other) {

        if (this.day != other.day) {
            return false;
        }

        return this.startTime.isBefore(other.endTime)
                && other.startTime.isBefore(this.endTime);
    }
}