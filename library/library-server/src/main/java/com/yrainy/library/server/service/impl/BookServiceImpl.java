package com.yrainy.library.server.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yrainy.library.server.entity.RespBean;
import com.yrainy.library.server.mapper.BookMapper;
import com.yrainy.library.server.pojo.Book;
import com.yrainy.library.server.pojo.LendInfo;
import com.yrainy.library.server.pojo.Reader;
import com.yrainy.library.server.service.IBookService;
import com.yrainy.library.server.service.ILendInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.time.LocalDateTime;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zhanglun
 * @since 2022-02-27
 */
@Service
@Transactional
public class BookServiceImpl extends ServiceImpl<BookMapper, Book> implements IBookService {

    @Autowired
    private IBookService bookService;
    @Autowired
    private ILendInfoService lendInfoService;
    /**
     * 借书
     * @param id
     * @param principal
     * @return
     */
    @Override
    public RespBean lendBook(Integer id, Principal principal) {
        try {
            this.update()
                    .eq("id", id)
                    .setSql("stock=stock-1")
                    .update();
            Book book = bookService.getById(id);
            UsernamePasswordAuthenticationToken authenticationToken = (UsernamePasswordAuthenticationToken) principal;
            Reader reader = (Reader) authenticationToken.getPrincipal();
            LendInfo lendInfo = new LendInfo();
            lendInfo.setBookId(id);
            lendInfo.setBookName(book.getName());
            lendInfo.setReaderId(reader.getId());
            lendInfo.setReaderName(reader.getName());
            lendInfo.setLendDate(LocalDateTime.now());
            lendInfo.setState(1);
            lendInfoService.save(lendInfo);
            return RespBean.success("借阅成功");
        } catch (Exception e) {
            return RespBean.error("借阅失败");
        }

    }

    /**
     * 还书
     * @param id
     * @return
     */
    @Override
    public RespBean backBook(Integer id, Integer bookId) {
        this.update().eq("id", bookId).setSql("stock=stock+1").update();
        lendInfoService.update()
                .eq("id", id)
                .set("back_date", LocalDateTime.now())
                .set("state", 2).update();
        return RespBean.success("还书成功");
    }

    /**
     * 续借
     * @param id
     * @param bookId
     * @return
     */
    @Override
    public RespBean renewBook(Integer id, Integer bookId) {
        try {
            this.update()
                    .eq("id", bookId)
                    .setSql("stock=stock-1")
                    .update();
            lendInfoService.update()
                    .eq("id", id)
                    .set("back_date", null)
                    .set("renew_date", LocalDateTime.now())
                    .set("state", 1)
                    .update();
            return RespBean.success("续借成功");
        } catch (Exception e) {
            return RespBean.error("续借失败");
        }
    }
}
