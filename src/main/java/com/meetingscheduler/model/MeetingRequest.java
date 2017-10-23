package com.meetingscheduler.model;

import java.time.Duration;
import java.time.LocalDateTime;

public class MeetingRequest {

    private LocalDateTime submissionTime;
    private String empID;
    private LocalDateTime startTime;
    private Duration duration;

    public MeetingRequest(final LocalDateTime submissionTime, final String empID, final LocalDateTime startTime, final Duration duration) {
        this.submissionTime = submissionTime;
        this.empID = empID;
        this.startTime = startTime;
        this.duration = duration;
    }

    public LocalDateTime getSubmissionTime() {
        return submissionTime;
    }

    public String getEmpID() {
        return empID;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public Duration getDuration() {
        return duration;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MeetingRequest that = (MeetingRequest) o;

        if (submissionTime != null ? !submissionTime.equals(that.submissionTime) : that.submissionTime != null)
            return false;
        if (empID != null ? !empID.equals(that.empID) : that.empID != null) return false;
        if (startTime != null ? !startTime.equals(that.startTime) : that.startTime != null) return false;
        return duration != null ? duration.equals(that.duration) : that.duration == null;
    }

    @Override
    public int hashCode() {
        int result = submissionTime != null ? submissionTime.hashCode() : 0;
        result = 31 * result + (empID != null ? empID.hashCode() : 0);
        result = 31 * result + (startTime != null ? startTime.hashCode() : 0);
        result = 31 * result + (duration != null ? duration.hashCode() : 0);
        return result;
    }
}
