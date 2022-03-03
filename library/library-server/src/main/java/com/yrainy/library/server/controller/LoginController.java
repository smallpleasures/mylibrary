package com.yrainy.library.server.controller;

import com.yrainy.library.server.entity.RespBean;
import com.yrainy.library.server.pojo.Admin;
import com.yrainy.library.server.pojo.LoginParam;
import com.yrainy.library.server.pojo.Reader;
import com.yrainy.library.server.service.IAdminService;
import com.yrainy.library.server.service.IReaderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

/**
 * 登录控制器
 * @author: zhanglun
 * @since: 1.0.0
 */

@Api(tags = "登录")
@RestController
public class LoginController {

    @Autowired
    private IAdminService adminService;
    @Autowired
    private IReaderService readerService;

    @ApiOperation(value = "登录之后返回token")
    @PostMapping("/login")
    public RespBean login(@RequestBody LoginParam loginParam,
                          HttpServletRequest request) {
        if (loginParam.getAdmin()) {
            return adminService.login(loginParam.getUsername(), loginParam.getPassword(), request);
        } else {
            return readerService.login(loginParam.getUsername(), loginParam.getPassword(), request);
        }
    }

    @ApiOperation(value = "获取当前用户信息")
    @GetMapping("/userinfo")
    public RespBean getUserInfo(@ApiIgnore Principal principal) {
        if (null == principal) {
            return null;
        }
        String username = principal.getName();
        UsernamePasswordAuthenticationToken authenticationToken = (UsernamePasswordAuthenticationToken) principal;
        if (authenticationToken.getPrincipal() instanceof Admin) {
            Admin admin = adminService.query().eq("username", username).one();
            admin.setPassword(null);
            return RespBean.success(admin);
        } else {
            Reader reader = readerService.query().eq("username", username).one();
            reader.setPassword(null);
            return RespBean.success(reader);
        }
    }

    @ApiOperation(value = "退出登录")
    @PostMapping("/logout")
    public RespBean logout() {
        return RespBean.success("注销成功！");
    }
}
