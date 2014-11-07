package com.foobar.mapper;

import java.util.List;
import java.util.Map;

import com.foobar.domain.TagBean;

public interface TagMapper {

    public int count(Map<String, Object> param);

    public List<TagBean> search(Map<String, Object> params);

    public TagBean fetchBean(Map<String, Object> param);

    public void insert(TagBean tagBean);

}
