package com.ll.test.dao.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.ll.test.dao.GoodsInfoDao;
import com.ll.test.entity.GoodsInfo;
import com.ll.test.entity.TaoBaoGoodsInfo;

/**
 * 存储商品的信息
 * 
 * @author Chris
 *
 */
@Repository
public class GoodsInfoDaoImpl implements GoodsInfoDao {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	/**
	 * 批量存储
	 * 
	 * @param infos
	 *            传入商品的信息列表
	 */
	public void saveBatch(List<GoodsInfo> infos) {
		try {
			 String sql = "insert INTO goods_info(" + "goods_id," + "goods_name," + "goods_price," + "img_url,"+"merchant_url,"+"merchant_name,"+"evaluations_num "+") "
		                + "VALUES(?,?,?,?,?,?,?)";
			for (GoodsInfo info : infos) {
				jdbcTemplate.update(sql.toString(), info.getGoodsId(), info.getGoodsName(), info.getGoodsPrice(),info.getImgUrl(),info.getMerchantUrl(), info.getMerchantName(), info.getEvaluationsNum());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
   /**
    * 淘宝的商品信息
    */
	@Override
	public void saveInfo(List<TaoBaoGoodsInfo> infos) {
		try {
			 String sql = "insert INTO  taobao_goods_info (" + "raw_title," + "view_fee," + "comment_count," + "item_loc,"+"detail_url"+") "+
		                  "VALUES(?,?,?,?,?)";
			for (TaoBaoGoodsInfo info : infos) {
				jdbcTemplate.update(sql.toString(), info.getRaw_title(), info.getView_fee(), info.getComment_count(),info.getItem_loc(),info.getDetail_url());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}

}
