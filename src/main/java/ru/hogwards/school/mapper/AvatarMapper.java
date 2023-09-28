package ru.hogwards.school.mapper;

import org.springframework.stereotype.Component;
import ru.hogwards.school.dto.AvatarDto;
import ru.hogwards.school.entity.Avatar;

@Component
public class AvatarMapper {
    public AvatarDto toDto(Avatar avatar){
        AvatarDto avatarDto = new AvatarDto();
        avatarDto.setId(avatarDto.getId());
        avatarDto.setFileSize(avatar.getFileSize());
        avatarDto.setMediaType(avatar.getMediaType());
        avatarDto.setAvatarUrl("http://localhost:8080/avatars/" + avatar.getId() + "/from-db");
        return avatarDto;
    }
}
