package com.foobar.service;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;
import com.drew.metadata.exif.ExifIFD0Directory;
import com.drew.metadata.exif.ExifSubIFDDirectory;
import com.foobar.document.PhotoDocument;
import com.foobar.domain.FileBean;
import com.foobar.domain.PhotoBean;
import com.foobar.domain.UserBean;
import com.foobar.manager.FileManager;
import com.foobar.manager.PhotoManager;
import com.foobar.manager.TagManager;
import com.foobar.repository.PhotoRepository;

@Service
public class ConsolePhotoService {

    private Log log = LogFactory.getLog(this.getClass());

    @Autowired
    private PhotoManager photoManager = null;
    @Autowired
    private PhotoRepository photoRepository = null;
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
            this.readFileInfo(photoBean, upload);

            final PhotoDocument photoDocument = new PhotoDocument();
            photoDocument.setTitle(photoBean.getName());
            photoDocument.setUserId(loginUser.getId());
            photoDocument.setUserName(loginUser.getNickname());
            this.readPhotoInfo(photoDocument, upload);

            final Calendar calendar = Calendar.getInstance(locale);
            if (photoDocument.getTaken() != null) {
                calendar.setTime(photoDocument.getTaken());
            }
            photoBean.setYear(calendar.get(Calendar.YEAR));
            photoBean.setMonth(calendar.get(Calendar.MONTH) + 1);
            photoBean.setDay(calendar.get(Calendar.DATE));

            this.photoManager.savePhoto(photoBean);

            photoDocument.setId(photoBean.getId());

            this.photoRepository.save(photoDocument);

            this.fileManager.saveFile(photoBean, upload);
            this.fileManager.convertImage(photoBean);

            fileModel.put("id", photoBean.getId());
            fileModel.put("name", photoBean.getName());

            result = true;
        }
        return result;
    }

    private void readFileInfo(final FileBean fileBean, final MultipartFile upload) {
        final String filename = upload.getOriginalFilename();
        final String name = FilenameUtils.getBaseName(filename);
        final String extension = FilenameUtils.getExtension(filename);
        fileBean.setName(name);
        fileBean.setExtension(StringUtils.lowerCase(extension));
    }

    private void readPhotoInfo(final PhotoDocument photoDocument, final MultipartFile upload) {
        BufferedInputStream input = null;
        try {
            input = new BufferedInputStream(upload.getInputStream());
            final Metadata metadata = ImageMetadataReader.readMetadata(input, true);

            photoDocument.setTags(new ArrayList<String>());
            photoDocument.setExif(new ArrayList<String>());

            if (metadata.containsDirectory(ExifIFD0Directory.class)) {
                final ExifIFD0Directory exifIFD0 = metadata.getDirectory(ExifIFD0Directory.class);
                if (exifIFD0.containsTag(ExifIFD0Directory.TAG_MAKE)) {
                    final String make = exifIFD0.getDescription(ExifIFD0Directory.TAG_MAKE);
                    photoDocument.setMake(make);
                }
                if (exifIFD0.containsTag(ExifIFD0Directory.TAG_MODEL)) {
                    final String model = exifIFD0.getDescription(ExifIFD0Directory.TAG_MODEL);
                    photoDocument.setModel(model);
                }

                for (final Tag tag : exifIFD0.getTags()) {
                    photoDocument.getExif().add(tag.getDescription());
                }
            }

            if (metadata.containsDirectory(ExifSubIFDDirectory.class)) {
                final ExifSubIFDDirectory exifSubIFD = metadata.getDirectory(ExifSubIFDDirectory.class);

                if (exifSubIFD.containsTag(ExifSubIFDDirectory.TAG_LENS_MODEL)) {
                    final String lensModel = exifSubIFD.getDescription(ExifSubIFDDirectory.TAG_LENS_MODEL);
                    photoDocument.setLensModel(lensModel);
                }

                if (exifSubIFD.containsTag(ExifSubIFDDirectory.TAG_FOCAL_LENGTH)) {
                    final String focalLength = exifSubIFD.getDescription(ExifSubIFDDirectory.TAG_FOCAL_LENGTH);
                    photoDocument.setFocalLength(focalLength);

                    //

                }

                if (exifSubIFD.containsTag(ExifSubIFDDirectory.TAG_SHUTTER_SPEED)) {
                    final String shutterSpeed = exifSubIFD.getDescription(ExifSubIFDDirectory.TAG_SHUTTER_SPEED);
                    photoDocument.setShutterSpeed(shutterSpeed);

                    //

                }

                if (exifSubIFD.containsTag(ExifSubIFDDirectory.TAG_APERTURE)) {
                    final String aperture = exifSubIFD.getDescription(ExifSubIFDDirectory.TAG_APERTURE);
                    photoDocument.setAperture(aperture);

                    //

                }

                if (exifSubIFD.containsTag(ExifSubIFDDirectory.TAG_ISO_EQUIVALENT)) {
                    final Integer isoEquivalent = exifSubIFD.getInteger(ExifSubIFDDirectory.TAG_ISO_EQUIVALENT);
                    photoDocument.setIso(isoEquivalent);

                    //

                }

                if (exifSubIFD.containsTag(ExifSubIFDDirectory.TAG_DATETIME_ORIGINAL)) {
                    final Date taken = exifSubIFD.getDate(ExifSubIFDDirectory.TAG_DATETIME_ORIGINAL);
                    photoDocument.setTaken(taken);
                }

                for (final Tag tag : exifSubIFD.getTags()) {
                    photoDocument.getExif().add(tag.getDescription());
                }

            }
        } catch (final IOException e) {
            this.log.error(e.getMessage(), e);
        } catch (final ImageProcessingException e) {
            this.log.error(e.getMessage(), e);
        } finally {
            IOUtils.closeQuietly(input);
        }
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

    public Page<PhotoDocument> findUserPhotos(final UserBean loginUser, final Pageable pageable) {
        final Integer userId = loginUser.getId();
        return this.photoRepository.findByUserId(userId, pageable);
    }

    public List<Map<String, Object>> findUserPhotoGroup(final UserBean loginUser) {
        final Map<String, Object> param = new HashMap<String, Object>();
        param.put("userBean", loginUser);
        final List<Map<String, Object>> groups = this.photoManager.findUserPhotoGroup(param);
        for (final Map<String, Object> group : groups) {
            final List<PhotoBean> content = this.photoManager.findUserPhotoByGroup(group);
            group.put("content", content);
        }
        return groups;
    }

    public List<Map<String, Object>> findUserPhotoGroup(final UserBean loginUser, final int year) {
        final Map<String, Object> param = new HashMap<String, Object>();
        param.put("userBean", loginUser);
        param.put("year", year);
        final List<Map<String, Object>> groups = this.photoManager.findUserPhotoGroup(param);
        for (final Map<String, Object> group : groups) {
            final List<PhotoBean> content = this.photoManager.findUserPhotoByGroup(group);
            group.put("content", content);
        }
        return groups;
    }

    public List<Map<String, Object>> findUserPhotoGroup(final UserBean loginUser, final int year, final int month) {
        final Map<String, Object> param = new HashMap<String, Object>();
        param.put("userBean", loginUser);
        param.put("year", year);
        param.put("month", month);
        final List<Map<String, Object>> groups = this.photoManager.findUserPhotoGroup(param);
        for (final Map<String, Object> group : groups) {
            final List<PhotoBean> content = this.photoManager.findUserPhotoByGroup(group);
            group.put("content", content);
        }
        return groups;
    }

    public List<Map<String, Object>> findUserPhotoGroup(final UserBean loginUser,
                                                        final int year,
                                                        final int month,
                                                        final int day) {
        final Map<String, Object> param = new HashMap<String, Object>();
        param.put("userBean", loginUser);
        param.put("year", year);
        param.put("month", month);
        param.put("day", day);
        final List<Map<String, Object>> groups = this.photoManager.findUserPhotoGroup(param);
        for (final Map<String, Object> group : groups) {
            final List<PhotoBean> content = this.photoManager.findUserPhotoByGroup(group);
            group.put("content", content);
        }
        return groups;
    }

    public PhotoDocument findUserPhoto(final UserBean loginUser, final Integer fileId) {
        final PhotoDocument document = this.photoRepository.findOne(fileId);
        if (document.getUserId().equals(loginUser.getId())) {
            return document;
        }
        return null;
    }

}
