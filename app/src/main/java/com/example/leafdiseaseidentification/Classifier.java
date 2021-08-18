package com.example.leafdiseaseidentification;



import android.graphics.Bitmap;
import android.graphics.RectF;
import java.util.List;



//classifier interface for interactingn with recognition
public interface Classifier {

    public class Recognition {
        //unique identifier for image class
        private final String id;
        //name of detected image
        private final String title;
//confidence score for teh disease detected
        private final Float confidence;
        private RectF location; //location of image under development


        //constructor
        public Recognition(
                final String id, final String title, final Float confidence, final RectF location) {
            this.id = id;
            this.title = title;
            this.confidence = confidence;
            this.location = location;
        }

        //getters and setters

        public String getId() {
            return id;
        }

        public String getTitle() {
            return title;
        }

        public Float getConfidence() {
            return confidence;
        }

        public RectF getLocation() {
            return new RectF(location);
        }

        public void setLocation(RectF location) {
            this.location = location;
        }

        @Override
        public String toString() {
            String resultString = "";
            if (id != null) {
                resultString += "[" + id + "] ";
            }

            if (title != null) {
                resultString += title + " ";
            }

            if (confidence != null) {
                resultString += String.format("(%.1f%%) ", confidence * 100.0f);
            }

            if (location != null) {
                resultString += location + " ";
            }

            return resultString.trim();
        }
    }

    List<Recognition> recognizeImage(Bitmap bitmap);

}
