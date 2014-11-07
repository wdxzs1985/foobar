package com.foobar.service;

import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.foobar.domain.PhotoBean;
import com.foobar.domain.UserBean;
import com.foobar.manager.FileManager;
import com.foobar.manager.PhotoManager;
import com.foobar.manager.TagManager;

@Service
public class ConsolePhotoService {

    @Autowired
    private PhotoManager photoManager = null;
    @Autowired
    private TagManager tagManager = null;
    @Autowired
    private FileManager fileManager = null;
    @Autowired
    private MessageSource messageSource = null;

    public boolean addPhoto(final UserBean loginUser,
                            final MultipartFile upload,
                            final Map<String, Object> fileModel,
                            final Locale locale) {
        boolean result = false;
        if (this.validatePhoto(upload, fileModel, locale)) {

            final PhotoBean photoBean = new PhotoBean();
            photoBean.setUserBean(loginUser);

            this.fileManager.readPhotoInfo(photoBean, upload);

            this.photoManager.savePhoto(photoBean);
            this.presetTag(photoBean);

            this.fileManager.saveFile(photoBean, upload);
            this.fileManager.convertImage(photoBean);

            fileModel.put("id", photoBean.getId());
            fileModel.put("name", photoBean.getName());

            result = true;
        }
        return result;
    }

    private void presetTag(final PhotoBean photoBean) {
        if(StringUtils.isNotBlank(photoBean.getAperture())) {
            
        }
        this.tagManager.create("");
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

}
