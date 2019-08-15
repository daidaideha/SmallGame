package com.lyl.game.bean;

/**
 * create lyl on 2019-08-13
 * </p>
 */
public class FoodBean {

    private PointBean pointBean;
    private int score;

    public FoodBean(PointBean pointBean) {
        this.pointBean = pointBean;
    }

    public FoodBean(PointBean pointBean, int score) {
        this.pointBean = pointBean;
        this.score = score;
    }

    public PointBean getPointBean() {
        return pointBean;
    }

    public void setPointBean(PointBean pointBean) {
        this.pointBean = pointBean;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
