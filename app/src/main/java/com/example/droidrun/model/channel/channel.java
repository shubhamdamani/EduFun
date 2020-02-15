package com.example.droidrun.model.channel;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.io.Serializable;
import java.util.List;

@Root(name = "channel", strict = false)                 //this class is basically based on tags of xml of rss feeds for fetching
public class channel implements Serializable {          // channel is the parent tag and sub tags are the elementsdescribed below

    @Element(name = "language")
    private String language;

    @Element(name = "copyright")
    private String copyright;

    @Element(name = "lastBuildDate")
    private String lastbuilddate;

    @ElementList(inline = true, name = "item")          // this itself is an array so for this we will create another array
    private List<Item> items;                           //main saman issi m h


    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getCopyright() {
        return copyright;
    }

    public void setCopyright(String copyright) {
        this.copyright = copyright;
    }

    public String getLastbuilddate() {
        return lastbuilddate;
    }

    public void setLastbuilddate(String lastbuilddate) {
        this.lastbuilddate = lastbuilddate;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    @Override
    public String toString() {
        return "Items : "+items+"\n";
    }

}
