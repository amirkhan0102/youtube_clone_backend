package dasturlash.uz.youtube.repository.custom;

import dasturlash.uz.youtube.dto.notification.NotificationFilterDTO;
import dasturlash.uz.youtube.entity.NotificationEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class CustomNotificationRepository {

    @Autowired
    private QueryPagination queryPagination;


    public Page<NotificationEntity> filter(NotificationFilterDTO dto, Integer page, Integer size){
        StringBuilder select =new StringBuilder( "SELECT n FROM NotificationEntity n ");
        StringBuilder count =new StringBuilder( "SELECT COUNT(DISTINCT n) FROM NotificationEntity n ");
        Map<String , Object> params = new HashMap<>();
        StringBuilder filter = new StringBuilder( "WHERE 1=1");

        if (dto.getProfileId() != null) {
            filter.append(" AND n.profileId = :profileId ");
            params.put("profileId", dto.getProfileId());
        }
        if (dto.getChannelId() != null) {
            filter.append(" AND n.channelId = :channelId ");
            params.put("channelId", dto.getChannelId());
        }
        if (dto.getVideoId() != null) {
            filter.append(" AND n.videoId = :videoId ");
            params.put("videoId", dto.getVideoId());
        }
        if (dto.getIsProfileReceived() != null) {
            filter.append(" AND n.isProfileReceived = :isProfileReceived ");
            params.put("isProfileReceived", dto.getIsProfileReceived());
        }
        if (dto.getDateFrom() != null) {
            filter.append(" AND n.createdDate >= :from");
            params.put("from", dto.getDateFrom());
        }
        if (dto.getDateTo() != null) {
            filter.append(" AND n.createdDate <= :to");
            params.put("to", dto.getDateTo());
        }


        select.append(filter).append("ORDER BY n.createdDate DESC ");
        count.append(filter);

        return queryPagination.getPaginationResult(
                select.toString(),
                count.toString(),
                params,
                page,
                size,
                NotificationEntity.class
        );
    }
}
