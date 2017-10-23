package com.meetingscheduler;

import com.meetingscheduler.model.MeetingRequest;
import com.meetingscheduler.model.OfficeHours;
import com.meetingscheduler.parser.DateAndTimeParser;
import com.meetingscheduler.parser.MeetingRequestParser;
import com.meetingscheduler.scheduler.MeetingScheduler;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.anyList;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class MeetingBookingSystemTest {

    String testFilePath = "src/test/resources/MeetingRequestInputTest";

    @Mock
    private MeetingRequestParser requestParser;

    @Mock
    private MeetingScheduler meetingScheduler;
    private OfficeHours officeHours = new OfficeHours(LocalTime.parse("09:00"), LocalTime.parse("17:00"));
    private MeetingBookingSystem subject;
    private LocalDateTime now = LocalDateTime.now();
    private MeetingRequest meetingRequestOne;
    private MeetingRequest meetingRequestTwo;
    private List<String> meetingInputList;

    @Before
    public void setup() {
        subject = new MeetingBookingSystem(testFilePath, requestParser, meetingScheduler);


        meetingRequestOne = new MeetingRequest(
                DateAndTimeParser.parseSubmissionDate("2015-08-17 10:20:00"),
                "EMP001",
                now,
                Duration.ZERO);

        meetingRequestTwo = new MeetingRequest(
                DateAndTimeParser.parseSubmissionDate("2015-08-17 10:10:00"),
                "EMP002",
                now,
                Duration.ZERO);

        given(requestParser.parse("request1")).willReturn(
                Optional.of(meetingRequestOne));

        given(requestParser.parse("request2")).willReturn(
                Optional.of(meetingRequestTwo));

        given(requestParser.parseOfficeHours("0900 1700")).willReturn(
                officeHours
        );

        meetingInputList = subject.readMeetingRequest();

    }

    @Test
    public void shouldRead_InputAndParse() throws IOException {
        subject.parseAndScheduleMeetings(meetingInputList);
        verify(requestParser).parse("request1");
    }


    @Test
    public void shouldSchedule_MeetingsOrderedBySubmissionTime() throws IOException {

        subject.parseAndScheduleMeetings(meetingInputList);
        InOrder order = inOrder(meetingScheduler);

        order.verify(meetingScheduler).scheduleMeeting(meetingRequestTwo);
        order.verify(meetingScheduler).scheduleMeeting(meetingRequestOne);
    }

    @Test
    public void shouldParse_OfficeHours() throws IOException {

        subject.parseAndScheduleMeetings(meetingInputList);
        String officeHourInput = "0900 1700";
        verify(requestParser).parseOfficeHours(officeHourInput);
        verify(meetingScheduler).setOfficeHours(officeHours);
    }


}