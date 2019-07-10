package com.nanchen.initproject;

import android.os.Bundle;

import com.nanchen.initproject.Helper.Injector;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Injector.applicationComponent().inject(this);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ButterKnife.bind(this);
	}
	
	
}
