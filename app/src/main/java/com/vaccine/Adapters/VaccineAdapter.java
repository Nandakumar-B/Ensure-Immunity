package com.vaccine.Adapters;

import android.content.*;
import android.view.*;
import android.view.animation.*;
import android.widget.*;
import com.vaccine.Models.*;
import com.vaccine.*;
import java.util.*;
import android.graphics.*;

public class VaccineAdapter extends ArrayAdapter<Vaccine>
{
	Typeface tp;
	private Context mCtx;
	private ArrayList<Vaccine> mList;
	private int mRes;
	
	public VaccineAdapter(Context c,int r,ArrayList<Vaccine> l){
		super(c,r,l);
		this.mCtx=c;
		this.mList=l;
		this.mRes=r;
	}

	@Override
	public View getView(int position, View v, ViewGroup parent)
	{
	     String name=getItem(position).getName();
		 String per=getItem(position).getPer();
		 
	   	 tp=Typeface.createFromAsset(mCtx.getAssets(),"Montserrat-Regular.ttf");
		 new Vaccine(name,per);
		 ViewHolder vh;
		 if(v==null){
		   v=LayoutInflater.from(mCtx).inflate(R.layout.vaccine_list,parent,false);
		   vh=new ViewHolder(v);
		   v.setTag(vh);
		 }
		 else{
			 vh=(ViewHolder)v.getTag();
		 }
		 vh.tName.setTypeface(Typeface.createFromAsset(mCtx.getAssets(),"Montserrat-Bold.ttf"));
		 vh.tPer.setTypeface(tp);
		 vh.pan.startAnimation(AnimationUtils.loadAnimation(mCtx,R.anim.zoom_in));
		 vh.tName.setText(name);
		 int d=Integer.parseInt(per);
		 if(d<=31){
			 if(d==1){
				 vh.tPer.setText("after "+per+" day");
			 }
			 else{
				 vh.tPer.setText("after "+per+" days");
			 }
		 }
		 else if(d>=31&&d<365.25){
			 if(d==31){
				 vh.tPer.setText("after "+per+" month");
			 }
			 else{
				 vh.tPer.setText("after "+String.valueOf(d/31)+" months");
			 }
		 }
		 else if(d>=365.25){
			 if(d==365.25){
				 vh.tPer.setText("after "+per+" year");
			 }
			 else{
				 vh.tPer.setText("after "+per+" years");
			 }
		 }
		 
		return v;
	}
	static class ViewHolder{
		RelativeLayout pan;
		TextView tName;
		TextView tPer;
		ViewHolder(View v){
		 pan=v.findViewById(R.id.vaccine_list_pan);
		 tName=v.findViewById(R.id.txt_vaccine);
		 tPer=v.findViewById(R.id.txt_period);
	    }
	}
}
