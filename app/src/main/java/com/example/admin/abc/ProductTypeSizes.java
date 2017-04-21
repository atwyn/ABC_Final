package com.example.admin.abc;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Geetha on 4/14/2017 for opening Product Types Size activity based on user clicked product .
 */

public class ProductTypeSizes extends AppCompatActivity {
    ImageView back;

    //Context c;
  //  final static String url = "http://192.168.0.4/abc/getProductSizes.php?ProductId="+"&ProductTypeId=";
    final static String url = "http://192.168.0.2/abc/getProductTypeSizes.php?";
    final String PRODUCTID_PARAM = "ProductId";
    final String PRODUCTTYPEID_PARAM = "ProductTypeId";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products_types_sizes);

        final ListView lv = (ListView) findViewById(R.id.productTypeSizesLv);

        // Get intent data
        Intent intent = this.getIntent(); // get Intent which we set from Previous Activity

        int pid = intent.getExtras().getInt("PRODUCTID_KEY");
        int ptid = intent.getExtras().getInt("PRODUCTTYPEID_KEY");
        Log.d("result PID: ", "> " + pid);

        Log.d("result PtID: ", "> " + ptid);
       // String urlAddress = url + pid + ptid;
       // String urlAddress = url + pid;

        Uri builtUri = Uri.parse(url)
                .buildUpon()
                .appendQueryParameter(PRODUCTID_PARAM, Integer.toString(pid))
                .appendQueryParameter(PRODUCTTYPEID_PARAM, Integer.toString(ptid))
                .build();
        URL urlAddress = null;
        try {
            urlAddress = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        new ProductTypeSizesDownloader(ProductTypeSizes.this,urlAddress,lv).execute();

        back=(ImageView)findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in=new Intent(ProductTypeSizes.this,Products.class);
                startActivity(in);
            }
        });
    }

}
