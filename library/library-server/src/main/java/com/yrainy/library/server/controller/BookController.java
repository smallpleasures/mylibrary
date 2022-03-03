package com.yrainy.library.server.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yrainy.library.server.entity.RespBean;
import com.yrainy.library.server.entity.RespPageBean;
import com.yrainy.library.server.pojo.Area;
import com.yrainy.library.server.pojo.Book;
import com.yrainy.library.server.service.IBookService;
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

@Api(tags = "图书管理")
@RestController
@RequestMapping("/book")
public class BookController {

    @Autowired
    private IBookService bookService;

    @ApiOperation(value = "获取所有图书（分页）")
    @GetMapping("/")
    public RespPageBean getBooks(@RequestParam(defaultValue = "1") Integer currentPage,
                                @RequestParam(defaultValue = "10") Integer size,
                                Book book, @RequestParam(defaultValue = "false") Boolean inLibrary
    ) {
        Page<Book> page = new Page<>(currentPage, size);
        Page<Book> bookPage = bookService.query()
                .select("*", "(select c.name from class c where c.id = book.class_id) as className",
                        "(select a.name from area a where a.id = book.area_id) as areaName")
                .like(StringUtils.isNotBlank(book.getName()), "name", book.getName())
                .like(StringUtils.isNotBlank(book.getAuthor()), "author", book.getAuthor())
                .like(StringUtils.isNotBlank(book.getPublish()), "publish", book.getPublish())
                .like(StringUtils.isNotBlank(book.getIsbn()), "isbn",book.getIsbn())
                .eq(book.getClassId() != null, "class_id", book.getClassId())
                .eq(book.getAreaId() != null, "area_id", book.getAreaId())
                .gt(inLibrary, "stock", 0)
                .page(page);
        return new RespPageBean(bookPage.getTotal(), bookPage.getRecords());
    }

    @ApiOperation(value = "添加图书")
    @PostMapping("/")
    public RespBean addArea(@RequestBody Book book) {
        book.setCreateTime(LocalDateTime.now());
        if (bookService.save(book)) {
            return RespBean.success("添加成功");
        }
        return RespBean.error("添加失败");
    }

    @ApiOperation(value = "更新图书")
    @PutMapping ("/")
    public RespBean updateArea(@RequestBody Book book) {
        book.setUpdateTime(LocalDateTime.now());
        if (bookService.updateById(book)) {
            return RespBean.success("更新成功");
        }
        return RespBean.error("更新失败");
    }

    @ApiOperation(value = "删除图书")
    @DeleteMapping ("/{id}")
    public RespBean deleteArea(@PathVariable Integer id) {
        if (bookService.removeById(id)) {
            return RespBean.success("删除成功");
        }
        return RespBean.error("删除失败");
    }

    @ApiOperation(value = "批量删除图书")
    @DeleteMapping ("/")
    public RespBean deleteArea(Integer[] ids) {
        if (bookService.removeByIds(Arrays.asList(ids))) {
            return RespBean.success("删除成功");
        }
        return RespBean.error("删除失败");
    }
}
