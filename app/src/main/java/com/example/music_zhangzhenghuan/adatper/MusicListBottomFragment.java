package com.example.music_zhangzhenghuan.adatper;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.music_zhangzhenghuan.R;
import com.example.music_zhangzhenghuan.entity.MusicInfo;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.List;

public class MusicListBottomFragment extends BottomSheetDialogFragment {


    List<MusicInfo> curMusicInfoList;

    TextView curMusicListSize;
    ConstraintLayout muicModeView;
    ImageView musicModeImage;
    TextView muiscModeText;

    private int curPlayMode = 0;

    RecyclerView musicListRecycle;
    MusicListRecycleAdapter musicListRecycleAdapter;


    public MusicListBottomFragment(List<MusicInfo> curMusicInfoList,int curPlayMode){
        this.curMusicInfoList = curMusicInfoList;
        this.curPlayMode = curPlayMode;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_music_list, container, false);

        curMusicListSize = view.findViewById(R.id.cur_music_list_size);
        curMusicListSize.setText(curMusicInfoList.size()+"");

         muicModeView = view.findViewById(R.id.music_mode_view);
         musicModeImage = view.findViewById(R.id.music_mode_image);
         muiscModeText = view.findViewById(R.id.music_mode_text);

        switch (curPlayMode){
            case 0:
                musicModeImage.setImageResource(R.drawable.seq_black);
                muiscModeText.setText("顺序播放");
                break;
            case 1:
                musicModeImage.setImageResource(R.drawable.loop_black);
                muiscModeText.setText("循环播放");
            case 2:
                musicModeImage.setImageResource(R.drawable.rand_black);
                muiscModeText.setText("随机播放");
        }

        RecyclerView musicListRecycle = view.findViewById(R.id.music_list_recycle);
        MusicListRecycleAdapter musicListRecycleAdapter = new MusicListRecycleAdapter(curMusicInfoList);
        musicListRecycle.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        musicListRecycle.setAdapter(musicListRecycleAdapter);
        return  view;

    }


}
