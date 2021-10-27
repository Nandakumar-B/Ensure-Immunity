package com.vaccine;

import android.content.*;
import android.database.*;
import android.os.Build;
import android.telephony.*;
import android.widget.*;
import java.io.*;
import java.text.*;
import java.util.*;
import org.json.*;

import android.icu.text.SimpleDateFormat;

import androidx.annotation.RequiresApi;

public class CompareReceiver extends BroadcastReceiver
{
	private DatabaseHelper helper;
	private Cursor c;
	private Context mCtx;
	private String ms2=" your child on ";
	private String ms1=",\n This is from xyz hospital.  We inform you to vaccinate ";
	@RequiresApi(api = Build.VERSION_CODES.N)
	@Override
	public void onReceive(Context ctx, Intent in)
	{
		this.mCtx=ctx;
		message("success!");
		//ctx.startService(new Intent(ctx,Compare.class));
		try
		{
			helper = new DatabaseHelper(mCtx);
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
						if(addDate(c.getString(2),job.getInt("period")).contains(addDate(currentDate(),1))){
							SmsManager.getDefault().sendTextMessage(c.getString(3),null,"Hi "+c.getString(1)+ms1+" ( "+job.getString("name")+" ) "+ms2+addDate(currentDate(),1),null,null);
							message("Message sending to : "+c.getString(1));
						}
					}
				}
			}
		} catch (Exception e)
		{
			message(e.getMessage());
		}
	}
	private String getJsonAssets()
	{
		String json=null;
		try
		{
			InputStream is=mCtx.getAssets().open("data.json");
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
	@RequiresApi(api = Build.VERSION_CODES.N)
	private String addDate(String dt, int dr){
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
	@RequiresApi(api = Build.VERSION_CODES.N)
	private String currentDate(){
		SimpleDateFormat date_format=new SimpleDateFormat("dd/MM/yyyy",Locale.getDefault());
		return date_format.format(new Date());
	}
	private void message(String m)
	{
		Toast.makeText(mCtx, m, Toast.LENGTH_LONG).show();
	}
}

