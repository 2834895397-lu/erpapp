package com.dongxin.erp.ps.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeNode;
import cn.hutool.core.lang.tree.TreeUtil;
import cn.hutool.core.map.MapBuilder;
import cn.hutool.core.map.MapUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dongxin.erp.bd.entity.WbsType;
import com.dongxin.erp.bd.service.WbsTypeService;
import com.dongxin.erp.ps.entity.TpsWbsModel;
import com.dongxin.erp.ps.mapper.TpsWbsModelMapper;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.exception.JeecgBootException;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.system.entity.SysUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.jeecg.common.system.base.service.BaseService;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @Description: 工作分解模板
 * @Author: jeecg-boot
 * @Date:   2021-01-18
 * @Version: V1.0
 */
@Service
public class TpsWbsModelService extends BaseService<TpsWbsModelMapper, TpsWbsModel> {

    @Autowired
    WbsTypeService wbsTypeService;

    public Result<?> wbsModelTree() {
        List<TpsWbsModel> list = this.list(
                new QueryWrapper<TpsWbsModel>()
        );

        List<TreeNode<String>> nodeList = CollUtil.newArrayList();

        for(int i = 0 ;i < list.size(); i++){
            Map map = MapUtil.builder()
                    .put("wbsDesc", list.get(i).getWbsDesc())
                    .put("wbsModelName", list.get(i).getWbsModelName())
                    .put("wbsTypeCode", list.get(i).getWbsTypeCode())
                    .build();

            String pId = StringUtils.isBlank(list.get(i).getParentId()) ? "0" : list.get(i).getParentId();

            nodeList.add(new TreeNode<>(
                    list.get(i).getId(),
                    pId,
                    list.get(i).getWbsDesc(),
                    5).setExtra(map)
            );
        }

        List<Tree<String>> treeList = TreeUtil.build(nodeList, "0");

        return Result.OK(treeList);
    }


    public Result<?> option() {
        List<TreeNode<String>> nodeList = CollUtil.newArrayList();

        List<Map<Object, Object>> select = CollUtil.newArrayList();

        wbsTypeService.list(
                new QueryWrapper<WbsType>()
        ).forEach(x -> {
            if(x.getStatusFlag() != 0){
                Map<Object, Object> map = MapUtil.builder()
                        .put("key", x.getId())
                        .put("title", x.getWbsTypeName())
                        .put("value", x.getId()).build();
                select.add(map);
            }
        });

        return Result.OK(
                MapUtil.builder()
                        .put("select", select)
                        .build());
    }


    @Transactional
    public Result<?> deleteBatch(String ids) {

        LoginUser user = (LoginUser) SecurityUtils.getSubject().getPrincipal();

        List<String> idsList = JSONUtil.parseArray(JSONUtil.parseObj(ids).get("ids")).toList(String.class);

        idsList.forEach(x->{
           if( this.list(
                    new QueryWrapper<TpsWbsModel>()
                            .eq("parent_id", x)
                            .eq("del_flag", 0)).size() > 0 ){

               throw new JeecgBootException("请先删除子节点！");
           };
        });

        if(idsList.size() > 0) {
            idsList.forEach(x -> {
                this.removeById(new TpsWbsModel().setId(x));
            });
        }
        return Result.OK();
    }


}
