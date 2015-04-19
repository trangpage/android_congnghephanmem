package com.trangpig.model;

import java.util.ArrayList;
import java.util.List;

public class Account {
	private long idAcc;
	private String name;
	private List<Friend> listFrs;
	public Account() {
		listFrs = new ArrayList<Friend>();
	}
	public Account(long idAcc, String name, List<Friend> listFrs) {
		super();
		this.idAcc = idAcc;
		this.name = name;
		this.listFrs = listFrs;
	}

	public long getIdAcc() {
		return idAcc;
	}

	public void setIdAcc(long idAcc) {
		this.idAcc = idAcc;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Friend> getListFrs() {
		return listFrs;
	}

	public void setListFrs(List<Friend> listFrs) {
		this.listFrs = listFrs;
	}

}
