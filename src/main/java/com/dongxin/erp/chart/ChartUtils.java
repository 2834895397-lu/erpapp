package com.dongxin.erp.chart;

import cn.hutool.core.collection.CollUtil;
import com.dongxin.erp.chart.vo.Chart;
import com.dongxin.erp.chart.vo.MultiBar;
import org.apache.commons.collections.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author lulei
 * @date 2020年12月21日 17:32:04
 */
public class ChartUtils {


    public static MultiBar getMultiBar(List<Chart> chartList){
        MultiBar multiBar = MultiBar.builder().build();
        if(!CollectionUtils.isEmpty(chartList)){

            List<String> types = chartList.stream().map(Chart::getTypeName).distinct().collect(Collectors.toList());
            List<Map<String,Object>> multiBarData = CollUtil.newArrayList();
            for(String typeName : types){
                Map<String,Object> dataMap = CollUtil.newHashMap();
                dataMap.put("type" , typeName);
                List<Chart>  typeFieldsList =  chartList.stream().filter(p->p.getTypeName().equals(typeName)).collect(Collectors.toList());
                for(Chart typeField : typeFieldsList){
                    dataMap.put(typeField.getField() , typeField.getVal());
                }
                multiBarData.add(dataMap);
            }
            multiBar.setData(multiBarData);
            multiBar.setFields(chartList.stream().map(Chart::getField).distinct().collect(Collectors.toList()));
        }
        return multiBar;
    }


}
