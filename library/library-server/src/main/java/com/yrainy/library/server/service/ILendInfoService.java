package com.yrainy.library.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yrainy.library.server.pojo.LendInfo;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zhanglun
 * @since 2022-02-27
 */
public interface ILendInfoService extends IService<LendInfo> {

    /**
     * 图书借书数据统计
     * @return
     */
    List<Map<String, Object>> datavLendBooks();

    /**
     * 读者阅读数据统计
     * @return
     */
    List<Map<String, Object>> datavReaders();
}
