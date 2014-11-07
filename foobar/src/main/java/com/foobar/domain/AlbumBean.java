package com.foobar.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_DEFAULT)
public class AlbumBean extends ArticleBean {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public static final Integer SINGLE = 1;

    public static final Integer MULTIPLE = 2;

    private Integer type = null;

    public Integer getType() {
        return this.type;
    }

    public void setType(final Integer type) {
        this.type = type;
    }
}
