package cn.itcast.erp.biz.impl;
import cn.itcast.erp.biz.IGoodstypeBiz;
import cn.itcast.erp.biz.utils.ErpException;
import cn.itcast.erp.dao.IGoodsDao;
import cn.itcast.erp.dao.IGoodstypeDao;
import cn.itcast.erp.entity.Goods;
import cn.itcast.erp.entity.Goodstype;
/**
 * 商品分类业务逻辑类
 * @author Administrator
 *
 */
public class GoodstypeBiz extends BaseBiz<Goodstype> implements IGoodstypeBiz {

	private IGoodstypeDao goodstypeDao;
	private IGoodsDao goodsDao;
	
	public void setGoodstypeDao(IGoodstypeDao goodstypeDao) {
		this.goodstypeDao = goodstypeDao;
		super.setBaseDao(this.goodstypeDao);
	}

	public void setGoodsDao(IGoodsDao goodsDao) {
		this.goodsDao = goodsDao;
	}

	@Override
	public void delete(Long uuid) {

		Goods goods = new Goods();
		goods.setGoodstype(new Goodstype());
		goods.getGoodstype().setUuid(uuid);

		long count = goodsDao.getCount(goods, null, null);
		if (count > 0) {
			throw new ErpException("该分类下含有商品,不能删除!");
		}
		super.delete(uuid);
	}
}
