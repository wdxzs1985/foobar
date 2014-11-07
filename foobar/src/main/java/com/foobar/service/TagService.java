package com.foobar.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.foobar.domain.TagBean;
import com.foobar.manager.TagManager;
import com.foobar.support.PaginateSupport;

@Service
@Transactional
public class TagService {

    @Autowired
    private TagManager tagManager = null;

    public void searchTag(final PaginateSupport<TagBean> paginate) {
        final int itemCount = this.tagManager.countTag(paginate.getParams());
        paginate.setItemCount(itemCount);
        paginate.compute();

        final List<TagBean> items = this.tagManager.searchTag(paginate.getParams());
        paginate.setItems(items);
    }

    public TagBean getTagById(final Integer tagId) {
        return this.tagManager.getById(tagId);
    }

    public TagBean getTagByTag(final String tag) {
        return this.tagManager.getByTag(tag);
    }

}
