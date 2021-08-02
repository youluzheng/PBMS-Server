package org.pbms.pbmsserver.common.request.image;

import org.pbms.pbmsserver.common.exception.BusinessStatus;

import javax.validation.constraints.Size;

public class ImageUploadReq {
    private Boolean compress;

    @Size(min = 1, message = "文件名称不能为空")
    private String fileName;

    public Boolean getCompress() {
        return compress;
    }

    public void setCompress(Boolean compress) {
        this.compress = compress;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public String toString() {
        return "ImageUploadReq{" +
                ", compress=" + compress +
                ", fileName='" + fileName + '\'' +
                '}';
    }
}
