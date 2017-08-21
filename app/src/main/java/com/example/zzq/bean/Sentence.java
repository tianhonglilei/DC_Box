package com.example.zzq.bean;

public class Sentence {
	
	private String  appid;
	private String  title;
	private String  price ;
	private String  fen ;
	private String  count ;
	private String  logo;
	private String  zhe ;
	private String   scroll ;
	private String   id ;
   private int select;
 	
	public Sentence(){
	
	}
	
	public int getSelect() {
		return select;
	}
	public void setSelect(int select) {
		this.select = select;
	}
	
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getScroll() {
		return scroll;
	}
	public void setScroll(String scroll) {
		this.scroll = scroll;
	}
	
	
	public String getZhe() {
		return zhe;
	}
	public void setZhe(String zhe) {
		this.zhe = zhe;
	}
	
	
	public String getLogo() {
		return logo;
	}
	public void setLogo(String logo) {
		this.logo = logo;
	}
	
	
	public String getCount() {
		return count;
	}
	public void setCount(String count) {
		this.count = count;
	}
	
	
	public String getFen() {
		return fen;
	}
	public void setFen(String fen) {
		this.fen = fen;
	}
	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}
	public String getAppid() {
		return appid;
	}

	public void setAppid(String appid) {
		this.appid = appid;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	
}
