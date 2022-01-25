package com.example.sansinyeong.listener;

import com.example.sansinyeong.model.TalkInfo;

public interface OnPostListener {
    void onDelete(TalkInfo talkInfo);
    void onModify();
}
