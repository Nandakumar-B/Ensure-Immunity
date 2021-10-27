package com.vaccine;

import android.app.*;
import android.content.*;
import android.database.*;
import android.os.*;
import android.widget.*;
import java.io.*;
import java.text.*;
import java.util.*;
import org.json.*;
import android.telephony.*;

public class Compare extends Service
{
	private DatabaseHelper helper;
	private Cursor c;
	
	@Override
	public IBinder onBind(Intent p1)
	{
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId)
	{
		message("service called");
	    try
		{
			helper = new DatabaseHelper(getApplicationContext());
			c = helper.get_data();
			if (c.getCount() != 0)
			{
				while (c.moveToNext())
				{
					JSONObject ob=new JSONObject(getJsonAssets());
					JSONArray ar=ob.getJSONArray("users");
					for (int i=0;i < ar.length();i++)
					{
						JSONObject job=ar.getJSONObject(i);
						//message(addDate(c.getString(2),job.getInt("period"))+" = "+addDate(currentDate(),1));
						if(addDate(c.getString(2),job.getInt("period")).contains(addDate(currentDate(),1))){
							//SmsManager.getDefault().sendTextMessage(c.getString(3),null,"vaccination : "+job.getString("name")+"\n date : "+addDate(currentDate(),1),null,null);
							message("Message sent to : "+c.getString(1));
						}
					}
//					while(a<ar.length()){
//						JSONObject job=ar.getJSONObject(a);
//						message(addDate(c.getString(2),job.getInt("period"))+" = "+addDate(currentDate(),1));
//						a++;
//					}
				}
			}
		} catch (Exception e)
		{
			message(e.getMessage());
		}
		return START_STICKY;
	}

	@Override
	public void onStart(Intent intent, int startId)
	{
		message("service start");
		return;
	}

	@Override
	public void onDestroy()
	{
		super.onDestroy();
	}
	private String getJsonAssets()
	{
		String json=null;
		try
		{
			InputStream is=this.getAssets().open("data.json");
			int size=is.available();
			byte[] buffer=new byte[size];
			is.read(buffer);
			is.close();
			json = new String(buffer, "UTF-8");
		}
		catch (Exception e)
		{
			message(e.getMessage());
		}
		return json;
	}
	private int getDates(String fm){
		int val=0;
		try{
			SimpleDateFormat format=new SimpleDateFormat("dd/MM/yyyy");
			Date dt=format.parse(fm);
		    val=dt.getDate();
		}catch(ParseException e){
			message(e.getMessage());
		}
		return val;
	}
	private String addDate(String dt,int dr){
		//String dt = "2012-01-04";  // Start date
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		Calendar c = Calendar.getInstance();
		try {
			c.setTime(sdf.parse(dt));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		c.add(Calendar.DATE, dr);  // number of days to add, can also use Calendar.DAY_OF_MONTH in place of Calendar.DATE
		SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy");
		String output = sdf1.format(c.getTime());
		return output;
	}
	private String currentDate(){
		SimpleDateFormat date_format=new SimpleDateFormat("dd/MM/yyyy",Locale.getDefault());
		return date_format.format(new Date());
	}
	private void message(String m)
	{
		Toast.makeText(getApplicationContext(), m, Toast.LENGTH_LONG).show();
	}
}
