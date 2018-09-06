package com.rongxun.xqlc.Beans.center;

import com.rongxun.xqlc.Beans.BaseBean;
import com.rongxun.xqlc.Beans.PageBean;

import java.util.List;



public class UserRepaymentList  extends BaseBean
{
	private static final long serialVersionUID = 5467303173842659039L;

	public UserRepaymentList() {
		setRcd("R0001");
	}

	private List<UserRepayment> userRepaymentList;

	private PageBean pageBean;


	public PageBean getPageBean() {
		return pageBean;
	}

	public void setPageBean(PageBean pageBean) {
		this.pageBean = pageBean;
	}

	public List<UserRepayment> getUserRepaymentList() {
		return userRepaymentList;
	}

	public void setUserRepaymentList(List<UserRepayment> userRepaymentList) {
		this.userRepaymentList = userRepaymentList;
	}

}
