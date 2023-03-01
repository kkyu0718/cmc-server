package cmc.service;


import cmc.domain.Avatar;
import cmc.domain.World;
import cmc.domain.WorldAvatar;
import cmc.error.exception.BusinessException;
import cmc.error.exception.ErrorCode;
import cmc.repository.AvatarRepository;
import cmc.domain.User;
import cmc.repository.UserRepository;
import cmc.repository.WorldAvatarRepository;
import cmc.repository.WorldRepository;
import cmc.utils.S3Util;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AvatarService {
    private final UserRepository userRepository;
    private final AvatarRepository avatarRepository;
    private final WorldRepository worldRepository;
    private final WorldAvatarRepository worldAvatarRepository;
    private final S3Util s3Util;

    @Transactional
    public void saveAvatar(Long userId, String avatarName, String avatarMessage, MultipartFile file) {

        String imgUrl = s3Util.upload(file, "avatar");

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        Avatar avatar = Avatar.builder()
                .avatarName(avatarName)
                .avatarMessage(avatarMessage)
                .avatarImg(imgUrl)
                .user(user)
                .build();

        avatarRepository.save(avatar);
    }

    public List<World> getWorldsByAvatar(Long avatarId) {
        return worldRepository.findWorldWithAvatar(avatarId);
    }

    @Transactional
    public void updateAvatarImg(Long avatarId, MultipartFile file) {

        String imgUrl = s3Util.upload(file, "avatar");

        Avatar avatar = avatarRepository.findById(avatarId)
                .orElseThrow(() -> new BusinessException(ErrorCode.AVATAR_NOT_FOUND));

        avatar.setAvatarImg(imgUrl);

        avatarRepository.save(avatar);
    }

    @Transactional
    public void updateAvatarInfo(Long avatarId, String avatarName, String avatarMessage) {

        Avatar avatar = avatarRepository.findById(avatarId)
                .orElseThrow(() -> new BusinessException(ErrorCode.AVATAR_NOT_FOUND));

        avatar.setAvatarName(avatarName);
        avatar.setAvatarMessage(avatarMessage);

        avatarRepository.save(avatar);
    }

    public Avatar getAvatarByAvatarId(Long avatarId) {
        return avatarRepository.findById(avatarId)
                .orElseThrow(() -> new BusinessException(ErrorCode.AVATAR_NOT_FOUND));
    }

    @Transactional
    public void deleteAvatarById(Long avatarId) {
        Avatar avatar = avatarRepository.findById(avatarId)
                .orElseThrow(() -> new BusinessException(ErrorCode.AVATAR_NOT_FOUND));
        avatarRepository.delete(avatar);
    }

    public void enterWorld(Long avatarId, Long worldId) {

        World world = worldRepository.findById(worldId)
                        .orElseThrow(() -> new BusinessException(ErrorCode.WORLD_NOT_FOUND));

        Avatar avatar = avatarRepository.findById(avatarId)
                        .orElseThrow(() -> new BusinessException(ErrorCode.AVATAR_NOT_FOUND));

        WorldAvatar worldAvatar = WorldAvatar.builder()
                .world(world)
                .avatar(avatar)
                .build();

        worldAvatarRepository.save(worldAvatar);
    }
}
