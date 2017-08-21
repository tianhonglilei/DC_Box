package com.example.zzq.bean;

/**
 * ��Ʒʵ����
 * 
 * @author wang
 * 
 */
public class ConfigInfo {
	private int proid;// ��Ʒid
	private int hid; // ����
	private int hgid; // ����
	private int max;// �����������
	private int mfinish;//���˶�����Ʒ����
	private String logo;// ��Ʒͼ��
	private String name;// ��Ʒ����
	private String haveNum; // ֻ���˶��ٻ�

	/** �ĸ������Ĺ��� ��Ʒid ,���� ,��Ʒ����, ��Ʒͼ�� ,��Ʒ���� */
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

	/** �޲ι��� */
	public ConfigInfo() {
		super();
	}

	/** ���������Ĺ��� ��������Ʒͼ�� */
	public ConfigInfo(int hid, String logo) {
		super();
		this.hid = hid;
		this.logo = logo;
	}

	/** ���������Ĺ��� ��������Ʒͼ�� */
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
