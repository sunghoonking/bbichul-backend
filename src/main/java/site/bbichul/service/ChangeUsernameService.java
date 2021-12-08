package site.bbichul.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import site.bbichul.models.User;
import site.bbichul.repository.UserRepository;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class ChangeUsernameService {
    private final UserRepository userRepository;

    @Transactional
    public Map<String, Object> getUsername(User user) {

        User user1 = userRepository.findById(user.getId()).orElseThrow(
                () -> new NullPointerException("해당 아이디가 존재하지 않습니다.")
        );

        Map<String, Object> map = new HashMap();	//<키 자료형, 값 자료형>
        map.put("nickName", user1.getUsername());

        return map;
    }
}
