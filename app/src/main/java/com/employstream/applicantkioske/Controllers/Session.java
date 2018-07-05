package com.employstream.applicantkioske.Controllers;

import android.graphics.Color;

import org.json.JSONObject;

public class Session {
    private static Session session;
    private String access_token = "";

    public static Session getSession() {
        if (session == null) {
            session = new Session();
        }

        return session;
    }

    public void startSession(JSONObject session) {
        try {
            this.access_token = session.getString("access_token");
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public int hex2Rgb(String colorStr) {
        return Color.argb(255, Integer.valueOf( colorStr.substring( 1, 3 ), 16 ), Integer.valueOf( colorStr.substring( 3, 5 ), 16 ), Integer.valueOf( colorStr.substring( 5, 7 ), 16 ));
    }
}