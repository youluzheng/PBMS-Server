package org.pbms.pbmsserver.common.request.image;

import lombok.Data;

import javax.validation.constraints.Size;

@Data
public class ImageUploadDTO {
    @Size(min = 1)
    private String fileName;
}
