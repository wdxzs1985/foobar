package com.foobar.mapper;

import java.util.Map;

import com.foobar.domain.ArticleBean;

public interface ArticleMapper {

    public void insert(ArticleBean articleBean);

    public void update(Map<String, Object> param);

    public void delete(ArticleBean articleBean);
}
