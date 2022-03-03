package com.yrainy.library.server.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 
 * </p>
 *
 * @author zhanglun
 * @since 2022-02-27
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="Book对象", description="")
public class Book implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "书籍名称")
    private String name;

    @ApiModelProperty(value = "作者")
    private String author;

    @ApiModelProperty(value = "出版社")
    private String publish;

    @ApiModelProperty(value = "ISBN号")
    @TableField("ISBN")
    private String isbn;

    @ApiModelProperty(value = "介绍")
    private String introduction;

    @ApiModelProperty(value = "语言")
    private String language;

    @ApiModelProperty(value = "价格")
    private Double price;

    @ApiModelProperty(value = "出版时间")
    private String pubDate;

    @ApiModelProperty(value = "书籍类型id")
    private Integer classId;

    @ApiModelProperty(value = "书籍类型")
    @TableField(exist = false)
    private String className;

    @ApiModelProperty(value = "书籍区域id")
    private Integer areaId;

    @ApiModelProperty(value = "书籍区域")
    @TableField(exist = false)
    private String areaName;

    @ApiModelProperty(value = "库存")
    private Integer stock;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
    private LocalDateTime updateTime;


}
