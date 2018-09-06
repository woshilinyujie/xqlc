package com.rongxun.xqlc.Beans.bank;

import com.google.gson.annotations.SerializedName;
import com.rongxun.xqlc.Beans.BaseBean;

import java.io.Serializable;
import java.util.List;


public class BankList extends BaseBean implements Serializable
{


	/**
	 * rmg : null
	 * id : 193473
	 * bankId : null
	 * realName : null
	 * cardNo : null
	 * branch : null
	 * status : null
	 * idNo : null
	 * registerTime : 20170808154334
	 * noIcon : null
	 * phone : 18606889455
	 * bankCardList : [{"bankId":"ICBC","bankName":"工商银行","bankLimit":"本次支付上限5万，单日支付上限5万","bankIcon":"http://192.168.0.149:8080/img/bankIcon/icon_ICBC.png"},{"bankId":"ABC","bankName":"农业银行","bankLimit":"本次支付上限20万，单日上限20万","bankIcon":"http://192.168.0.149:8080/img/bankIcon/icon_ABC.png"},{"bankId":"CCB","bankName":"建设银行","bankLimit":"本次支付上限20万，单日上限50万","bankIcon":"http://192.168.0.149:8080/img/bankIcon/icon_CCB.png"},{"bankId":"BOC","bankName":"中国银行","bankLimit":"本次支付上限5万，单日上限5万","bankIcon":"http://192.168.0.149:8080/img/bankIcon/icon_BOC.png"},{"bankId":"PSBC","bankName":"邮储银行","bankLimit":"本次支付上限20万，单日上限50万","bankIcon":"http://192.168.0.149:8080/img/bankIcon/icon_PSBC.png"},{"bankId":"PAB","bankName":"平安银行","bankLimit":"本次支付上限20万，单日上限50万","bankIcon":"http://192.168.0.149:8080/img/bankIcon/icon_PAB.png"},{"bankId":"BCOM","bankName":"交通银行","bankLimit":"本次支付上限20万，单日上限20万","bankIcon":"http://192.168.0.149:8080/img/bankIcon/icon_BCOM.png"},{"bankId":"CIB","bankName":"兴业银行","bankLimit":"本次支付上限5万，单日上限5万","bankIcon":"http://192.168.0.149:8080/img/bankIcon/icon_CIB.png"},{"bankId":"SPDB","bankName":"浦发银行","bankLimit":"本次支付上限5万，单日上限30万","bankIcon":"http://192.168.0.149:8080/img/bankIcon/icon_SPDB.png"},{"bankId":"CEB","bankName":"光大银行","bankLimit":"本次支付上限20万，单日上限50万","bankIcon":"http://192.168.0.149:8080/img/bankIcon/icon_CEB.png"},{"bankId":"CITIC","bankName":"中信银行","bankLimit":"本次支付上限1万，单日上限1万","bankIcon":"http://192.168.0.149:8080/img/bankIcon/icon_CITIC.png"},{"bankId":"CMBC","bankName":"民生银行","bankLimit":"本次支付上限20万，单日上限50万","bankIcon":"http://192.168.0.149:8080/img/bankIcon/icon_CMBC.png"},{"bankId":"GDB","bankName":"广发银行","bankLimit":"本次支付上限20万，单日上限50万","bankIcon":"http://192.168.0.149:8080/img/bankIcon/icon_GDB.png"},{"bankId":"CMB","bankName":"招商银行","bankLimit":"本次支付上限1000，单日上限2万","bankIcon":"http://192.168.0.149:8080/img/bankIcon/icon_CMB.png"}]
	 */

	@SerializedName("rmg")
	private String rmgX;
	private String id;
	private String bankId;
	private String realName;
	private String cardNo;
	private String branch;
	private String status;
	private String idNo;
	private String registerTime;
	private String noIcon;
	private String phone;
	private List<BankCardListBean> bankCardList;
	private String realStatus;

	public String getRealStatus(){
		return realStatus;
	}

	public void setRealStatus(String realStatus){
		this.realStatus=realStatus;
	}

	public Object getRmgX() {
		return rmgX;
	}

	public void setRmgX(String rmgX) {
		this.rmgX = rmgX;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getBankId() {
		return bankId;
	}

	public void setBankId(String bankId) {
		this.bankId = bankId;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getCardNo() {
		return cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}

	public Object getBranch() {
		return branch;
	}

	public void setBranch(String branch) {
		this.branch = branch;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getIdNo() {
		return idNo;
	}

	public void setIdNo(String idNo) {
		this.idNo = idNo;
	}

	public String getRegisterTime() {
		return registerTime;
	}

	public void setRegisterTime(String registerTime) {
		this.registerTime = registerTime;
	}

	public Object getNoIcon() {
		return noIcon;
	}

	public void setNoIcon(String noIcon) {
		this.noIcon = noIcon;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public List<BankCardListBean> getBankCardList() {
		return bankCardList;
	}

	public void setBankCardList(List<BankCardListBean> bankCardList) {
		this.bankCardList = bankCardList;
	}

	public static class BankCardListBean implements Serializable{
		/**
		 * bankId : ICBC
		 * bankName : 工商银行
		 * bankLimit : 本次支付上限5万，单日支付上限5万
		 * bankIcon : http://192.168.0.149:8080/img/bankIcon/icon_ICBC.png
		 */

		private String bankId;
		private String bankName;
		private String bankLimit;
		private String bankIcon;
		public boolean isCheck;

		public String getBankId() {
			return bankId;
		}

		public void setBankId(String bankId) {
			this.bankId = bankId;
		}

		public String getBankName() {
			return bankName;
		}

		public void setBankName(String bankName) {
			this.bankName = bankName;
		}

		public String getBankLimit() {
			return bankLimit;
		}

		public void setBankLimit(String bankLimit) {
			this.bankLimit = bankLimit;
		}

		public String getBankIcon() {
			return bankIcon;
		}

		public void setBankIcon(String bankIcon) {
			this.bankIcon = bankIcon;
		}
	}
}
