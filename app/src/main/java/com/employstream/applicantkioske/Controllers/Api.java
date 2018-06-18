package com.employstream.applicantkioske.Controllers;

import android.os.AsyncTask;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.employstream.applicantkioske.Interfaces.ApiInterface;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;

/**
 * Created by matthewbowen on 6/10/18.
 */

public class Api {
    public static class Get extends AsyncTask<Void, Void, Boolean> {
        private ApiInterface mCallback;
        private JSONObject mData;
        private String mUrl;
        private String mRequest;

        public Get(String request, String url, ApiInterface callback) {
            this.mCallback = callback;
            this.mUrl = url;
            this.mRequest = request;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                StringRequest getRequest = new StringRequest(Request.Method.GET, this.mUrl,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jsonObj = new JSONObject(response);
                                    Object data = jsonObj.get("data");
                                    if(data instanceof String) {
                                        mData = new JSONObject();
                                        mData.put("img", jsonObj.getString("data"));
                                    } else if (data instanceof JSONArray) {
                                        mData = new JSONObject();
                                        mData.put("colors", new JSONArray(jsonObj.getString("data")));
                                    } else {
                                        mData = new JSONObject(jsonObj.getString("data"));
                                    }
                                    mCallback.onRequestComplete(mRequest, mData);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError e) {
                                e.printStackTrace();
                            }
                        }
                ) {

                };
                Volley.newRequestQueue(MyApplication.getContext()).add(getRequest);
            } catch (Exception e) {
                e.printStackTrace();
                return false;


            }

            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
        }

        @Override
        protected void onCancelled() {

        }
    }
}
