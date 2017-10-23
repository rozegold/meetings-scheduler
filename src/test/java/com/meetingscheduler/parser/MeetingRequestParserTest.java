package com.meetingscheduler.parser;

import com.meetingscheduler.model.MeetingRequest;
import com.meetingscheduler.parser.DateAndTimeParser;
import com.meetingscheduler.parser.MeetingRequestParser;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.time.Duration;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(JUnit4.class)
public class MeetingRequestParserTest {

    private MeetingRequestParser subject = new MeetingRequestParser();

    @Test
    public void shouldParse_MeetingRequest() {
        String meetingRequestInput = "2015-08-17 10:17:06 EMP001 2015-08-21 09:00 2";
        MeetingRequest expected = new MeetingRequest(DateAndTimeParser.parseSubmissionDate("2015-08-17 10:17:06"),
                "EMP001",
                DateAndTimeParser.parseMeetingDate("2015-08-21 09:00"),
                Duration.ofHours(2));
        assertThat(subject.parse(meetingRequestInput), is(Optional.of(expected)));

    }

    @Test
    public void shouldReject_InvalidDateInMeetingRequest(){
        String invalidMeetingRequestInput = "2015-08-:17 10:17:06 EMP001 2015-08-21 09:00 2";
        assertThat(subject.parse(invalidMeetingRequestInput), is(Optional.empty()));
    }

    @Test
    public void shouldReject_InvalidPatternInMeetingRequest(){
        String invalidMeetingRequestInput = "2015invalid-08-17 10:17:06 EMP001 2015-08-21 09:00 2";
        assertThat(subject.parse(invalidMeetingRequestInput), is(Optional.empty()));
    }
}