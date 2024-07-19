package com.example.music_zhangzhenghuan.entity;

import android.animation.TypeEvaluator;

public class ScaleEvaluator2 implements TypeEvaluator<Float> {


    @Override
    public Float evaluate(float fraction, Float startValue, Float endValue) {
        if(fraction < 0.5f){
            return startValue * 0.8f;
        }else
        {
            return endValue;
        }
    }
}
