package com.yrainy.library.server.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yrainy.library.server.entity.RespBean;
import com.yrainy.library.server.entity.RespPageBean;
import com.yrainy.library.server.pojo.Book;
import com.yrainy.library.server.pojo.Class;
import com.yrainy.library.server.service.IClassService;
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
@Api(tags = "图书类别")
@RestController
@RequestMapping("/class")
public class ClassController {

    @Autowired
    private IClassService classService;

    @ApiOperation(value = "获取所有图书类别（分页）")
    @GetMapping("/")
    public RespPageBean getClasses(@RequestParam(defaultValue = "1") Integer currentPage,
                                 @RequestParam(defaultValue = "10") Integer size,
                                 Class classInfo
                                 ) {
        Page<Class> page = new Page<>(currentPage, size);
        Page<Class> classPage = classService.query()
                .eq(StringUtils.isNotBlank(classInfo.getName()), "name", classInfo.getName())
                .page(page);
        return new RespPageBean(classPage.getTotal(), classPage.getRecords());
    }

    @ApiOperation(value = "添加图书类别")
    @PostMapping ("/")
    public RespBean addClass(@RequestBody Class classInfo) {
        classInfo.setCreateTime(LocalDateTime.now());
        if (classService.save(classInfo)) {
            return RespBean.success("添加成功");
        }
        return RespBean.error("添加失败");
    }

    @ApiOperation(value = "更新图书类别")
    @PutMapping ("/")
    public RespBean updateClass(@RequestBody Class classInfo) {
        classInfo.setUpdateTime(LocalDateTime.now());
        if (classService.updateById(classInfo)) {
            return RespBean.success("更新成功");
        }
        return RespBean.error("更新失败");
    }

    @ApiOperation(value = "删除图书类别")
    @DeleteMapping ("/{id}")
    public RespBean deleteClass(@PathVariable Integer id) {
        if (classService.removeById(id)) {
            return RespBean.success("删除成功");
        }
        return RespBean.error("删除失败");
    }

    @ApiOperation(value = "批量删除图书类别")
    @DeleteMapping ("/")
    public RespBean deleteClass(Integer[] ids) {
        if (classService.removeByIds(Arrays.asList(ids))) {
            return RespBean.success("删除成功");
        }
        return RespBean.error("删除失败");
    }
}
