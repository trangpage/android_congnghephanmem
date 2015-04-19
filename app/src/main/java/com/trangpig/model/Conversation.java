package com.trangpig.model;

import java.util.ArrayList;
import java.util.List;

public class Conversation {
	private long idCon;
	private List<Friend> friends;
	private List<MessageChat> listMes;

    public Conversation(long idCon, List<Friend> friends, List<MessageChat> listMes) {
        this.idCon = idCon;
        this.friends = friends;
        this.listMes = listMes;
    }

    public Conversation() {
		friends = new ArrayList<Friend>();
		listMes = new ArrayList<MessageChat>();
	}
	public long getIdCon() {
		return idCon;
	}
	public void setIdCon(long idCon) {
		this.idCon = idCon;
	}
	public List<Friend> getFriends() {
		return friends;
	}
	public void setFriends(List<Friend> friends) {
		this.friends = friends;
	}
	public List<MessageChat> getListMes() {
		return listMes;
	}
	public void setListMes(List<MessageChat> listMes) {
		this.listMes = listMes;
	}
	public String getTen(){
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < friends.size(); i++){
            s.append(friends.get(i).getName()+",");
        }
        s.substring(0,s.length()-1);
        return new String(s);
    }
}
