package de.prog3.ackerschlagkartei.data.repositories;

import android.app.Application;

import androidx.lifecycle.MutableLiveData;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.firestore.GeoPoint;

import org.json.JSONException;
import org.json.JSONObject;

import de.prog3.ackerschlagkartei.R;
import de.prog3.ackerschlagkartei.data.models.WeatherModel;
import de.prog3.ackerschlagkartei.utils.Status;

public class WeatherRepository {
    private final Application application;
    private final String apiUrl = "https://api.openweathermap.org/data/2.5/weather?appid=ed185425a6c757c3f10bc6904baa1341";

    private final MutableLiveData<WeatherModel> weatherModelGetData;
    private final MutableLiveData<Status> weatherModelGetStatus;

    public WeatherRepository(Application application) {
        this.application = application;
        this.weatherModelGetData = new MutableLiveData<>();
        this.weatherModelGetStatus = new MutableLiveData<>(Status.INITIAL);
    }

    public void loadWeather(GeoPoint geoPoint) {
        this.weatherModelGetStatus.postValue(Status.LOADING);

        String url = apiUrl;
        url += "&lat=" + geoPoint.getLatitude();
        url += "&lon=" + geoPoint.getLongitude();
        url += "&units=metric";
        url += "&lang=" + application.getResources().getString(R.string.temp_language);

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {
                    String name = response.getString("name");
                    double temp = response.getJSONObject("main").getDouble("temp");
                    String desc = response.getJSONArray("weather").getJSONObject(0).getString("description");

                    weatherModelGetData.postValue(new WeatherModel(name, temp, desc));
                    weatherModelGetStatus.postValue(Status.SUCCESS);

                } catch (JSONException e) {
                    weatherModelGetStatus.postValue(Status.ERROR);
                }

            }

        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError e) {
                weatherModelGetStatus.postValue(Status.ERROR);
            }
        });

        Volley.newRequestQueue(application).add(req);
    }

    public MutableLiveData<WeatherModel> getWeatherModelGetData() {
        return weatherModelGetData;
    }

    public MutableLiveData<Status> getWeatherModelGetStatus() {
        return weatherModelGetStatus;
    }
}
