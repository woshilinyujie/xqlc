package com.rongxun.xqlc.Beans.center;


import com.google.gson.annotations.SerializedName;
import com.rongxun.xqlc.Beans.BaseBean;

import java.util.List;

public class UserTenderList  extends BaseBean
{
	private static final long serialVersionUID = 5467303173842659039L;


	/**
	 * rmg : null
	 * userRepaymentList : [{"repaymentStatus":0,"repaymentStatusShow":"待收","repaymentDate":1491097756000,"waitTotal":"100.28","waitAccount":"100.0","waitInterest":"0.28","serviceCharge":"0.00","borrowName":"新手标第226期","periods":1,"repaymentPeriods":1,"refunDays":-114,"tenderTime":1490493158000},{"repaymentStatus":0,"repaymentStatusShow":"待收","repaymentDate":1492414395000,"waitTotal":"101.78","waitAccount":"100.00","waitInterest":"1.78","serviceCharge":"0","borrowName":"新标zdl004","periods":1,"repaymentPeriods":1,"refunDays":-99,"tenderTime":1492422340000},{"repaymentStatus":0,"repaymentStatusShow":"待收","repaymentDate":1492414395000,"waitTotal":"101.78","waitAccount":"100.00","waitInterest":"1.78","serviceCharge":"0","borrowName":"新标zdl004","periods":1,"repaymentPeriods":1,"refunDays":-99,"tenderTime":1492422722000},{"repaymentStatus":0,"repaymentStatusShow":"待收","repaymentDate":1492414395000,"waitTotal":"203.56","waitAccount":"200.00","waitInterest":"3.56","serviceCharge":"0","borrowName":"新标zdl004","periods":1,"repaymentPeriods":1,"refunDays":-99,"tenderTime":1492479704000},{"repaymentStatus":0,"repaymentStatusShow":"待收","repaymentDate":1492414395000,"waitTotal":"305.34","waitAccount":"300.00","waitInterest":"5.34","serviceCharge":"0","borrowName":"新标zdl004","periods":1,"repaymentPeriods":1,"refunDays":-99,"tenderTime":1492479742000},{"repaymentStatus":0,"repaymentStatusShow":"待收","repaymentDate":1492414395000,"waitTotal":"101.78","waitAccount":"100.00","waitInterest":"1.78","serviceCharge":"0","borrowName":"新标zdl004","periods":1,"repaymentPeriods":1,"refunDays":-99,"tenderTime":1492481661000},{"repaymentStatus":0,"repaymentStatusShow":"待收","repaymentDate":1492414395000,"waitTotal":"101.78","waitAccount":"100.00","waitInterest":"1.78","serviceCharge":"0","borrowName":"新标zdl004","periods":1,"repaymentPeriods":1,"refunDays":-99,"tenderTime":1492482477000},{"repaymentStatus":0,"repaymentStatusShow":"待收","repaymentDate":1492484709000,"waitTotal":"101.74","waitAccount":"100.00","waitInterest":"1.74","serviceCharge":"0","borrowName":"有问题的标","periods":1,"repaymentPeriods":1,"refunDays":-98,"tenderTime":1492484981000},{"repaymentStatus":0,"repaymentStatusShow":"待收","repaymentDate":1493222399000,"waitTotal":"100.0","waitAccount":"100.0","waitInterest":"0.00","serviceCharge":"0","borrowName":"测试李昂11","periods":1,"repaymentPeriods":1,"refunDays":-90,"tenderTime":1493110637000},{"repaymentStatus":0,"repaymentStatusShow":"待收","repaymentDate":1493256688000,"waitTotal":"1000.20","waitAccount":"1000.0","waitInterest":"0.20","serviceCharge":"0.00","borrowName":"旧-测试李昂1-1天","periods":1,"repaymentPeriods":1,"refunDays":-89,"tenderTime":1493170288000}]
	 * pageBean : {"pageNumber":1,"totalCount":80,"pageCount":8,"pageSize":10}
	 */

	@SerializedName("rmg")
	private Object rmgX;
	private PageBeanBean pageBean;
	private List<UserRepaymentListBean> userRepaymentList;

	public Object getRmgX() {
		return rmgX;
	}

	public void setRmgX(Object rmgX) {
		this.rmgX = rmgX;
	}

	public PageBeanBean getPageBean() {
		return pageBean;
	}

	public void setPageBean(PageBeanBean pageBean) {
		this.pageBean = pageBean;
	}

	public List<UserRepaymentListBean> getUserRepaymentList() {
		return userRepaymentList;
	}

	public void setUserRepaymentList(List<UserRepaymentListBean> userRepaymentList) {
		this.userRepaymentList = userRepaymentList;
	}

	public static class PageBeanBean {
		/**
		 * pageNumber : 1
		 * totalCount : 80
		 * pageCount : 8
		 * pageSize : 10
		 */

		private int pageNumber;
		private int totalCount;
		private int pageCount;
		private int pageSize;

		public int getPageNumber() {
			return pageNumber;
		}

		public void setPageNumber(int pageNumber) {
			this.pageNumber = pageNumber;
		}

		public int getTotalCount() {
			return totalCount;
		}

		public void setTotalCount(int totalCount) {
			this.totalCount = totalCount;
		}

		public int getPageCount() {
			return pageCount;
		}

		public void setPageCount(int pageCount) {
			this.pageCount = pageCount;
		}

		public int getPageSize() {
			return pageSize;
		}

		public void setPageSize(int pageSize) {
			this.pageSize = pageSize;
		}
	}

	public static class UserRepaymentListBean {
		/**
		 * repaymentStatus : 0
		 * repaymentStatusShow : 待收
		 * repaymentDate : 1491097756000
		 * waitTotal : 100.28
		 * waitAccount : 100.0
		 * waitInterest : 0.28
		 * serviceCharge : 0.00
		 * borrowName : 新手标第226期
		 * periods : 1
		 * repaymentPeriods : 1
		 * refunDays : -114
		 * tenderTime : 1490493158000
		 */

		private int repaymentStatus;
		private String repaymentStatusShow;
		private long repaymentDate;
		private String waitTotal;
		private String waitAccount;
		private String waitInterest;
		private String serviceCharge;
		private String borrowName;
		private int periods;
		private int repaymentPeriods;
		private int refunDays;
		private long tenderTime;

		public int getRepaymentStatus() {
			return repaymentStatus;
		}

		public void setRepaymentStatus(int repaymentStatus) {
			this.repaymentStatus = repaymentStatus;
		}

		public String getRepaymentStatusShow() {
			return repaymentStatusShow;
		}

		public void setRepaymentStatusShow(String repaymentStatusShow) {
			this.repaymentStatusShow = repaymentStatusShow;
		}

		public long getRepaymentDate() {
			return repaymentDate;
		}

		public void setRepaymentDate(long repaymentDate) {
			this.repaymentDate = repaymentDate;
		}

		public String getWaitTotal() {
			return waitTotal;
		}

		public void setWaitTotal(String waitTotal) {
			this.waitTotal = waitTotal;
		}

		public String getWaitAccount() {
			return waitAccount;
		}

		public void setWaitAccount(String waitAccount) {
			this.waitAccount = waitAccount;
		}

		public String getWaitInterest() {
			return waitInterest;
		}

		public void setWaitInterest(String waitInterest) {
			this.waitInterest = waitInterest;
		}

		public String getServiceCharge() {
			return serviceCharge;
		}

		public void setServiceCharge(String serviceCharge) {
			this.serviceCharge = serviceCharge;
		}

		public String getBorrowName() {
			return borrowName;
		}

		public void setBorrowName(String borrowName) {
			this.borrowName = borrowName;
		}

		public int getPeriods() {
			return periods;
		}

		public void setPeriods(int periods) {
			this.periods = periods;
		}

		public int getRepaymentPeriods() {
			return repaymentPeriods;
		}

		public void setRepaymentPeriods(int repaymentPeriods) {
			this.repaymentPeriods = repaymentPeriods;
		}

		public int getRefunDays() {
			return refunDays;
		}

		public void setRefunDays(int refunDays) {
			this.refunDays = refunDays;
		}

		public long getTenderTime() {
			return tenderTime;
		}

		public void setTenderTime(long tenderTime) {
			this.tenderTime = tenderTime;
		}
	}
}
