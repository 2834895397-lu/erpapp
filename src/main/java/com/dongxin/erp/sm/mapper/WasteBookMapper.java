package com.dongxin.erp.sm.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import com.dongxin.erp.sm.entity.WasteBook;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Component;

/**
 * @Description: 出入库流水表
 * @Author: jeecg-boot
 * @Date:   2020-11-10
 * @Version: V1.0
 */
@Component
public interface WasteBookMapper extends BaseMapper<WasteBook> {

}
