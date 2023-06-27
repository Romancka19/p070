package com.example.p070;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class newtheatre extends AppCompatActivity {

    TextView tvInfo;
    EditText tvName;
    EditText tvAddress;
    MyTask mtn;
    Button tvButton;
    newtheatre.MyTaskN mt;
    newtheatre.MyTaskTF mttf;
    ListView lvMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newtheatre);
        tvInfo = (TextView) findViewById(R.id.tvInfo);
        tvName = (EditText) findViewById(R.id.editTextTextPersonName);
        tvAddress = (EditText) findViewById(R.id.editTextTextPersonAddress);
        lvMain = (ListView) findViewById(R.id.lvMain);
        lvMain.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        tvButton = (Button) findViewById(R.id.tvButton);
        mtn = new newtheatre.MyTask();
        mtn.execute();
    }

    class MyTask extends AsyncTask<Void, Void, ArrayList<String[]>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            tvInfo.setText("Begin");
        }

        @Override
        protected ArrayList<String[]> doInBackground(Void... params) {
            ArrayList<String[]> res = new ArrayList<>();
            HttpURLConnection myConnection = null;
            try {
                URL githubEndpoint = new URL("http://10.0.2.2:8080/kino/allfilm");
                myConnection =
                        (HttpURLConnection) githubEndpoint.openConnection();
            } catch (MalformedURLException e) {
                e.printStackTrace();
                tvInfo.setText("1");

            } catch (IOException e) {
                e.printStackTrace();
                tvInfo.setText("2");
            }

            int i = 0;
            try {
                i = myConnection.getResponseCode();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (i == 200) {
                InputStream responseBody = null;
                try {
                    responseBody = myConnection.getInputStream();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                InputStreamReader responseBodyReader = null;
                try {
                    responseBodyReader = new InputStreamReader(responseBody, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                BufferedReader r = new BufferedReader(new InputStreamReader(responseBody));

                String total = "";

                try {
                    total = r.readLine();
                } catch (IOException e) {
                }

                JSONArray JA = new JSONArray();

                try {
                    JA = new JSONArray(total);
                } catch (JSONException e) {
                    total = total + "]";
                    try {
                        JA = new JSONArray(total);
                    } catch (JSONException a) {
                    }
                }
                for (int j = 0; j < JA.length(); j++) {
                    JSONObject JO = null;
                    try {
                        JO = JA.getJSONObject(j);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    String[] st = new String[2];
                    try {
                        st[0] = JO.getString("name").toString();
                        st[1] = JO.getString("id").toString();
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
            newtheatre.ClAdapter clAdapter = new newtheatre.ClAdapter(tvInfo.getContext(), result);
            lvMain.setAdapter(clAdapter);
            tvButton.setEnabled(true);
        }
    }

    class ClAdapter extends BaseAdapter {
        Context ctx;
        LayoutInflater lInflater;
        List<String[]> lines;

        ClAdapter(Context context, List<String[]> elines) {
            ctx = context;
            lines = elines;
            lInflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
                view = lInflater.inflate(R.layout.itemt, parent, false);
            }
            ;
            String[] p = (String[]) getItem(position);
            ((TextView) view.findViewById(R.id.tvText1)).setText(p[0]);
            return view;
        }

        ;

        public boolean getCheck(int position) {

            return true;
        }
    }

    public void onclick(View v) {
        mt = new newtheatre.MyTaskN();
        mt.execute(tvName.getText().toString(), tvAddress.getText().toString());
    }

    class MyTaskN extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String total = "";
            BufferedOutputStream os = null;
            HttpURLConnection myConnection = null;
            JSONObject obj = new JSONObject();
            try {
                URL githubEndpoint = new URL("http://10.0.2.2:8080/kino/newtheatre");
                myConnection = (HttpURLConnection) githubEndpoint.openConnection();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            myConnection.setRequestProperty("Accept", "application/vnd.github.v3+kino");
            myConnection.setRequestProperty("Contact-Me", "hathibelagal@example.com");
            try {
                myConnection.setRequestMethod("POST");
            } catch (ProtocolException e) {
                e.printStackTrace();
            }
            myConnection.setDoOutput(true);
            myConnection.setDoOutput(true);
            myConnection.setRequestProperty("Content-Type", "application/json;charset=utf-8");
            myConnection.setRequestProperty("X-Requested-With", "XMLHttpRequest");
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("name", params[0]);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
            try {
                jsonObject.put("address", params[1]);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
            String message = jsonObject.toString();
            try {
                os = new BufferedOutputStream(myConnection.getOutputStream());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            try {
                os.write(message.getBytes());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            try {
                os.flush();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            int i = 0;
            try {
                i = myConnection.getResponseCode();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (i == 200) {
                InputStream responseBody = null;
                try {
                    responseBody = myConnection.getInputStream();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                BufferedReader r = new BufferedReader(new InputStreamReader(responseBody));

                total = "";

                try {
                    total = r.readLine();
                } catch (IOException e) {
                }

                try {
                    obj = new JSONObject(total);
                } catch (JSONException e) {
                    total = total + "]";
                    try {
                        obj = new JSONObject(total);
                    } catch (JSONException a) {
                    }
                }
                try {
                    total = obj.getString("result");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return total;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            int n = lvMain.getChildCount();
            int m = 0;
            JSONArray JA = new JSONArray();
            for (int i = 0; i < n; i++) {
                String[] st = (String[]) lvMain.getAdapter().getItem(i);
                LinearLayout ll = (LinearLayout) lvMain.getChildAt(i);
                CheckBox ch = (CheckBox) ll.getChildAt(0);
                if (ch.isChecked()) {
                    try {
                        JA.put(m, new JSONObject());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    try {
                        JA.getJSONObject(m).put("theatreID", result);
                        JA.getJSONObject(m).put("filmID", st[1]);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    m++;
                }
            }
            mttf = new newtheatre.MyTaskTF();
            mttf.execute(JA);
        }
    }

    class MyTaskTF extends AsyncTask<JSONArray, Void, String> {

        @Override
        protected String doInBackground(JSONArray... jsonArrays) {
            String total = "";
            ArrayList<String[]> res = new ArrayList<>();
            BufferedOutputStream os = null;
            HttpURLConnection myConnection = null;
            JSONObject obj = null;
            try {
                URL githubEndpoint = new URL("http://10.0.2.2:8080/kino/newtheatre_film");
                myConnection = (HttpURLConnection) githubEndpoint.openConnection();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            myConnection.setRequestProperty("Accept", "application/vnd.github.v3+kino");
            myConnection.setRequestProperty("Contact-Me", "hathibelagal@example.com");
            try {
                myConnection.setRequestMethod("POST");
            } catch (ProtocolException e) {
                e.printStackTrace();
            }
            myConnection.setDoOutput(true);
            myConnection.setDoOutput(true);
            myConnection.setRequestProperty("Content-Type", "application/json;charset=utf-8");
            myConnection.setRequestProperty("X-Requested-With", "XMLHttpRequest");

            String message = jsonArrays[0].toString();

            try {
                os = new BufferedOutputStream(myConnection.getOutputStream());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            try {
                os.write(message.getBytes());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
//clean up
            try {
                os.flush();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            int i = 0;
            try {
                i = myConnection.getResponseCode();
            } catch (IOException e) {
                e.printStackTrace();
            }
//                tvInfo.setText(str);
            if (i == 200) {
                InputStream responseBody = null;
                try {
                    responseBody = myConnection.getInputStream();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                BufferedReader r = new BufferedReader(new InputStreamReader(responseBody));

                total = "";

                try {
                    total = r.readLine();
                } catch (IOException e) {
                }

                JSONArray JA = new JSONArray();

                try {
                    JA = new JSONArray(total);
                } catch (JSONException e) {
                    total = total + "]";
                    try {
                        JA = new JSONArray(total);
                    } catch (JSONException a) {
                    }
                }
                for (int j = 0; j < JA.length(); j++) {
                    JSONObject JO = null;
                    try {
                        JO = JA.getJSONObject(j);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    String[] st = new String[2];
                    try {
                        st[0] = JO.getString("result").toString();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    res.add(st);
                }
            }
            return total;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            tvInfo.setText("End");
            return;
        }
    }
}