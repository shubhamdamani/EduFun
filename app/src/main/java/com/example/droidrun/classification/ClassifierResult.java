package com.example.droidrun.classification;

import android.util.ArrayMap;
import android.util.Log;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Readable representation of the models output vector.
 * Contains labelName, labelName position in result vector and prob.
 */
public class ClassifierResult {
    private String labelName;
    private int labelIndex;
    private float prob;
    private List<Integer> topKList; // contains the index

    public ClassifierResult(float[] result, ArrayList<String> labels) {
        this.labelIndex = findMaxProbIndex(result); // set index position
        this.prob = result[labelIndex]; // set prob
        this.labelName = labels.get(labelIndex); // search for labelName
        this.topKList = getTopkLabels(5, result);

    }

    // find the index with the maximum prob
    private int findMaxProbIndex(float[] result) {
        float maxProb = 0.0f;
        int maxIndex = -1;
        for (int i = 0; i < result.length; i++) {
            if (result[i] > maxProb) {
                maxProb = result[i];
                maxIndex = i;
            }
        }
        if (maxIndex == -1) {
            Log.e("Result class", "findMaxProbIndex found no maximum");
        }
        return maxIndex;
    }


    // returns the top k labels with prob
    private List<Integer> getTopkLabels(int k, float[] result) {
        List<Integer> topKList = new LinkedList<>();
        for (int j = 0; j < k; j++) {
            float maxProb = 0.0f;
            int maxIndex = -1;
            for (int i = 0; i < result.length; i++) {
                if (!topKList.contains(i)) {
                    if (result[i] > maxProb) {
                        maxProb = result[i];
                        maxIndex = i;
                    }
                }
            }
            topKList.add(maxIndex);
        }

        return topKList;
    }

    public String getLabel() { return this.labelName; }

    public float getProbability() {
        return this.prob;
    }

    public List<Integer> getTopK() {
        return this.topKList;
    }

    public int getLabelIndex() {
        return this.labelIndex;
    }
}

