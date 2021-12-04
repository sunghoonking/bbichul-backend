package site.bbichul.models;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import site.bbichul.dto.CalendarMemoDto;

import javax.persistence.*;
import javax.xml.soap.Text;


@NoArgsConstructor
@Entity
@Getter
public class CalendarMemo {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @ManyToOne
    @JoinColumn(name = "userCalendarId",nullable = true)
    private UserCalendar userCalendar;


    @Column(nullable = false, length = 200)
    private String dateData;

    @Column(nullable = true, length = 500)
    private String contents;


    public CalendarMemo(CalendarMemoDto calendarMemoDto) {
        this.dateData = calendarMemoDto.getDateData();
        this.contents = calendarMemoDto.getContents();
    }

    public CalendarMemo(CalendarMemoDto calendarMemoDto, UserCalendar userCalendar) {
        this.userCalendar = userCalendar;
        this.dateData = calendarMemoDto.getDateData();
        this.contents = calendarMemoDto.getContents();
    }



    public void updateMemo(CalendarMemoDto calendarMemoDto){
        this.dateData = calendarMemoDto.getDateData();
        this.contents = calendarMemoDto.getContents();
    }


}
