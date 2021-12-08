package site.bbichul.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import site.bbichul.models.UserInfo;

import javax.transaction.Transactional;
import java.util.Optional;

@Repository
public interface UserInfoRepository extends JpaRepository<UserInfo, Long> {
    // DB에 있는 정보 찾기
//    @Transactional
//    Optional<UserInfo> findById(Long id);

    @Transactional
    Optional<UserInfo>findById(Long id);

    Long getById(UserInfo userInfo);
}
