package com.foobar.www;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.foobar.domain.TagBean;
import com.foobar.domain.UserBean;
import com.foobar.exception.PageNotFoundException;
import com.foobar.service.TagService;
import com.foobar.support.PaginateSupport;

@Controller
@SessionAttributes("LOGIN_USER")
@Transactional
public class HomeTagController {

    @Autowired
    private MessageSource messageSource = null;
    @Autowired
    private TagService tagService = null;

    private Log log = LogFactory.getLog(this.getClass());

    @RequestMapping(method = RequestMethod.GET, value = "/tag/search")
    @ResponseBody
    public List<String> doGetSearchTag(@RequestParam final String query) {
        if (this.log.isDebugEnabled()) {
            this.log.debug("query = " + query);
        }
        if (StringUtils.isBlank(query)) {
            return Collections.emptyList();
        }

        final PaginateSupport<TagBean> page = new PaginateSupport<>(1);
        page.addParam("tag", query);

        this.tagService.searchTag(page);

        if (CollectionUtils.isNotEmpty(page.getItems())) {
            final List<String> tagList = new ArrayList<String>();
            for (final TagBean tagBean : page.getItems()) {
                tagList.add(tagBean.getTag());
            }
            return tagList;
        }

        return Collections.emptyList();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/tag/{tagId}")
    public String doGetTag(@PathVariable final Integer tagId,
                           @ModelAttribute("LOGIN_USER") final UserBean loginUser,
                           final Model model,
                           final Locale locale) {
        final TagBean tagBean = this.tagService.getTagById(tagId);

        if (tagBean == null) {
            throw new PageNotFoundException(this.messageSource.getMessage("error.pageNotFound",
                                                                          null,
                                                                          locale));
        }

        model.addAttribute("tagBean", tagBean);
        return "home/tag/index";
    }

}
