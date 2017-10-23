package com.meetingscheduler.scheduler;

import com.meetingscheduler.meeting.Meeting;
import com.meetingscheduler.model.MeetingRequest;
import com.meetingscheduler.model.OfficeHours;
import com.meetingscheduler.parser.DateAndTimeParser;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(JUnit4.class)
public class MeetingSchedulerTest {

    private MeetingScheduler subject;
    private MeetingRequest meetingRequestOne;
    private MeetingRequest meetingRequestTwo;
    private Meeting meetingOne;
    private Meeting meetingTwo;
    private LocalDate dayOne = LocalDate.parse("2015-08-21");
    private LocalDate dayTwo = LocalDate.parse("2015-08-22");

    @Before
    public void setUp() {

        subject = new MeetingScheduler();
        subject.setOfficeHours(new OfficeHours(LocalTime.parse("09:00"),LocalTime.parse("17:00")));
        meetingRequestOne = new MeetingRequest(DateAndTimeParser
                .parseSubmissionDate("2015-08-17 10:17:06"), "EMP001",
                DateAndTimeParser.parseMeetingDate("2015-08-21 09:00"), Duration.ofHours(2));

        meetingRequestTwo = new MeetingRequest(DateAndTimeParser
                .parseSubmissionDate("2015-08-17 20:17:06"), "EMP002",
                DateAndTimeParser.parseMeetingDate("2015-08-22 09:00"), Duration.ofHours(4));
        meetingOne = new Meeting(dayOne, LocalTime.parse("09:00"), LocalTime.parse("11:00"), "EMP001");
        meetingTwo = new Meeting(dayTwo, LocalTime.parse("09:00"), LocalTime.parse("13:00"), "EMP002");

    }


    @Test
    public void shouldArrange_MeetingForTwoUniqueMeetingRequests() {
        subject.scheduleMeeting(meetingRequestOne);
        Map<LocalDate, List<Meeting>> meetings = subject.getMeetings();

        assertThat(meetings.size(), is(1));
        assertThat(meetings.get(dayOne).get(0), is(meetingOne));

        subject.scheduleMeeting(meetingRequestTwo);
        assertThat(meetings.size(), is(2));
        assertThat(meetings.get(dayOne).stream().findFirst().get(), is(meetingOne));
        assertThat(meetings.get(dayTwo).stream().findFirst().get(), is(meetingTwo));
    }

    @Test
    public void shouldReject_MeetingRequestForAlreadyBookedSlot() {

        MeetingRequest requestAtConflictingTime = new MeetingRequest(
                DateAndTimeParser.parseSubmissionDate("2015-08-18 10:17:06"), "EMP002",
                DateAndTimeParser.parseMeetingDate("2015-08-21 10:00"), Duration.ofHours(2)
        );
        subject.scheduleMeeting(meetingRequestOne);
        subject.scheduleMeeting(requestAtConflictingTime);
        Map<LocalDate, List<Meeting>> meetings = subject.getMeetings();
        assertThat(meetings.get(dayOne).stream().findFirst().get(), is(meetingOne));
        assertThat(meetings.get(dayOne).size(), is(1));

    }

    @Test
    public void shouldReject_MeetingRequestForAlreadyBookedExactSameSlot() {

        MeetingRequest requestAtConflictingTime = new MeetingRequest(
                DateAndTimeParser.parseSubmissionDate("2015-08-18 10:17:06"), "EMP002",
                DateAndTimeParser.parseMeetingDate("2015-08-21 09:00"), Duration.ofHours(2)
        );
        subject.scheduleMeeting(meetingRequestOne);
        subject.scheduleMeeting(requestAtConflictingTime);
        Map<LocalDate, List<Meeting>> meetings = subject.getMeetings();
        assertThat(meetings.get(dayOne).stream().findFirst().get(), is(meetingOne));
        assertThat(meetings.get(dayOne).size(), is(1));

    }


    @Test
    public void shouldAllow_MeetingRequestForDifferentSlotsOnSameDay() {

        MeetingRequest requestAtConflictingTime = new MeetingRequest(
                DateAndTimeParser.parseSubmissionDate("2015-08-18 10:17:06"), "EMP002",
                DateAndTimeParser.parseMeetingDate("2015-08-21 11:00"), Duration.ofHours(1)
        );
        subject.scheduleMeeting(meetingRequestOne);
        subject.scheduleMeeting(requestAtConflictingTime);
        Map<LocalDate, List<Meeting>> meetings = subject.getMeetings();
        assertThat(meetings.get(dayOne).stream().findFirst().get(), is(meetingOne));
        assertThat(meetings.get(dayOne).size(), is(2));

    }


    @Test
    public void shouldReject_MeetingsOutsideOfficeHours() {

        MeetingRequest requestOutsideOfficeHours = new MeetingRequest(
                DateAndTimeParser.parseSubmissionDate("2015-08-18 10:17:06"), "EMP002",
                DateAndTimeParser.parseMeetingDate("2015-08-21 16:00"), Duration.ofHours(2)
        );

        subject.scheduleMeeting(requestOutsideOfficeHours);

        Map<LocalDate, List<Meeting>> meetings = subject.getMeetings();
        assertThat(meetings.size(), is(0));

    }

}