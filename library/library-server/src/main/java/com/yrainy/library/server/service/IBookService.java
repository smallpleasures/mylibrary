package com.yrainy.library.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yrainy.library.server.entity.RespBean;
import com.yrainy.library.server.pojo.Book;
import io.swagger.models.auth.In;

import java.security.Principal;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zhanglun
 * @since 2022-02-27
 */
public interface IBookService extends IService<Book> {

    /**
     * 借书
     * @param id
     * @param principal
     * @return
     */
    RespBean lendBook(Integer id, Principal principal);

    /**
     * 还书
     * @param id
     * @return
     */
    RespBean backBook(Integer id, Integer bookId);

    /**
     * 续借
     * @param id
     * @param bookId
     * @return
     */
    RespBean renewBook(Integer id, Integer bookId);
}
