package com.foobar.mapper;

import java.util.List;
import java.util.Map;

import com.foobar.domain.ArticleTagBean;
import com.foobar.domain.TagBean;

public interface ArticleTagMapper {

    public void insert(ArticleTagBean articleTagBean);

    public void delete(ArticleTagBean articleTagBean);

    public List<TagBean> fetchList(Map<String, Object> param);
}
