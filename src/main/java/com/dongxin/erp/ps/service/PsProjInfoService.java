package com.dongxin.erp.ps.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeNode;
import cn.hutool.core.lang.tree.TreeUtil;
import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dongxin.erp.ps.entity.PsBudget;
import com.dongxin.erp.ps.entity.PsProjInfo;
import com.dongxin.erp.ps.entity.PsWbsNode;
import com.dongxin.erp.ps.mapper.PsProjInfoMapper;
import org.apache.commons.lang.StringUtils;
import org.checkerframework.checker.units.qual.A;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.system.entity.SysDictItem;
import org.jeecg.modules.system.service.ISysDictItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.jeecg.common.system.base.service.BaseService;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @Description: 项目基础信息
 * @Author: ljc
 * @Date:   2020-11-13
 * @Version: V1.0
 */
@Service
public class PsProjInfoService extends BaseService<PsProjInfoMapper, PsProjInfo> {

    @Autowired
    private PsWbsNodeService nodeService;

    @Autowired
    private PsBudgetService budgetService;

    /**
     * @Author: ljc
     * 获取WBS树
     * @param id
     * @return
     */
    public Map getWbstree(String id, String tenantid) {

        List<PsWbsNode> nodes = nodeService.list(
                new QueryWrapper<PsWbsNode>()
                        .eq("proj_id", id)
                        .orderByAsc("sort")
        );

        List<TreeNode<String>> nodeList = CollUtil.newArrayList();

        nodes.forEach(x -> {
            String pId = StringUtils.isBlank(x.getParentId()) ? "0" : x.getParentId();
            nodeList.add(new TreeNode<>(x.getId(), pId, x.getName(), 5));
        });

        List<Tree<String>> treeList = TreeUtil.build(nodeList, "0");

        return MapUtil.builder("tree", treeList).build();
    }


    public Map queryProjWbsActi(String projId, String tenantid, String wbsId,PsBudget param,Integer pageNo,Integer pageSize, HttpServletRequest req) {

        QueryWrapper<PsBudget> queryWrapper = QueryGenerator.initQueryWrapper(param, req.getParameterMap());

        queryWrapper.eq("wbs_id", wbsId);

        Page<PsBudget> page = new Page((long)pageNo, (long)pageSize);

        return MapUtil.builder("pages", this.budgetService.page(page, queryWrapper)).build();
    }
}
