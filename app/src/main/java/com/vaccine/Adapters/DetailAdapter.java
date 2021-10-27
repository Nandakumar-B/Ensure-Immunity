package com.vaccine.Adapters;

import android.content.*;
import android.os.Build;
import android.view.*;
import android.widget.*;
import com.vaccine.*;
import com.vaccine.Models.*;
import java.util.*;
import java.text.*;
import android.view.animation.*;
import android.graphics.*;

import androidx.annotation.RequiresApi;

public class DetailAdapter extends ArrayAdapter<Details>
{
	private Typeface tp;
	private Typeface tph;
	private Context mCtx;
	private int mRes;
	private ArrayList<Details> mList;
	
	public DetailAdapter(Context c,int r,ArrayList<Details> l){
		super(c,r,l);
		this.mCtx=c;
		this.mRes=r;
		this.mList=l;
	}

	@RequiresApi(api = Build.VERSION_CODES.M)
	@Override
	public View getView(int position, View v, ViewGroup parent)
	{
		String name=getItem(position).getName();
		String day=getItem(position).getDay();
		String mail=getItem(position).getMail();
		
		tp=Typeface.createFromAsset(mCtx.getAssets(),"Montserrat-Bold.ttf");
		tph=Typeface.createFromAsset(mCtx.getAssets(),"Montserrat-Regular.ttf");
		ViewHolder vh;
		if(v==null){
		  v=LayoutInflater.from(mCtx).inflate(R.layout.detail_list,parent,false);
		  vh=new ViewHolder(v);
		  v.setTag(vh);
		}
		else{
		  vh=(ViewHolder)v.getTag();
		}
		vh.tn.setText(name);
		vh.td.setText(day);
		vh.tm.setText(mail);
		vh.tn.setTypeface(tp);
		vh.td.setTypeface(tph);
		vh.tm.setTypeface(tph);
		vh.tt.setTypeface(tph);
		float dif=Daybetween(currentDate(),day);
		if(dif>0){
		   vh.tt.setTextColor(mCtx.getColor(R.color.red));
		   vh.im.setBackgroundResource(R.drawable.ic_timer_on);
			if(dif>=365.25){
				if(dif==365.25){
					vh.tt.setText(String.valueOf((int)dif/(int)365.25)+" year left");
				}
				else{
					vh.tt.setText(String.valueOf((int)dif/(int)365.25)+" years left");
				}
			}
			else if(dif>32&&(-1*dif)<365.25){
				vh.tt.setText(String.valueOf((int)dif/31)+" months left");
			}
			else if(dif<32){
				if(dif==1){
					vh.tt.setText(String.valueOf((int)dif)+" day left");
				}else{
				    vh.tt.setText(String.valueOf((int)dif)+" days left");
				}
			}
		}
		else{
			vh.tt.setTextColor(mCtx.getColor(R.color.green));
			vh.im.setBackgroundResource(R.drawable.ic_timer_off);
			if(-1*dif>=365.25){
				if(-1*dif==365.25){
					vh.tt.setText(String.valueOf((int)(-1*dif)/(int)365.25)+" year ago");
				}
				else{
					vh.tt.setText(String.valueOf((int)(-1*dif)/(int)365.25)+" years ago");
				}
			}
			else if((-1*dif)>=31&&(-1*dif)<365.25){
				vh.tt.setText(String.valueOf((int)(-1*dif)/31)+" months ago");
			}
			else if((-1*dif)<=32){
				if(-1*dif==0){
					vh.tt.setText("today");
				}
				else if(-1*dif==1){
					vh.tt.setText(String.valueOf((int)(-1*dif))+" day ago");
				}else{
				    vh.tt.setText(String.valueOf((int)(-1*dif))+" days ago");
				}
			}
		}
		vh.pan.startAnimation(AnimationUtils.loadAnimation(mCtx,R.anim.zoom_in));
		if(Daybetween(currentDate(),mail)<=0){
			vh.mi.setBackgroundResource(R.drawable.ic_mail);
		}
		else{
			vh.mi.setBackgroundResource(R.drawable.ic_mail_send);
		}
		
		return v;
	}
	private String currentDate(){
		SimpleDateFormat date_format=new SimpleDateFormat("dd/MM/yyyy",Locale.getDefault());
		return date_format.format(new Date());
	}
	
	public long Daybetween(String date1,String date2)
	{
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		Date Date1 = null,Date2 = null;
		try{
			Date1 = sdf.parse(date1);
			Date2 = sdf.parse(date2);
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		assert Date1 != null;
		assert Date2 != null;
		return (Date2.getTime() - Date1.getTime())/(24*60*60*1000);
	}
	private void message(String m){
		Toast.makeText(mCtx,m,Toast.LENGTH_SHORT).show();
	}
	static class ViewHolder{
		RelativeLayout pan;
		TextView tn;
		TextView td;
		TextView tm;
		TextView tt;
		ImageView mi;
		ImageView im;
		
		ViewHolder(View v){
		 pan=v.findViewById(R.id.detail_list_pan);
		 tn=v.findViewById(R.id.txt_det_vaccine);
		 td=v.findViewById(R.id.txt_det_day);
		 tm=v.findViewById(R.id.txt_det_mail);
		 tt=v.findViewById(R.id.txt_det_time);
		 mi=v.findViewById(R.id.det_mail_img);
		 im=v.findViewById(R.id.det_time_img);
		}
	}
}
