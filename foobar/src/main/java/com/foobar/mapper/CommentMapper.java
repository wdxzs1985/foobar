package com.foobar.mapper;

import java.util.List;
import java.util.Map;

import com.foobar.domain.CommentBean;

public interface CommentMapper {

    void insert(CommentBean commentBean);

    int countComicComment(Map<String, Object> params);

    List<CommentBean> fetchComicComment(Map<String, Object> params);

    int countMusicComment(Map<String, Object> params);

    List<CommentBean> fetchMusicComment(Map<String, Object> params);

    CommentBean fetchBean(Map<String, Object> params);

}
