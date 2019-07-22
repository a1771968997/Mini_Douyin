package com.example.mini_douyin.bean;

import com.google.gson.annotations.SerializedName;

public class PostVideoResponse {
    @SerializedName("success")
    private boolean successs;
    @SerializedName("result")
    private Feed result;

    public boolean isSuccesss() {
        return successs;
    }

    public void setSuccesss(boolean successs) {
        this.successs = successs;
    }

    public Feed getResult() {
        return result;
    }

    public void setResult(Feed result) {
        this.result = result;
    }
}
