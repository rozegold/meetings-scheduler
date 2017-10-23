package com.meetingscheduler;

import com.meetingscheduler.meeting.Meeting;
import com.meetingscheduler.model.MeetingRequest;
import com.meetingscheduler.model.OfficeHours;
import com.meetingscheduler.parser.MeetingRequestParser;
import com.meetingscheduler.scheduler.MeetingScheduler;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * MeetingBookingSystem - reads the meetings requests from the input file,
 * Makes call to MeetingRequestParser and MeetingScheduler to schedule meeting
 * and displays list of all meetings
 *
 */
class MeetingBookingSystem {


    private String filePath;

    private MeetingRequestParser requestParser;
    private MeetingScheduler meetingScheduler;

    MeetingBookingSystem(final String filePath, final MeetingRequestParser requestParser, final MeetingScheduler meetingScheduler) {
        this.filePath = filePath;
        this.requestParser = requestParser;
        this.meetingScheduler = meetingScheduler;
    }

    public static void main(String[] args) {

        String filePath = "src/main/resources/MeetingRequestInput";

        MeetingBookingSystem meetingBookingSystem = new MeetingBookingSystem(filePath, new MeetingRequestParser(), new MeetingScheduler());

        List<String> meetingRequestsInput = meetingBookingSystem.readMeetingRequest();

        meetingBookingSystem.parseAndScheduleMeetings(meetingRequestsInput);
        meetingBookingSystem.showAllMeetings();

    }

    List<String> readMeetingRequest() {
        Path filePath = Paths.get(this.filePath);
        Stream<String> lines = null;
        try {
            lines = Files.lines(filePath);
        } catch (NoSuchFileException ex) {
            System.out.println("Please specify correct file name");
        } catch (IOException ex) {
            System.out.println("Unable to read file");
        }

        return lines.collect(Collectors.toList());
    }

    void parseAndScheduleMeetings(List<String> inputList) {

        if (null == inputList) {
            return;
        }

        extractOfficeHours(inputList);

        inputList.stream().skip(1).map(meetingRequestInput -> requestParser.parse(meetingRequestInput))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .sorted(Comparator.comparing(MeetingRequest::getSubmissionTime))
                .forEach(meetingRequest ->
                        meetingScheduler.scheduleMeeting(meetingRequest));
    }

    private void extractOfficeHours(final List<String> inputList) {
        if (null == inputList || inputList.isEmpty())
            return;

        OfficeHours officeHours = requestParser.parseOfficeHours(inputList.get(0));
        meetingScheduler.setOfficeHours(officeHours);
    }

    private void showAllMeetings() {

        Map<LocalDate, List<Meeting>> meetings = meetingScheduler.getMeetings();

        for (LocalDate meetingDate : meetings.keySet()) {
            System.out.println(meetingDate);
            meetings.get(meetingDate)
                    .forEach(meeting ->
                            System.out.println(meeting.getStartTime() + " " + meeting.getEndTime() + " " + meeting.getEmpId())
                    );
        }
    }
}
