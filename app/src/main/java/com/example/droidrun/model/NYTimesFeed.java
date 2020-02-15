package com.example.droidrun.model;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import java.io.Serializable;

import com.example.droidrun.model.channel.channel;


@Root(name = "rss", strict = false)
public class NYTimesFeed implements Serializable {

    @Element(name = "channel")
    private channel mChannel;

    public channel getmChannel() {
        return mChannel;
    }

    public void setmChannel(channel mChannel) {
        this.mChannel = mChannel;
    }

    @Override
    public String toString() {
     //   return "Feed: \n [Channel : \n "+mChannel + " ]";
        return "Feed : channel : "+mChannel+"\n";
    }
}
