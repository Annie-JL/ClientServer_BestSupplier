package models;

import java.io.Serializable;
import java.sql.Date;


public class Order implements Serializable {
    private int id;
    private float score;
    private Date date;
    private String BestS;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBestS() {
        return BestS;
    }

    public void setBestS(String bestS) {
        BestS = bestS;
    }

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
