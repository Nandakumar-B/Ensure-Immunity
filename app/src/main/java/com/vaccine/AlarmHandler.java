package com.vaccine;

import android.content.*;
import android.app.*;
import android.widget.*;

public class AlarmHandler
{
	Context context;

	public AlarmHandler(Context context){
		this.context=context;
	}
	public void setAlarmManager(){
		Intent in=new Intent(context,CompareReceiver.class);
		PendingIntent sender=PendingIntent.getBroadcast(context,2,in,0);
		AlarmManager am=(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
		if(am!=null){
			long triggerAfter=5*60*1000; //Trigger after 1 hr.
			long triggerEvery=5*60*1000; //Repeat after every 1 hr.
			am.setRepeating(AlarmManager.RTC,triggerAfter,triggerEvery,sender);
			Toast.makeText(context,"Timer started!",Toast.LENGTH_SHORT).show();
		}
	}
	public void cancelAlarmManager(){
		Intent in=new Intent(context,CompareReceiver.class);
		PendingIntent sender=PendingIntent.getBroadcast(context,2,in,0);
		AlarmManager am=(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
		if(am!=null){
			am.cancel(sender);
			Toast.makeText(context,"Timer cancelled!",Toast.LENGTH_SHORT).show();
		}
	}
}

