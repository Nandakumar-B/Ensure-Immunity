package com.vaccine;

import android.app.*;
import android.os.*;
import android.content.*;
import me.anwarshahriar.calligrapher.*;
import android.widget.*;
import android.view.animation.*;

public class SplashActivity extends Activity
{
	Calligrapher call;
	TextView tv;
	ImageView im;
	
	Animation sp;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash_screen);
		tv=findViewById(R.id.splash_name);
		im=findViewById(R.id.splash_img);
		call=new Calligrapher(this);
		sp=AnimationUtils.loadAnimation(SplashActivity.this,R.anim.anim_splash);
		tv.startAnimation(sp);
		im.startAnimation(sp);
		call.setFont(this,"Montserrat-Bold.ttf",true);
		sp.setAnimationListener(new Animation.AnimationListener(){
				@Override
				public void onAnimationStart(Animation p1){}
				@Override
				public void onAnimationEnd(Animation p1)
				{
					Intent in=new Intent(SplashActivity.this,MainActivity.class);
					in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
					startActivity(in);
				}
				@Override
				public void onAnimationRepeat(Animation p1){}		
		});
	}
	
}
