package com.example.admin.abc;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class AddProductSizes extends AppCompatActivity implements View.OnClickListener {
    private static final String UPLOAD_URL = Config.productSizesCRUD;
    private EditText txtwidth, txtheight, txtlength;
    Context context;
    final ArrayList<SizesDB> sizesDBs = new ArrayList<>();
    private Spinner sp1;
    private Button btnAdd1;
    private ArrayAdapter<SizesDB> adapter;

    public int ipid=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product_sizes);
        //Intent intent = this.getIntent(); // get Intent which we set from Previous Activity
      //  final int pid = intent.getExtras().getInt("PRODUCTID_KEY");
       // final String name = intent.getExtras().getString("PRODUCTNAME_KEY");
        Toolbar actionbar = (Toolbar) findViewById(R.id.toolbar);
        if (null != actionbar) {
            actionbar.setNavigationIcon(R.mipmap.backbutton);

            //  actionbar.setTitle(R.string.title_activity_settings);
            actionbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent in = new Intent(AddProductSizes.this,Products.class);

                    startActivity(in);
                }
            });

        }

        txtwidth = (EditText) findViewById(R.id.width1);

        txtheight = (EditText) findViewById(R.id.height1);
        txtlength = (EditText) findViewById(R.id.length1);
        btnAdd1 = (Button) findViewById(R.id.addbtn1);

        sp1 = (Spinner) findViewById(R.id.productsspinner);

        btnAdd1.setOnClickListener(this);
    }
    @Override
    public void onStart(){
        super.onStart();
        BackTask bt = new BackTask();
        bt.execute();
    }
    private class BackTask extends AsyncTask<Void,Void,Void> {

        protected void onPreExecute() {
            super.onPreExecute();

        }

        protected Void doInBackground(Void... params) {
            InputStream is = null;
            String result = "";
            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(Config.productAllSizeSpinner);
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity entity = response.getEntity();
                // Get our response as a String.
                is = entity.getContent();
            } catch (IOException e) {
                e.printStackTrace();
            }

            //convert response to string
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "utf-8"));
                String line = null;
                while ((line = reader.readLine()) != null) {
                    result += line;
                }
                is.close();
                //result=sb.toString();
            } catch (Exception e) {
                e.printStackTrace();
            }
            // parse json data
            try {
                JSONArray ja = new JSONArray(result);
                JSONObject jo=null;
                sizesDBs.clear();
                SizesDB sizesDB;
                for (int i = 0; i < ja.length(); i++) {
                    jo=ja.getJSONObject(i);
                    // add interviewee name to arraylist

                    ipid =jo.getInt("ProductId");

                    String pname =jo.getString("ProductName");
                    sizesDB=new SizesDB();

                    sizesDB.setProductId(ipid);
                    sizesDB.setName(pname);

                    sizesDBs.add(sizesDB);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(Void result) {

            final ArrayList<String> listItems = new ArrayList<>();

            for(int i=0;i<sizesDBs.size();i++){
                listItems.add(sizesDBs.get(i).getName());

            }
                /*for(int i=0;i<sizesDBs.size();i++){

                }*/
            adapter = new ArrayAdapter(AddProductSizes.this,R.layout.spinner_layout5, R.id.txt5,listItems);
            sp1.setAdapter(adapter);
            adapter.notifyDataSetChanged();


        }
    }

    @Override
    public void onClick(View view) {
        if(view == btnAdd1){
            checkData();
            //for checking empty values
        }
    }

    private void checkData() {
        if (txtwidth.length() < 1 || txtheight.length() < 1) {
            Toast.makeText(AddProductSizes.this, "Fill All", Toast.LENGTH_SHORT).show();
        } else {
            uploadMultipart();
            Toast.makeText(this, "Successfully Completed", Toast.LENGTH_SHORT).show();
            txtwidth.setText("");
            txtheight.setText("");
            //txtlength.setText("");
            adapter.notifyDataSetChanged();

            BackTask bt = new BackTask();
            bt.execute();

        }

    }


    public void uploadMultipart() {

        String width=txtwidth.getText().toString();
        int wid=Integer.parseInt(width);

        String height=txtheight.getText().toString();
        int heig=Integer.parseInt(height);

        String length=txtlength.getText().toString();
        // int leng=Integer.parseInt(length);


        String spinSelVal = sp1.getSelectedItem().toString();

        sp1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> arg0, View selectedItemView,
                                       int position, long id) {
                SizesDB sizesDB = (SizesDB) sizesDBs.get(position);
                //final String productName = sizesDB.getName();

                ipid =sizesDB.getProductId() ;

                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int which) {
                        // TODO Auto-generated method stub
                        dialog.dismiss();
                    }
                };
            }
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
                Toast.makeText(AddProductSizes.this,
                        "Your Selected : Nothing",
                        Toast.LENGTH_SHORT).show();
            }

        });

        SizesDB s = new SizesDB();
        s.setWidth(wid);
        s.setHeight(heig);
        s.setLength(length);
        s.setProductId(ipid);

        //Uploading code
        try {
            AndroidNetworking.post(UPLOAD_URL)
                    .addBodyParameter("action","save")
                    .addBodyParameter("width", String.valueOf(wid))
                    .addBodyParameter("height",String.valueOf(heig))
                    .addBodyParameter("length",String.valueOf(length))
                    .addBodyParameter("productid", String.valueOf(ipid))

                    .setTag("TAG_ADD")
                    .build()
                    .getAsJSONArray(new JSONArrayRequestListener() {
                        @Override
                        public void onResponse(JSONArray response) {
                            if(response != null)
                                try {
                                    //SHOW RESPONSE FROM SERVER
                                    String responseString = response.get(0).toString();
                                    Toast.makeText(AddProductSizes.this, "PHP SERVER RESPONSE : " + responseString, Toast.LENGTH_SHORT).show();
                                    if (responseString.equalsIgnoreCase("Success")) {
                                        //CLEAR EDITXTS
                                        txtwidth.setText("");
                                        txtheight.setText("");
                                        txtlength.setText("");
                                        adapter.notifyDataSetChanged();

                                       BackTask bt = new BackTask();
                                        bt.execute();

                                    }else
                                    {
                                        Toast.makeText(AddProductSizes.this, "PHP WASN'T SUCCESSFUL. ", Toast.LENGTH_SHORT).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Toast.makeText(AddProductSizes.this, "GOOD RESPONSE BUT JAVA CAN'T PARSE JSON IT RECEIVED : "+e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                        }
                        //ERROR
                        @Override
                        public void onError(ANError anError) {
                            Toast.makeText(AddProductSizes.this, "UNSUCCESSFUL :  ERROR IS : "+anError.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
               /* String uploadId = UUID.randomUUID().toString();

                //Creating a multi part request
                new MultipartUploadRequest(this, uploadId, UPLOAD_URL)
                        .addParameter("action","save")
                        .addParameter("width", String.valueOf(wid))
                        .addParameter("height",String.valueOf(heig))
                        .addParameter("length",String.valueOf(length))
                        .addParameter("productid", String.valueOf(ipid))
                        .addParameter("producttypeid",String.valueOf(iptid))
                        .setNotificationConfig(new UploadNotificationConfig())
                        .setMaxRetries(2)
                        .startUpload(); //Starting the upload*/

        } catch (Exception exc) {
            Toast.makeText(this, exc.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

}
