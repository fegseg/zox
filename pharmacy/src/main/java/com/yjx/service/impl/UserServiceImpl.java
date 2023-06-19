package com.yjx.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.yjx.common.lang.Result;
import com.yjx.common.lang.ResultInfo;
import com.yjx.dto.RegisterDto;
import com.yjx.entity.User;
import com.yjx.mapper.UserMapper;
import com.yjx.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author yang123
 * @since 2021-10-17
 */
@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private UserMapper userMapper;

    @Override
    public Result updateUserPassword(RegisterDto updateDto) {
        String emailCode = (String) redisTemplate.opsForValue().get("emailCode" + updateDto.getEmail());
        // 发送了，判断redis缓存里的验证码有没有过期
        if (emailCode == null) {
            log.info("验证码已过期，请重新获取");
            return Result.error().message("验证码已过期，请重新获取");
        }
        // 没有过期，进行匹配
        else if (!updateDto.getEmailCode().toLowerCase().equals(emailCode.toLowerCase())) {
            // 匹配失败
            log.info("验证码错误");
            return Result.error().message("验证码错误");
        } else {
            // 验证码成功的情况下执行
            System.out.println("updateDto = " + updateDto);
            UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
            updateWrapper.set("password", DigestUtils.md5DigestAsHex(updateDto.getPassword().getBytes()))
                    .eq("username", updateDto.getUsername());
            userMapper.update(new User(),updateWrapper);
            // 修改成功将验证码删除，避免验证码可重复使用
            redisTemplate.delete("emailCode" + updateDto.getEmail());
            return Result.success().message("修改成功");
        }
    }

    @Override
    public Result getNewUserOnWeek() {

        Integer newUsers = userMapper.getNewUserOnWeek();
        return Result.success().codeAndMessage(ResultInfo.SUCCESS).data("responseBody", newUsers);
    }
}
