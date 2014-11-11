package com.foobar.document;

import java.util.Date;
import java.util.List;

import org.apache.solr.client.solrj.beans.Field;
import org.springframework.data.annotation.Id;
import org.springframework.data.solr.core.mapping.SolrDocument;

@SolrDocument(solrCoreName = "foobar")
public class PhotoDocument {

    @Id
    @Field
    private Integer id = null;

    @Field
    private String title = null;

    @Field
    private String description = null;

    @Field
    private String make = null;

    @Field
    private String model = null;

    @Field
    private String lensModel = null;

    @Field
    private String focalLength = null;

    @Field
    private String shutterSpeed = null;

    @Field
    private String aperture = null;

    @Field
    private Integer iso = null;

    @Field
    private Date taken = null;

    @Field
    private List<String> tags = null;

    @Field
    private List<String> exif = null;

    @Field
    private Integer userId = null;

    @Field
    private String userName = null;

    public String getMake() {
        return this.make;
    }

    public void setMake(final String make) {
        this.make = make;
    }

    public String getModel() {
        return this.model;
    }

    public void setModel(final String model) {
        this.model = model;
    }

    public String getLensModel() {
        return this.lensModel;
    }

    public void setLensModel(final String lensModel) {
        this.lensModel = lensModel;
    }

    public String getFocalLength() {
        return this.focalLength;
    }

    public void setFocalLength(final String focalLength) {
        this.focalLength = focalLength;
    }

    public String getShutterSpeed() {
        return this.shutterSpeed;
    }

    public void setShutterSpeed(final String shutterSpeed) {
        this.shutterSpeed = shutterSpeed;
    }

    public String getAperture() {
        return this.aperture;
    }

    public void setAperture(final String aperture) {
        this.aperture = aperture;
    }

    public Date getTaken() {
        return this.taken;
    }

    public void setTaken(final Date taken) {
        this.taken = taken;
    }

    public Integer getIso() {
        return this.iso;
    }

    public void setIso(final Integer iso) {
        this.iso = iso;
    }

    public List<String> getTags() {
        return this.tags;
    }

    public void setTags(final List<String> tags) {
        this.tags = tags;
    }

    public List<String> getExif() {
        return this.exif;
    }

    public void setExif(final List<String> exif) {
        this.exif = exif;
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(final Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return this.userId;
    }

    public void setUserId(final Integer userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return this.userName;
    }

    public void setUserName(final String userName) {
        this.userName = userName;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

}
