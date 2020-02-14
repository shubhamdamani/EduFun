package com.example.droidrun;

import android.support.v4.app.Fragment;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.example.droidrun.classification.ClassifierResult;


public class fragment_2 extends Fragment {

    private View mainView;

  //  private DoodleImgClassifier classifier; // complete image classification
    DrawingDesign drawingView; // custom drawing view
    MainActivity parent;
    TextView score;
    TextView timer;
    TextView textViewResult;
    TextView textViewDraw;
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
    public void onCreate(Bundle savedInstanceState) {


        //  for(String s : labelList)
        //    System.out.println(s + "\n");


        super.onCreate(savedInstanceState);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //     this.mainActivity=(MainActivity)getActivity();
        View view = inflater.inflate(R.layout.fragment_2, container, false);
        Button bb1=(Button)view.findViewById(R.id.btn_detect);
        Button bb2=view.findViewById(R.id.btn_next);
        Button bb3=view.findViewById(R.id.btn_clear3);

        try {
            labelList = loadLabelList(getActivity());
        } catch (IOException e) {
            e.printStackTrace();
        }

        bb1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onDetectClick(view);
            }
        });

        bb2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onNextClick(view);
            }
        });

        bb3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClearClick(view);
            }
        });


        parent = (MainActivity)getActivity();

        drawingView = (DrawingDesign) view.findViewById(R.id.drawingView);
        drawingView.init(); // initial drawing view
        score = (TextView) view.findViewById(R.id.user_score);
        textViewResult = (TextView) view.findViewById(R.id.txt_result_label);
        textViewDraw = (TextView) view.findViewById(R.id.txt_draw_label);
        timer = (TextView) view.findViewById(R.id.timer);
        mediaRight = MediaPlayer.create(getContext(), R.raw.tada);
        mediaWrong = MediaPlayer.create(getContext(), R.raw.wrong);
        mediaOver = MediaPlayer.create(getContext(), R.raw.game_over);

        score.setText("90");

        // Timer in background for total play time

        int val = 60;
        Thread timer_thread = new Thread(new Runnable() {
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

        // instantiate classifier
    /*    try {
            parent.classifier = new DoodleImgClassifier(getActivity());
        } catch (IOException e) {
            Log.e("MainActivity", "Cannot initialize tfLite model!", e);
            e.printStackTrace();
        }

        */

        this.mainView = view;//.findViewById(R.id.activity_main).getRootView();

        resetView();

        return  view;
    }







    public void onClearClick(View view) {
        Log.i("MainActivity", "Clear sketch event triggers");
        this.drawingView.clear();

    }

    public void onDetectClick(View view) {
        Log.i("MainActivity", "Detect sketch event triggers");
        if (parent.classifier == null) {
            Log.e("MainActivity", "Cannot initialize tfLite model!");
            return;
        }

        Bitmap sketch = drawingView.getNormalizedBitmap(); // get resized bitmap

        //showImage(drawingView.scaleBitmap(40, sketch));

        // create the result
        ClassifierResult result = parent.classifier.classify(sketch);

        // float mx=-1;

        // render results
        textViewResult.setText("");
        for(int index : result.getTopK()) {
            textViewResult.setText(
                    textViewResult.getText()
                            +"\n"
                            +parent.classifier.getLabel(index)
                            + " ("
                            + String.format("%.02f", parent.classifier.getProbability(index) * 100)
                            + "%)"
            );
        }

        int expectedIndex = parent.classifier.getExpectedIndex();
        if (result.getTopK().contains(expectedIndex)) {
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
        parent.classifier.setExpectedIndex(new Random().nextInt(parent.classifier.getNumberOfClasses()));
        textViewDraw.setText("Draw " + parent.classifier.getLabel(parent.classifier.getExpectedIndex()));
        //   textViewDraw.setText(labelList.get(ind));




    }
}