package com.zhang.demo.dao;

import com.zhang.demo.entity.ZUserEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 
 * 
 * @author zhang
 * @date 2020-04-20 14:38:30
 */
@Mapper
public interface ZUserDao extends BaseMapper<ZUserEntity> {
	
}
