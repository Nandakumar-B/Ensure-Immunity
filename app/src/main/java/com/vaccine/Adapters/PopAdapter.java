package com.vaccine.Adapters;

import android.widget.*;
import android.view.*;
import com.vaccine.Models.*;
import java.util.*;
import com.vaccine.*;
import android.graphics.*;
import android.content.*;

public class PopAdapter extends BaseAdapter
{
	private List<Pop> mList;
	private Context mCtx;
	Typeface tp;
	
	public PopAdapter(Context c,List<Pop> list){
		this.mList=list;
		this.mCtx=c;
	}

	@Override
	public int getCount()
	{
		return mList.size();
	}

	@Override
	public Pop getItem(int p1)
	{
		return mList.get(p1);
	}

	@Override
	public long getItemId(int p1)
	{
		return 0;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent)
	{
		tp=Typeface.createFromAsset(mCtx.getAssets(),"Montserrat-Regular.ttf");
		ViewHolder h;
		if(view==null){
			view=LayoutInflater.from(parent.getContext()).inflate(R.layout.popup_list,null);
			h=new ViewHolder(view);
			view.setTag(h);
		}
		else{
			h=(ViewHolder)view.getTag();
		}
		h.tv.setTypeface(tp);
		h.im.setImageResource(getItem(position).getImg());
		h.tv.setText(getItem(position).getTitle());
		return view;
	}
    static class ViewHolder{
		public ImageView im;
		public TextView tv;
		ViewHolder(View v){
			im=v.findViewById(R.id.img);
			tv=v.findViewById(R.id.txt);
		}
	}
}
