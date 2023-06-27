package com.example.p070;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class theatres_films extends AppCompatActivity {
    TextView tvInfo;
    EditText tvName;
    theatres_films.MyTask mt;

    class ClAdapter extends BaseAdapter {
        Context ctx;
        LayoutInflater lInflater;
        List<String[]> lines;
        ClAdapter(Context context, List<String[]> elines){
            ctx = context;
            lines = elines;
            lInflater = (LayoutInflater) ctx
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        @Override
        public int getCount() {
            return lines.size();
        }
        @Override
        public Object getItem(int position) {
            return lines.get(position);
        }
        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;
            if (view == null) {
                view = lInflater.inflate(R.layout.item, parent, false);
            };
            String[] p =(String[]) getItem(position);
            ((TextView) view.findViewById(R.id.tvText)).setText(p[0]);
            ((TextView) view.findViewById(R.id.tvText1)).setText(p[1]);

            return view;
        };

    }

    public void onclick(View v) {
        mt = new theatres_films.MyTask();
        mt.execute(tvName.getText().toString());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_films);
        tvInfo = (TextView) findViewById(R.id.tvInfo);
        tvName = (EditText) findViewById(R.id.editTextTextPersonName);
    }

    class MyTask extends AsyncTask<String, Void, ArrayList<String[]>> {
        @Override
        protected void onPreExecute() {
            tvInfo.setText("Begin");
        }
        @Override
        protected ArrayList<String[]> doInBackground(String... params) {
            ArrayList<String[]> res=new ArrayList <>();
            HttpURLConnection myConnection = null;
            String line="";
            String total="";
            try {
                URL mySite = new URL("http://10.0.2.2:8080/kino/theatres_films?name="+params[0]);
                myConnection =
                        (HttpURLConnection) mySite.openConnection();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            int i=0;
            try {
                i = myConnection.getResponseCode();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (i==200) {
                InputStream responseBody=null;
                try {
                    responseBody = myConnection.getInputStream();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                InputStreamReader responseBodyReader = null;
                try {
                    responseBodyReader =
                            new InputStreamReader(responseBody, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                BufferedReader r = new BufferedReader(new InputStreamReader(responseBody));
                while (true) {
                    try {
                        if (!((line = r.readLine()) != null)) break;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    total=total+line;
                }
                JSONArray JA=null;
                try {
                    JA=new JSONArray(total);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                for (int j=0;j<JA.length();j++) {
                    JSONObject JO=null;
                    try {
                        JO=JA.getJSONObject(j);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    String[] st= new String[3];
                    try {
                        st[0] = JO.getString("fname").toString();
                        st[1] = JO.getString("tname").toString();
                        st[2] = JO.getString("taddress").toString();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    res.add(st);
                }
            }
            myConnection.disconnect();
            return res;
        }
        @Override
        protected void onPostExecute(ArrayList<String[]> result) {
            super.onPostExecute(result);
            theatres_films.ClAdapter clAdapter = new theatres_films.ClAdapter(tvInfo.getContext(),result);
            ListView lvMain = (ListView) findViewById(R.id.lvMain);
            lvMain.setAdapter(clAdapter);
            tvInfo.setText("End");
        }

    }

}