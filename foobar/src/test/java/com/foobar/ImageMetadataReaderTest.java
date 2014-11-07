package com.foobar;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.MetadataException;
import com.drew.metadata.Tag;
import com.drew.metadata.exif.ExifIFD0Directory;
import com.drew.metadata.exif.ExifSubIFDDirectory;

public class ImageMetadataReaderTest {

    private Log log = LogFactory.getLog(this.getClass());

    @Test
    public void testReadMetadataFile() throws ImageProcessingException,
                                      IOException,
                                      MetadataException {
        final File input = new File("C:/Users/wangjue/Pictures", "7.jpg");
        final Metadata metadata = ImageMetadataReader.readMetadata(input);

        for (final Directory directory : metadata.getDirectories()) {
            for (final Tag tag : directory.getTags()) {
                this.log.info(tag);
            }
        }

        if (metadata.containsDirectory(ExifIFD0Directory.class)) {
            final ExifIFD0Directory exifIFD0 = metadata.getDirectory(ExifIFD0Directory.class);
            final String model = exifIFD0.getDescription(ExifIFD0Directory.TAG_MODEL);
            this.log.info(model);
        }
        if (metadata.containsDirectory(ExifSubIFDDirectory.class)) {
            final ExifSubIFDDirectory exifSubIFD = metadata.getDirectory(ExifSubIFDDirectory.class);

            final String lensModel = exifSubIFD.getDescription(ExifSubIFDDirectory.TAG_LENS_MODEL);
            this.log.info(lensModel);

            final String focalLength = exifSubIFD.getDescription(ExifSubIFDDirectory.TAG_FOCAL_LENGTH);
            this.log.info(focalLength);

            final String shutterSpeed = exifSubIFD.getDescription(ExifSubIFDDirectory.TAG_SHUTTER_SPEED);
            this.log.info(shutterSpeed);

            final String aperture = exifSubIFD.getDescription(ExifSubIFDDirectory.TAG_APERTURE);
            this.log.info(aperture);

            final String iso = exifSubIFD.getDescription(ExifSubIFDDirectory.TAG_ISO_EQUIVALENT);
            this.log.info(iso);

            final String taken = exifSubIFD.getDescription(ExifSubIFDDirectory.TAG_DATETIME_ORIGINAL);
            this.log.info(taken);

            final Date taken_date = exifSubIFD.getDate(ExifSubIFDDirectory.TAG_DATETIME_ORIGINAL);
            this.log.info(taken_date);
        }
    }
}
