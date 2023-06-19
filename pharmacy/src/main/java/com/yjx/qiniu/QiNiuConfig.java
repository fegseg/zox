package com.yjx.qiniu;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @className: QiNiuConfig
 * @description: 七牛云的配置
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "qiniu")
public class QiNiuConfig implements Serializable {

    /**
     * 一个账号最多拥有两对密钥(Access/Secret Key)
     */
    @NotBlank(message = "Access Key 不能为空")
    private String accessKey;
    /**
     * 一个账号最多拥有两对密钥(Access/Secret Key)
     */
    @NotBlank(message = "Secret Key 不能为空")
    private String secretKey;
    /**
     * 存储空间名称作为唯一的 Bucket 识别符
     */
    @NotBlank(message = "Bucket 不能为空")
    private String bucket;
    /**
     * Zone表示与机房的对应关系
     * 华东	Zone.zone0()
     * 华北	Zone.zone1()
     * 华南	Zone.zone2()
     * 北美	Zone.zoneNa0()
     * 东南亚	Zone.zoneAs0()
     */
    @NotBlank(message = "Zone 不能为空")
    private String zone;
    /**
     * 外链域名，可自定义，需在七牛云绑定
     */
    @NotBlank(message = "外链域名不能为空")
    private String host;
    /**
     * 空间类型：公开/私有
     */
    private String type = "公开";

    public boolean check() {
        return StringUtils.isNotEmpty(accessKey)
                && StringUtils.isNotEmpty(secretKey)
                && StringUtils.isNotEmpty(bucket)
                && StringUtils.isNotEmpty(zone)
                && StringUtils.isNotEmpty(host)
                && StringUtils.isNotEmpty(type);
    }
}
