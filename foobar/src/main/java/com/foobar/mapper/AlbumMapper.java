package com.foobar.mapper;

import java.util.List;
import java.util.Map;

import com.foobar.domain.AlbumBean;
import com.foobar.domain.ArticleBean;

public interface AlbumMapper {

    public int count(final Map<String, Object> params);

    public void insert(AlbumBean albumBean);

    public void delete(ArticleBean articleBean);

    public List<AlbumBean> search(final Map<String, Object> params);

    public AlbumBean fetchOne(Map<String, Object> params);

}
