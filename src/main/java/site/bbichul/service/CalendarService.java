package site.bbichul.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.bbichul.dto.CalendarMemoDto;
import site.bbichul.exception.BbichulErrorCode;
import site.bbichul.exception.BbichulException;
import site.bbichul.models.CalendarMemo;
import site.bbichul.models.User;
import site.bbichul.models.UserCalendar;
import site.bbichul.repository.CalendarMemoRepository;
import site.bbichul.repository.UserCalendarRepository;
import site.bbichul.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CalendarService {

    private final CalendarMemoRepository calendarMemoRepository;
    private final UserRepository userRepository;
    private final UserCalendarRepository userCalendarRepository;


    public CalendarMemo getMemoClickedDay(String dateData,String calendarType, String username) {
        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new BbichulException(BbichulErrorCode.NOT_FOUND_USER));

        UserCalendar userCalendar = null;
        Long calendarId = null;
        if (calendarType.substring(0, 1).equals("P")) {
            userCalendar = userCalendarRepository.findByUserIdAndCalendarType(
                    user.getId(),
                    calendarType).orElseThrow(
                    () -> new BbichulException(BbichulErrorCode.NOT_FOUND_MATCHED_CALENDAR)
            );

            calendarId = userCalendar.getId();
        } else if (calendarType.substring(0, 1).equals("T")) {
            userCalendar = userCalendarRepository.findByTeamIdAndCalendarType(
                    user.getTeam().getId(),
                    calendarType).orElseThrow(
                    () -> new BbichulException(BbichulErrorCode.NOT_FOUND_MATCHED_CALENDAR)
            );

            calendarId = userCalendar.getId();
        }

        try{
            CalendarMemo calendarMemo = calendarMemoRepository.findByUserCalendarIdAndDateData(calendarId, dateData).orElseThrow(
                    ()-> new BbichulException(BbichulErrorCode.NOT_FOUND_MEMO));
            return calendarMemo;
        }catch (BbichulException e){
            CalendarMemoDto calendarMemoDto = new CalendarMemoDto();
            calendarMemoDto.setCalendarType(calendarType);
            calendarMemoDto.setDateData(dateData);
            calendarMemoDto.setContents("");
            System.out.println(calendarMemoDto);
            return new CalendarMemo(calendarMemoDto);
        }

    }

    @Transactional
    public void updateMemo(CalendarMemoDto calendarMemoDto, String username) {

        User user = userRepository.findByUsername(username).orElseThrow(
                    () -> new BbichulException(BbichulErrorCode.NOT_FOUND_USER));

        String checkTeam = calendarMemoDto.getCalendarType().substring(0, 1);


        UserCalendar userCalendar = null;
        Long calendarId = null;
        if (checkTeam.equals("P")) {
            userCalendar = userCalendarRepository.findByUserIdAndCalendarType(
                    user.getId(),
                    calendarMemoDto.getCalendarType()).orElseThrow(
                    () -> new BbichulException(BbichulErrorCode.NOT_FOUND_MATCHED_CALENDAR)
            );

            calendarId = userCalendar.getId();
        } else if (checkTeam.equals("T")) {
            userCalendar = userCalendarRepository.findByTeamIdAndCalendarType(
                    user.getTeam().getId(),
                    calendarMemoDto.getCalendarType()).orElseThrow(
                    () -> new BbichulException(BbichulErrorCode.NOT_FOUND_MATCHED_CALENDAR)
            );

            calendarId = userCalendar.getId();
        }

        try{
            CalendarMemo getMemo = calendarMemoRepository.findByUserCalendarIdAndDateData(calendarId, calendarMemoDto.getDateData()).orElseThrow(
                () -> new BbichulException(BbichulErrorCode.NOT_FOUND_MEMO));
            getMemo.updateMemo(calendarMemoDto);
        }catch (BbichulException e){
            CalendarMemo calendarMemo = new CalendarMemo(calendarMemoDto, userCalendar);
            calendarMemoRepository.save(calendarMemo);
        }
    }

    @Transactional
    public List<UserCalendar> getUserInfo(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new BbichulException(BbichulErrorCode.NOT_FOUND_USER)
        );

        boolean isPrivateEmptied = userCalendarRepository.findAllByUserId(user.getId()).isEmpty();


        if (isPrivateEmptied) {
            UserCalendar userCalendar = new UserCalendar(user);
            userCalendar.setCalendarType("P1");
            userCalendar.setUserCount(1);
            userCalendarRepository.save(userCalendar);
        }

        if (user.getTeam() != null) {
            Long getTeamCalendarId = user.getTeam().getId();
            boolean isTeamEmptied = userCalendarRepository.findAllByTeamId(getTeamCalendarId).isEmpty();

            if (isTeamEmptied) {
                UserCalendar userCalendarT = new UserCalendar(user.getTeam());
                userCalendarT.setCalendarType("T1");
                userCalendarT.setTeamCount(1);

                userCalendarRepository.save(userCalendarT);
            }
        }
        Long teamId = user.getTeam() != null ? user.getTeam().getId() : null;

        return userCalendarRepository.findAllByUserIdOrTeamId(user.getId(), teamId);
    }

    public List<CalendarMemo> getTypeAllMemo(String calendarType, String username) {

        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new BbichulException(BbichulErrorCode.NOT_FOUND_USER));

        UserCalendar userCalendar;
        Long calendarId = null;
        if (calendarType.substring(0, 1).equals("P")) {
            userCalendar = userCalendarRepository.findByUserIdAndCalendarType(
                    user.getId(),
                    calendarType).orElseThrow(
                    () -> new BbichulException(BbichulErrorCode.NOT_FOUND_MATCHED_CALENDAR)
            );

            calendarId = userCalendar.getId();
        } else if (calendarType.substring(0, 1).equals("T")) {

            userCalendar = userCalendarRepository.findByTeamIdAndCalendarType(
                    user.getTeam().getId(),
                    calendarType).orElseThrow(
                    () -> new BbichulException(BbichulErrorCode.NOT_FOUND_MATCHED_CALENDAR)
            );

            calendarId = userCalendar.getTeam().getId();

        }

        List<CalendarMemo> calendarMemoList = calendarMemoRepository.findAllByUserCalendarId(calendarId);

        return calendarMemoList;

    }

    @Transactional
    public void addCalendar(boolean isPrivated, String username) {

        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new BbichulException(BbichulErrorCode.NOT_FOUND_USER));


        List<UserCalendar> userCalendarList = null;
        UserCalendar userCalendar = null;
        if (isPrivated) {
            userCalendarList = userCalendarRepository.findAllByUserId(user.getId());

            int listLength = userCalendarList.size();

            for (int i = 0; i < listLength; i++) {
                userCalendarList.get(i).setUserCount(listLength+1);
            }

            userCalendar = new UserCalendar(user);
            userCalendar.setUserCount(listLength+1);
            userCalendar.setCalendarType("P" + Integer.toString(listLength+1));


        } else if (!isPrivated) {

            userCalendarList = userCalendarRepository.findAllByTeamId(user.getTeam().getId());

            int listLength = userCalendarList.size();

            for (int i = 0; i < listLength; i++) {
                userCalendarList.get(i).setTeamCount(listLength+1);
            }

            userCalendar = new UserCalendar(user.getTeam());
            userCalendar.setUserCount(listLength+1);
            userCalendar.setCalendarType("T" + Integer.toString(listLength+1));
        }

        userCalendarRepository.save(userCalendar);
    }
}

