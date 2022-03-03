package com.yrainy.library.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yrainy.library.server.mapper.LendInfoMapper;
import com.yrainy.library.server.pojo.LendInfo;
import com.yrainy.library.server.service.ILendInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zhanglun
 * @since 2022-02-27
 */
@Service
public class LendInfoServiceImpl extends ServiceImpl<LendInfoMapper, LendInfo> implements ILendInfoService {

    @Autowired
    private ILendInfoService lendInfoService;

    /**
     * 图书借阅数据统计
     * @return
     */
    @Override
    public List<Map<String, Object>> datavLendBooks() {
        return lendInfoService.getBaseMapper().selectMaps(
                new QueryWrapper<LendInfo>().select("book_id"
                        , "book_name"
                        , "count(*) as count")
                .groupBy("book_id")
                .orderByDesc("count")
                .last("limit 10")
        );
    }

    /**
     * 读者阅读数据统计
     * @return
     */
    @Override
    public List<Map<String, Object>> datavReaders() {
        return lendInfoService.getBaseMapper().selectMaps(
                new QueryWrapper<LendInfo>().select("reader_id"
                        , "reader_name"
                        , "count(*) as count")
                        .groupBy("reader_id")
                        .orderByDesc("count")
                        .last("limit 10")
        );
    }
}
