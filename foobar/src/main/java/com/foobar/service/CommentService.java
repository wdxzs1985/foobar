package com.foobar.service;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.foobar.domain.CommentBean;
import com.foobar.manager.CommentManager;
import com.foobar.support.PaginateSupport;

@Service
@Transactional
public class CommentService {

    @Autowired
    private MessageSource messageSource = null;
    @Autowired
    private CommentManager commentManager = null;

    public void searchComment(final PaginateSupport<CommentBean> paginate) {
        final int itemCount = this.commentManager.countMusicComment(paginate.getParams());
        paginate.setItemCount(itemCount);
        paginate.compute();

        final List<CommentBean> items = this.commentManager.searchMusicComment(paginate.getParams());
        paginate.setItems(items);
    }

    public boolean doComment(final CommentBean commentBean,
                             final Integer parentId,
                             final Map<String, Object> model,
                             final Locale locale) {
        if (this.validateForComment(commentBean, model, locale)) {
            if (parentId != null) {
                final CommentBean parent = this.commentManager.getComment(parentId);
                if (parent == null) {
                    return false;
                }
                commentBean.setParent(parent);
                commentBean.setRoot(parent.getRoot());
            } else {
                commentBean.setRoot(commentBean);
            }
            this.commentManager.addComment(commentBean);
            return true;
        }
        return false;
    }

    private boolean validateForComment(final CommentBean commentBean,
                                       final Map<String, Object> model,
                                       final Locale locale) {
        boolean isValid = true;
        if (!this.commentManager.validateContent(commentBean.getContent(),
                                                 "error",
                                                 model,
                                                 locale)) {
            isValid = false;
        }
        return isValid;
    }

}
