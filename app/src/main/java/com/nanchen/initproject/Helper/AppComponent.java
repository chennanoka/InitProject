package com.nanchen.initproject.Helper;

import javax.inject.Singleton;

import dagger.Component;
import com.nanchen.initproject.App;
import com.nanchen.initproject.MainActivity;
@Singleton
@Component(modules = {AppModule.class})
public interface AppComponent {
	void inject(App application);
	void inject(MainActivity mainActivity);
}
