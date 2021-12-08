package site.bbichul.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.bbichul.models.TeamTask;

import java.util.List;

public interface TeamTaskRepository extends JpaRepository<TeamTask, Long> {
    List<TeamTask> findAllByTeamId(Long id);

    Long countByTeamIdAndDone(Long id, Boolean done);
}
