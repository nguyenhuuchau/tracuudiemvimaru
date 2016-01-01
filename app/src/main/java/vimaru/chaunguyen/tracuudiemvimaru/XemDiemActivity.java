package vimaru.chaunguyen.tracuudiemvimaru;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.Window;
import android.widget.TextView;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


public class XemDiemActivity extends AppCompatActivity {
    private final int LOAD_SV_INFOR_SUCCESS=1;
    private final int LOAD_DIEM_SUCESS=2;
    private final int ERROR=3;
    private TextView svInfor;
    private RecyclerView rv;
    private Adapter adt;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<Item> dataSet;
    private String maSV="";
    private String formBuildID="";
    private String formID="";
    private Handler handler=new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if(msg.what==LOAD_SV_INFOR_SUCCESS)
            {
                svInfor.setText(Html.fromHtml(msg.getData().getString("ttsv")));
            }
            if(msg.what==LOAD_DIEM_SUCESS)
            {
                adt.notifyDataSetChanged();
                //setProgressBarIndeterminateVisibility(false);
            }
            if(msg.what==ERROR)
            {
                String err_msg=msg.getData().getString("error_msg");
                AlertDialog.Builder builder=new AlertDialog.Builder(XemDiemActivity.this);
                builder.setTitle("Lỗi");
                builder.setMessage(err_msg);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        XemDiemActivity.this.finish();
                    }
                });
                builder.create().show();

            }
            return false;
        }
    });
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //getActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_xem_diem);
        //requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        //setProgressBarIndeterminateVisibility(true);
        Intent intent =getIntent();
        maSV=intent.getStringExtra("masv");
        formBuildID=intent.getStringExtra("form_build_id");
        formID=intent.getStringExtra("form_id");
        svInfor=(TextView)findViewById(R.id.textViewTTSV);
        rv=(RecyclerView)findViewById(R.id.RvDSKyhoc);
        layoutManager=new LinearLayoutManager(this);
        rv.setLayoutManager(layoutManager);

        RecyclerView.ItemDecoration itemDecoration= new DividerItemDecoration(this,LinearLayoutManager.VERTICAL);
        rv.addItemDecoration(itemDecoration);

        dataSet=new ArrayList<>();

        adt=new Adapter(dataSet);
        rv.setAdapter(adt);

        Thread t=new Thread(new Runnable() {
            @Override
            public void run() {
                post();
            }
        });
        t.start();
    }
    public void post()
    {
        HttpClient client=new DefaultHttpClient();
        HttpPost httppost = new HttpPost("http://khaothi.vimaru.edu.vn/system/ajax");
        httppost.setHeader("User-Agent","Mozilla/5.0");
        httppost.setHeader("Referer","http://khaothi.vimaru.edu.vn/tracuudiem");
        httppost.setHeader("Accept-Language", "en-US,en;q=0.5");
        httppost.setHeader("Accept-Encoding","gzip, deflate");
        try {
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("masv", maSV));
            nameValuePairs.add(new BasicNameValuePair("namhoc", "0"));
            nameValuePairs.add(new BasicNameValuePair("form_build_id", formBuildID));
            nameValuePairs.add(new BasicNameValuePair("form_id", formID));
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            HttpResponse response=client.execute(httppost);
            if(response.getStatusLine().getStatusCode()==200) {
                InputStream input = response.getEntity().getContent();
                BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(input));
                String htmlData = bufferedReader.readLine();
                JSONArray jsonArray=new JSONArray(htmlData);//chuyen post response thanh mang json
                if(jsonArray.length()==3) {
                    JSONObject jsonData = jsonArray.getJSONObject(1);//phan tu chua thong tin ve diem cua sinh vien la phan tu thu hai
                    htmlData=jsonData.getString("data");// truong data la truong chua ma html cua bang diem
                    Document doc = Jsoup.parse(htmlData);//chuyen chuoi du lieu html thanh doi tuong document
                    //lay thong tin sinh vien
                    if(!doc.getElementById("test-ajax").html().equals("")) {
                        String ttsv = "";
                        ttsv = doc.getElementsByTag("pre").html();//doc phan thong tin ma, ten, ngay sinh cua sinh vien
                        Log.i("TTSV", ttsv);
                        Message msg = handler.obtainMessage(LOAD_SV_INFOR_SUCCESS);//
                        Bundle dt = new Bundle();//
                        dt.putString("ttsv", ttsv);//
                        msg.setData(dt);//
                        handler.sendMessage(msg);//gui thong tin sinh vien toi luong giao dien chinh
                        //lay thong tin ki hoc va mon hoc
                        Elements trTags = doc.getElementsByTag("tbody");
                        trTags = trTags.get(0).getElementsByTag("tr");
                        Log.i("TTSV", trTags.get(0).text());
                        for (Element element : trTags) {
                            Elements tdTags = element.getElementsByTag("td");
                            if (tdTags.size() == 1) {
                                dataSet.add(new Kyhoc(tdTags.get(0).text()));
                            } else if (tdTags.size() == 8) {
                                dataSet.add(new Monhoc(tdTags.get(0).text(),
                                        tdTags.get(1).text(),
                                        tdTags.get(2).text(),
                                        tdTags.get(3).text(),
                                        tdTags.get(4).text(),
                                        tdTags.get(5).text(),
                                        tdTags.get(6).text(),
                                        tdTags.get(7).text()));
                            }
                            handler.sendEmptyMessage(LOAD_DIEM_SUCESS);
                        }
                    }
                    else
                    {
                        Message msg=handler.obtainMessage(ERROR);
                        Bundle bundle=new Bundle();
                        bundle.putString("error_msg", "Không tìm thấy thông tin");
                        msg.setData(bundle);
                        handler.sendMessage(msg);
                    }
                }
                else
                {
                    Message msg=handler.obtainMessage(ERROR);
                    Bundle bundle=new Bundle();
                    bundle.putString("error_msg", "Không tìm thấy thông tin");
                    msg.setData(bundle);
                    handler.sendMessage(msg);
                }
            }
            else
            {
                Message msg=handler.obtainMessage(ERROR);
                Bundle bundle=new Bundle();
                bundle.putString("error_msg","Không tìm thấy máy chủ");
                msg.setData(bundle);
                handler.sendMessage(msg);
            }
        } catch (IOException e) {
            e.printStackTrace();
            Message msg=handler.obtainMessage(ERROR);
            Bundle bundle=new Bundle();
            bundle.putString("error_msg","Không thể kết nối tới máy chủ");
            msg.setData(bundle);
            handler.sendMessage(msg);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}