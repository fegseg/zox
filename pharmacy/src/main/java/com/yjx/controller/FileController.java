package com.yjx.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yjx.common.lang.Result;
import com.yjx.common.lang.ResultInfo;
import com.yjx.dto.AdminInfoDto;
import com.yjx.entity.Admin;
import com.yjx.qiniu.QiNiuContent;
import com.yjx.qiniu.QiNiuService;
import com.yjx.service.AdminService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 */
@Slf4j
@RestController
@SuppressWarnings("all")
public class FileController {


    @Autowired
    QiNiuService qiNiuService;

    /**
     * 上传文件
     *
     * @param file 文件
     * @return Result
     */
    @PostMapping("file/upload")
    public Result uploadCarouselPicture(MultipartFile file) {
        long size = file.getSize();
        int mb = 1024;
        int maxSize = 1;
        if (size / mb / mb > maxSize) {
            return Result.error().codeAndMessage(ResultInfo.PICTURE_MAX_SIZE);
        } else {
            try {
                QiNiuContent qiNiuContent = qiNiuService.upload(file);
                String contentUrl = qiNiuContent.getUrl();
                log.info(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime()) + "上传文件" + contentUrl);
                Map<String, Object> map = new HashMap<>(5);
                map.put("fileUrl", contentUrl);
                return Result.success().codeAndMessage(ResultInfo.SUCCESS).data("data", map);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return Result.error().codeAndMessage(ResultInfo.PICTURE_UPLOAD_FAIL);
        }
    }

}

