package com.example.callfromhome.data;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.callfromhome.data.model.LoggedInUser;
import com.example.callfromhome.ui.login.LoginActivity;
import com.example.callfromhome.utilities.CustomUtility;
import com.example.callfromhome.utilities.MySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import javax.security.auth.login.LoginException;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class LoginDataSource {


    boolean isSuccess = false;
    public Result<LoggedInUser> login(Context context,String username, String password) {

        try {
            // TODO: handle loggedInUser authentication
            LoggedInUser fakeUser =
                    new LoggedInUser(
                            java.util.UUID.randomUUID().toString(),
                            "Jane Doe");

            getResult(new VolleyCallBack() {
                @Override
                public void onSuccess() {
                    isSuccess = true;
                }

                @Override
                public void onFailure() {
                    isSuccess = false;
                }

            }, context);

            if(isSuccess)
            {
                return new Result.Success<>(fakeUser);
            }
            else
            {
                return new Result.Error(new LoginException("Error logging in"));
            }
        } catch (Exception e) {
            return new Result.Error(new IOException("Error logging in", e));
        }
    }

    public void logout() {
        // TODO: revoke authentication
    }

    public interface VolleyCallBack {
        void onSuccess();
        void onFailure();
    }

    public void  getResult(final VolleyCallBack callBack, Context context)
    {
        String url = "https://bkash.imslpro.com/api/login/login.php";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        try {
                            String code = jsonObject.getString("success");
                            String message = jsonObject.getString("message");
                            if (code.equals("true")) {
                                isSuccess = true;
                                jsonObject = jsonObject.getJSONObject("userData");
                                   /* sharedPreferences = getSharedPreferences("bkash_user",MODE_PRIVATE);
                                    SharedPreferences.Editor editor = getSharedPreferences("bkash_user",MODE_PRIVATE).edit();
                                    editor.putString("name",jsonObject.getString("UserFullName"));
                                    editor.putString("id",jsonObject.getString("RecordId"));
                                    editor.putString("team",jsonObject.getString("TeamName"));
                                    editor.apply();
                                    Intent intent = new Intent(getApplicationContext(), ActivityForm.class);
                                    startActivity(intent);
                                    finish();*/
                                callBack.onSuccess();
                            }
                            else{
                                Log.e("mess",message);
                                //CustomUtility.showError(context,"Incorrect user name or password","Login Failed");
                                isSuccess = false;
                                callBack.onFailure();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error
                        Log.e("res",error.toString());
                        //CustomUtility.showError(LoginActivity.this, "Network Error, try again!", "Login failed");
                    }
                });

        MySingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);

    }


}