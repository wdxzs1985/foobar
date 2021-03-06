package com.foobar.manager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.foobar.domain.TagBean;
import com.foobar.mapper.ArticleTagMapper;
import com.foobar.mapper.TagMapper;

@Component
public class TagManager {

    @Autowired
    private TagMapper tagMapper = null;
    @Autowired
    private ArticleTagMapper articleTagMapper = null;

    public int countTag(final Map<String, Object> params) {
        return this.tagMapper.count(params);
    }

    public List<TagBean> searchTag(final Map<String, Object> params) {
        return this.tagMapper.search(params);
    }

    public TagBean getById(final Integer tagId) {
        final Map<String, Object> param = new HashMap<>();
        param.put("id", tagId);
        return this.tagMapper.fetchBean(param);
    }

    public TagBean getByTag(final String tag) {
        final Map<String, Object> param = new HashMap<>();
        param.put("tag", tag);
        return this.tagMapper.fetchBean(param);
    }

    public TagBean create(final String tag) {
        TagBean tagBean = this.getByTag(tag);
        if (tagBean == null) {
            tagBean = new TagBean();
            tagBean.setTag(tag);
            this.tagMapper.insert(tagBean);
        }
        return tagBean;
    }

}
