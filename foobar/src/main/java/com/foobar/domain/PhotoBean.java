package com.foobar.domain;

public class PhotoBean extends FileBean {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private Integer year = null;

    private Integer month = null;

    private Integer day = null;

    public Integer getYear() {
        return this.year;
    }

    public void setYear(final Integer year) {
        this.year = year;
    }

    public Integer getMonth() {
        return this.month;
    }

    public void setMonth(final Integer month) {
        this.month = month;
    }

    public Integer getDay() {
        return this.day;
    }

    public void setDay(final Integer day) {
        this.day = day;
    }

}
