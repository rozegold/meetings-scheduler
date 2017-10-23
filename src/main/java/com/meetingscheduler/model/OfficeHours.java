package com.meetingscheduler.model;

import java.time.LocalTime;

public class OfficeHours {
    private LocalTime startTime;
    private LocalTime endTime;

    public OfficeHours() {
        this.startTime = LocalTime.of(9, 0);
        this.endTime = LocalTime.of(17, 30);
    }

    public OfficeHours(final LocalTime startTime, final LocalTime endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }
}
