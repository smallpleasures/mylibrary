package com.yrainy.library.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yrainy.library.server.entity.RespBean;
import com.yrainy.library.server.pojo.Admin;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zhanglun
 * @since 2022-02-27
 */
public interface IAdminService extends IService<Admin> {

    /**
     * 管理员登录之后返回token
     * @param username
     * @param password
     * @param request
     * @return
     */
    RespBean login(String username, String password, HttpServletRequest request);
}
