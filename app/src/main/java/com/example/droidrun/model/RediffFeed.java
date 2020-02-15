package com.example.droidrun.model;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.io.Serializable;
import java.util.List;

import com.example.droidrun.model.rediffitem.RediffItem;

@Root(name = "channel", strict = false)
public class RediffFeed implements Serializable {

    @ElementList(inline = true, name="item" , required=false)
    private List<RediffItem> itemRediff;

    public List<RediffItem> getItemRediff() {
        return itemRediff;
    }

    public void setItemRediff(List<RediffItem> itemRediff) {
        this.itemRediff = itemRediff;
    }





    @Override
    public String toString() {
        return "Feed: \n [itemRed : \n "+itemRediff + " ]";
    }

}
