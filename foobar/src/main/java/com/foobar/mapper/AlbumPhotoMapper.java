package com.foobar.mapper;

import java.util.Map;

import com.foobar.domain.AlbumPhotoBean;

public interface AlbumPhotoMapper {

    void insert(AlbumPhotoBean albumPhotoBean);

    int count(Map<String, Object> params);

    void delete(AlbumPhotoBean albumPhotoBean);

    void update(AlbumPhotoBean albumPhotoBean);

}
