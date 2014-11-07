package com.foobar.manager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.foobar.domain.PhotoBean;
import com.foobar.mapper.FileMapper;
import com.foobar.mapper.PhotoMapper;

@Component
public class PhotoManager {

    @Autowired
    private FileMapper fileMapper = null;
    @Autowired
    private PhotoMapper photoMapper = null;

    public void savePhoto(final PhotoBean photoBean) {
        this.fileMapper.insert(photoBean);
        this.photoMapper.insert(photoBean);
    }

}
