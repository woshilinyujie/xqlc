package com.rongxun.xqlc.Beans;



public class RegUserBean extends BaseBean {

	private static final long serialVersionUID = -7683603982464447706L;

	public RegUserBean() {
		setRcd("R0001");
	}

	private Integer id;

	private Integer userId;

	private String token;

	private String userExist;
	public void setUserExist(String userExist){
		this.userExist=userExist;
	}

	public String getUserExist(){
		return userExist;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}


	public Integer getUserId()
	{
		return userId;
	}

	public void setUserId(Integer userId)
	{
		this.userId = userId;
	}

	public String getToken()
	{
		return token;
	}

	public void setToken(String token)
	{
		this.token = token;
	}
}
