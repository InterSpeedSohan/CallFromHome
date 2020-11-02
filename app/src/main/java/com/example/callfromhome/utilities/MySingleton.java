package com.example.callfromhome.utilities;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

public class MySingleton {
    private static com.example.callfromhome.utilities.MySingleton mInstance;
    private RequestQueue requestQueue;
    private static Context mCtx;

    public MySingleton(Context context) {
        mCtx = context;
        requestQueue=getRequestQueue();
    }

    private RequestQueue getRequestQueue() {
        if(requestQueue==null)
            requestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());
        return requestQueue;
    }

    public static synchronized com.example.callfromhome.utilities.MySingleton getInstance(Context context)
    {
        if(mInstance==null)
        {
            mInstance=new com.example.callfromhome.utilities.MySingleton(context);
        }
        return mInstance;
    }

    public<T> void addToRequestQue(Request<T> request)
    {
        getRequestQueue().add(request);
    }

    public void addToRequestQueue(JsonObjectRequest jsonObjectRequest) {
        getRequestQueue().add(jsonObjectRequest);
    }
}
