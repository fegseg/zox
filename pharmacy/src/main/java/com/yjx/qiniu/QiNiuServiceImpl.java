package com.yjx.qiniu;

import com.alibaba.fastjson.JSON;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import com.yjx.util.FileUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @className: QiNiuServiceImpl
 * @description:
 */
@Service
@Slf4j
public class QiNiuServiceImpl implements QiNiuService {

    @Autowired
    private QiNiuConfig qiNiuConfig;


    @Override
    public QiNiuContent upload(MultipartFile file) {

        // 构造一个带指定Zone对象的配置类
        Configuration cfg = new Configuration(QiNiuUtils.getRegion(qiNiuConfig.getZone()));
        UploadManager uploadManager = new UploadManager(cfg);
        Auth auth = Auth.create(qiNiuConfig.getAccessKey(), qiNiuConfig.getSecretKey());
        //生成上传文件Token
        String upToken = auth.uploadToken(qiNiuConfig.getBucket());
        QiNiuContent qiNiuContent = new QiNiuContent();
        try {
            SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
            String key = FileUtils.getFileNameNoExtension(file.getOriginalFilename()) + df.format(new Date()) + "." + FileUtils.getExtensionName(file.getOriginalFilename());
            Response response = uploadManager.put(file.getBytes(), key, upToken);
            //解析
            DefaultPutRet defaultPutRet = JSON.parseObject(response.bodyString(), DefaultPutRet.class);
            //将结果存入数据库
            qiNiuContent.setSuffix(FileUtils.getExtensionName(defaultPutRet.key));
            qiNiuContent.setBucket(qiNiuConfig.getBucket());
            qiNiuContent.setType(qiNiuConfig.getType());
            qiNiuContent.setName(FileUtils.getFileNameNoExtension(defaultPutRet.key));
            qiNiuContent.setUrl(qiNiuConfig.getHost() + "/" + defaultPutRet.key);
            qiNiuContent.setSize(FileUtils.getSizeString(Integer.parseInt(file.getSize() + "")));
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        return qiNiuContent;
    }


}
