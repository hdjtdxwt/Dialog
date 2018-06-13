package com.epsit.downloadmanager;


import java.util.List;

/**
 * 地图下载接口响应
 */

public class MapLoadResponse {
    private String code;
    private String message;
    private String robotId;
    private String pageNum;
    private String pageSize;
    private String total;
    private String pages;

    private List<MapImgData> data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getRobotId() {
        return robotId;
    }

    public void setRobotId(String robotId) {
        this.robotId = robotId;
    }

    public String getPageNum() {
        return pageNum;
    }

    public void setPageNum(String pageNum) {
        this.pageNum = pageNum;
    }

    public String getPageSize() {
        return pageSize;
    }

    public void setPageSize(String pageSize) {
        this.pageSize = pageSize;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getPages() {
        return pages;
    }

    public void setPages(String pages) {
        this.pages = pages;
    }

    public List<MapImgData> getData() {
        return data;
    }

    public void setData(List<MapImgData> data) {
        this.data = data;
    }

}

