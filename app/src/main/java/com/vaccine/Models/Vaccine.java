package com.vaccine.Models;

public class Vaccine
{
	private String mName;
	private String mPer;
	
	public Vaccine(String name,String per){
		this.mName=name;
		this.mPer=per;
	}
	public void setName(String name){
		this.mName=name;
	}
	public String getName(){
		return mName;
	}
	public void setPer(String per){
		this.mPer=per;
	}
	public String getPer(){
		return mPer;
	}
}
