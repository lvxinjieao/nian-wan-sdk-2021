package com.mini.sdk.entity;

public class UserLogin {
	/** 登陆返回用户ID */
	private String accountId;
	/** 用户名 */
	private String account;
	/** 密码 */
	private String password;
	/** 登陆成功返回字符串 */
	private int code;
	/** 登陆结果提示信息 */
	private String message;
	/** 登录token */
	private String token;
	/** 登录签名 */
	private String sign;
	///////////////////////////////////////////////////////////////////////
	/** 用户昵称 */
	private String nikeName;
	/** 平台币余额 */
	private float platformMoney = 0;
	/** 平台币余额 */
	private float bindPtbMoney;
	/** 手机号 */
	private String phoneNumber;
	/** 邮箱 */
	private String email;
	private String id;
	private String age_status;
	private String real_name;
	private String idcard;
	/** 0游客 1帐号 2手机 */
	private int userRegisteType = -1;
	
    public String getUserPtb() {
        return getPlatformMoney() + "";
    }
	
	public String getNikeName() {
		return nikeName;
	}
	public void setNikeName(String nikeName) {
		this.nikeName = nikeName;
	}
	public float getPlatformMoney() {
		return platformMoney;
	}
	public void setPlatformMoney(float platformMoney) {
		this.platformMoney = platformMoney;
	}
	public float getBindPtbMoney() {
		return bindPtbMoney;
	}
	public void setBindPtbMoney(float bindPtbMoney) {
		this.bindPtbMoney = bindPtbMoney;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getAge_status() {
		return age_status;
	}
	public void setAge_status(String age_status) {
		this.age_status = age_status;
	}
	public String getReal_name() {
		return real_name;
	}
	public void setReal_name(String real_name) {
		this.real_name = real_name;
	}
	public String getIdcard() {
		return idcard;
	}
	public void setIdcard(String idcard) {
		this.idcard = idcard;
	}
	public int getUserRegisteType() {
		return userRegisteType;
	}
	public void setUserRegisteType(int userRegisteType) {
		this.userRegisteType = userRegisteType;
	}
	
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public String getAccountId() {
		return accountId;
	}
	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}

 
 

}
