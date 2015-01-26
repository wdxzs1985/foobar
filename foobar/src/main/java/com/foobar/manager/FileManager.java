package com.foobar.manager;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.im4java.core.ConvertCmd;
import org.im4java.core.IM4JavaException;
import org.im4java.core.IMOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.foobar.domain.FileBean;
import com.foobar.domain.UserBean;

@Component
public class FileManager {

    public static final int[] COVER_SIZE_ARRAY = new int[] { 60, 120, 180, 400 };
    public static final int[] IMAGE_SIZE_ARRAY = new int[] { 480, 1080 };

    public static final String USER = "u";
    public static final String ALBUM = "a";
    public static final String PHOTO = "p";

    public static final String COVER = "cover.jpg";

    public static final String JPG = "jpg";

    public static final String[] ACCEPT_IMAGE = { "image/jpeg", "image/png" };

    public static final String IMAGE = "image";

    private Log log = LogFactory.getLog(this.getClass());

    @Value("${file.inputPath}")
    private String inputPath = null;

    @Value("${file.outputPath}")
    private String outputPath = null;

    @Value("${file.convertPath}")
    private String convertPath = null;

    public String getInputPath() {
        return this.inputPath;
    }

    public String getOutputPath() {
        return this.outputPath;
    }

    public String getConvertPath() {
        return this.convertPath;
    }

    @Autowired
    private MessageSource messageSource = null;

    public File getFile(final String dirId, final String name) {
        final String rootPath = this.getInputPath();
        final String path = String.format("%s/%s/%s", rootPath, dirId, name);
        return new File(path);
    }

    public File getDir(final String dirId) {
        final String rootPath = this.getInputPath();
        final String path = String.format("%s/%s", rootPath, dirId);
        return new File(path);
    }

    public File getPhotoFile(final FileBean fileBean, final String name) {
        return this.getFile(PHOTO + fileBean.getId(), name);
    }

    public void saveUserCover(final UserBean dtoBean, final MultipartFile fileItem) {
        final String dirId = FileManager.USER + dtoBean.getId();
        final File inputFile = this.getFile(dirId, FileManager.COVER);
        if (fileItem != null && !fileItem.isEmpty()) {
            this.saveFile(fileItem, inputFile);
            this.convertCover(inputFile, dirId);
        } else {
            if (!inputFile.exists()) {
                for (final int size : FileManager.COVER_SIZE_ARRAY) {
                    this.copyDefaultCover(dirId, size);
                }
            }
        }
    }

    public void saveFile(final FileBean fileBean, final MultipartFile fileItem) {
        final String name = String.format("original.%s", fileBean.getExtension());
        final File file = this.getPhotoFile(fileBean, name);
        this.saveFile(fileItem, file);
    }

    public void deleteFile(final FileBean fileBean) {
        final String rootPath = this.getInputPath();
        final File dir = new File(rootPath + "/" + fileBean.getId());
        try {
            FileUtils.deleteDirectory(dir);
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected void saveFile(final MultipartFile fileItem, final File file) {
        InputStream input = null;
        OutputStream output = null;
        try {
            input = fileItem.getInputStream();
            output = FileUtils.openOutputStream(file);
            IOUtils.copy(input, output);
        } catch (final IOException e) {
            throw new RuntimeException(e);
        } finally {
            IOUtils.closeQuietly(input);
            IOUtils.closeQuietly(output);
        }
    }

    public void convertCover(final File inputFile, final String id) {
        for (final int size : FileManager.COVER_SIZE_ARRAY) {
            final String name = String.format("cover_%d.jpg", size);
            final File outputFile = this.getFile(id, name);
            this.convertCover(inputFile, outputFile, size);
        }
    }

    protected void convertCover(final File input, final File output, final int size) {
        final ConvertCmd cmd = new ConvertCmd();
        // cmd.setAsyncMode(true);
        cmd.setSearchPath(this.getConvertPath());
        // create the operation, add images and operators/options
        final IMOperation op = this.getImOperation();
        op.addImage(input.getAbsolutePath());
        op.resize(size, size, "^");
        op.gravity("center");
        op.extent(size, size);
        op.addImage(output.getAbsolutePath());
        // execute the operation
        if (this.log.isDebugEnabled()) {
            this.log.debug(this.getConvertPath());
            this.log.debug(op.toString());
        }
        try {
            cmd.run(op);
        } catch (final IOException e) {
            // throw new RuntimeException(e);
            this.log.error(e, e);
        } catch (final InterruptedException e) {
            // throw new RuntimeException(e);
            this.log.error(e, e);
        } catch (final IM4JavaException e) {
            // throw new RuntimeException(e);
            this.log.error(e, e);
        } finally {
            // FileUtils.deleteQuietly(input);
        }
    }

    public void convertImage(final FileBean fileBean) {
        final String name = String.format("original.%s", fileBean.getExtension());
        final File file = this.getPhotoFile(fileBean, name);

        for (final int size : FileManager.COVER_SIZE_ARRAY) {
            final String thumbnail = String.format("thumbnail_%d.jpg", size);
            final File thumbnailFile = this.getPhotoFile(fileBean, thumbnail);
            this.convertCover(file, thumbnailFile, size);
        }
        for (final int size : FileManager.IMAGE_SIZE_ARRAY) {
            final String thumbnail = String.format("thumbnail_%d.jpg", size);
            final File thumbnailFile = this.getPhotoFile(fileBean, thumbnail);
            this.convertImage(file, thumbnailFile, size);
        }
    }

    protected void convertImage(final File input, final File output, final int size) {
        final ConvertCmd cmd = new ConvertCmd();
        // cmd.setAsyncMode(true);
        cmd.setSearchPath(this.getConvertPath());
        // create the operation, add images and operators/options
        final IMOperation op = this.getImOperation();
        op.addImage(input.getAbsolutePath());
        op.resize(null, size, ">");
        op.addImage(output.getAbsolutePath());
        // execute the operation
        if (this.log.isDebugEnabled()) {
            this.log.debug(this.getConvertPath());
            this.log.debug(op.toString());
        }
        try {
            cmd.run(op);
        } catch (final IOException e) {
            // throw new RuntimeException(e);
            this.log.error(e, e);
        } catch (final InterruptedException e) {
            // throw new RuntimeException(e);
            this.log.error(e, e);
        } catch (final IM4JavaException e) {
            // throw new RuntimeException(e);
            this.log.error(e, e);
        } finally {
            // FileUtils.deleteQuietly(input);
        }
    }

    protected IMOperation getImOperation() {
        final IMOperation operation = new IMOperation();
        operation.density(72).quality(90d);
        return operation;
    }

    public void copyDefaultCover(final String dirId, final int size) {
        final String name = String.format("cover_%d.jpg", size);
        final File src = this.getFile("default", name);
        final File dest = this.getFile(dirId, name);

        try {
            FileUtils.copyFile(src, dest);
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean validateContentType(final String contentType,
                                       final String[] accepted,
                                       final String errorAttribute,
                                       final Map<String, Object> model,
                                       final Locale locale) {
        boolean isValid = true;
        if (!ArrayUtils.contains(accepted, contentType)) {
            final String message = this.messageSource.getMessage("error.fileNotAccept", null, locale);
            model.put(errorAttribute, message);
            isValid = false;
        }
        return isValid;
    }

}
