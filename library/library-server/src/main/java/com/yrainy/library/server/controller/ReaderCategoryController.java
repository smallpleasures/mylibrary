package com.yrainy.library.server.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yrainy.library.server.entity.RespBean;
import com.yrainy.library.server.entity.RespPageBean;
import com.yrainy.library.server.pojo.ReaderCategory;
import com.yrainy.library.server.service.IReaderCategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
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

@Api(tags = "读者类别管理")
@RestController
@RequestMapping("/readerCategory")
public class ReaderCategoryController {

    @Autowired
    private IReaderCategoryService readerCategoryService;

    @ApiOperation(value = "获取所有读者类别（分页）")
    @GetMapping("/")
    public RespPageBean getReaderCategories(@RequestParam(defaultValue = "1") Integer currentPage,
                                @RequestParam(defaultValue = "10") Integer size,
                                ReaderCategory readerCategoryInfo
    ) {
        Page<ReaderCategory> page = new Page<>(currentPage, size);
        Page<ReaderCategory> readerCategoryPage = readerCategoryService.query()
                .eq(StringUtils.isNotBlank(readerCategoryInfo.getName()), "name", readerCategoryInfo.getName())
                .page(page);
        return new RespPageBean(readerCategoryPage.getTotal(), readerCategoryPage.getRecords());
    }

    @ApiOperation(value = "添加读者类别")
    @PostMapping("/")
    public RespBean addReaderCategory(@RequestBody ReaderCategory readerCategoryInfo) {
        readerCategoryInfo.setCreateTime(LocalDateTime.now());
        if (readerCategoryService.save(readerCategoryInfo)) {
            return RespBean.success("添加成功");
        }
        return RespBean.error("添加失败");
    }

    @ApiOperation(value = "更新读者类别")
    @PutMapping ("/")
    public RespBean updateReaderCategory(@RequestBody ReaderCategory readerCategoryInfo) {
        readerCategoryInfo.setUpdateTime(LocalDateTime.now());
        if (readerCategoryService.updateById(readerCategoryInfo)) {
            return RespBean.success("更新成功");
        }
        return RespBean.error("更新失败");
    }

    @ApiOperation(value = "删除读者类别")
    @DeleteMapping ("/{id}")
    public RespBean deleteReaderCategory(@PathVariable Integer id) {
        if (readerCategoryService.removeById(id)) {
            return RespBean.success("删除成功");
        }
        return RespBean.error("删除失败");
    }

    @ApiOperation(value = "批量删除读者类别")
    @DeleteMapping ("/")
    public RespBean deleteReaderCategory(Integer[] ids) {
        if (readerCategoryService.removeByIds(Arrays.asList(ids))) {
            return RespBean.success("删除成功");
        }
        return RespBean.error("删除失败");
    }
}
