package com.example.zzq.bean;

/**
 * 商品实体类
 * 
 * @author wang
 * 
 */
public class ConfigInfo {
	private int proid;// 商品id
	private int hid; // 货道
	private int hgid; // 货柜
	private int max;// 补仓最大数量
	private int mfinish;//卖了多少商品数量
	private String logo;// 商品图标
	private String name;// 商品名称
	private String haveNum; // 只补了多少货

	/** 四个参数的构造 商品id ,货道 ,商品数量, 商品图标 ,商品名称 */
	public ConfigInfo(int proid, int hid, int hgid, int max, int mfinish,String logo,
			String name, String havaNum) {
		super();
		this.proid = proid;
		this.hid = hid;
		this.hgid = hgid;
		this.max = max;
		this.mfinish = mfinish;
		this.logo = logo;
		this.name = name;
		this.haveNum = havaNum;
	}

	/** 无参构造 */
	public ConfigInfo() {
		super();
	}

	/** 两个参数的构造 货道，商品图标 */
	public ConfigInfo(int hid, String logo) {
		super();
		this.hid = hid;
		this.logo = logo;
	}

	/** 两个参数的构造 货道，商品图标 */
	public ConfigInfo(int hid, int hgid, String logo) {
		super();
		this.hid = hid;
		this.hgid = hgid;
		this.logo = logo;
	}

	public int getProid() {
		return proid;
	}

	public void setProid(int proid) {
		this.proid = proid;
	}

	public int getHid() {
		return hid;
	}

	public void setHid(int hid) {
		this.hid = hid;
	}

	public int getHgid() {
		return hgid;
	}

	public void setHgid(int hgid) {
		this.hgid = hgid;
	}


	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getHaveNum() {
		return haveNum;
	}

	public void setHaveNum(String haveNum) {
		this.haveNum = haveNum;
	}

	public int getMax() {
		return max;
	}

	public void setMax(int max) {
		this.max = max;
	}

	public int getMfinish() {
		return mfinish;
	}

	public void setMfinish(int mfinish) {
		this.mfinish = mfinish;
	}

	@Override
	public String toString() {
		return "ConfigInfo [proid=" + proid + ", hid=" + hid + ", hgid=" + hgid
				+ ", max=" + max + ", mfinish=" + mfinish + ", logo=" + logo
				+ ", name=" + name + ", haveNum=" + haveNum + "]";
	}
	
}
