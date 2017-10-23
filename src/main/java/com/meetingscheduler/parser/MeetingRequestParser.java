package com.meetingscheduler.parser;

import com.meetingscheduler.model.MeetingRequest;
import com.meetingscheduler.model.OfficeHours;

import java.time.Duration;
import java.time.format.DateTimeParseException;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * MeetingRequestParser - parses the meeting
 * request input
 */
public class MeetingRequestParser {


    public Optional<MeetingRequest> parse(final String input) {
        //TODO : improve this regex
        final String regex = "([\\d- :]{19}) (.*) ([\\d- :]{16}) (.*)";
        final Matcher matcher = Pattern.compile(regex).matcher(input);

        if (!matcher.find())
            return Optional.empty();

        String submissionTime = matcher.group(1);
        String empID = matcher.group(2);
        String meetingStartTime = matcher.group(3);
        String duration = matcher.group(4);

        try {
            return Optional.of(new MeetingRequest(DateAndTimeParser.parseSubmissionDate(submissionTime),
                    empID,
                    DateAndTimeParser.parseMeetingDate(meetingStartTime),
                    Duration.ofHours(Long.parseLong(duration))));
        } catch (DateTimeParseException ex) {

            // TODO log exception
            return Optional.empty();
        }
    }


    public OfficeHours parseOfficeHours(final String officeHourInput) {
        //TODO validate input
        final String WHITE_SPACE = " ";
        final String[] officeHoursString = officeHourInput.split(WHITE_SPACE);
        try {
            return new OfficeHours(DateAndTimeParser.parseOfficeHours(officeHoursString[0]), DateAndTimeParser.parseOfficeHours(officeHoursString[1]));
        } catch (DateTimeParseException ex) {
            return new OfficeHours();
        }
    }
}
