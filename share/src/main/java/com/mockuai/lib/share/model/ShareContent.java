package com.mockuai.lib.share.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.mockuai.lib.share.annotate.ShareContentTypeDef;

/**
 * Created by zhangyuan on 16/3/21.
 */
public class ShareContent implements Parcelable {

    public static final int TEXT = 1;

    public static final int IMAGE = 2;

    public static final int MUSIC = 3;

    public static final int VIDEO = 4;

    public static final int WEB_PAGE = 5;

    private int type;

    private String title;

    private String text;

    private String url;

    private String imageUrl;

    public ShareContent(Builder builder) {
        this.type = builder.type;
        this.title = builder.title;
        this.text = builder.text;
        this.url = builder.url;
        this.imageUrl = builder.imageUrl;
    }

    protected ShareContent(Parcel in) {
        type = in.readInt();
        title = in.readString();
        text = in.readString();
        url = in.readString();
        imageUrl = in.readString();
    }

    public static final Creator<ShareContent> CREATOR = new Creator<ShareContent>() {
        @Override
        public ShareContent createFromParcel(Parcel in) {
            return new ShareContent(in);
        }

        @Override
        public ShareContent[] newArray(int size) {
            return new ShareContent[size];
        }
    };

    public int getType() {
        return type;
    }

    public void setType(@ShareContentTypeDef int type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(type);
        dest.writeString(title);
        dest.writeString(text);
        dest.writeString(url);
        dest.writeString(imageUrl);
    }

    public static class Builder {

        private int type;

        private String title;

        private String text;

        private String url;

        private String imageUrl;

        public Builder type(@ShareContentTypeDef int type) {
            this.type = type;
            return this;
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder text(String text) {
            this.text = text;
            return this;
        }

        public Builder url(String url) {
            this.url = url;
            return this;
        }

        public Builder imageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
            return this;
        }

        public ShareContent build() {
            return new ShareContent(this);
        }
    }

}
