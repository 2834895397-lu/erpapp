package com.dongxin.erp.bd.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import com.dongxin.erp.bd.entity.AttachedUrl;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 附件地址表
 * @Author: huangheng
 * @Date: 2020-12-24
 * @Version: V1.0
 */
public interface AttachedUrlMapper extends BaseMapper<AttachedUrl> {

	void updateDelFlagById(@Param("id") String id, @Param("tenant") String tenant);

	AttachedUrl selectOne(@Param("relationId") String relationId, @Param("url") String url, @Param("sort") int sort,
			@Param("tenant") String tenant);

}
