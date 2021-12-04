package site.bbichul.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import site.bbichul.dto.TeamRequestDto;
import site.bbichul.dto.TeamTaskRequestDto;
import site.bbichul.dto.TeamProgressbarResponseDto;
import site.bbichul.models.Team;
import site.bbichul.models.TeamTask;
import site.bbichul.models.User;
import site.bbichul.repository.TeamRepository;
import site.bbichul.repository.TeamTaskRepository;
import site.bbichul.repository.UserRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class TeamService {

    private final TeamRepository teamRepository;
    private final UserRepository userRepository;
    private final TeamTaskRepository teamTaskRepository;

    public String checkTeam(User user) {
        if (user.getTeam() == null) {
            return "아직 소속된 팀이 없습니다.";
        }

        return user.getTeam().getTeamname();
    }

    @Transactional
    public String createTeam(TeamRequestDto teamRequestDto, User user) {
        String teamname = teamRequestDto.getTeamname();

        Optional<Team> found = teamRepository.findByTeamname(teamname);
        if (found.isPresent()) {
            throw new IllegalArgumentException("중복된 팀명이 존재합니다.");
        }

        Team team = new Team(teamname);
        teamRepository.save(team);

        user.setTeam(team);
        userRepository.save(user);
        return team.getTeamname();
    }

    public TeamTask addTask(TeamTaskRequestDto teamTaskRequestDto, User user) {
        TeamTask teamTask = new TeamTask(teamTaskRequestDto);
        teamTask.setDone(false);
        teamTask.setTeam(user.getTeam());
        teamTaskRepository.save(teamTask);

        return teamTask;
    }

    public List<TeamTask> showTask(User user) {
        Long teamId = user.getTeam().getId();
        List<TeamTask> teamTask = teamTaskRepository.findAllByTeamId(teamId);
        return teamTask;
    }

    public void deleteTask(TeamTaskRequestDto teamTaskRequestDto) {
        Long id = teamTaskRequestDto.getId();
        teamTaskRepository.deleteById(id);
    }

    @Transactional
    public void changeTask(TeamTaskRequestDto teamTaskRequestDto) {
        Long id = teamTaskRequestDto.getId();
        Optional<TeamTask> teamTask = teamTaskRepository.findById(id);
        if (teamTask.get().getDone() == false) {
            teamTask.get().setDone(true);
        }
        else {
            teamTask.get().setDone(false);
        }
    }

    public List<User> checkStatus(User user) {
        List<User> users = userRepository.findAllByTeamId(user.getTeam().getId());
        return users;
    }

    public String signupTeam(TeamRequestDto teamRequestDto, User user) {
        String teamname = teamRequestDto.getTeamname();
        String message;

        Optional<Team> found = teamRepository.findByTeamname(teamname);
        if (found.isPresent()) {
            user.setTeam(found.get());
            userRepository.save(user);
            message = "초대받은 팀에 가입되었습니다.";
        }
        else {
            message = "존재하지 않는 팀입니다. 팀 이름을 확인해주세요.";
        }
        return message;
    }

    public String checkName(TeamRequestDto teamRequestDto) {
        String teamname = teamRequestDto.getTeamname();
        String message;

        Optional<Team> result = teamRepository.findByTeamname(teamname);
        if (result.isPresent()) {
            message = "중복되는 팀 이름입니다. 다시 입력해주세요.";
        } else {
            message = "사용할 수 있는 팀 이름입니다.";
        }
        return message;
    }

    public TeamProgressbarResponseDto getTeamProgressbar(TeamProgressbarResponseDto teamProgressbarResponseDto, User user) {
        Long teamId = user.getTeam().getId();
        Long doneCount = teamTaskRepository.countByTeamIdAndDone(teamId, true);
        Long notDoneCount = teamTaskRepository.countByTeamIdAndDone(teamId, false);
        Integer total = (int) (doneCount + notDoneCount);
        Integer percent = (int) Math.round(((double) doneCount / total) * 100);

        if (total == 0) {
            teamProgressbarResponseDto.setDoneCount(0L);
            teamProgressbarResponseDto.setPercent(0);
        } else {
            teamProgressbarResponseDto.setDoneCount(doneCount);
            teamProgressbarResponseDto.setPercent(percent);
        }

        return teamProgressbarResponseDto;
    }
}
