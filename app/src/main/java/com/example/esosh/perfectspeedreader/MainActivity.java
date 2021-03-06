package com.example.esosh.perfectspeedreader;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import nl.siegmann.epublib.domain.Book;
import nl.siegmann.epublib.domain.TOCReference;
import nl.siegmann.epublib.epub.BookProcessor;
import nl.siegmann.epublib.epub.EpubReader;

import static java.lang.Math.cosh;
import static java.lang.Math.floor;
import static java.lang.Math.sin;
import static java.lang.StrictMath.pow;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    TextView tv;
    EditText inputName , inputChapter;
    int count=0;
    String words[];
    List <String> textlike;
    Toast m_currentToast;
    double param1,param2;
    int profile=1;
    double waitconstant=80;
    String bookname="";
    Handler handler = new Handler();
int chapter;
    boolean play=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% MY code %%%%%%%%%%%%%%%%%%%%%%%%%%%
        resetParameters();
        tv=findViewById(R.id.MainTextView);textlike=new ArrayList<>();
        inputChapter=findViewById(R.id.chapterT);
        inputName=findViewById(R.id.nameT);

        Button faster = findViewById(R.id.faster) ;
        faster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                waitconstant*=.95;
                showToast("timeconstant= "+Double.toString(waitconstant));
            }
        });

        Button slower = findViewById(R.id.slower) ;
        slower.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                waitconstant*=1.05;
                showToast("timeconstant= "+Double.toString(waitconstant));}
        });

        Button P1up = findViewById(R.id.P1up) ;
        P1up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                param1*=1.01;
                showToast("param1= "+Double.toString(param1));}
        });

        Button P1down = findViewById(R.id.P1down) ;
        P1down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                param1*=.99;
                showToast("param1= "+Double.toString(param1));}
        });

        Button P2down = findViewById(R.id.P2down) ;
        P2down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                param2*=.9;
                showToast("param2= "+Double.toString(param2));}
        });

        Button P2up = findViewById(R.id.P2up) ;
        P2up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                param2*=1.1;
                showToast("param2= "+Double.toString(param2));}
        });

        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            if(play){
            play=false;
                showToast("pause");
            }
            else{
                play=true;
                Handler handler = new Handler();
                handler.postDelayed(periodicUpdate, 100);
//                showToast("play");
            bookname= String.valueOf(inputName.getText());
                chapter= Integer.parseInt(String.valueOf(inputChapter.getText()));
                showToast(String.valueOf(chapter)+ bookname);
                Log.d("mystuf", String.valueOf(chapter)+ bookname);

            }


            }
        });
        //bookname="GLS";
openChapter(chapter);
//%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onPause() {
        play=false;
        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.prof1) {
            profile=1;
            param1=1.7;
            waitconstant=20;
            param2=3;
        } else if (id == R.id.prof2) {
            profile=2;
            param1=.35;
            waitconstant=35;
            param2=3;
        } else if (id == R.id.prof3) {
            profile=3;
            param1=5;
            param2=3;
            waitconstant=50;
        } else if (id == R.id.prof4) {
            profile=4;
            param1=1.3;
            waitconstant=35;
            param2=3;
        } else if (id == R.id.Bgls) {
            bookname="ulysses";
            openChapter(4);
        } else if (id == R.id.Bmgs) {
            bookname="FMGS";
            openChapter(4);
        } else if (id == R.id.Btaots) {
            bookname="TAOTS";
            openChapter(4);
    } else if (id == R.id.Btwotw) {
            bookname="TWOTW";
            openChapter(5);
    }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void openChapter(int chaptrernum){
        play =false;
        count=0;
        textlike.clear();
        ReadChapter rc =new ReadChapter();
        rc.doInBackground(chaptrernum);
        handler.postDelayed(periodicUpdate, 100 );
    }

    private class ReadChapter extends AsyncTask<Integer, Void, Void> {
        @Override
        protected Void doInBackground(Integer... chaptrernum) {
            String weblike="";
            try {  File file = new File(Environment.getExternalStorageDirectory() + "/"+bookname+".epub");

                FileInputStream  fileInputStream = new FileInputStream(file);
                InputStream epubInputStream = fileInputStream;
                // Load Book from inputStream
                Book book = (new EpubReader()).readEpub(epubInputStream);

                Log.d("mystuf", "book content size= "+String.valueOf(book.getContents().size()));
                char cbuf[] = new char[10000];
                book.getContents().get(chaptrernum[0]).getReader().read(cbuf);
                 weblike=String.valueOf(cbuf);

            } catch (IOException e) {
                Log.e("epublib", e.getMessage());
            }
//            showToast("started reading it");
            String selector="p";
            HtmlToPlainText formatter = new HtmlToPlainText();
            Document doc= Jsoup.parse(weblike);

            Elements elements = doc.select(selector); // get each element that matches the CSS selector
if(!elements.isEmpty()){
            for (Element element : elements) {
                String plainText = formatter.getPlainText(element); // format that element to plain text

                String buf=plainText.replace("\n", " ").replace("\r", "");

                textlike.addAll(Arrays.asList(buf.split("\\s+")));         //   tv.setText(plainText);


            }//showToast("ended reading it");
                        }else{showToast("this chapter is empty"); }


            return null;

        }
    }

    private Runnable periodicUpdate = new Runnable () {
        Handler handler = new Handler();

        public void run() {

            if(textlike.size()>count){
                int l=textlike.get(count).length();
                tv.setText(textlike.get(count));
//                +"\n"+String.valueOf(l)
            long delay=100;//default value 100ms
            if(profile==1) {
                delay = (long) (waitconstant *( pow(l, param1)+param2));
            }
            else if(profile==2){
                delay = (long) (waitconstant * (cosh(param1*l )+param2));
            }
            else if(profile==3){
                delay = (long) (waitconstant *(param1*floor(l/param1)+param2));
            }
            else if(profile==4){
                delay = (long) (waitconstant *( pow(param1, l)+param2));
            }
if(play) {
    handler.postDelayed(periodicUpdate, delay);
    count++;
}
        }else {showToast("reached the end of chapter"); }
        }
    };

    void showToast(String text)
    {
        if(m_currentToast != null)
        {m_currentToast.cancel(); }
        m_currentToast = Toast.makeText(this, text, Toast.LENGTH_LONG);
        m_currentToast.show();
    }
    void resetParameters(){
        profile=1;
        param1=1.7;
        waitconstant=20;
        param2=3;
    }
}
