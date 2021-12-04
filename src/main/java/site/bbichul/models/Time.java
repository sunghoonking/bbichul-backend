package site.bbichul.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import site.bbichul.dto.GraphRequestDto;
import site.bbichul.dto.TimeRequestDto;

import javax.persistence.*;

@Setter
@Getter
@NoArgsConstructor
@Entity
public class Time extends TimeStamped {
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private Long id;

    @Column(nullable = false)
    private int year;

    @Column(nullable = false)
    private int month;

    @Column(nullable = false)
    private int day;

    @Column(nullable = false)
    private int weekday;

    @Column(nullable = false)
    private int study_time;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "user_id",nullable = false)
    private User user;
;

    public Time(TimeRequestDto timeRequestDto, User user) {
        // 로그인 되어있는  ID 저장
        this.user = user;
        this.year = timeRequestDto.getYear();
        this.month = timeRequestDto.getMonth();
        this.day = timeRequestDto.getDay();
        this.weekday = timeRequestDto.getWeekday();
        this.study_time = timeRequestDto.getStudy_time();

    }


    public void updateStudyTime(int study_time){
        this.study_time = study_time;
    }
}