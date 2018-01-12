package cn.itcast.erp.action;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import cn.itcast.erp.biz.utils.ErpException;
import cn.itcast.erp.utils.ActionUtil;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.apache.struts2.ServletActionContext;

import com.alibaba.fastjson.JSON;

import cn.itcast.erp.biz.IBaseBiz;

/**
 * 通用Action类
 * @author Administrator
 *
 * @param <T>
 */
public class BaseAction<T> {

	private IBaseBiz<T> baseBiz;
	
	public void setBaseBiz(IBaseBiz<T> baseBiz) {
		this.baseBiz = baseBiz;
	}
	
	//属性驱动:条件查询
	private T t1;
	private T t2;
	private Object param;
	public T getT2() {
		return t2;
	}
	public void setT2(T t2) {
		this.t2 = t2;
	}
	public Object getParam() {
		return param;
	}
	public void setParam(Object param) {
		this.param = param;
	}
	public T getT1() {
		return t1;
	}
	public void setT1(T t1) {
		this.t1 = t1;
	}
	
	private int page = 1;//页码
	private int rows = 10;//每页的记录数
	
	public int getPage() {
		return page;
	}
	public void setPage(int page) {
		this.page = page;
	}
	public int getRows() {
		return rows;
	}
	public void setRows(int rows) {
		this.rows = rows;
	}
	/**
	 * 条件查询
	 */
	public void list(){
		List<T> list = baseBiz.getList(t1,t2,param);
		//把部门列表转JSON字符串
		String listString = JSON.toJSONString(list);
		ActionUtil.write(listString);
	}
	
	public void listByPage(){
		System.out.println("页码：" + page + " 记录数:" + rows);
		int firstResult = (page -1) * rows;
		List<T> list = baseBiz.getListByPage(t1,t2,param,firstResult, rows);
		long total = baseBiz.getCount(t1,t2,param);
		//{total: total, rows:[]}
		Map<String, Object> mapData = new HashMap<String, Object>();
		mapData.put("total", total);
		mapData.put("rows", list);
		//把部门列表转JSON字符串
		String listString = JSON.toJSONString(mapData, SerializerFeature.DisableCircularReferenceDetect);
		ActionUtil.write(listString);
	}
	
	/**新增，修改*/
	private T t;
	public T getT() {
		return t;
	}
	public void setT(T t) {
		this.t = t;
	}
	/**
	 * 新增
	 * @param
	 */
	public void add(){
		//{"success":true,"message":""}
		//返回前端的JSON数据
		Map<String, Object> rtn = new HashMap<String, Object>();
		try {
			baseBiz.add(t);
			rtn.put("success",true);
			rtn.put("message","新增成功");
		} catch (Exception e) {
			e.printStackTrace();
			rtn.put("success",false);
			rtn.put("message","新增失败");
		}
		ActionUtil.write(JSON.toJSONString(rtn));
	}
	
	private long id;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	/**
	 * 删除
	 * @param
	 */
	public void delete(){

		try {
			baseBiz.delete(id);
			ActionUtil.ajaxReturn(true, "删除成功");
		} catch (ErpException e) {
			ActionUtil.ajaxReturn(false,e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			ActionUtil.ajaxReturn(false, "删除失败");
		}
	}
	
	/**
	 * 通过编辑查询对象
	 */
	public void get(){
		T t = baseBiz.get(id);

		String jsonString = JSON.toJSONStringWithDateFormat(t,"yyyy-MM-dd");
		System.out.println("转换前：" + jsonString);
		//{"name":"管理员组","tele":"000011","uuid":1}
		String jsonStringAfter = ActionUtil.mapData(jsonString, "t");
		System.out.println("转换后：" + jsonStringAfter);
		ActionUtil.write(jsonStringAfter);
	}
	
	/**
	 * 修改
	 */
	public void update(){
		try {
			baseBiz.update(t);
			ActionUtil.ajaxReturn(true, "修改成功");
		} catch (Exception e) {
			e.printStackTrace();
			ActionUtil.ajaxReturn(false, "修改失败");
		}
	}
}
