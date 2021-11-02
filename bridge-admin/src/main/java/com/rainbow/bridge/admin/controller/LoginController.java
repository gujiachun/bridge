package com.rainbow.bridge.admin.controller;

import com.rainbow.bridge.admin.model.LoginVo;
import com.rainbow.bridge.admin.model.TokenVo;
import com.rainbow.bridge.admin.model.UserVo;
import com.rainbow.bridge.core.Result;
import com.rainbow.bridge.core.ResultEnum;
import org.springframework.web.bind.annotation.*;

/**
 * @author gujiachun
 */
@RestController
@RequestMapping("/api/v1")
public class LoginController {

    @PostMapping("/user/login")
    public Result<TokenVo> login(@RequestBody LoginVo loginVo){

        if ("admin".equals(loginVo.getUsername())
                && "111111".equals(loginVo.getPassword())){
            TokenVo tokenVo = new TokenVo();
            tokenVo.setToken("admin-token");

            return Result.success(tokenVo);
        }
        return Result.fail(ResultEnum.USER_ERROR_PASSWORD);
    }

    @GetMapping("/user/info")
    public Result<UserVo> getInfo(@RequestParam("token") String token){

        UserVo userVo = new UserVo();
        userVo.setAvatar("https://p.ampmake.com/fed/image/jpeg/4b6a71afafd61cac30f535d56c89a258.jpeg");
        userVo.setName("rainbow");

        return Result.success(userVo);
    }

    @PostMapping("/user/logout")
    public Result<Void> logout(){
        return Result.success();
    }
}
