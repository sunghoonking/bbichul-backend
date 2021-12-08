package site.bbichul.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import site.bbichul.models.UserCalendar;

import java.util.List;

@Repository
public interface UserCalendarRepository extends JpaRepository<UserCalendar, Long> {

    List<UserCalendar> findAllByUserId(Long userId);

    List<UserCalendar> findAllByTeamId(Long getTeamCalendarId);

    List<UserCalendar> findAllByUserIdOrTeamId(Long userId, Long teamId);

}
