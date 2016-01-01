package vimaru.chaunguyen.tracuudiemvimaru;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.renderscript.Element;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.XMLReader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class MainActivity extends AppCompatActivity {
    private final String TAG="Tra cuu diem Vimaru";
    private final int LOAD_DONE=1;
    private final int LOAD_FAIL=0;
    private final int MASV_FAIL=2;
    private final String URL="http://khaothi.vimaru.edu.vn/tracuudiem";
    private String formBuildId="";
    private String formId="tra_diem_truc_tuyen_form";
    //
    private EditText edtMaSV;
    private Button btnTraDiem;
    //
    Handler mHandler=new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if(msg.what==LOAD_DONE)
            {
                btnTraDiem.setEnabled(true);
            }
            if(msg.what==LOAD_FAIL)
            {
                String err_msg=msg.getData().getString("error_msg");
                AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Lỗi");
                builder.setMessage(err_msg);
                builder.setNegativeButton("Exit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        MainActivity.this.finish();
                    }
                });
                builder.setPositiveButton("Reload", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                init();
                            }
                        }).start();
                    }
                });
                builder.create().show();
            }
            if(msg.what==MASV_FAIL)
            {
                Toast.makeText(MainActivity.this,"Mã sinh viên không đúng",Toast.LENGTH_LONG).show();
            }
            return false;
        }
    });
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        edtMaSV=(EditText)findViewById(R.id.masv);
        btnTraDiem=(Button)findViewById(R.id.tradiem);
        btnTraDiem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtMaSV.getText().toString().matches("^\\d{5}$"))
                {
                    Intent intent=new Intent(MainActivity.this,XemDiemActivity.class);
                    intent.putExtra("form_build_id",formBuildId);
                    intent.putExtra("form_id",formId);
                    intent.putExtra("masv",edtMaSV.getText().toString());
                    MainActivity.this.startActivity(intent);
                }
                else
                {
                    mHandler.sendEmptyMessage(MASV_FAIL);
                }
            }
        });
        new Thread(new Runnable() {
            @Override
            public void run() {
                init();
            }
        }).start();
    }
    private void init()
    {
        try {
            HttpClient httpClient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(URL);
            try {
                HttpResponse response = httpClient.execute(httpGet);
                //Log.i(TAG, "Response code: " + String.valueOf(response.getStatusLine().getStatusCode()));
                //Log.i(TAG, "*************************");
                if(response.getStatusLine().getStatusCode()==200) {
                    for (Header h : response.getAllHeaders()) {
                        Log.i(TAG, h.toString());
                    }
                    //Log.i(TAG, "*************************");
                    org.jsoup.nodes.Document doc = Jsoup.parse(response.getEntity().getContent(), "UTF-8", URL);
                    //lay form_build_id và form_id
                    Elements inputs = doc.getElementsByTag("input");
                    for (org.jsoup.nodes.Element element : inputs) {
                        //Log.i(TAG, "{" + element.attr("name") + ": " + element.attr("value") + "}");
                        if (element.attr("name").equals("form_build_id")) {
                            formBuildId = element.attr("value");
                        }
                        if (element.attr("name").equals("form_id")) {
                            formId = element.attr("value");
                        }
                    }
                    if(!formBuildId.equals("")&&!formId.equals(""))
                    {
                        mHandler.sendEmptyMessage(LOAD_DONE);
                    }
                    else
                    {
                        Message msg=mHandler.obtainMessage(LOAD_FAIL);
                        Bundle bundle=new Bundle();
                        bundle.putString("error_msg", "Hệ thống đã thay đổi\nVui lòng chờ cập nhập");
                        msg.setData(bundle);
                        mHandler.sendMessage(msg);
                    }
                }
                else
                {
                    Message msg=mHandler.obtainMessage(LOAD_FAIL);
                    Bundle bundle=new Bundle();
                    bundle.putString("error_msg", "Không tìm thấy máy chủ");
                    msg.setData(bundle);
                    mHandler.sendMessage(msg);
                }
            } catch (IOException e) {
                e.printStackTrace();
                Message msg=mHandler.obtainMessage(LOAD_FAIL);
                Bundle bundle=new Bundle();
                bundle.putString("error_msg", "Không thể kết nối tới máy chủ\nVui lòng kiểm tra lại kết nối internet và thử lại");
                msg.setData(bundle);
                mHandler.sendMessage(msg);
            }
        }catch(Exception e)
        {
            mHandler.sendEmptyMessage(LOAD_FAIL);
            Message msg=mHandler.obtainMessage(LOAD_FAIL);
            Bundle bundle=new Bundle();
            bundle.putString("error_msg", "Không thể kết nối tới máy chủ\nVui lòng kiểm tra lại kết nối internet và thử lại");
            msg.setData(bundle);
            mHandler.sendMessage(msg);
        }
    }
}
