package com.yrainy.library.server.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yrainy.library.server.mapper.ClassMapper;
import com.yrainy.library.server.pojo.Class;
import com.yrainy.library.server.service.IClassService;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zhanglun
 * @since 2022-02-27
 */
@Service
public class ClassServiceImpl extends ServiceImpl<ClassMapper, Class> implements IClassService {

}
