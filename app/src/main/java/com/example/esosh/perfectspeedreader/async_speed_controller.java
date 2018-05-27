package com.example.esosh.perfectspeedreader;

import android.os.AsyncTask;
import android.os.Handler;
import android.widget.TextView;
import android.view.View;

import static java.lang.StrictMath.pow;
//import static java.lang.Math.pow;

public class async_speed_controller  {

//    TextView tv;
//int count=10;
//String words[];
//
//    public void setWords(TextView tv2,String... words2)
//    {
//        tv= tv2;
//        words=words2;
////        waitthencalltask wtcb=new waitthencalltask();
////        wtcb.doInBackground((long) 10000);
//        Handler handler = new Handler();
//        handler.postDelayed(periodicUpdate, 100 );
//    }
//
//
//
//    private Runnable periodicUpdate = new Runnable () {
//        Handler handler = new Handler();
//
//        public void run() {
//            // scheduled another events to be in 10 seconds later
//             tv.setText(words[count]);
//
//            handler.postDelayed(periodicUpdate, 80*(long)(pow((words[count].length())*1.00,1.3)) );
//                    // below is whatever you want to do
//            count++;
//        }
//    };
//
//    static class waitthencalltask extends AsyncTask<Long, Void, Void> {
//        @Override
//        protected Void doInBackground(Long... w) {
//            try {
////                wait(w[0]);
//                Thread.sleep(w[0]);
//                tv.setText(words[count]);
//count++;
//waitthencalltask wtcb=new waitthencalltask();
//wtcb.doInBackground((long) 10000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            return null;
//        }
//
//    }
//
//




}
