package com.vaccine.Adapters;

import android.content.*;
import android.view.*;
import android.view.animation.*;
import android.widget.*;
import com.vaccine.Models.*;
import com.vaccine.*;
import java.util.*;
import android.graphics.*;

public class UserAdapter extends ArrayAdapter<User>
{
	Typeface tp,tpt;
	private Context mCtx;
	private int mResource;
	private ArrayList<User> mList;

	public UserAdapter(Context ctx, int resource, ArrayList<User> objects)
	{
		super(ctx, resource, objects);
		this.mCtx = ctx;
		this.mResource = resource;
		this.mList = objects;
	}

	@Override
	public int getCount()
	{
		return mList.size();
	}

	@Override
	public long getItemId(int position)
	{
		return position;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent)
	{
		String mId=getItem(position).getId();
		String mNm=getItem(position).getName();
		String mDob=getItem(position).getDates();
		String mPh=getItem(position).getPhone();

		tp=Typeface.createFromAsset(mCtx.getAssets(),"Montserrat-Bold.ttf");
		tpt=Typeface.createFromAsset(mCtx.getAssets(),"Montserrat-Regular.ttf");
		ViewHolder vh;
		new User(mId, mNm, mDob, mPh);
		if(view==null){
		    view = LayoutInflater.from(mCtx).inflate(mResource, parent, false);
			vh=new ViewHolder(view);
			view.setTag(vh);
		}
		else{
			vh=(ViewHolder)view.getTag();
		}

		vh.tid.setText(mId);
		vh.tnm.setText(mNm);
		vh.tdb.setText(mDob);
		vh.tph.setText(mPh);
		vh.tnm.setTypeface(tp);
		vh.tdb.setTypeface(tpt);
		vh.tph.setTypeface(tpt);
		vh.tnm.setSelected(true);
		vh.pan.startAnimation(AnimationUtils.loadAnimation(mCtx,R.anim.zoom_in));

		return view;
	}
	static class ViewHolder{
		RelativeLayout pan;
		TextView tid;
		TextView tnm;
		TextView tdb;
		TextView tph;
		ViewHolder(View view){
			pan=view.findViewById(R.id.custom_list_pan);
			tid = view.findViewById(R.id.txt_id);
			tnm = view.findViewById(R.id.txt_name);
			tdb = view.findViewById(R.id.txt_dob);
			tph = view.findViewById(R.id.txt_phone);
		}
	}
}
