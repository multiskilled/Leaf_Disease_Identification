/* Copyright 2016 The TensorFlow Authors. All Rights Reserved.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
==============================================================================*/

package com.example.leafdiseaseidentification;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.os.Trace;

import org.tensorflow.Operation;
import org.tensorflow.contrib.android.TensorFlowInferenceInterface;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Vector;

//CLASSIFIER CLASS FOR LOADING, LABELLING IMAGES USING TENSORFLOW
public class ImageClassifierTf implements Classifier {


  // Only return this many results with at least this confidence.
  private static final int MAXIMUM_RESULTS = 3;
  private static final float THRESHOLD = 0.1f;


  //DECLARING THE VALUES
  private String inputName;
  private String outputName;
  private int inputSize;
  private int imageMean;
  private float imageStd;

//BUFFERS
  //PRE-ALLOCATED
  private Vector<String> labels = new Vector<String>(); //FOR STORIGN LABELS
  private int[] intValues;
  private float[] floatValues;    //FLOAT VALUE FOR SIZE INPUT
  private float[] outputs;        //FLOAT VALUE FOR SIZE OUTPUT
  private String[] outputNames;       //STRING TYPE FOR OUTPUT NAME
  private TensorFlowInferenceInterface inferenceInterface; //DELCLARING TENSORFLOW INFERENCE AP
  private boolean logStats = false; //FOR LOG STATUS


  private ImageClassifierTf() {}
  //INITIALIZING THE TENSOR VALUES AND ASSETS
  public static Classifier create(
      AssetManager assetManager, //ASSET MANAGER FOR LOADING ASSETS
      String modelFilename,     //FILE LOCATION OF MODEL FILE NAME
      String labelFilename,     //FILE LOCATION OF LABEL FILE
      int inputSize,          //INPUT SIZE OF IMAGE - SIZEOFINPUT * SIZEOFINPUT
      int imageMean,        // ASSUMED MEAN VALUE FOR IMAGE
      float imageStd,           //ASSUMED STANDARD DEVIATION VALUE FOR IMAGE
      String inputName,         //LABELS FOR INPUT IMAGE
      String outputName) {          //LABEL FOR OUTPUT IMAGE
    ImageClassifierTf OBJ = new ImageClassifierTf();
    OBJ.inputName = inputName;
    OBJ.outputName = outputName;


    OBJ.inferenceInterface = new TensorFlowInferenceInterface(assetManager, modelFilename);

    BufferedReader br = null;
    try {
      br = new BufferedReader(new InputStreamReader(assetManager.open(labelFilename)));
      String line;
      while ((line = br.readLine()) != null) {
        OBJ.labels.add(line);
      }
      br.close();
    } catch (IOException e) {
      throw new RuntimeException("ERROR READING LABEL" , e);
    }



    // The shape of the output is [N, NUM_CLASSES], where N is the batch size.
    final Operation operation = OBJ.inferenceInterface.graphOperation(outputName);
    final int numClasses = (int) operation.output(0).shape().size(1);
    ///Log.i(TAG, "Read " + OBJ.labels.size() + " labels, output layer size is " + numClasses);

    // Ideally, inputSize could have been retrieved from the shape of the input operation.  Alas,
    // the placeholder node for input in the graphdef typically used does not specify a shape, so it
    // must be passed in as a parameter.
    OBJ.inputSize = inputSize;
    OBJ.imageMean = imageMean;
    OBJ.imageStd = imageStd;

    // Pre-allocate buffers.
    OBJ.outputNames = new String[] {outputName};
    OBJ.intValues = new int[inputSize * inputSize];
    OBJ.floatValues = new float[inputSize * inputSize * 3];
    OBJ.outputs = new float[numClasses];

    return OBJ;
  }

  @Override
  public List<Recognition> recognizeImage(final Bitmap bitmap) {
    // Log this method so that it can be analyzed with systrace.
    Trace.beginSection("recognizeImage");

    Trace.beginSection("preprocessBitmap");
    // Preprocess the image data from 0-255 int to normalized float based
    // on the provided parameters.
    bitmap.getPixels(intValues, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());
    for (int i = 0; i < intValues.length; ++i) {
      final int val = intValues[i];
      floatValues[i * 3 + 0] = (((val >> 16) & 0xFF) - imageMean) / imageStd;
      floatValues[i * 3 + 1] = (((val >> 8) & 0xFF) - imageMean) / imageStd;
      floatValues[i * 3 + 2] = ((val & 0xFF) - imageMean) / imageStd;
    }
    Trace.endSection();

    // Copy the input data into TensorFlow.
    Trace.beginSection("feed");
    inferenceInterface.feed(inputName, floatValues, 1, inputSize, inputSize, 3);
    Trace.endSection();

    // Run the inference call.
    Trace.beginSection("run");
    inferenceInterface.run(outputNames, logStats);
    Trace.endSection();

    // Copy the output Tensor back into the output array.
    Trace.beginSection("fetch");
    inferenceInterface.fetch(outputName, outputs);
    Trace.endSection();

    // Find the best classifications.
    PriorityQueue<Recognition> pq =
        new PriorityQueue<Recognition>(
            3,
            new Comparator<Recognition>() {
              @Override
              public int compare(Recognition lhs, Recognition rhs) {
                // Intentionally reversed to put high confidence at the head of the queue.
                return Float.compare(rhs.getConfidence(), lhs.getConfidence());
              }
            });
    for (int i = 0; i < outputs.length; ++i) {
      if (outputs[i] > THRESHOLD) {
        pq.add(
            new Recognition(
                "" + i, labels.size() > i ? labels.get(i) : "unknown", outputs[i], null));
      }
    }
    final ArrayList<Recognition> recognitions = new ArrayList<Recognition>();
    int recognitionsSize = Math.min(pq.size(), MAXIMUM_RESULTS);
    for (int i = 0; i < recognitionsSize; ++i) {
      recognitions.add(pq.poll());
    }
    Trace.endSection(); // "recognizeImage"
    return recognitions;
  }


}
