package com.yrainy.library.server.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yrainy.library.server.entity.RespBean;
import com.yrainy.library.server.entity.RespPageBean;
import com.yrainy.library.server.pojo.Book;
import com.yrainy.library.server.pojo.LendInfo;
import com.yrainy.library.server.pojo.Reader;
import com.yrainy.library.server.service.IBookService;
import com.yrainy.library.server.service.ILendInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author zhanglun
 * @since 2022-02-27
 */

@Api(tags = "借阅信息")
@RestController
@RequestMapping("/lendInfo")
public class LendInfoController {

    @Autowired
    private ILendInfoService lendInfoService;
    @Autowired
    private IBookService bookService;

    @ApiOperation(value = "获取所有图书借阅历史（分页）")
    @GetMapping("/")
    public RespPageBean getBooks(@RequestParam(defaultValue = "1") Integer currentPage,
                                 @RequestParam(defaultValue = "10") Integer size,
                                 LendInfo lendInfo
    ) {
        Page<LendInfo> page = new Page<>(currentPage, size);
        Page<LendInfo> lendInfoPage = lendInfoService.query()
                .like(StringUtils.isNotBlank(lendInfo.getBookName()), "book_name", lendInfo.getBookName())
                .like(StringUtils.isNotBlank(lendInfo.getReaderName()), "reader_name", lendInfo.getReaderName())
                .eq(lendInfo.getState() != null, "state", lendInfo.getState())
                .page(page);
        return new RespPageBean(lendInfoPage.getTotal(), lendInfoPage.getRecords());
    }

    @ApiOperation(value = "获取当前读者图书借阅历史（分页）")
    @GetMapping("/currentReader")
    public RespPageBean getBooksByReaderId(@RequestParam(defaultValue = "1") Integer currentPage,
                                           @RequestParam(defaultValue = "10") Integer size,
                                           Principal principal, LendInfo lendInfo
    ) {
        UsernamePasswordAuthenticationToken authenticationToken = (UsernamePasswordAuthenticationToken) principal;
        Reader reader = (Reader) authenticationToken.getPrincipal();
        Page<LendInfo> page = new Page<>(currentPage, size);
        Page<LendInfo> lendInfoPage = lendInfoService.query()
                .like(StringUtils.isNotBlank(lendInfo.getBookName()), "book_name", lendInfo.getBookName())
                .eq(lendInfo.getState() != null, "state", lendInfo.getState())
                .eq("reader_id", reader.getId())
                .page(page);
        return new RespPageBean(lendInfoPage.getTotal(), lendInfoPage.getRecords());
    }

    @ApiOperation(value = "借书")
    @PostMapping("/{bookId}")
    public RespBean lendBook(Principal principal, @PathVariable Integer bookId) {

        return bookService.lendBook(bookId, principal);
    }

    @ApiOperation(value = "还书")
    @PostMapping("/{id}/{bookId}")
    public RespBean backBook(@PathVariable Integer id, @PathVariable Integer bookId) {
        return bookService.backBook(id, bookId);
    }

    @ApiOperation(value = "续借")
    @PutMapping("/renewBook/{id}/{bookId}")
    public RespBean renewBook(@PathVariable Integer id, @PathVariable Integer bookId) {
        return bookService.renewBook(id, bookId);
    }

    @ApiOperation(value = "图书借阅数据统计")
    @GetMapping("/datavLendBooks")
    public RespBean datavLendBooks() {
        List<Map<String, Object>> datavLendBooks = lendInfoService.datavLendBooks();
        return RespBean.success(datavLendBooks);
    }

    @ApiOperation(value = "读者阅读数据统计")
    @GetMapping("/datavReaders")
    public RespBean datavReaders() {
        List<Map<String, Object>> datavReaders = lendInfoService.datavReaders();
        return RespBean.success(datavReaders);
    }

}
