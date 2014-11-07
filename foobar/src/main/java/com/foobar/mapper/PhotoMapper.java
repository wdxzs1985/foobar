package com.foobar.mapper;

import java.util.List;
import java.util.Map;

import com.foobar.domain.PhotoBean;

public interface PhotoMapper {

    void insert(PhotoBean photoBean);

    List<PhotoBean> fetchList(Map<String, Object> params);
}
