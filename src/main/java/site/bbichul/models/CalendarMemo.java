package site.bbichul.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.bbichul.dto.CalendarMemoDto;
import site.bbichul.utills.CalendarMemoValidator;

import javax.persistence.*;


@NoArgsConstructor
@Entity
@Getter
public class CalendarMemo {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "userCalendarId",nullable = true)
    private UserCalendar userCalendar;


    @Column(nullable = false, length = 50)
    private String dateData;

    @Column(nullable = true, columnDefinition = "TEXT")
    private String contents;

    public CalendarMemo(CalendarMemoDto calendarMemoDto, UserCalendar userCalendar) {
        CalendarMemoValidator.validateCreateCalendarMemo(calendarMemoDto, userCalendar);
        this.userCalendar = userCalendar;
        this.dateData = calendarMemoDto.getDateData();
        this.contents = calendarMemoDto.getContents();
    }

    public void updateMemo(CalendarMemoDto calendarMemoDto){
        CalendarMemoValidator.validateUpdateCalendarMemo(calendarMemoDto);
        this.dateData = calendarMemoDto.getDateData();
        this.contents = calendarMemoDto.getContents();
    }


}
