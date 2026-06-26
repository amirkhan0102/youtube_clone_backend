package dasturlash.uz.youtube.repository;


import dasturlash.uz.youtube.entity.ProfileEntity;
import dasturlash.uz.youtube.enums.ProfileStatusEnum;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;


public interface ProfileRepository extends CrudRepository<ProfileEntity, Integer> {

    Optional<ProfileEntity> findByEmailAndStatus(String username, ProfileStatusEnum profileStatusEnum);

    Optional<ProfileEntity> findByEmail(String email);

}
