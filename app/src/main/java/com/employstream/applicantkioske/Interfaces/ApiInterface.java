package com.employstream.applicantkioske.Interfaces;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by matthewbowen on 6/10/18.
 */

public interface ApiInterface {
    void onRequestComplete(String request, JSONObject requestData);
}
