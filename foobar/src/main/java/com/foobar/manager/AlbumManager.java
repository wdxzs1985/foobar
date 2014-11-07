package com.foobar.manager;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import com.foobar.domain.AccessBean;
import com.foobar.domain.AlbumBean;
import com.foobar.domain.AlbumPhotoBean;
import com.foobar.domain.ArticleBean;
import com.foobar.domain.ArticleTagBean;
import com.foobar.domain.FileBean;
import com.foobar.domain.PhotoBean;
import com.foobar.domain.TagBean;
import com.foobar.domain.UserBean;
import com.foobar.mapper.AccessMapper;
import com.foobar.mapper.AlbumMapper;
import com.foobar.mapper.AlbumPhotoMapper;
import com.foobar.mapper.ArticleMapper;
import com.foobar.mapper.ArticleTagMapper;
import com.foobar.mapper.FileMapper;
import com.foobar.mapper.PhotoMapper;
import com.foobar.mapper.TagMapper;

@Component
public class AlbumManager {

    public static final int TITLE_LENGTH = 100;

    @Autowired
    private MessageSource messageSource = null;
    @Autowired
    private AlbumMapper albumMapper = null;
    @Autowired
    private ArticleMapper articleMapper = null;
    @Autowired
    private AccessMapper accessMapper = null;
    @Autowired
    private ArticleTagMapper articleTagMapper = null;
    @Autowired
    private TagMapper tagMapper = null;
    @Autowired
    private FileMapper fileMapper = null;
    @Autowired
    private PhotoMapper photoMapper = null;
    @Autowired
    private AlbumPhotoMapper albumPhotoMapper = null;

    public void insert(final AlbumBean albumBean) {
        this.articleMapper.insert(albumBean);
        this.albumMapper.insert(albumBean);
    }

    public void update(final ArticleBean articleBean) {
        final Map<String, Object> param = new HashMap<>();
        param.put("id", articleBean.getId());
        param.put("title", articleBean.getTitle());
        param.put("description", articleBean.getDescription());
        this.articleMapper.update(param);
    }

    public void delete(final ArticleBean articleBean) {
        this.articleMapper.delete(articleBean);
        this.albumMapper.delete(articleBean);
    }

    public void publish(final ArticleBean articleBean) {
        final Map<String, Object> param = new HashMap<>();
        param.put("id", articleBean.getId());
        param.put("publishFlg", "1");
        this.articleMapper.update(param);
    }

    public void addAccess(final ArticleBean articleBean, final UserBean userBean) {
        final AccessBean accessBean = new AccessBean();
        accessBean.setArticleBean(articleBean);
        accessBean.setUserBean(userBean);
        this.accessMapper.insert(accessBean);
        this.accessMapper.updateArticle(accessBean);

        int accessCount = articleBean.getAccessCount();
        accessCount++;
        articleBean.setAccessCount(accessCount);
    }

    public void addTag(final ArticleBean articleBean, final TagBean tagBean) {
        final ArticleTagBean articleTagBean = new ArticleTagBean();
        articleTagBean.setArticleBean(articleBean);
        articleTagBean.setTagBean(tagBean);
        this.articleTagMapper.insert(articleTagBean);
    }

    public List<TagBean> getTags(final ArticleBean articleBean) {
        final Map<String, Object> param = new HashMap<>();
        param.put("articleBean", articleBean);
        return this.articleTagMapper.fetchList(param);
    }

    public void removeTags(final ArticleBean articleBean) {
        final ArticleTagBean articleTagBean = new ArticleTagBean();
        articleTagBean.setArticleBean(articleBean);
        this.articleTagMapper.delete(articleTagBean);
    }

    public List<FileBean> getFiles(final Integer articleId, final String contentType) {
        final Map<String, Object> param = new HashMap<>();
        param.put("articleId", articleId);
        param.put("contentType", contentType);
        return this.fileMapper.fetchList(param);
    }

    public FileBean getFile(final Integer id) {
        final Map<String, Object> param = new HashMap<>();
        param.put("id", id);
        return this.fileMapper.fetchBean(param);
    }

    public void addFile(final FileBean fileBean) {
        this.fileMapper.insert(fileBean);
    }

    public void deleteFile(final FileBean fileBean) {
        this.fileMapper.delete(fileBean);
    }

    public boolean validateTitle(final String title,
                                 final String errorAttribute,
                                 final Map<String, Object> model,
                                 final Locale locale) {
        boolean isValid = true;
        final String fieldName = this.messageSource.getMessage("ArticleBean.title", null, locale);
        if (StringUtils.isBlank(title)) {
            model.put(errorAttribute,
                      this.messageSource.getMessage("validate.empty", new Object[] { fieldName }, locale));
            isValid = false;
        } else if (StringUtils.length(title) > AlbumManager.TITLE_LENGTH) {
            model.put(errorAttribute,
                      this.messageSource.getMessage("validate.tooLong", new Object[] { fieldName,
                              AlbumManager.TITLE_LENGTH }, locale));
            isValid = false;
        }
        return isValid;
    }

    public int countAlbum(final Map<String, Object> params) {
        return this.albumMapper.count(params);
    }

    public List<AlbumBean> searchAlbum(final Map<String, Object> params) {
        return this.albumMapper.search(params);
    }

    public AlbumBean findById(final Integer id) {
        final Map<String, Object> params = new HashMap<String, Object>();
        params.put("id", id);
        return this.albumMapper.fetchOne(params);
    }

    public List<PhotoBean> findPhotoByAlbum(final AlbumBean albumBean) {
        final Map<String, Object> params = new HashMap<String, Object>();
        params.put("articleBean", albumBean);
        return this.photoMapper.fetchList(params);
    }

    public AlbumPhotoBean savePhoto(final AlbumBean albumBean, final PhotoBean photoBean) {
        this.fileMapper.insert(photoBean);
        this.photoMapper.insert(photoBean);

        final Map<String, Object> params = new HashMap<String, Object>();
        params.put("articleBean", albumBean);
        final int orderNo = this.albumPhotoMapper.count(params) + 1;

        final AlbumPhotoBean albumPhotoBean = new AlbumPhotoBean();
        albumPhotoBean.setArticleBean(albumBean);
        albumPhotoBean.setFileBean(photoBean);
        albumPhotoBean.setOrderNo(orderNo);

        this.albumPhotoMapper.insert(albumPhotoBean);

        return albumPhotoBean;
    }

    public void deletePhoto(final AlbumBean albumBean, final PhotoBean photoBean) {
        final AlbumPhotoBean albumPhotoBean = new AlbumPhotoBean();
        albumPhotoBean.setArticleBean(albumBean);
        albumPhotoBean.setFileBean(photoBean);
        this.albumPhotoMapper.delete(albumPhotoBean);
    }

    public void sortPhoto(final AlbumBean albumBean, final List<PhotoBean> photoList) {
        for (int i = 0; i < photoList.size(); i++) {
            final AlbumPhotoBean albumPhotoBean = new AlbumPhotoBean();
            albumPhotoBean.setArticleBean(albumBean);
            albumPhotoBean.setFileBean(photoList.get(i));
            albumPhotoBean.setOrderNo(i + 1);

            this.albumPhotoMapper.update(albumPhotoBean);
        }
    }

}