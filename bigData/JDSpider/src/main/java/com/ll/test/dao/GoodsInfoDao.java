package com.ll.test.dao;

import java.util.List;

import com.ll.test.entity.GoodsInfo;
import com.ll.test.entity.TaoBaoGoodsInfo;

/**
 * 对爬取的商品信息进行存储的dao
 * @author Chris
 *
 */
public interface GoodsInfoDao {
	/**
     * 插入商品信息
     * @param infos
     */
    void saveBatch(List<GoodsInfo> infos);
    /**
     * 插入淘宝的商品信息
     * @param map
     */
    void saveInfo(List<TaoBaoGoodsInfo> infos);
}
