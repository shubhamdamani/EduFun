package com.example.droidrun;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.droidrun.classification.ClassifierResult;
import com.example.droidrun.classification.DoodleImgClassifier;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SinglePlayer extends AppCompatActivity {

    private View mainView;

    DrawingDesign drawingView; // custom drawing view

    TextView score;
    TextView timer;
    TextView textViewResult;
    TextView textViewDraw;
    Thread timer_thread;
    Button det,cle,nex;
    private DoodleImgClassifier classifier;
    MediaPlayer mediaRight, mediaWrong, mediaOver;
    private static final String LABEL_PATH = "100/labels.csv";
    private List<String> labelList;
    private List<String> loadLabelList(Activity activity) throws IOException {
        int ind = -1;
        List<String> labelList = new ArrayList<String>();
        BufferedReader reader =
                new BufferedReader(new InputStreamReader(activity.getAssets().open(LABEL_PATH)));
        String line;
        while ((line = reader.readLine()) != null) {
            ind++;
            if (ind < 10)
                labelList.add(line.substring(2));
            else
                labelList.add(line.substring(3));
        }
        reader.close();
        return labelList;
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_player);

        det=findViewById(R.id.mybtn_detect);
        nex=findViewById(R.id.mybtn_next);
        cle=findViewById(R.id.mybtn_clear3);
        score=findViewById(R.id.myscore);



        drawingView = (DrawingDesign) findViewById(R.id.mydrawingView);
        drawingView.init();

        textViewResult = (TextView) findViewById(R.id.mytxt_result_label);
        textViewDraw = (TextView) findViewById(R.id.mytxt_draw_label);
        timer = (TextView) findViewById(R.id.mytimer);
        mediaRight = MediaPlayer.create(this, R.raw.tada);
        mediaWrong = MediaPlayer.create(this, R.raw.wrong);
        mediaOver = MediaPlayer.create(this, R.raw.game_over);


        try {
            labelList = loadLabelList(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
        int val = 60;
        timer_thread = new Thread(new Runnable() {
            @Override
            public void run() {
                int total = 60;
                // timer.setText(Integer.toString(total));

                try {
                    //  System.out.println(total);
                    while (total >= 0) {

                        timer.setText(Integer.toString(total));
                        Thread.sleep(1000);
                        total--;
                    }

                    mediaOver.start();


                    // Do some stuff
                } catch (Exception e) {
                    e.getLocalizedMessage();
                }
            }

        });

        timer_thread.start();

        nex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onNextClick(v);
            }
        });

        cle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClearClick(v);
            }
        });

        det.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDetectClick(v);
            }
        });

       // this.mainView = view;//.findViewById(R.id.activity_main).getRootView();

        //resetView();

      //  return  view;
        try {
            this.classifier = new DoodleImgClassifier(this);
        } catch (IOException e) {
            Log.e("MainActivity", "Cannot initialize tfLite model!", e);
            e.printStackTrace();
        }

        this.mainView = this.findViewById(R.id.activity_single_player).getRootView();

        resetView();

    }

    @Override
    public void onBackPressed() {
        timer_thread.interrupt();
        Log.i("MainActivity", "done");

        super.onBackPressed();
    }

    public void onClearClick(View view) {
        Log.i("MainActivity", "Clear sketch event triggers");
        this.drawingView.clear();

    }

    public void onDetectClick(View view) {
        Log.i("MainActivity", "Detect sketch event triggers");
        if (classifier == null) {
            Log.e("MainActivity", "Cannot initialize tfLite model!");
            return;
        }

        Bitmap sketch = drawingView.getNormalizedBitmap(); // get resized bitmap

        //showImage(drawingView.scaleBitmap(40, sketch));

        // create the result
        ClassifierResult result =classifier.classify(sketch);

        // float mx=-1;

        // render results
        textViewResult.setText("");
        for(int index : result.getTopK()) {
            textViewResult.setText(
                    textViewResult.getText()
                            +"\n"
                            +classifier.getLabel(index)
                            + " ("
                            + String.format("%.02f", classifier.getProbability(index) * 100)
                            + "%)"
            );
        }

        int expectedIndex = classifier.getExpectedIndex();
        if (result.getTopK().contains(expectedIndex)) {

            String s=score.getText().toString();
            StringBuilder sn=new StringBuilder(s);
            sn.replace(0,1,Character.toString((char)(sn.charAt(0)+1)));
            String u=sn.toString();
           // sn.reverse();
           // s=sn.toString();
           // Toast.makeText(getContext(),s,Toast.LENGTH_LONG).show();
            score.setText(u);


            //mainActivity.sendReceive.write(s.getBytes());

            mediaRight.start();
            mainView.setBackgroundColor(Color.rgb(78,175,36));
        } else {
            mainView.setBackgroundColor(Color.rgb(204, 0,0));
            mediaWrong.start();
        }

    }




    public void onNextClick(View view) {
        resetView();
    }

    // debug: ImageView with rescaled 28x28 bitmap
  /*  private void showImage(Bitmap bitmap) {
        Dialog builder = new Dialog(this);
        builder.requestWindowFeature(Window.FEATURE_NO_TITLE);
        builder.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
            }
        });

        ImageView imageView = new ImageView(this);
        imageView.setImageBitmap(bitmap);
        builder.addContentView(imageView, new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        builder.show();
    }

    */


    private void resetView() {
        mainView.setBackgroundColor(Color.WHITE);
        drawingView.clear();
        textViewResult.setText("");

        // get a random label and set as expected class
        //    static int ind=0;
        //     ind++;
       classifier.setExpectedIndex(new Random().nextInt(classifier.getNumberOfClasses()));
        textViewDraw.setText("Draw " + classifier.getLabel(classifier.getExpectedIndex()));
        //   textViewDraw.setText(labelList.get(ind));




    }
}
