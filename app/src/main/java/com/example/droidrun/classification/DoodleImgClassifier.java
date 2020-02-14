package com.example.droidrun.classification;

import android.app.Activity;
import android.graphics.Bitmap;
import android.util.Log;
import org.tensorflow.lite.Interpreter;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.MappedByteBuffer;
import java.util.ArrayList;



public class DoodleImgClassifier {

    private static final String MODEL_FILE = "100/model100.tflite";
    private static final String LABELS_FILE = "100/labels.csv";

    private static final int DIM_BATCH_SIZE = 1;
    public static final int DIM_IMG_SIZE_HEIGHT = 28;
    public static final int DIM_IMG_SIZE_WIDTH = 28;
    private static final int DIM_PIXEL_SIZE = 1;

    private ArrayList<String> labelsList; // list of all labels
    private ByteBuffer inputImgData = null; // models input format
    private Interpreter tflite; // the model
    private int[] imagePixels = new int[DIM_IMG_SIZE_HEIGHT * DIM_IMG_SIZE_WIDTH];
    private final float[][] result; // depending on models architecture (possible multiple output)

    private int requiredIndex; // random label index which is to be drawn

    public DoodleImgClassifier(Activity activity) throws IOException {

        // load model
        MappedByteBuffer modelBuffered = ClassifierInput.loadModel(activity, MODEL_FILE);
        Log.i("Model ", "" + modelBuffered.isLoaded());
        this.tflite = new Interpreter(modelBuffered);

        // load labelsList
        this.labelsList = ClassifierInput.readImageLabels(activity, LABELS_FILE);
        this.result = new float[1][labelsList.size()];

        // allocate memory for model input
        this.inputImgData = ByteBuffer.allocateDirect(4 * DIM_BATCH_SIZE * DIM_IMG_SIZE_HEIGHT * DIM_IMG_SIZE_WIDTH * DIM_PIXEL_SIZE);
        this.inputImgData.order(ByteOrder.nativeOrder());

        Log.i("Model", "Successfully created a Tensorflow Lite doodle recognition model.");
    }

    public ClassifierResult classify(Bitmap bitmap) {
        convertBitmapToByteBuffer(bitmap); // flatten bitmap to byte array
        tflite.run(inputImgData, result); // classify task
        return new ClassifierResult(result[0], labelsList); // create the result
    }

    private void convertBitmapToByteBuffer(Bitmap bitmap) {
        if (inputImgData == null) {
            return;
        }
        inputImgData.rewind();

        bitmap.getPixels(imagePixels, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());

        int pixel = 0;
        for (int i = 0; i < DIM_IMG_SIZE_WIDTH; ++i) {
            for (int j = 0; j < DIM_IMG_SIZE_HEIGHT; ++j) {
                final int color = imagePixels[pixel++];
                inputImgData.putFloat((((color >> 16) & 0xFF) + ((color >> 8) & 0xFF) + (color & 0xFF)) / 3.0f / 255.0f);
            }
        }
    }


    public void setExpectedIndex(int index) {
        this.requiredIndex = index;
    }


    public int getExpectedIndex() {
        return requiredIndex;
    }

    public float getProbability(int index) {
        return result[0][index];
    }

    public String getLabel(int index) {
        return labelsList.get(index);
    }

    public int getNumberOfClasses() {
        return labelsList.size();
    }
}

