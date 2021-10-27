package com.vaccine.Models;

public class User
{
	private String id;
	private String name;
	private String date;
	private String phone;
	
	public User(String id,String name,String date,String phone){
		this.id=id;
		this.name=name;
		this.date=date;
		this.phone=phone;
	}
	public void setId(String id){
		this.id=id;
	}
	public String getId(){
		return id;
	}
	public void setName(String name){
		this.name=name;
	}
	public String getName(){
		return name;
	}
	public void setDates(String date){
		this.date=date;
	}
	public String getDates(){
		return date;
	}
	public void setPhone(String phone){
		this.phone=phone;
	}
	public String getPhone(){
		return phone;
	}
}
