package org.pbms.pbmsserver.common.request.image;

import lombok.Data;

import javax.validation.constraints.Size;

@Data
public class ImageUploadReq {
    @Size(min = 1, message = ImageErrorMessage.IMAGE_NAME_EMPTY)
    private String fileName;
}
