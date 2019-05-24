package ru.homecatering;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.JsonReader;
import android.util.JsonToken;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class LeftMenuAdapter extends BaseExpandableListAdapter {
    private Activity activity;
    private List<LeftMenuModel> lmHeader = new ArrayList<>();
    private Map<LeftMenuModel, List<LeftMenuModel>> lmChildren = new HashMap<>();

    LeftMenuAdapter(Activity activity) {
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
            lmHeader.add(lmOuter);
            lmChildren.put(lmOuter, lmc);
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

    @Override
    public int getGroupCount() {
        return this.lmHeader.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        List<LeftMenuModel> lmc = this.lmChildren.get(getGroup(groupPosition));
        return lmc == null ? 0 : lmc.size();
    }

    @Override
    public LeftMenuModel getGroup(int groupPosition) {
        return lmHeader.get(groupPosition);
    }

    @Override
    public LeftMenuModel getChild(int groupPosition, int childPosition) {
        return lmChildren.get(getGroup(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        LeftMenuModel lmm = getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.left_menu_item, null);
        }

        convertView.setTag(lmm.getId());
        ImageView icon = convertView.findViewById(R.id.icon);
        icon.setImageDrawable(lmm.getIcon());
        icon.setContentDescription(lmm.getText());
        TextView text = convertView.findViewById(R.id.text);
        text.setText(lmm.getText());
        if (lmm.hasChildren) {
            if (isExpanded) {
                convertView.findViewById(R.id.more).setVisibility(View.INVISIBLE);
                convertView.findViewById(R.id.less).setVisibility(View.VISIBLE);
            } else {
                convertView.findViewById(R.id.more).setVisibility(View.VISIBLE);
                convertView.findViewById(R.id.less).setVisibility(View.INVISIBLE);
            }
        }
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        LeftMenuModel lmm = getChild(groupPosition, childPosition);
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.left_menu_item_inner, null);
        }

        convertView.setTag(lmm.getId());
        ImageView icon = convertView.findViewById(R.id.icon);
        icon.setImageDrawable(lmm.getIcon());
        icon.setContentDescription(lmm.getText());
        TextView text = convertView.findViewById(R.id.text);
        text.setText(lmm.getText());
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
