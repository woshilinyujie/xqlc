package com.rongxun.xqlc.Beans.user;

import com.google.gson.annotations.SerializedName;
import com.rongxun.xqlc.Beans.BaseBean;
import com.rongxun.xqlc.Beans.PageBean;

import java.util.List;



public class UserHongbaoBean extends BaseBean
{

	private static final long serialVersionUID = -4685847152264946796L;
	/**
	 * rmg : null
	 * hbNum : 7
	 * sumMoney : 588.00
	 * userHongbaoViews : [{"money":8,"status":0,"name":"用户注册","endTime":1494863999000,"endTimeStr":"2017年05月15日","limitStart":0,"investFullMomey":800},{"money":10,"status":0,"name":"用户注册","endTime":1495900799000,"endTimeStr":"2017年05月27日","limitStart":30,"investFullMomey":1000},{"money":20,"status":0,"name":"用户注册","endTime":1495900799000,"endTimeStr":"2017年05月27日","limitStart":30,"investFullMomey":2000},{"money":50,"status":0,"name":"用户注册","endTime":1495900799000,"endTimeStr":"2017年05月27日","limitStart":30,"investFullMomey":5000},{"money":100,"status":0,"name":"用户注册","endTime":1498924799000,"endTimeStr":"2017年07月01日","limitStart":60,"investFullMomey":10000},{"money":100,"status":0,"name":"用户注册","endTime":1498924799000,"endTimeStr":"2017年07月01日","limitStart":60,"investFullMomey":10000},{"money":300,"status":0,"name":"用户注册","endTime":1504972799000,"endTimeStr":"2017年09月09日","limitStart":90,"investFullMomey":30000}]
	 * expiredNum : 0
	 * useNum : 0
	 */

	@SerializedName("rmg")
	private Object rmgX;
	private String hbNum;
	private String sumMoney;
	private String expiredNum;
	private String useNum;
	@SerializedName("userHongbaoViews")
	private List<UserHongbaoViewsBean> userHongbaoViewsX;

	public UserHongbaoBean() {
		setRcd("R0001");
	}





	private List<UserHongbaoViews> userHongbaoViews;//红包列表
	private PageBean pageBean;

	public List<UserHongbaoViews> getUserHongbaoViews() {
		return userHongbaoViews;
	}

	public void setUserHongbaoViews(List<UserHongbaoViews> userHongbaoViews) {
		this.userHongbaoViews = userHongbaoViews;
	}



	public PageBean getPageBean() {
		return pageBean;
	}

	public void setPageBean(PageBean pageBean) {
		this.pageBean = pageBean;
	}

	public Object getRmgX() {
		return rmgX;
	}

	public void setRmgX(Object rmgX) {
		this.rmgX = rmgX;
	}

	public String getHbNum() {
		return hbNum;
	}

	public void setHbNum(String hbNum) {
		this.hbNum = hbNum;
	}

	public String getSumMoney() {
		return sumMoney;
	}

	public void setSumMoney(String sumMoney) {
		this.sumMoney = sumMoney;
	}

	public String getExpiredNum() {
		return expiredNum;
	}

	public void setExpiredNum(String expiredNum) {
		this.expiredNum = expiredNum;
	}

	public String getUseNum() {
		return useNum;
	}

	public void setUseNum(String useNum) {
		this.useNum = useNum;
	}

	public List<UserHongbaoViewsBean> getUserHongbaoViewsX() {
		return userHongbaoViewsX;
	}

	public void setUserHongbaoViewsX(List<UserHongbaoViewsBean> userHongbaoViewsX) {
		this.userHongbaoViewsX = userHongbaoViewsX;
	}


	public static class UserHongbaoViewsBean {
		/**
		 * money : 8.0
		 * status : 0
		 * name : 用户注册
		 * endTime : 1494863999000
		 * endTimeStr : 2017年05月15日
		 * limitStart : 0
		 * investFullMomey : 800
		 */

		private int limitStart;
		private int investFullMomey;

		public int getLimitStart() {
			return limitStart;
		}

		public void setLimitStart(int limitStart) {
			this.limitStart = limitStart;
		}

		public int getInvestFullMomey() {
			return investFullMomey;
		}

		public void setInvestFullMomey(int investFullMomey) {
			this.investFullMomey = investFullMomey;
		}
	}
}
