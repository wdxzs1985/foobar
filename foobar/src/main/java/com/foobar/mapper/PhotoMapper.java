package com.foobar.mapper;

import java.util.List;
import java.util.Map;

import com.foobar.domain.PhotoBean;

public interface PhotoMapper {

    void insert(PhotoBean photoBean);

    List<Map<String, Object>> fetchPhotoGroup(Map<String, Object> param);

    List<PhotoBean> fetchPhotoByGroup(Map<String, Object> param);
}
