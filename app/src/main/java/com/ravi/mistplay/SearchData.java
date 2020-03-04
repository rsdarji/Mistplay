package com.ravi.mistplay;

public class SearchData {

    private String genre;
    private String imgURL;
    private String subgenre;
    private String title;
    private String pid;
    private String rating;
    private String rCount;

    public SearchData(String genre, String imgURL, String subgenre, String title, String pid, String rating, String rCount) {
        this.genre = genre;
        this.imgURL = imgURL;
        this.subgenre = subgenre;
        this.title = title;
        this.pid = pid;
        this.rating = rating;
        this.rCount = rCount;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getImgURL() {
        return imgURL;
    }

    public void setImgURL(String imgURL) {
        this.imgURL = imgURL;
    }

    public String getSubgenre() {
        return subgenre;
    }

    public void setSubgenre(String subgenre) {
        this.subgenre = subgenre;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getrCount() {
        return rCount;
    }

    public void setrCount(String rCount) {
        this.rCount = rCount;
    }
}
