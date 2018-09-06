package com.rongxun.xqlc.Beans;

import java.util.List;


public class UserAccDetailList extends BaseBean
{

	private static final long serialVersionUID = -8652792952418520978L;


	public UserAccDetailList() {
		setRcd("R0001");
	}
	

	private PageBean pageBean;
	private List<UserAccDetail> userAccDetailList;
	
	
	public PageBean getPageBean() {
		return pageBean;
	}
	public void setPageBean(PageBean pageBean) {
		this.pageBean = pageBean;
	}
	public List<UserAccDetail> getUserAccDetailList() {
		return userAccDetailList;
	}
	public void setUserAccDetailList(List<UserAccDetail> userAccDetailList) {
		this.userAccDetailList = userAccDetailList;
	}
	
	
	
	
	
	
	
	
	
	
}
