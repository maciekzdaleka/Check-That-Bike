package com.example.maciek.myapplication;

/**
 * Created by Maciek on 13/03/2017.
 */

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;

public class CSGetResult extends GenericJson {
    @Key
    private String status;

    @Key
    private String name;

    public String getStatus() {
        return status;
    }

    public String getName() {
        return name;
    }
}