package com.foobar.domain;

public class AlbumPhotoBean extends DtoBean {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private ArticleBean articleBean = null;

    private FileBean fileBean = null;

    private Integer orderNo = null;

    public Integer getOrderNo() {
        return this.orderNo;
    }

    public void setOrderNo(final Integer orderNo) {
        this.orderNo = orderNo;
    }

    public ArticleBean getArticleBean() {
        return this.articleBean;
    }

    public void setArticleBean(final ArticleBean articleBean) {
        this.articleBean = articleBean;
    }

    public FileBean getFileBean() {
        return this.fileBean;
    }

    public void setFileBean(final FileBean fileBean) {
        this.fileBean = fileBean;
    }

}
