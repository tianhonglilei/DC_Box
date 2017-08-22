package com.example.zzq.bean;

import java.util.ArrayList;
import java.util.List;

public class ProInstance {
	public List<UserInfo> proInfosList = new ArrayList<UserInfo>();
	/* 单例实例对象 */
	private static ProInstance instance = null;

	/**
	 * 私有化的构造方法，保证外部的类不能通过构造器来实例化。
	 */
	private ProInstance() {

	}

	public static ProInstance getInstance() {
		if (instance == null) {
			instance = new ProInstance();
		}
		return instance;
	}

}
