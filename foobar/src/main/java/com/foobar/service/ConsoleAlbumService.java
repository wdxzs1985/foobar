package com.foobar.service;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.foobar.domain.AlbumBean;
import com.foobar.domain.AlbumPhotoBean;
import com.foobar.domain.PhotoBean;
import com.foobar.manager.AlbumManager;
import com.foobar.manager.FileManager;
import com.foobar.support.PaginateSupport;

@Service
public class ConsoleAlbumService {

    @Autowired
    private AlbumManager albumManager = null;
    @Autowired
    private FileManager fileManager = null;
    @Autowired
    private MessageSource messageSource = null;

    public void searchAlbum(final PaginateSupport<AlbumBean> paginate) {
        final int itemCount = this.albumManager.countAlbum(paginate.getParams());
        paginate.setItemCount(itemCount);
        paginate.compute();

        final List<AlbumBean> items = this.albumManager.searchAlbum(paginate.getParams());
        paginate.setItems(items);
    }

    public boolean doCreate(final AlbumBean albumBean, final Map<String, Object> model, final Locale locale) {
        boolean result = false;
        if (this.validateInputAlbum(albumBean, model, locale)) {
            this.albumManager.insert(albumBean);
            result = true;
        }
        return result;
    }

    private boolean validateInputAlbum(final AlbumBean albumBean, final Map<String, Object> model, final Locale locale) {
        boolean isValid = true;
        final String title = albumBean.getTitle();
        if (!this.albumManager.validateTitle(title, "title.error", model, locale)) {
            isValid = false;
        }
        return isValid;
    }

    public AlbumBean findById(final Integer id) {
        return this.albumManager.findById(id);
    }

    public List<PhotoBean> findPhotoByAlbum(final AlbumBean albumBean) {
        return this.albumManager.findPhotoByAlbum(albumBean);
    }

    public boolean addPhoto(final AlbumBean albumBean,
                            final MultipartFile upload,
                            final Map<String, Object> fileModel,
                            final Locale locale) {
        boolean result = false;
        if (this.validatePhoto(upload, fileModel, locale)) {

            final PhotoBean photoBean = new PhotoBean();
            photoBean.setUserBean(albumBean.getUserBean());

            this.fileManager.readPhotoInfo(photoBean, upload);

            final AlbumPhotoBean albumPhotoBean = this.albumManager.savePhoto(albumBean, photoBean);

            this.fileManager.saveFile(photoBean, upload);
            this.fileManager.convertImage(photoBean);

            if (albumPhotoBean.getOrderNo() == 1) {
                this.fileManager.saveAlbumCover(albumBean, photoBean);
            }

            fileModel.put("id", photoBean.getId());
            fileModel.put("name", photoBean.getName());

            result = true;
        }
        return result;
    }

    private boolean validatePhoto(final MultipartFile upload, final Map<String, Object> fileModel, final Locale locale) {
        boolean isValid = true;
        if (!this.fileManager.validateContentType(upload.getContentType(),
                                                  FileManager.ACCEPT_IMAGE,
                                                  "error",
                                                  fileModel,
                                                  locale)) {
            isValid = false;
        }
        return isValid;
    }

    public boolean deletePhoto(final AlbumBean albumBean, final PhotoBean photoBean) {
        this.albumManager.deletePhoto(albumBean, photoBean);
        // this.fileManager.deleteFile(photoBean);

        final List<PhotoBean> photoList = this.albumManager.findPhotoByAlbum(albumBean);
        this.albumManager.sortPhoto(albumBean, photoList);

        return true;
    }

    public void sortPhoto(final AlbumBean albumBean, final List<PhotoBean> photoList) {
        this.albumManager.sortPhoto(albumBean, photoList);
    }

}
