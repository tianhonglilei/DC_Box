package com.example.zzq.bean;

import java.util.ArrayList;
import java.util.List;

public class ProInstance {
	public List<UserInfo> proInfosList = new ArrayList<UserInfo>();
	/* ����ʵ������ */
	private static ProInstance instance = null;

	/**
	 * ˽�л��Ĺ��췽������֤�ⲿ���಻��ͨ����������ʵ������
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
