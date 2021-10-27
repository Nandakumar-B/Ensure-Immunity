package com.vaccine.Models;

public class Details
{
	private String mName;
	private String mDate;
	private String mMail;
	
	public Details(String name,String date,String mail){
		this.mName=name;
		this.mDate=date;
		this.mMail=mail;
	}
	public void setName(String name){
		this.mName=name;
	}
	public String getName(){
		return mName;
	}
	public void setDay(String date){
		this.mDate=date;
	}
	public String getDay(){
		return mDate;
	}
	public void setMail(String mail){
		this.mMail=mail;
	}
	public String getMail(){
		return mMail;
	}
}
