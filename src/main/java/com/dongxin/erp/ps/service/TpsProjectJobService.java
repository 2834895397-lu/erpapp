package com.dongxin.erp.ps.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeNode;
import cn.hutool.core.lang.tree.TreeUtil;
import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dongxin.erp.ps.entity.ProjectWbs;
import com.dongxin.erp.ps.entity.PsMaterialPlan;
import com.dongxin.erp.ps.entity.TpsProjectJob;
import com.dongxin.erp.ps.entity.TpsWbsModel;
import com.dongxin.erp.ps.mapper.TpsProjectJobMapper;
import org.apache.commons.lang.StringUtils;
import org.checkerframework.checker.units.qual.A;
import org.jeecg.common.api.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.jeecg.common.system.base.service.BaseService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Description: 项目作业
 * @Author: jeecg-boot
 * @Date:   2021-01-21
 * @Version: V1.0
 */
@Service
public class TpsProjectJobService extends BaseService<TpsProjectJobMapper, TpsProjectJob> {

    @Autowired
    private ProjectWbsService projectWbsService;

    @Autowired
    private PsMaterialPlanService materialPlanService;


    public Result<?> jobTree(String id) {

        List<TreeNode<String>> nodeList = CollUtil.newArrayList();

        projectWbsService.list(
                new QueryWrapper<ProjectWbs>()
                .eq("project_id", id)
        ).forEach(w->{

            Map top = MapUtil.builder()
                    .put("text", "menu")
                    .put("action", false)
                    .build();

            nodeList.add(new TreeNode<String>(w.getId(),
                    StringUtils.isBlank(w.getParentId()) ? "0" : w.getParentId(),
                    w.getWbsDesc(), 5).setExtra(top));

            List<TpsProjectJob> jobs = new ArrayList<>();

            jobs = this.list(
                    new QueryWrapper<TpsProjectJob>()
                    .eq("project_wbs_id", w.getId())
            );

            if(jobs.size() > 0){
                jobs.forEach(j->{
                    Map children = MapUtil.builder()
                            .put("text", "line")
                            .put("action", true)
                            .put("order",
                                    (j.getPurchaseStatus().equals(0)||StringUtils.isBlank(j.getPurchaseStatus()+""))?
                                            false:true
                            )
                            .build();
                    nodeList.add(new TreeNode<String>(j.getId(), w.getId(), j.getJobDesc(), 5).setExtra(children));
                });
            }
        });

        return Result.OK(TreeUtil.build(nodeList, "0"));
    }


    public Result<?> materialInfo(String id) {
        return Result.OK(
                materialPlanService.list(
                        new QueryWrapper<PsMaterialPlan>()
                                .eq("id", id)
                )
        );
    }


    public Result<?> orderPlan(String id) {
        System.err.println(id);
        try {
            this.update(
                    (TpsProjectJob.class.newInstance()).setPurchaseStatus(1),
                    new QueryWrapper<TpsProjectJob>().eq(StringUtils.isNotBlank(id), "id", id));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Result.OK();
    }
}
