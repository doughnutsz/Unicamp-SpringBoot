package com.perfectmatch.unicampspringboot.services;

public interface AvatarServices {
    void setAvatar(Long id, String img);

    String getAvatar(Long id);
}
