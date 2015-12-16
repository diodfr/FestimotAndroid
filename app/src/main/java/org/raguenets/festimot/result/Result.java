package org.raguenets.festimot.result;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.common.api.Api;

public class Result implements Parcelable {
    private String definition;
    private String correctAnswer;
    private String userAnswer;
    private int points;

    public Result(String definition, String correctAnswer, String userAnswer, int points) {
        this.definition = notNull(definition);
        this.correctAnswer = notNull(correctAnswer);
        this.userAnswer = notNull(userAnswer);
        this.points = points;
    }

    public String getDefinition() {
        return definition;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public String getUserAnswer() {
        return userAnswer;
    }

    public static String notNull(String field) {
        if (field == null) return "";
        return field;
    }

    public int getPoints() {
        return points;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeString(definition);
        out.writeString(correctAnswer);
        out.writeString(userAnswer);
        out.writeInt(points);
    }

    public static final Parcelable.Creator<Result> CREATOR
            = new Parcelable.Creator<Result>() {
        public Result createFromParcel(Parcel in) {
            return new Result(in);
        }

        public Result[] newArray(int size) {
            return new Result[size];
        }
    };

    private Result(Parcel in) {
        definition = in.readString();
        correctAnswer = in.readString();
        userAnswer = in.readString();
        points = in.readInt();
    }
}
