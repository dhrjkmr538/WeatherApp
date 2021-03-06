package io.leftshift.weather.weatherinfo.domain.usecase;

import android.support.annotation.NonNull;

import java.io.IOException;
import java.util.List;

import io.leftshift.weather.UseCase;
import io.leftshift.weather.data.WeatherDatasource;
import io.leftshift.weather.data.WeatherRepository;
import io.leftshift.weather.weatherinfo.domain.model.WeatherInfo;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by dhirajhimani on 8/17/2016.
 */
public class GetWeatherInfos extends UseCase<GetWeatherInfos.RequestValues, GetWeatherInfos.ResponseValue> {

	private final WeatherRepository mWeatherRepository;

	/**
	 * The constant URL.
	 */
	public static String URL = "http://api.openweathermap.org/data/2.5/forecast/daily?q=%s&mode=json&cnt=14&APPID=f6bddf40a2298dc31e2c905776412034&units=metric";

	/**
	 * Instantiates a new Get weather infos.
	 *
	 * @param weatherRepository the weather repository
	 */
	public GetWeatherInfos(@NonNull WeatherRepository weatherRepository) {
		mWeatherRepository = checkNotNull(weatherRepository, "WeatherRepository cannot be null!");
	}

	@Override
	protected void executeUseCase(final RequestValues values) {
		try {
			mWeatherRepository.getWeatherInfo(new WeatherDatasource.LoadTasksCallback() {
				@Override
				public void onTasksLoaded(List<WeatherInfo> weatherInfos) {
					ResponseValue responseValue = new ResponseValue(weatherInfos);
					getUseCaseCallback().onSuccess(responseValue);
				}

				@Override
				public void onDataNotAvailable() {
					getUseCaseCallback().onError();
				}
			}, values.url, values.city);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * The type Request values.
	 */
	public static final class RequestValues implements UseCase.RequestValues {

		private final String url, city;

		/**
		 * Instantiates a new Request values.
		 *
		 * @param url  the url
		 * @param city the city
		 */
		public RequestValues(String url, String city) {
			this.url = url;
			this.city = city;
		}
	}

	/**
	 * The type Response value.
	 */
	public static final class ResponseValue implements UseCase.ResponseValue {

		private final List<WeatherInfo> mWeatherInfos;

		/**
		 * Instantiates a new Response value.
		 *
		 * @param weatherInfos the weather infos
		 */
		public ResponseValue(@NonNull List<WeatherInfo> weatherInfos) {
			mWeatherInfos = checkNotNull(weatherInfos, "weatherInfos cannot be null!");
		}

		/**
		 * Gets infos.
		 *
		 * @return the infos
		 */
		public List<WeatherInfo> getInfos() {
			return mWeatherInfos;
		}
	}
}