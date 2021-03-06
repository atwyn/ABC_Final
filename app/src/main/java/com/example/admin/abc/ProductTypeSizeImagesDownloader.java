package com.example.admin.abc;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.GridView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Geetha on 4/18/2017.
 */

class ProductTypeSizeImagesDownloader extends AsyncTask<Void, Void, String> {

    Context c;
    URL urlAddress;
    GridView gv;
   int pid,ptid,ptsid;
    String pname;

    public ProductTypeSizeImagesDownloader(Context c, URL urlAddress, GridView gv,int pid, String pname, int ptid, int ptsid) {
        this.c = c;
        this.urlAddress = urlAddress;
        this.gv = gv;
        this.pid = pid;
        this.ptid = ptid;
        this.pname = pname;
        this.ptsid = ptsid;

        Log.d("newActivity url: ", "> " + urlAddress);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(Void... params) {
        String data = downloadTypeData();
        return data;

    }
    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        if(s==null)
        {
            Toast.makeText(c,"Unsuccessful,Null returned",Toast.LENGTH_SHORT).show();
        }else {
            //CALL DATA PARSER TO PARSE
            ProductTypeSizeImagesDataParser parser=new ProductTypeSizeImagesDataParser(c, gv, s,pid,pname,ptid,ptsid);
            parser.execute();
        }
    }
    private String downloadTypeData() {
        HttpURLConnection con = Connector.connect(String.valueOf(urlAddress));
        if (con == null) {
            return null;
        }
        try {
            InputStream is = new BufferedInputStream(con.getInputStream());
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line;
            StringBuffer jsonData = new StringBuffer();
            while ((line = br.readLine()) != null) {
                jsonData.append(line + "n");
            }
            br.close();
            is.close();
            return jsonData.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
