package com.dongxin.erp.ps.vo;

import com.dongxin.erp.ps.entity.PsProjInfo;
import com.dongxin.erp.ps.entity.PsWbs;
import com.dongxin.erp.ps.entity.PsWbsNode;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class PsPojWbsNodeVo extends PsProjInfo {

    private List<PsWbs> wbs = new ArrayList<>();

    private List<PsWbsNode> nodes = new ArrayList<>();

}
