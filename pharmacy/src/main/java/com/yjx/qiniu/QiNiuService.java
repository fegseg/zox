package com.yjx.qiniu;

import org.springframework.web.multipart.MultipartFile;

/**
 * @className: QiNiuService
 * @description:
 */
public interface QiNiuService {

    /**
     * 上传文件到七牛云
     *
     * @param file 要上传的文件
     * @return 七牛云信息
     */
    QiNiuContent upload(MultipartFile file);

}
