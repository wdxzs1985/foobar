package com.foobar.domain;

import java.util.Date;

public class PhotoBean extends FileBean {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private String make = null;

    private String model = null;

    private String lensModel = null;

    private String focalLength = null;

    private String shutterSpeed = null;

    private String aperture = null;

    private Integer isoEquivalent = null;

    private Date taken = null;

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

    public Integer getIsoEquivalent() {
        return this.isoEquivalent;
    }

    public void setIsoEquivalent(final Integer isoEquivalent) {
        this.isoEquivalent = isoEquivalent;
    }

    public Date getTaken() {
        return this.taken;
    }

    public void setTaken(final Date taken) {
        this.taken = taken;
    }

}
