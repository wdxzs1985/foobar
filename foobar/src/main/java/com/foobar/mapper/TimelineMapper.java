package com.foobar.mapper;

import java.util.List;
import java.util.Map;

import com.foobar.domain.TimelineBean;

public interface TimelineMapper {

    int countUserTimeline(Map<String, Object> params);

    List<TimelineBean> searchUserTimeline(Map<String, Object> params);

    int countFollowTimeline(Map<String, Object> params);

    List<TimelineBean> searchFollowTimeline(Map<String, Object> params);

}
