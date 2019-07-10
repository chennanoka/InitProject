package com.nanchen.initproject;

import android.app.Application;

import com.nanchen.initproject.Helper.Injector;
public class App extends Application {
	private static App singleton;
	
	
	public static App getInstance(){
		return singleton;
	}
	@Override
	public void onCreate() {
		super.onCreate();
		singleton = this;
		
		Injector.initialize(this);
		Injector.applicationComponent().inject(this);
		
	}
}
