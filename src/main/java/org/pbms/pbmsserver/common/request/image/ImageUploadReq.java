package org.pbms.pbmsserver.common.request.image;

import javax.validation.constraints.Size;

public class ImageUploadReq {
    @Size(min = 1, message = ImageErrorMessage.IMAGE_NAME_EMPTY)
    private String fileName;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public String toString() {
        return "ImageUploadReq{" +
                "fileName='" + fileName + '\'' +
                '}';
    }
}
