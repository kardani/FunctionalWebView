package com.kardani.fwebview;


import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.util.Date;

public class SplashActivity extends AppCompatActivity {
	
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {

//		setTheme(R.style.HoloIran14);

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);


		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
			getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
		}
		//changing statusbar color and navigationbar color
		if (Build.VERSION.SDK_INT >= 21) {
			Window window = this.getWindow();
			window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
			window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			window.setStatusBarColor(this.getResources().getColor(R.color.app_base_color));
			window.setNavigationBarColor(ContextCompat.getColor(this, R.color.app_base_color));
		}


		////////////////////////////////////////////

		StartAnimations();


		//////////////////////////////////////////////


//		Typeface fontIRANSans = Typeface.createFromAsset(getAssets(), "fonts/IRANSans.ttf");

//		TextView txtLabel = (TextView) findViewById(R.id.txtLabel);
//		txtLabel.setTypeface(fontIRANSans);



		new DoStuffAsync().execute();


	}

	private void StartAnimations() {

        Animation anim_ma1 = AnimationUtils.loadAnimation (this, R.anim.anim_ma1);
        ImageView imgLogoMa1 = (ImageView) findViewById(R.id.imgLogoMa1);
		imgLogoMa1.clearAnimation();
		imgLogoMa1.startAnimation(anim_ma1);

		Animation anim_ma2 = AnimationUtils.loadAnimation (this, R.anim.anim_ma2);
		ImageView imgLogoMa2 = (ImageView) findViewById(R.id.imgLogoMa2);
		imgLogoMa2.clearAnimation();
		imgLogoMa2.startAnimation(anim_ma2);

		Animation anim_na = AnimationUtils.loadAnimation (this, R.anim.anim_na);
		ImageView imgLogoNa = (ImageView) findViewById(R.id.imgLogoNa);
		imgLogoNa.clearAnimation();
		imgLogoNa.startAnimation(anim_na);

//		Animation anim_plus = AnimationUtils.loadAnimation (this, R.anim.anim_plus);
//		ImageView imgLogoPlus = (ImageView) findViewById(R.id.imgLogoPlus);
//		imgLogoPlus.clearAnimation();
//		imgLogoPlus.startAnimation(anim_plus);

	}

	private class DoStuffAsync extends AsyncTask<Void, Integer, Long> {

		@Override
		protected Long doInBackground(Void... arg0) {

	        long start = new Date().getTime();

	        // copy assents

	        long end = new Date().getTime();
	        try {
	        	if ( end-start < 2800 ){

					Thread.sleep( 2800-(end-start));
	        	}


			} catch (InterruptedException e) {

				e.printStackTrace();
			}

	         return null;
	     }

	     protected void onPostExecute(Long result) {


	         finish();
	     }

	 }

	@Override
	public void onBackPressed() {

	}

}
