package com.example.droidrun.model;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import java.io.Serializable;

import com.example.droidrun.model.channelYahoo.channelYahoo;

@Root(name = "channel", strict = false)
public class YahooFeed implements Serializable {

    @Element(name = "channel")
    private channelYahoo mChannel;

    public channelYahoo getmChannel() {
        return mChannel;
    }

    public void setmChannel(channelYahoo mChannel) {
        this.mChannel = mChannel;
    }

    @Override
    public String toString() {
        //   return "Feed: \n [Channel : \n "+mChannel + " ]";
        return "Feed : channel : "+mChannel+"\n";
    }






}
