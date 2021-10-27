package com.vaccine.Models;

public class Pop
{
	private String title;
	private int img;
	
	public Pop(String t,int i){
		this.title=t;
		this.img=i;
	}
	public void setTitle(String t){
		this.title=t;
	}
	public String getTitle(){
		return title;
	}
	public void setImg(int i){
		this.img=i;
	}
	public int getImg(){
		return img;
	}
}
