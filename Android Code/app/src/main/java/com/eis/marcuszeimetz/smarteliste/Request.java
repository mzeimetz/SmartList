package com.eis.marcuszeimetz.smarteliste;

import android.content.Context;
import android.util.Log;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Request {

    private String url = "http://139.6.56.147:9000";


    int duration;
    JSONObject arr [] = new JSONObject[30];
    int zeit;
    int auswahlZeit = 2000000000;
    JSONObject[] auswahlMitfahrer = new JSONObject[3];
    JSONObject best = new JSONObject();

    public JSONObject[] getarr(){

        return arr;
    }

    public interface VolleyCallback{
        void onSuccess(JSONObject result) throws JSONException;
        void onError(JSONObject result);
        void onSucces(JSONArray result);


    }






    public void post(Context context, JSONObject body, final Map<String, String> params, final String ressource, final VolleyCallback callback) {

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        url += ressource;


        final String mRequestBody = body.toString();

        try {
            JsonObjectRequest request_json = new JsonObjectRequest(com.android.volley.Request.Method.POST, url, new JSONObject(mRequestBody),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                            //Log.d("Response ADV API", String.valueOf(response));
                            try {
                                callback.onSuccess(response);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        }


                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {


                    NetworkResponse response = error.networkResponse;
                    if (response != null && response.data != null) {
                        switch (response.statusCode) {
                            case 500:
                                try {

                                    JSONObject jobj = new JSONObject(new String(response.data));
                                    callback.onError(jobj);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                break;
                        }
                    }

                }
            }) {
                @Override
                public Map<String, String> getHeaders() {
                    if (params == null) {
                        Map<String, String> params = new HashMap<>();
                        params.put("Content-Type", "application/json");
                        return params;
                    } else {
                        return params;
                    }


                }

                @Override
                protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                    try {
                        String jsonString = new String(response.data,
                                HttpHeaderParser.parseCharset(
                                        response.headers, PROTOCOL_CHARSET));

                        JSONObject jsonResponse = new JSONObject();
                        jsonResponse.put("headers", new JSONObject(response.headers));
                        return Response.success(jsonResponse, HttpHeaderParser.parseCacheHeaders(response));


                    } catch (UnsupportedEncodingException e) {
                        return Response.error(new ParseError(e));
                    } catch (JSONException je) {
                        return Response.error(new ParseError(je));
                    }    }


            };

            requestQueue.add(request_json);
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    public void get(Context context, final Map<String, String> params, String ressource, final VolleyCallback callback) {


        RequestQueue queue = Volley.newRequestQueue(context);
        url += ressource;

        JsonObjectRequest request_json = new JsonObjectRequest(com.android.volley.Request.Method.GET, url, new JSONObject(),
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            callback.onSuccess(response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }



                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse response = error.networkResponse;
                if (response != null && response.data != null) {
                    switch (response.statusCode) {
                        case 401:
                            try {

                                JSONObject jobj = new JSONObject(new String(response.data));
                                callback.onError(jobj);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            break;
                    }
                }
            }

        } ) {

            @Override
            public Map<String, String> getHeaders() {
                if (params == null) {
                    Map<String, String> params = new HashMap<>();
                    params.put("Content-Type", "application/json");
                    return params;
                } else {
                    return params;
                }


            }

            /*
            @Override
            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                try {
                    String jsonString = new String(response.data,
                            HttpHeaderParser.parseCharset(
                                    response.headers, PROTOCOL_CHARSET));


                    JSONArray jsonArray = new JSONArray(jsonString);

                    //jsonArray.getJSONObject(0);

                    JSONObject jsonResponse = jsonArray.toJSONObject(jsonArray);





                    //JSONObject jsonResponse = new JSONObject();
                    //jsonResponse.put("headers", new JSONObject(response.headers));

                    return Response.success(jsonResponse,HttpHeaderParser.parseCacheHeaders(response));
                    //return Response.success(jsonResponse, HttpHeaderParser.parseCacheHeaders(response));


                } catch (UnsupportedEncodingException e) {
                    return Response.error(new ParseError(e));
                } catch (JSONException je) {
                    return Response.error(new ParseError(je));
                }    }

                */

        };
        // Add the request to the RequestQueue.
        queue.add(request_json);




    }

    public void put(Context context,JSONObject body, final Map<String, String> params, String ressource, final VolleyCallback callback) {


        RequestQueue requestQueue = Volley.newRequestQueue(context);
        url += ressource;

        final String mRequestBody = body.toString();

        try {
        JsonObjectRequest request_json = new JsonObjectRequest(com.android.volley.Request.Method.PUT, url, new JSONObject(mRequestBody),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            callback.onSuccess(response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


                Log.d("Test","Fehler");



                }} ) {
            @Override
            public Map<String, String> getHeaders() {
                if (params == null) {
                    Map<String, String> params = new HashMap<>();
                    params.put("Content-Type", "application/json");
                    return params;
                } else {
                    return params;
                }


            }


        };
            requestQueue.add(request_json);
        } catch (JSONException e) {
            e.printStackTrace();
        }




    }


    public void getJsonArray(Context context, final Map<String, String> params, String ressource, final VolleyCallback callback) {


        RequestQueue queue = Volley.newRequestQueue(context);
        url += ressource;

        JsonArrayRequest request_json = new JsonArrayRequest(com.android.volley.Request.Method.GET, url, new JSONArray(),
                new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        callback.onSucces(response);
                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse response = error.networkResponse;
                if (response != null && response.data != null) {
                    switch (response.statusCode) {
                        case 401:
                            try {

                                JSONObject jobj = new JSONObject(new String(response.data));
                                callback.onError(jobj);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            break;
                    }
                }
            }

        } ) {

            @Override
            public Map<String, String> getHeaders() {
                if (params == null) {
                    Map<String, String> params = new HashMap<>();
                    params.put("Content-Type", "application/json");
                    return params;
                } else {
                    return params;
                }


            }
            @Override
            protected Response<JSONArray> parseNetworkResponse(NetworkResponse response) {
                try {
                    String jsonString = new String(response.data, HttpHeaderParser.parseCharset(response.headers, PROTOCOL_CHARSET));



                    String patternStr = "\\{\"student\"";


                    Pattern pattern = Pattern.compile(patternStr);
                    Matcher matcher = pattern.matcher(jsonString);

                    String replaceAll = matcher.replaceAll(",\\{\"student\"");

                    char [] s = replaceAll.toCharArray();


                    char [] s1 = Arrays.copyOfRange(s,1,s.length);


                    String string = new String(s1);

                    string = "[" + string + "]";





                    JSONArray jsonArray = new JSONArray(string);

                    //jsonArray.getJSONObject(0);




                   // JSONArray jsonResponse = jsonArray;


                    //JSONObject jsonResponse = new JSONObject();
                    //jsonResponse.put("headers", new JSONObject(response.headers));

                    return Response.success(jsonArray,HttpHeaderParser.parseCacheHeaders(response));


                } catch (UnsupportedEncodingException e) {
                    return Response.error(new ParseError(e));
                } catch (JSONException je) {
                    return Response.error(new ParseError(je));
                }    }


        };
        // Add the request to the RequestQueue.
        queue.add(request_json);




    }





}




