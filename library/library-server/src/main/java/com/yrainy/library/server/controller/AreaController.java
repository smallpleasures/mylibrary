package com.yrainy.library.server.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yrainy.library.server.entity.RespBean;
import com.yrainy.library.server.entity.RespPageBean;
import com.yrainy.library.server.pojo.Area;
import com.yrainy.library.server.pojo.Area;
import com.yrainy.library.server.pojo.Class;
import com.yrainy.library.server.service.IAreaService;
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
@Api(tags = "图书馆区域管理")
@RestController
@RequestMapping("/area")
public class AreaController {

    @Autowired
    private IAreaService areaService;
    
    @ApiOperation(value = "获取所有图书区域（分页）")
    @GetMapping("/")
    public RespPageBean getAreas(@RequestParam(defaultValue = "1") Integer currentPage,
                                 @RequestParam(defaultValue = "10") Integer size,
                                 Area areaInfo
    ) {
        Page<Area> page = new Page<>(currentPage, size);
        Page<Area> AreaPage = areaService.query()
                .eq(StringUtils.isNotBlank(areaInfo.getName()), "name", areaInfo.getName())
                .page(page);
        return new RespPageBean(AreaPage.getTotal(), AreaPage.getRecords());
    }

    @ApiOperation(value = "添加图书区域")
    @PostMapping ("/")
    public RespBean addArea(@RequestBody Area areaInfo) {
        areaInfo.setCreateTime(LocalDateTime.now());
        if (areaService.save(areaInfo)) {
            return RespBean.success("添加成功");
        }
        return RespBean.error("添加失败");
    }

    @ApiOperation(value = "更新图书区域")
    @PutMapping ("/")
    public RespBean updateArea(@RequestBody Area areaInfo) {
        areaInfo.setUpdateTime(LocalDateTime.now());
        if (areaService.updateById(areaInfo)) {
            return RespBean.success("更新成功");
        }
        return RespBean.error("更新失败");
    }

    @ApiOperation(value = "删除图书区域")
    @DeleteMapping ("/{id}")
    public RespBean deleteArea(@PathVariable Integer id) {
        if (areaService.removeById(id)) {
            return RespBean.success("删除成功");
        }
        return RespBean.error("删除失败");
    }

    @ApiOperation(value = "批量删除图书区域")
    @DeleteMapping ("/")
    public RespBean deleteArea(Integer[] ids) {
        if (areaService.removeByIds(Arrays.asList(ids))) {
            return RespBean.success("删除成功");
        }
        return RespBean.error("删除失败");
    }
}
