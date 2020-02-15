package com.example.droidrun.model;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import java.io.Serializable;

@Root(name = "rss", strict = false)                                                 //rediff ki parent class, here parent tag is channel
public class MainRediffFeed implements Serializable {

    @Element(name = "channel")
    private RediffFeed mChannel;

    public RediffFeed getmChannel() {
        return mChannel;
    }

    public void setmChannel(RediffFeed mChannel) {
        this.mChannel = mChannel;
    }

    @Override
    public String toString() {
        //   return "Feed: \n [Channel : \n "+mChannel + " ]";
        return "Feed : channel : "+mChannel+"\n";
    }
}

