package site.bbichul.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import site.bbichul.dto.GoalRequestDto;
import site.bbichul.security.UserDetailsImpl;
import site.bbichul.service.GoalService;

import java.util.Map;

@RequestMapping("/api")
@RequiredArgsConstructor
@RestController
public class GoalController {

    private final GoalService goalService;

    @PutMapping("/users/goal")
    public Map<String, String> updateGoal(@RequestBody GoalRequestDto goalRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return goalService.updateGoal(goalRequestDto, userDetails.getUser());
    }

    @GetMapping("/users/goal")
    public Map<String,Object> getGoal(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return goalService.getGoal(userDetails.getUser());
    }
}