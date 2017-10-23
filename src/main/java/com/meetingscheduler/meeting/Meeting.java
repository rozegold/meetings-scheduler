package com.meetingscheduler.meeting;

import java.time.LocalDate;
import java.time.LocalTime;

public class Meeting {

    private LocalDate meetingDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private String empId;

    public Meeting(final LocalDate meetingDate, final LocalTime startTime, final LocalTime endTime, String empId) {
        this.meetingDate = meetingDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.empId = empId;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public String getEmpId() {
        return empId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Meeting meeting = (Meeting) o;

        if (meetingDate != null ? !meetingDate.equals(meeting.meetingDate) : meeting.meetingDate != null) return false;
        if (startTime != null ? !startTime.equals(meeting.startTime) : meeting.startTime != null) return false;
        if (endTime != null ? !endTime.equals(meeting.endTime) : meeting.endTime != null) return false;
        return empId != null ? empId.equals(meeting.empId) : meeting.empId == null;
    }

    @Override
    public int hashCode() {
        int result = meetingDate != null ? meetingDate.hashCode() : 0;
        result = 31 * result + (startTime != null ? startTime.hashCode() : 0);
        result = 31 * result + (endTime != null ? endTime.hashCode() : 0);
        result = 31 * result + (empId != null ? empId.hashCode() : 0);
        return result;
    }
}
