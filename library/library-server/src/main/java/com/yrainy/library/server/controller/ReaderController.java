package com.yrainy.library.server.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yrainy.library.server.entity.RespBean;
import com.yrainy.library.server.entity.RespPageBean;
import com.yrainy.library.server.pojo.LendInfo;
import com.yrainy.library.server.pojo.Reader;
import com.yrainy.library.server.service.IReaderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Arrays;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author zhanglun
 * @since 2022-02-27
 */
@Api(tags = "读者管理")
@RestController
@RequestMapping("/reader")
public class ReaderController {

    @Autowired
    private IReaderService readerService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @ApiOperation(value = "获取所有读者（分页）")
    @GetMapping("/")
    public RespPageBean getReaders(@RequestParam(defaultValue = "1") Integer currentPage,
                                @RequestParam(defaultValue = "10") Integer size,
                                Reader reader
    ) {
        Page<Reader> page = new Page<>(currentPage, size);
        Page<Reader> ReaderPage = readerService.query()
                .select("*","(select rc.name from reader_category rc where rc.id = reader.reader_category_id) as readerCategoryName")
                .like(StringUtils.isNotBlank(reader.getName()), "name", reader.getName())
                .like(StringUtils.isNotBlank(reader.getUsername()), "username", reader.getUsername())
                .like(StringUtils.isNotBlank(reader.getAddress()), "address", reader.getAddress())
                .eq(reader.getSex() != null, "sex", reader.getSex())
                .eq(reader.getReaderCategoryId() != null, "reader_category_id", reader.getReaderCategoryId())
                .page(page);
        return new RespPageBean(ReaderPage.getTotal(), ReaderPage.getRecords());
    }

    @ApiOperation(value = "添加读者")
    @PostMapping("/")
    public RespBean addReader(@RequestBody Reader reader) {
        reader.setCreateTime(LocalDateTime.now());
        reader.setPassword(passwordEncoder.encode(reader.getPassword()));
        if (readerService.save(reader)) {
            return RespBean.success("添加成功");
        }
        return RespBean.error("添加失败");
    }

    @ApiOperation(value = "更新读者")
    @PutMapping ("/")
    public RespBean updateReader(@RequestBody Reader reader) {
        reader.setUpdateTime(LocalDateTime.now());
        if (readerService.updateById(reader)) {
            return RespBean.success("更新成功");
        }
        return RespBean.error("更新失败");
    }

    @ApiOperation(value = "删除读者")
    @DeleteMapping ("/{id}")
    public RespBean deleteReader(@PathVariable Integer id) {
        if (readerService.removeById(id)) {
            return RespBean.success("删除成功");
        }
        return RespBean.error("删除失败");
    }

    @ApiOperation(value = "批量删除读者")
    @DeleteMapping ("/")
    public RespBean deleteReader(Integer[] ids) {
        if (readerService.removeByIds(Arrays.asList(ids))) {
            return RespBean.success("删除成功");
        }
        return RespBean.error("删除失败");
    }

}
