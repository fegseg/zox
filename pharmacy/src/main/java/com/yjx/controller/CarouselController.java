package com.yjx.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yjx.common.lang.Result;
import com.yjx.common.lang.ResultInfo;
import com.yjx.entity.Admin;
import com.yjx.entity.Carousel;
import com.yjx.qiniu.QiNiuContent;
import com.yjx.qiniu.QiNiuService;
import com.yjx.service.AdminService;
import com.yjx.service.CarouselService;
import com.yjx.util.MyPictureUploadUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
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
@RestController
@Slf4j
public class CarouselController {

    @Autowired
    CarouselService carouselService;

    @Autowired
    AdminService adminService;

    @Autowired
    QiNiuService qiNiuService;


    /**
     * 后台查询轮播图的信息
     *
     * @return Result
     */
    @GetMapping("/query/carousels")
    public Result getCarouselList() {

        Page<Carousel> carouselPage = new Page<>();
        Page<Carousel> pageData = carouselService.page(carouselPage);
        return Result.success().codeAndMessage(ResultInfo.SUCCESS).data("data", pageData);
    }

    /**
     * 首页得到允许展示的图片
     *
     * @return Result
     */
    @GetMapping("/query/enable/carousels")
    public Result getCarousel() {

        List<Carousel> list = carouselService.list();
        System.out.println("list = " + list);
        list.removeIf(carousel -> carousel.getStatus() == 0);
        return Result.success().codeAndMessage(ResultInfo.SUCCESS).data("data", list);
    }

    /**
     * 选择图片是否在主页显示
     *
     * @param id 图片编号
     * @return Result
     */
    @GetMapping("/picture/isShow/{id}")
    public Result changeStatus(@PathVariable(name = "id") Integer id) {

        Carousel one = carouselService.getById(id);
        Integer status = one.getStatus();

        UpdateWrapper<Carousel> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", id);
        if (status == 1) {
            updateWrapper.set("status", 0);
        } else {
            updateWrapper.set("status", 1);
        }
        carouselService.update(null, updateWrapper);
        return Result.success().codeAndMessage(ResultInfo.SUCCESS);
    }

    /**
     * 更换轮播图,保存到 img/carousel/ 下
     *
     * @param file 文件
     * @return Result
     */
    @PostMapping("/upload/carousel/picture/{id}")
    public Result uploadCarouselPicture(MultipartFile file, @PathVariable Integer id) {
        long size = file.getSize();
        int mb = 1024;
        if (size / mb / mb > 2) {
            return Result.error().codeAndMessage(ResultInfo.PICTURE_MAX_SIZE);
        } else {
            try {
                QiNiuContent qiNiuContent = qiNiuService.upload(file);
                String contentUrl = qiNiuContent.getUrl();
                UpdateWrapper<Carousel> updateWrapper = new UpdateWrapper<>();
                updateWrapper.eq("id", id);
                updateWrapper.set("path", contentUrl);
                boolean updateRes = carouselService.update(new Carousel(), updateWrapper);
                log.info(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime()) + "上传图片" + contentUrl);
                Map<String, Object> map = new HashMap<>(2);
                map.put("path", contentUrl);
                map.put("updateRes", updateRes);
                return Result.success().codeAndMessage(ResultInfo.SUCCESS).data("data", map);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return Result.error().codeAndMessage(ResultInfo.PICTURE_UPLOAD_FAIL);
        }
    }

    /**
     * 上传轮播图
     *
     * @param file 文件
     * @return Result
     */
    @PostMapping("/upload/carousel/picture")
    public Result uploadCarouselPicture2(MultipartFile file) {
        long size = file.getSize();
        int mb = 1024;
        if (size / mb / mb > 2) {
            return Result.error().codeAndMessage(ResultInfo.PICTURE_MAX_SIZE);
        } else {
            MyPictureUploadUtils myPictureUploadUtils = new MyPictureUploadUtils();
            // 返回一个生成的路径：carousel/年/月/日/文件名
            String path = myPictureUploadUtils.createDir(file, "carousel");

            // 获取当前项目的根路径（编译后的）： /E:/GraduationProject/pharmacy/target/classes/
            String rootPath = MedicineController.class.getResource("/").getPath();
            // 获取存储的有效路径
            String effectivePath = rootPath + "static/img/";
            // 保存图片，返回成功或者失败
            boolean res = myPictureUploadUtils.savePicture(file, effectivePath + path);
            if (res) {
                // 保存完毕，给前端返回图片的相对路径
                String fileNewPath = "img/" + path;

                log.info(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime()) + "上传图片" + fileNewPath);

                return Result.success().codeAndMessage(ResultInfo.SUCCESS).data("data", fileNewPath);
            } else {
                return Result.error().codeAndMessage(ResultInfo.PICTURE_UPLOAD_FAIL);
            }
        }
    }

}
