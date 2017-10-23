package com.meetingscheduler.scheduler;

import com.meetingscheduler.meeting.Meeting;
import com.meetingscheduler.model.MeetingRequest;
import com.meetingscheduler.model.OfficeHours;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

/**
 * MeetingScheduler - checks for conflicting meetings
 * and schedules meetings
 */
public class MeetingScheduler {

    private final Map<LocalDate, List<Meeting>> meetings = new HashMap<>();
    private OfficeHours officeHours;

    //TODO: Do something about this nano-second that we have to add
    public MeetingScheduler() {

    }

    public void scheduleMeeting(final MeetingRequest meetingRequest) {
        LocalDate meetingDate = meetingRequest.getStartTime().toLocalDate();
        LocalTime startTime = meetingRequest.getStartTime().toLocalTime();
        LocalTime endTime = startTime.plus(meetingRequest.getDuration());

        if (meetingInsideOfficeHours(startTime, endTime)) {
            scheduleMeeting(meetingRequest, meetingDate, startTime, endTime);
        }
    }

    private void scheduleMeeting(final MeetingRequest meetingRequest, final LocalDate meetingDate, final LocalTime startTime, final LocalTime endTime) {
        List<Meeting> meetingList = meetings.get(meetingDate);

        if (null == meetingList) {
            meetingList = new ArrayList<>();
            meetingList.add(new Meeting(meetingDate, startTime, endTime, meetingRequest.getEmpID()));
            meetings.put(meetingDate, meetingList);
        } else {
            Optional<Meeting> conflictingMeeting = findConflictingMeeting(startTime, endTime, meetingList);

            if (!conflictingMeeting.isPresent())
                meetingList.add(new Meeting(meetingDate, startTime, endTime, meetingRequest.getEmpID()));
        }
    }

    private boolean meetingInsideOfficeHours(final LocalTime startTime, final LocalTime endTime) {
        return timeBetween(startTime, officeHours.getStartTime(), officeHours.getEndTime())
                && timeBetween(endTime, officeHours.getStartTime(), officeHours.getEndTime());
    }

    private Optional<Meeting> findConflictingMeeting(final LocalTime startTime, final LocalTime endTime, final List<Meeting> meetingList) {
        return meetingList.stream()
                .filter(meeting -> conflictsWith(meeting.getStartTime(), meeting.getEndTime(), startTime, endTime))
                .findAny();
    }


    private boolean conflictsWith(final LocalTime startTime, final LocalTime endTime, final LocalTime currentStartTime, final LocalTime currentEndTime) {
        return (startTime.equals(currentStartTime) && endTime.equals(currentEndTime)) || (timeBetween(currentStartTime, startTime, endTime)
                || timeBetween(currentEndTime, startTime, endTime));
    }

    private boolean timeBetween(final LocalTime targetTime, final LocalTime startTime, final LocalTime endTime) {
        return targetTime.isAfter(startTime) && targetTime.isBefore(endTime);
    }

    public Map<LocalDate, List<Meeting>> getMeetings() {
        return meetings;
    }

    public void setOfficeHours(OfficeHours officeHours) {
        officeHours.setStartTime(officeHours.getStartTime().minusNanos(1));
        officeHours.setEndTime(officeHours.getEndTime().plusNanos(1));
        this.officeHours = officeHours;
    }
}
