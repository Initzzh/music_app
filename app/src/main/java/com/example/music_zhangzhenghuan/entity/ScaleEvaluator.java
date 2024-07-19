package com.example.music_zhangzhenghuan.entity;

import android.animation.TypeEvaluator;

public class ScaleEvaluator implements TypeEvaluator<Float> {
    @Override
    public Float evaluate(float fraction, Float startValue, Float endValue) {
        if(fraction<0.5){
            return startValue*1.2f;
        }else {
            return endValue;
        }
    }
}
