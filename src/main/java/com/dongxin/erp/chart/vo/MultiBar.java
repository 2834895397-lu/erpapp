package com.dongxin.erp.chart.vo;

import cn.hutool.core.collection.CollUtil;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @author lulei
 * @date 2020年12月22日 09:50:11
 */

@Data
@Builder
public class MultiBar implements Serializable {

    private List<String> fields;

    private List<Map<String,Object>> data;

}
