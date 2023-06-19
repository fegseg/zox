package com.yjx.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yjx.common.lang.Result;
import com.yjx.common.lang.ResultInfo;
import com.yjx.dto.BuyDrugDto;
import com.yjx.dto.ShoppingCartDto;
import com.yjx.entity.Medicine;
import com.yjx.entity.MedicineSort;
import com.yjx.entity.ShoppingCart;
import com.yjx.service.MedicineService;
import com.yjx.service.MedicineSortSecondService;
import com.yjx.service.MedicineSortService;
import com.yjx.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;


@Slf4j
@SuppressWarnings("all")
@RestController
public class MedicineController {

    @Autowired
    MedicineService medicineService;

    @Autowired
    MedicineSortService medicineSortService;

    @Autowired
    private MedicineSortSecondService medicineSortSecondService;

    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    ShoppingCartService shoppingCartService;

    // ----------------------------------------   后 台  ---------------------------------------------------

    /**
     * 查询药品
     *
     * @param currentPage 当前所在的页数
     * @param pageSize    每页的数量
     * @return 数据
     */
    @GetMapping(value = {
            "/search/with/{currentPage}/{pageSize}",
            "/search/with/{currentPage}/{pageSize}/{nameOrId}"})
    public Result searchMedicine(@PathVariable(name = "currentPage") Integer currentPage,
                                 @PathVariable(name = "pageSize") Integer pageSize,
                                 @PathVariable(name = "nameOrId", required = false) String nameOrIdOrSort) {

        IPage<Medicine> pageData = medicineService.searchMedicine(currentPage, pageSize, nameOrIdOrSort);
        return Result.success().codeAndMessage(ResultInfo.SUCCESS).data("data", pageData);
    }

    /**
     * 查询药品
     *
     * @param currentPage 当前所在的页数
     * @param pageSize    每页的数量
     * @return 数据
     */
    @GetMapping(value = {
            "/search2/with/{currentPage}/{pageSize}",
            "/search2/with/{currentPage}/{pageSize}/{nameOrId}"})
    public Result searchMedicine2(@PathVariable(name = "currentPage") Integer currentPage,
                                 @PathVariable(name = "pageSize") Integer pageSize,
                                 @PathVariable(name = "nameOrId", required = false) String nameOrIdOrSort) {

        IPage<Medicine> pageData = medicineService.searchMedicine2(currentPage, pageSize, nameOrIdOrSort);
        return Result.success().codeAndMessage(ResultInfo.SUCCESS).data("data", pageData);
    }


    /**
     * 获取打折扣的药品
     *
     * @return discountList
     */
    @PostMapping("/medicine/discount/all")
    public Result getDiscountDrugs() {

        List<Medicine> discountList = medicineService.getDiscountDrugs();
        return Result.success().codeAndMessage(ResultInfo.SUCCESS).data("responseBody", discountList);
    }

    /**
     * 添加药品
     *
     * @param medicine 药品基本信息
     * @return 操作结果
     */
    @PostMapping("/medicine/add")
    public Result addMedicine(@RequestBody Medicine medicine) {

        boolean res = medicineService.save(medicine);
        System.out.println("保存结果 ===> " + res);
        return Result.success().codeAndMessage(ResultInfo.SUCCESS).data("responseBody", res);
    }

    /**
     * 选择药品是否显示
     *
     * @param id 药品编号
     * @return Result
     */
    @RequiresAuthentication
    @GetMapping("/medicine/isShow/{id}")
    public Result changeStatus(@PathVariable(name = "id") Long id) {
        Medicine one = medicineService.getById(id);
        Integer status = one.getStatus();

        UpdateWrapper<Medicine> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", id);
        if (status == 1) {
            updateWrapper.set("status", 0);
        } else {
            updateWrapper.set("status", 1);
        }
        medicineService.update(new Medicine(), updateWrapper);
        return Result.success().codeAndMessage(ResultInfo.SUCCESS);
    }

    /**
     * 单个删除药品-----逻辑删除
     *
     * @param id 药品编号
     * @return Result
     */
    @RequiresAuthentication
    @GetMapping("/medicine/delete/{id}")
    public Result deleteMedicine(@PathVariable(name = "id") Long id) {

        // 根据要删除的id查询出药品的分类编号
        Medicine medicine = medicineService.getById(id);
        String sort = medicine.getSort();

        // 删除
        medicineService.removeById(id);
        return Result.success().codeAndMessage(ResultInfo.SUCCESS);
    }

    /**
     * 批量删除药品-----逻辑删除
     *
     * @return Result
     */
    @RequiresAuthentication
    @PostMapping("/medicine/batch/delete")
    public Result batchDeleteMedicine(Long[] drugIds) {

        // 将前端传过来的数组转换成集合
        List<Long> list = Arrays.asList(drugIds);
        // 通过药品id的集合进行批量删除
        medicineService.removeByIds(list);

        return Result.success().codeAndMessage(ResultInfo.SUCCESS);
    }

    /**
     * 通过药品的id获取对应药品信息
     *
     * @param id 药品编号
     * @return 对应药品信息
     */
    @GetMapping("/medicine/get/info/{id}")
    public Result getMedicineInfoById(@PathVariable(name = "id") Long id) {
        // 先通过药品的id获取对应药品信息，然后拿到里面的分类sortId
        Medicine medicineInfo = medicineService.getById(id);
//        System.out.println("medicineInfo = " + medicineInfo);
//        String sortId = medicineInfo.getSort();
//        String secondSortId = medicineInfo.getSecondSort();
        // 再通过sortId拿到对应的分类用途
//        MedicineSort medicineSort = medicineSortService.getById(sortId);
//        MedicineSortSecond medicineSortSecond = medicineSortSecondService.getById(secondSortId);
//        String category = medicineSort.getCategory();
//        String secondCategory = medicineSortSecond.getSecondSort();
//
//        medicineInfo.setSort(category);
//        medicineInfo.setSecondSort(secondCategory);

        return Result.success().codeAndMessage(ResultInfo.SUCCESS).data("data", medicineInfo);
    }

    /**
     * 推荐药品信息展示，根据药品名称获取分类id，再根据分类id获取同一类的药品
     *
     * @param name 药品名称
     * @return 对应药品信息
     */
    @GetMapping("/medicine/recommend/{name}")
    public Result getSameRecommend(@PathVariable(name = "name") String name) {
        // 先通过药品的名称获取对应药品信息，然后拿到里面的分类sortId
        Medicine medicineInfo = medicineService.getOne(new QueryWrapper<Medicine>().eq("name", name));
        String sortId = medicineInfo.getSort();
        // 再通过sortId拿到对应的分类用途
        Page<Medicine> page = new Page<>(1, 8);
        Page<Medicine> pageData = medicineService.page(page, new QueryWrapper<Medicine>().eq("sort", sortId));

        return Result.success().codeAndMessage(ResultInfo.SUCCESS).data("data", pageData);
    }



    /**
     * 通过药品的名称获取对应药品信息
     *
     * @param name 药品名称
     * @return 对应药品信息
     */
    @GetMapping("/medicine/info/name/{name}")
    public Result getMedicineInfoByName(@PathVariable(name = "name") String name) {
        // 先通过药品的名称获取对应药品信息，然后拿到里面的分类sortId
        Medicine medicineInfo = medicineService.getOne(new QueryWrapper<Medicine>().eq("name", name));
        String sortId = medicineInfo.getSort();
        // 再通过sortId拿到对应的分类用途
        MedicineSort medicineSort = medicineSortService.getById(sortId);
        String category = medicineSort.getCategory();

        medicineInfo.setSort(category);


        return Result.success().codeAndMessage(ResultInfo.SUCCESS).data("data", medicineInfo);
    }

    /**
     * 健康小贴士文章详情页推荐药品
     *
     * @param sort
     * @return
     */
    @GetMapping("/health/tips/drugs/{sort}")
    public Result getMedicineBySort(@PathVariable String sort) {

        // 根据分类名称拿到分类的全部信息
        MedicineSort category = medicineSortService.getOne(new QueryWrapper<MedicineSort>().eq("category", sort));
        // 根据分类的id拿到该分类的药品,推荐几个就可以了
        Page<Medicine> page = new Page<>(1, 8);
        Page<Medicine> list = medicineService.page(page, new QueryWrapper<Medicine>().eq("sort", category.getSortId()));
        return Result.success().codeAndMessage(ResultInfo.SUCCESS).data("data", list);
    }

    /**
     * 获取最新的药，首页展示
     *
     * @return Result
     */
    @GetMapping("/home/new/drugs")
    public Result getMedicineByTime() {

        Page<Medicine> page = new Page<>(1, 8);
        Page<Medicine> list = medicineService.page(page, new QueryWrapper<Medicine>().orderByDesc("gmt_create"));
        return Result.success().codeAndMessage(ResultInfo.SUCCESS).data("data", list);
    }


    /**
     * 更新药品
     *
     * @param medicine 药品信息
     * @param id       编号
     * @return Result
     */
    @PostMapping("/update/medicine/info/{id}")
    public Result updateMedicineInfo(@RequestBody Medicine medicine, @PathVariable(name = "id") Long id) {

        // 更新数据
        UpdateWrapper<Medicine> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", id);
        updateWrapper.set("name", medicine.getName())
                .set("picture", medicine.getPicture())
                .set("upload_user", medicine.getUploadUser())
                .set("sort", medicine.getSort())
                .set("second_sort", medicine.getSecondSort())
                .set("price", medicine.getPrice())
                .set("stock", medicine.getStock())
                .set("description", medicine.getDescription());

        boolean res = medicineService.update(new Medicine(), updateWrapper);
        System.out.println("更新结果 ===> " + res);
        return Result.success().codeAndMessage(ResultInfo.SUCCESS).data("responseBody", res);
    }

    /**
     * 获取药品的总数量
     *
     * @return Result
     */
    @PostMapping(value = "/admin/drug/nums")
    public Result getDrugNums() {

        Integer drugNums = medicineService.getDrugNums();
        return Result.success().codeAndMessage("200", "操作成功！").data("responseBody", drugNums);
    }


    // ----------------------------------------   前 台  ---------------------------------------------------

    /**
     * 在药品详情页面点击购买，把药名，用户账号，数量存到redis里
     *
     * @param username 用户账号
     * @param drugs    drugs = [{drugName=布洛芬缓释胶囊, amount=1}]
     * @return Result
     */
    @PostMapping("/buy/drugs/{username}")
    public Result buySingleDrug(@PathVariable String username, @RequestBody Object[] drugs) {

        Assert.notNull(username, "用户账号不能为空！");
        if (username != null) {
            System.out.println("drugs = " + Arrays.asList(drugs));
            redisTemplate.opsForValue().set(username + "wantBuy", Arrays.asList(drugs), 1, TimeUnit.DAYS);
            return Result.success().codeAndMessage(ResultInfo.SUCCESS);
        } else {
            return Result.error();
        }
    }

    @GetMapping("/what/buy/{username}")
    public Result getBuySingleDrug(@PathVariable String username) {

        System.out.println("username = " + username);
        Object info = redisTemplate.opsForValue().get(username + "wantBuy");
        System.out.println("info = " + info);
        return Result.success().codeAndMessage(ResultInfo.SUCCESS).data("data", info);
    }

    @PostMapping("/buy/these/{username}")
    public Result getBuySingleDrug(@PathVariable String username, @RequestBody BuyDrugDto[] drugArr) {

        Assert.notNull(username, "用户账号不能为空！");
        if (username != null) {
            List<BuyDrugDto> list = Arrays.asList(drugArr);
            System.out.println("list = " + list);
            List<ShoppingCartDto> data = new ArrayList<>();
            for (BuyDrugDto item : list) {
                Medicine drug = medicineService.getOne(new QueryWrapper<Medicine>().eq("name", item.getDrugName()));
                QueryWrapper<ShoppingCart> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("drug_name", item.getDrugName());
                ShoppingCart shoppingCart = shoppingCartService.getOne(queryWrapper);
                ShoppingCartDto drugInfo = new ShoppingCartDto();
                drugInfo.setPicture(drug.getPicture())
                        .setDrugName(item.getDrugName())
                        .setStandard(drug.getStandard())
                        .setPrice(drug.getPrice())
                        .setAmount(item.getAmount())
                        .setKey(shoppingCart.getId().toString());
                data.add(drugInfo);
            }
            redisTemplate.opsForValue().set(username + "orderInfo", data, 1, TimeUnit.DAYS);
            return Result.success().codeAndMessage(ResultInfo.SUCCESS).data("data", data);
        } else {
            return Result.error();
        }
    }

    @GetMapping("/get/these/{username}")
    public Result getBuyInfo(@PathVariable String username) {

        Object data = redisTemplate.opsForValue().get(username + "orderInfo");
        return Result.success().codeAndMessage(ResultInfo.SUCCESS).data("data", data);
    }

    /**
     * 前台搜索获取药品
     *
     * @param drugName 药品名称
     * @param current  当前页
     * @return 结果
     */
    @PostMapping("/search/drug/name")
    public Result searchDrugByName(String drugName, Integer current) {

        Page<Medicine> page = new Page<>(current, 10);
        Page<Medicine> list = medicineService.page(page, new QueryWrapper<Medicine>().likeRight("name", drugName));
        return Result.success().data("responseBody", list);
    }

}
