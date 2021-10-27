package com.vaccine;

import android.*;
import android.app.*;
import android.content.*;
import android.content.pm.*;
import android.database.*;
import android.graphics.*;
import android.net.*;
import android.os.*;
import android.provider.*;
import android.view.*;
import android.view.animation.*;
import com.vaccine.Adapters.*;
import com.vaccine.Models.*;
import java.io.*;
import java.text.*;
import java.util.*;
import me.anwarshahriar.calligrapher.*;
import org.json.*;

import java.text.ParseException;
import android.util.*;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListPopupWindow;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.SearchView;

public class MainActivity extends Activity 
{
	EditText ename,edob,ephone;
	ImageButton mAddBtn,mClkBtn,mListBtn,mCloseBtn,mSchBtn;
	TextView thead,tinfo,vinfo,dinfo,h1,h2,h3;
	Button btnAdd;
	ListView lv;
	GridView slv,dlv;
	SearchView sv;
	LinearLayout mainPan,secPan,detPan;

	DatabaseHelper helper;
	UserAdapter udp;
	VaccineAdapter vdp;
	DetailAdapter ddp;
	AlarmHandler ah;

	Calligrapher call;
	Typeface tp;
	Animation sr,sl,mup,mdn,zin,zout;
	AlertDialog dialog;

	private ArrayList<User> list;
	private ArrayList<Vaccine> vlist;
	private ArrayList<Details> dlist;

	private boolean isSearch=false;
	private boolean isDetSearch=false;
	private boolean isMain=true;
	private boolean isList=false;
	private boolean isDet=false;
	private boolean press=false;

	@Override
	public void onBackPressed()
	{
		if (press)
		{
			super.onBackPressed();
			return;
		}
		press = true;
		message("Press back once more");
		if (isSearch)
		{
			sv.startAnimation(sl);
			sv.setVisibility(View.GONE);
			tinfo.setText("");
			vinfo.setText("");
			dinfo.setText("");
			isSearch = false;
			readUserData();
		}
		new Handler().postDelayed(new Runnable(){
				@Override
				public void run()
				{
					press = false;
				}
			}, 2000);
	}

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

		smsPermission();

        ah = new AlarmHandler(this);

	    mAddBtn = findViewById(R.id.menu_btn_add);
		mClkBtn = findViewById(R.id.menu_btn_clock);
		mListBtn = findViewById(R.id.menu_btn_list);
		mCloseBtn = findViewById(R.id.menu_btn_close);
		mSchBtn = findViewById(R.id.menu_btn_search);
		lv = findViewById(R.id.mainListView);
		slv = findViewById(R.id.secondListView);
		dlv = findViewById(R.id.detailListView);
		sv = findViewById(R.id.mainSearchView);
		mainPan = findViewById(R.id.main_pan);
		secPan = findViewById(R.id.second_pan);
		detPan = findViewById(R.id.detail_pan);
		tinfo = findViewById(R.id.txt_list_info);
		vinfo = findViewById(R.id.txt_vac_info);
		dinfo = findViewById(R.id.txt_det_info);
		h1 = findViewById(R.id.txt_head1);
		h2 = findViewById(R.id.txt_head2);
		h3 = findViewById(R.id.txt_head3);

		sr = AnimationUtils.loadAnimation(this, R.anim.scale_right);
		sl = AnimationUtils.loadAnimation(this, R.anim.scale_left);
		mup = AnimationUtils.loadAnimation(this, R.anim.move_up);
		mdn = AnimationUtils.loadAnimation(this, R.anim.move_down);
		zin = AnimationUtils.loadAnimation(this, R.anim.zoom_in);
		zout = AnimationUtils.loadAnimation(this, R.anim.zoom_out);
		mainPan.startAnimation(mup);
		mClkBtn.startAnimation(zin);
		mListBtn.startAnimation(zin);
		mSchBtn.startAnimation(zin);

		tp = Typeface.createFromAsset(getAssets(), "Montserrat-Regular.ttf");
		call = new Calligrapher(this);
		call.setFont(this, "Montserrat-Bold.ttf", true);
		helper = new DatabaseHelper(this);
		readUserData();
		readJson();

		try
		{
			checkTimer();
		}
		catch (Exception e)
		{
			message(e.getMessage());
			try
			{
				FileOutputStream out=openFileOutput("timer", MODE_PRIVATE);
				OutputStreamWriter writer=new OutputStreamWriter(out);
				writer.write("on");		
				writer.close();
				checkTimer();
			}
			catch (Exception ee)
			{
				message(ee.getMessage());
			}
		}
		zout.setAnimationListener(new Animation.AnimationListener(){

				@Override
				public void onAnimationStart(Animation p1)
				{}
				@Override
				public void onAnimationEnd(Animation p1)
				{
					mAddBtn.clearAnimation();
					mAddBtn.setVisibility(View.INVISIBLE);
					if (isDet)
					{
						mClkBtn.clearAnimation();
						mListBtn.clearAnimation();
						mClkBtn.setVisibility(View.INVISIBLE);
						mListBtn.setVisibility(View.INVISIBLE);
						isDet = false;
					}
				}
				@Override
				public void onAnimationRepeat(Animation p1)
				{}

			});
		mListBtn.setOnClickListener(new View.OnClickListener(){

				@Override
				public void onClick(View p1)
				{
					if (!isList)
					{
						isMain = false;
						h1.clearAnimation();
						h1.setVisibility(View.INVISIBLE);
						h2.startAnimation(sr);
						h2.setText("VACCINE");
						h2.setVisibility(View.VISIBLE);
						mainPan.setVisibility(View.GONE);
						mListBtn.setImageResource(R.drawable.hide_list_icon);
						mAddBtn.startAnimation(zout);
						mAddBtn.setVisibility(View.INVISIBLE);
						secPan.setVisibility(View.VISIBLE);
						secPan.startAnimation(mup);
						isList = true;
					}
					else
					{
						isMain = true;
						h2.clearAnimation();
						h1.startAnimation(sr);
						h1.setVisibility(View.VISIBLE);
						h2.setText("");
						h2.setVisibility(View.INVISIBLE);
						secPan.setVisibility(View.GONE);
						mListBtn.setImageResource(R.drawable.show_list_icon);
						mAddBtn.setVisibility(View.VISIBLE);
						mAddBtn.startAnimation(zin);
						mainPan.setVisibility(View.VISIBLE);
						mainPan.startAnimation(mup);
						isList = false;
					}
				}


			});
		mClkBtn.setOnClickListener(new View.OnClickListener(){
				@Override 
				public void onClick(View v)
				{
					try
					{

						if (getTimer() == null || getTimer().equals("off"))
						{
							FileOutputStream out=openFileOutput("timer", MODE_PRIVATE);
							OutputStreamWriter writer=new OutputStreamWriter(out);
							writer.write("on");		
							writer.close();
							checkTimer();
							ah.setAlarmManager();
						}
						else
						{
							FileOutputStream out=openFileOutput("timer", MODE_PRIVATE);
							OutputStreamWriter writer=new OutputStreamWriter(out);
							writer.write("off");
							writer.close();
							checkTimer();
							ah.cancelAlarmManager();
						}
					}
					catch (Exception e)
					{
						message(e.getMessage());
					}
				}
			});
		mCloseBtn.setOnClickListener(new View.OnClickListener(){

				@Override
				public void onClick(View p1)
				{
					if (!isDet)
					{
						isMain = true;
						mCloseBtn.clearAnimation();
						detPan.clearAnimation();
						h3.clearAnimation();
						h1.startAnimation(sr);
						h2.startAnimation(sr);
						h1.setVisibility(View.VISIBLE);
						h2.setVisibility(View.VISIBLE);
						mCloseBtn.setVisibility(View.GONE);
						detPan.setVisibility(View.GONE);
						h3.setVisibility(View.GONE);
						mAddBtn.setVisibility(View.VISIBLE);
						mClkBtn.setVisibility(View.VISIBLE);
						mListBtn.setVisibility(View.VISIBLE);
						mainPan.setVisibility(View.VISIBLE);
						mainPan.startAnimation(mup);
						mAddBtn.startAnimation(zin);
						mClkBtn.startAnimation(zin);
						mListBtn.startAnimation(zin);
					}
				}

			});
		mSchBtn.setOnClickListener(new View.OnClickListener(){

				@Override
				public void onClick(View p1)
				{
					if (!isSearch)
					{
						isSearch = true;
						sv.setVisibility(View.VISIBLE);
						sv.startAnimation(sr);
						sv.setIconified(false);
					}
					else
					{
						isSearch = false;
						sv.startAnimation(sl);
						sv.setVisibility(View.GONE);
					}
				}


			});
		lv.setOnItemLongClickListener(new ListView.OnItemLongClickListener(){
			@Override
				public boolean onItemLongClick(AdapterView<?> p1, View p2, int p3, long p4)
				{
					try
					{
						//lv.setItemChecked(p3,true);
						isMain = false;
					    showPop(p2);
					}
					catch (Exception e)
					{
						message(e.getMessage());
					}
					return false;
				}


			});
//		lv.setOnItemClickListener(new ListView.OnItemClickListener(){
//
//				@Override
//				public void onItemClick(AdapterView<?> p1, View p2, int p3, long p4)
//				{
//					
//			});
		sv.setOnCloseListener(new SearchView.OnCloseListener(){

				@Override
				public boolean onClose()
				{
					isSearch = false;
					sv.startAnimation(sl);
					//shBtn.setVisibility(View.VISIBLE);
					sv.setVisibility(View.GONE);
					tinfo.setText("");
					vinfo.setText("");
					dinfo.setText("");
					readUserData();
					return false;
				}
			});
		sv.setOnQueryTextListener(new SearchView.OnQueryTextListener(){

				@Override
				public boolean onQueryTextSubmit(String p1)
				{
					return false;
				}
				@Override
				public boolean onQueryTextChange(String p1)
				{
					if (isMain)
					{
						ArrayList<User> l=new ArrayList<>();
						for (User u:list)
						{
							if (u.getName().toUpperCase().contains(p1.toUpperCase()))
							{
								l.add(u);
							}
							else if (u.getPhone().contains(p1))
							{
								l.add(u);
							}
							else if (u.getDates().contains(p1))
							{
								l.add(u);
							}
						}
						if (l.size() == 0)
						{
							tinfo.setText(R.string.no_matches);
						}
						else if (l.size() == 1)
						{
							tinfo.setText(l.size() + " item matched");
						}
						else
						{
							tinfo.setText(l.size() + " items matched");
						}
						udp = new UserAdapter(MainActivity.this, R.layout.custom_list, l);
						lv.setAdapter(udp);
					}
					if (isList)
					{
						ArrayList<Vaccine> vl=new ArrayList<>();
						for (Vaccine v:vlist)
						{
							if (v.getName().toUpperCase().contains(p1.toUpperCase()))
							{
								vl.add(v);
							}
							else if (v.getPer().contains(p1))
							{
								vl.add(v);
							}
						}
						if (vl.size() == 0)
						{
							vinfo.setText("No matches found!");
						}
						else if (vl.size() == 1)
						{
							vinfo.setText(vl.size() + " item matched");
						}
						else
						{
							vinfo.setText(vl.size() + " items matched");
						}
						vdp = new VaccineAdapter(MainActivity.this, R.layout.vaccine_list, vl);
						slv.setAdapter(vdp);
					}
					if (isDetSearch)
					{
						ArrayList<Details> dtl=new ArrayList<>();
						for (Details a:dlist)
						{
							if (a.getName().toUpperCase().contains(p1.toUpperCase()))
							{
								dtl.add(a);
							}
							else if (a.getDay().contains(p1))
							{
								dtl.add(a);
							}
						}
						if (dtl.size() == 0)
						{
							dinfo.setText("No items matched");
						}
						else if (dtl.size() == 1)
						{
							dinfo.setText(dtl.size() + " item matched");
						}
						else
						{
							dinfo.setText(dtl.size() + " items matched");
						}
						ddp = new DetailAdapter(MainActivity.this, R.layout.detail_list, dtl);
						dlv.setAdapter(ddp);
					}
					return true;
				}
		    });
//		AlarmHandler ah=new AlarmHandler(this);
//		ah.cancelAlarmManager();
//		ah.setAlarmManager();
//		Toast.makeText(this,"Alarm set",Toast.LENGTH_SHORT).show();

        createDialog();
		mAddBtn.setOnClickListener(new View.OnClickListener(){
				@Override
				public void onClick(View v)
				{
					clearForm();
					btnAdd.setText("ADD");
					btnAdd.setOnClickListener(new Button.OnClickListener(){

							@Override
							public void onClick(View p1)
							{
								if (s(ename).isEmpty() || s(ename).startsWith(" ") ||
									s(edob).isEmpty() || s(edob).startsWith(" ") ||
									s(ephone).isEmpty() || !Patterns.PHONE.matcher(s(ephone)).matches())
								{
									message("Please enter valid details!");
								}
								else
								{
									try
									{
										addUserData(s(ename), s(edob), s(ephone));
									}
									catch (Exception e)
									{
										message(e.getMessage());
									}
								}
							}

						});
					dialog.show();
				}
		    });
	}
	private void checkTimer()
	{
		if (getTimer() != null)
		{
			if (getTimer().equals("on"))
			{
				mClkBtn.setImageResource(R.drawable.timer_on_icon);
			}
			else
			{
				mClkBtn.setImageResource(R.drawable.timer_off_icon);
			}
		}
		else
		{
			try
			{
				FileOutputStream out=openFileOutput("timer", MODE_PRIVATE);
				OutputStreamWriter writer=new OutputStreamWriter(out);
				writer.write("off");		
				writer.close();
				checkTimer();
			}
			catch (IOException e)
			{
				message(e.getMessage());
			}
		}
	}
	private String getTimer()
	{
		String time=null;
		try
		{
			FileInputStream inp=openFileInput("timer");
			InputStreamReader reader=new InputStreamReader(inp);
			BufferedReader buf=new BufferedReader(reader);
			StringBuilder builder=new StringBuilder();
			if ((time = buf.readLine()) != null)
			{
				builder.append(time);
			}
			inp.close();
			reader.close();
		}
		catch (IOException e)
		{
			message(e.getMessage());
		}
		return time;
	}
	private void showPop(final View v)
	{
		final TextView td=v.findViewById(R.id.txt_id);
		final TextView tn=v.findViewById(R.id.txt_name);
		final TextView tb=v.findViewById(R.id.txt_dob);
		final TextView tp=v.findViewById(R.id.txt_phone);
		List<Pop> pList=new ArrayList<>();
		pList.add(new Pop("VIEW", R.drawable.ic_view));
		pList.add(new Pop("EDIT", R.drawable.ic_edit));
		pList.add(new Pop("DELETE", R.drawable.ic_delete));
		Display dsp=getWindowManager().getDefaultDisplay();
		final ListPopupWindow lp=createPop(v, dsp.getWidth()/2, pList);
		lp.setOnItemClickListener(new AdapterView.OnItemClickListener(){

				@Override
				public void onItemClick(AdapterView<?> adapter, View view, int position, long id)
				{
					TextView t=view.findViewById(R.id.txt);
					switch (t.getText().toString())
					{
						case "VIEW":
							viewClick(v);
							lp.dismiss();
							break;
						case "EDIT":
							lp.dismiss();
							updateUser(td.getText().toString(),
							           tn.getText().toString(),
									   tp.getText().toString(),
									   tb.getText().toString());
							break;
						case "DELETE":
							lp.dismiss();
							deleteUser(td.getText().toString(), tn.getText().toString());
							break;
					}
				}

			});
		lp.show();
	}
	private ListPopupWindow createPop(View v, int w, List<Pop> l)
	{
		ListPopupWindow lp=new ListPopupWindow(this);
		lp.setAnchorView(v);
		lp.setWidth(w);
		lp.setAdapter(new PopAdapter(this, l));
		return lp;
	}
	private void updateUser(final String id, String nm, String ph, String db)
	{
		ename.setText(nm);
		ephone.setText(ph);
		edob.setText(db);
		ename.setSelection(ename.getText().toString().length());
		btnAdd.setText("UPDATE");
		btnAdd.setOnClickListener(new View.OnClickListener(){
				@Override
				public void onClick(View v)
				{
					if (s(ename).isEmpty() || s(ename).startsWith(" ") ||
						!Patterns.PHONE.matcher(s(ephone)).matches())
					{
						message("Please enter valid details!");
					}
					else
					{
						boolean ed=helper.update_data(id, s(ename), s(edob), s(ephone));
						if (ed)
						{
							message("Data updated");
							readUserData();
							dialog.dismiss();
						}
						else
						{
							dialog.dismiss();
							message("Data not updated");
						}
					}
				}
			});
		dialog.show();
	}
	private void viewClick(View p2)
	{
		isDet = true;
		isDetSearch = true;
		TextView mtn=p2.findViewById(R.id.txt_name);
		TextView mtd=p2.findViewById(R.id.txt_dob);
		ImageView mi=p2.findViewById(R.id.det_mail_img);

		mainPan.clearAnimation();
		mainPan.setVisibility(View.GONE);
		h1.clearAnimation();
		h2.clearAnimation();
		h1.setVisibility(View.INVISIBLE);
		h2.setVisibility(View.INVISIBLE);
		mAddBtn.startAnimation(zout);
		mClkBtn.startAnimation(zout);
		mListBtn.startAnimation(zout);
		mCloseBtn.startAnimation(zin);
		detPan.startAnimation(mup);
		mCloseBtn.setVisibility(View.VISIBLE);		
		detPan.setVisibility(View.VISIBLE);
		h3.setVisibility(View.VISIBLE);
		h3.startAnimation(sr);
		h3.setText(mtn.getText().toString());
		readDet(mtd.getText().toString(), mi);

	}
	private void createDialog()
	{
		AlertDialog.Builder ad=new AlertDialog.Builder(MainActivity.this);
		View view=LayoutInflater.from(MainActivity.this).inflate(R.layout.activity_add, null);
		ad.setView(view);
		ad.setCancelable(true);
		dialog = ad.create();
		thead = view.findViewById(R.id.add_head);
		ename = view.findViewById(R.id.et_name);
		ephone = view.findViewById(R.id.et_phone);
		edob = view.findViewById(R.id.et_date);
		btnAdd = view.findViewById(R.id.btn_add);
		ename.setTypeface(tp);
		ephone.setTypeface(tp);
		edob.setTypeface(tp);
		thead.setTypeface(Typeface.createFromAsset(getAssets(), "Montserrat-Bold.ttf"));
		btnAdd.setTypeface(Typeface.createFromAsset(getAssets(), "Montserrat-Bold.ttf"));
		// Date picker
		final Calendar newCalendar = Calendar.getInstance();
		final DatePickerDialog  StartTime = new DatePickerDialog(MainActivity.this, new DatePickerDialog.OnDateSetListener() {
				public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
				{
					Calendar newDate = Calendar.getInstance();
					newDate.set(year, monthOfYear, dayOfMonth);
					edob.setText(new SimpleDateFormat("dd/MM/yyyy").format(newDate.getTime()));
				}

			}, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
		edob.setOnClickListener(new View.OnClickListener(){

				@Override
				public void onClick(View p1)
				{
					StartTime.show();
				}
			});

		if (dialog.getWindow() != null)
		{
			dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
			dialog.getWindow().getAttributes().windowAnimations = R.style.zoomAlert;
		}
	}
	private void addUserData(String n, String d, String p)
	{
		boolean added=helper.inert_data(n, d, p);
		if (added)
		{
			message("Data added");
			dialog.dismiss();
			clearForm();
			readUserData();
		}
		else
		{
		    message("Data not added");
		}
	}
	private void readUserData()
	{
	    list = new ArrayList<>();
	    Cursor c=helper.get_data();
		if (c.getCount() == 0)
		{
			tinfo.setText("No files found! Create new!");
			message("No files found");
		}
		else
		{
			tinfo.setText("");
			while (c.moveToNext())
			{
				User u=new User(c.getString(0), c.getString(1), c.getString(2), c.getString(3));
				list.add(u);
				udp = new UserAdapter(this, R.layout.custom_list, list);
				lv.setAdapter(udp);
			}
		}
	}
//	public void compare(View v)
//	{
//		try
//		{
//			startService(new Intent(this, Compare.class));
//		}
//		catch (IllegalArgumentException e)
//		{
//			message(e.getMessage());
//		}
//		catch (Exception e)
//		{
//			message(e.getMessage());
//		}
//	}

	private void clearForm()
	{
		ename.setText("");
		edob.setText("");
		ephone.setText("");
	}
	private void deleteUser(final String id, String name)
	{
		AlertDialog.Builder ad=new AlertDialog.Builder(this);
	    View v=getLayoutInflater().from(this).inflate(R.layout.alert_activity, null, false);
		ad.setView(v);
		final AlertDialog dia=ad.create();
		TextView ath=v.findViewById(R.id.alert_head);
		TextView atm=v.findViewById(R.id.alert_message);
		TextView aneg=v.findViewById(R.id.btn_neg);
		TextView apos=v.findViewById(R.id.btn_pos);
		ath.setTypeface(Typeface.createFromAsset(getAssets(), "Montserrat-Bold.ttf"));
		aneg.setTypeface(Typeface.createFromAsset(getAssets(), "Montserrat-Bold.ttf"));
		apos.setTypeface(Typeface.createFromAsset(getAssets(), "Montserrat-Bold.ttf"));
		atm.setText("Delete data of " + name + " ?");
		aneg.setOnClickListener(new TextView.OnClickListener(){

				@Override
				public void onClick(View p1)
				{
					dia.dismiss();
				}

			});
		apos.setOnClickListener(new TextView.OnClickListener(){

				@Override
				public void onClick(View p1)
				{
					Integer de=helper.delete_data(id);
					if (de > 0)
					{
						message("Data deleted!");
						readUserData();
						dia.dismiss();
					}
					else
					{
					    dia.dismiss();
						message("Data not deleted!");
					}
				}


			});

		if (dia.getWindow() != null)
		{
			dia.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
			dia.getWindow().getAttributes().windowAnimations = R.style.zoomAlert;
		}
		dia.show();
	}
	private void readDet(String dob, ImageView im)
	{
		try
		{
			dlist = new ArrayList<>();
			JSONObject ob=new JSONObject(getJsonAssets());
			JSONArray ar=ob.getJSONArray("users");
			for (int i=0;i < ar.length();i++)
			{
				JSONObject job=ar.getJSONObject(i);
				Details det=new Details(job.getString("name"), addDate(dob, job.getInt("period")), addDate(addDate(dob, job.getInt("period")), -1));
				dlist.add(det);
			}

			ddp = new DetailAdapter(MainActivity.this, R.layout.detail_list, dlist);
			dlv.setAdapter(ddp);
		}
		catch (JSONException e)
		{
			message(e.getMessage());
		}
		catch (Exception e)
		{
			message(e.getMessage());
		}
	}
	private void readJson()
	{
		try
		{
			vlist = new ArrayList<>();
			JSONObject ob=new JSONObject(getJsonAssets());
			JSONArray ar=ob.getJSONArray("users");
			for (int i=0;i < ar.length();i++)
			{
				JSONObject job=ar.getJSONObject(i);
				Vaccine vc=new Vaccine(job.getString("name"), job.getString("period"));
				vlist.add(vc);
			}
			vdp = new VaccineAdapter(MainActivity.this, R.layout.vaccine_list, vlist);
			slv.setAdapter(vdp);
		}
		catch (JSONException e)
		{
			message(e.getMessage());
		}
		catch (Exception e)
		{
			message(e.getMessage());
		}
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
	private String addDate(String dt, int dr)
	{
		//String dt = "2012-01-04";  // Start date
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		Calendar c = Calendar.getInstance();
		try
		{
			c.setTime(sdf.parse(dt));
		}
		catch (ParseException e)
		{
			e.printStackTrace();
		}
		c.add(Calendar.DATE, dr);  // number of days to add, can also use Calendar.DAY_OF_MONTH in place of Calendar.DATE
		SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy");
		String output = sdf1.format(c.getTime());
		return output;
	}
	private void smsPermission()
	{
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
		{
			if (checkSelfPermission(Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_DENIED)
			{
				String[] permission={Manifest.permission.SEND_SMS};
				requestPermissions(permission, 1);
			}
		}
	}

	@RequiresApi(api = Build.VERSION_CODES.M)
	@Override
	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)
	{
		if (requestCode == 1)
		{
			if (grantResults[0] == -1)
			{
				if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE))
				{
					message("Please give permission! Sms permission is required for sending messages!");
					smsPermission();
				}
				else
				{
				    message("please give permission! Sms permission is required for sending messages! ");
					Intent in=new Intent();
					in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
					in.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
					Uri uri=Uri.fromParts("package", this.getPackageName(), null);
					in.setData(uri);
					startActivity(in);
				}
			}
			else
			{
				readUserData();
			}
		}
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
	}

	@Override
	protected void onRestart()
	{
		smsPermission();
		super.onRestart();
	}


	private String s(EditText et)
	{
		return et.getText().toString();
	}
	private void message(String m)
	{
		Toast.makeText(this, m, Toast.LENGTH_LONG).show();
	}
}
