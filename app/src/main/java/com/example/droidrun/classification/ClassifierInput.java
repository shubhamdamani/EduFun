package com.example.droidrun.classification;



import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Arrays;
import android.app.Activity;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.util.Log;
import com.example.droidrun.MainActivity;


/**
 * Helper class to read TensorFlow model and labels from file
 */
abstract class ClassifierInput {

    /*
     * Reads the compressed model as MappedByteBuffer from file.
     *
     */
    public static MappedByteBuffer loadModel(Activity activity, String model) throws IOException {
        AssetManager assetManager = activity.getAssets();
        AssetFileDescriptor fileDescriptor = assetManager.openFd(model);
        FileInputStream inputStream = new FileInputStream(fileDescriptor.getFileDescriptor());
        FileChannel fileChannel = inputStream.getChannel();
        long startOffset = fileDescriptor.getStartOffset();
        long declaredLength = fileDescriptor.getDeclaredLength();
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength);
    }

    /*
     * Read labels from file to an array list of labels.
     * This list represents the mapping from index position to label.
     *
     */
    public static ArrayList readImageLabels(Activity activity, String File) {
        AssetManager assetManager = activity.getAssets();
        ArrayList<String> result = new ArrayList<>();
        try {
            InputStream is = assetManager.open(File);
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line;
            while ((line = br.readLine()) != null) {
                String[] arr  = line.split(",");
                result.add(arr[1]);
            }

            return result;
        } catch (IOException ex) {
            throw new IllegalStateException("Cannot read labels from " + File + " " + ex);
        }
    }
}


