package com.meetingscheduler.parser;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * DateAndTimeParser - parses submission date,
 * meeting date and office hours
 *
 */
public class DateAndTimeParser {


    public static LocalDateTime parseSubmissionDate(final String inputSubmissionDate) {
        return LocalDateTime.parse(
                inputSubmissionDate,
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    public static LocalDateTime parseMeetingDate(final String inputMeetingDate) {
        return LocalDateTime.parse(
                inputMeetingDate,
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
    }

    public static LocalTime parseOfficeHours(final String officeHour) {
        return LocalTime.parse(
                officeHour,
                DateTimeFormatter.ofPattern("HHmm"));
    }

}
