package com.nanchen.initproject.Helper;

import com.nanchen.initproject.App;
public class Injector {
	
	private static AppComponent applicationComponent;
	
	private Injector() {
	}
	
	public static void initialize(final App application) {
		applicationComponent = DaggerAppComponent.builder().App(new AppModule(application)).build();
	}
	
	public static AppComponent applicationComponent() {
		return applicationComponent;
	}
}
