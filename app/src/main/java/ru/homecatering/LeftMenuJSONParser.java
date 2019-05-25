package ru.homecatering;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.util.JsonReader;
import android.util.JsonToken;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class LeftMenuJSONParser {
    private Activity activity;
    private List<LeftMenuModel> menuHeader = new ArrayList<>();
    private Map<LeftMenuModel, List<LeftMenuModel>> menuChildren = new HashMap<>();

    LeftMenuJSONParser(Activity activity) {
        this.activity = activity;
        try {
            InputStream is = activity.getResources().openRawResource(R.raw.left_menu);
            JsonReader reader = new JsonReader(new InputStreamReader(is, StandardCharsets.UTF_8));
            readOuter(reader);
            is.close();
        } catch (IOException e) {
            Log.e("JSON_ERROR", "Error while reading json.", e);
        }
    }

    private void readOuter(JsonReader reader) throws IOException {
        reader.beginArray();
        while (reader.hasNext()) {
            String id = "", text = "";
            Drawable drawable = null;
            List<LeftMenuModel> lmc = null;
            reader.beginObject();
            while (reader.hasNext()) {
                switch (reader.nextName()) {
                    case "id":
                        id = reader.nextString();
                        break;
                    case "text":
                        text = activity.getString(activity.getResources().getIdentifier(reader.nextString(), "string", activity.getPackageName()));
                        break;
                    case "icon":
                        drawable = activity.getDrawable(activity.getResources().getIdentifier(reader.nextString(), "drawable", activity.getPackageName()));
                        break;
                    case "children":
                        if (reader.peek() != JsonToken.NULL) {
                            lmc = readChild(reader);
                        } else {
                            reader.skipValue();
                        }
                        break;
                }
            }
            reader.endObject();
            LeftMenuModel lmOuter = new LeftMenuModel(text, id, drawable, lmc != null);
            menuHeader.add(lmOuter);
            menuChildren.put(lmOuter, lmc);
        }
        reader.endArray();
    }

    private List<LeftMenuModel> readChild(JsonReader reader) throws IOException {
        List<LeftMenuModel> lmc = new ArrayList<>();
        String id = "", text = "";
        Drawable drawable = null;
        reader.beginArray();
        while (reader.hasNext()) {
            reader.beginObject();
            while (reader.hasNext()) {
                switch (reader.nextName()) {
                    case "id":
                        id = reader.nextString();
                        break;
                    case "text":
                        text = activity.getString(activity.getResources().getIdentifier(reader.nextString(), "string", activity.getPackageName()));
                        break;
                    case "icon":
                        drawable = activity.getDrawable(activity.getResources().getIdentifier(reader.nextString(), "drawable", activity.getPackageName()));
                        break;
                }
            }
            reader.endObject();
            lmc.add(new LeftMenuModel(text, id, drawable, false));
        }
        reader.endArray();
        return lmc;
    }

    List<LeftMenuModel> getMenuHeader(){
        return menuHeader;
    }

    Map<LeftMenuModel,List<LeftMenuModel>> getMenuChildren(){
        return menuChildren;
    }
}
