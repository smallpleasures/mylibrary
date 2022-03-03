package com.yrainy.library.server.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author: zhanglun
 * @since: 1.0.0
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RespPageBean {

    private Long total;
    private List<?> data;
}
