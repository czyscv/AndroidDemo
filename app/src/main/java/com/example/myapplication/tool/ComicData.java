package com.example.myapplication.tool;

public class ComicData {
    //自动递增
    private Integer id ;
    //漫画名
    private String name ;
    //作者
    private String author ;
    //相对路径
    private String path ;
    //上线时间
    private String time ;
    //限制等级
    private Integer limitLevel ;
    //是否显示
    private Integer toShow ;
    //页数
    private Integer pageNum ;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Integer getLimitLevel() {
        return limitLevel;
    }

    public void setLimitLevel(Integer limitLevel) {
        this.limitLevel = limitLevel;
    }

    public Integer getToShow() {
        return toShow;
    }

    public void setToShow(Integer toShow) {
        this.toShow = toShow;
    }

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }
}
