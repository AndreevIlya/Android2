package ru.homecatering;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class HomeFragment extends Fragment {

    private TextView text;

    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences pref = getActivity().getPreferences(Context.MODE_PRIVATE);
        String city = pref.getString("city", "No city");
        text.setText(city);
        SharedPreferences.Editor editor = pref.edit();
        editor.remove("city");
        editor.apply();
    }

    @Override
    public void onPause() {
        super.onPause();
        SharedPreferences pref = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("city", "Moscow");
        editor.apply();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View content = inflater.inflate(R.layout.home_fragment, container, false);
        text = content.findViewById(R.id.name);
        return content;
    }
}
