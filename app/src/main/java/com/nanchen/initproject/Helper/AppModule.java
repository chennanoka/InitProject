package com.nanchen.initproject.Helper;

import android.app.Application;
import android.content.Context;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.nanchen.initproject.BuildConfig;
import com.nanchen.initproject.IgnoredByGson;

import java.io.IOException;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import androidx.annotation.NonNull;
import dagger.Module;
import dagger.Provides;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by nanchen on 2018-02-18.
 */

@Module
public class AppModule {
	
	private final Application application;
	
	public AppModule(Application application) {
		this.application = application;
	}
	
	@Provides
	@Singleton
	public Gson provideGson() {
		return new GsonBuilder()
				.setExclusionStrategies(new ExclusionStrategy() {
					@Override
					public boolean shouldSkipField(FieldAttributes f) {
						return f.getAnnotation(IgnoredByGson.class) != null;
					}
					
					@Override
					public boolean shouldSkipClass(Class<?> clazz) {
						return false;
					}
				})
				.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
				.create();
	}
	
	@Provides
	@Singleton
	public Context provideApplicationContext() {
		return this.application.getApplicationContext();
	}
	
	@Provides
	@Singleton
	public OkHttpClient providesOkHttpClient(final OkHttpRequestHeaders okHttpRequestHeaders) {
		
		final HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
		if (!BuildConfig.DEBUG) {
			logging.setLevel(HttpLoggingInterceptor.Level.NONE);
		} else {
			logging.setLevel(HttpLoggingInterceptor.Level.BODY);
		}
		
		return new OkHttpClient.Builder()
				.connectTimeout(15, TimeUnit.SECONDS)
				.readTimeout(45, TimeUnit.SECONDS)
				.writeTimeout(45, TimeUnit.SECONDS)
				.addInterceptor(okHttpRequestHeaders)
				.addInterceptor(logging)
				.build();
	}
	
	@Provides
	@Singleton
	@Named("haha")
	public Retrofit provideQualityRetrofit(final OkHttpClient client) {
		final Gson gson = new GsonBuilder()
				.setExclusionStrategies(new GsonExclusionStrategy())
				.create();
		
		return new Retrofit.Builder()
				.baseUrl("https://httpbin.org/")
				.addConverterFactory(GsonConverterFactory.create(gson))
				.client(client)
				.addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
				.build();
	}
	
	public class GsonExclusionStrategy implements ExclusionStrategy {
		
		@Override
		public boolean shouldSkipField(FieldAttributes f) {
			return f.getAnnotation(Exclude.class) != null;
		}
		
		@Override
		public boolean shouldSkipClass(Class<?> clazz) {
			return false;
		}
	}
	
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.FIELD)
	public @interface Exclude {
	
	}
	
	public static class OkHttpRequestHeaders implements Interceptor {
		@Inject
		public OkHttpRequestHeaders() {
		}
		
		@Override
		public Response intercept(@NonNull final Chain chain) throws IOException {
			
			final Request newRequest = chain.request().newBuilder()
					.build();
			return chain.proceed(newRequest);
		}
		
	}
	
	
}