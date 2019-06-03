package ru.homecatering;

import android.content.Context;
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
        text.setText(getActivity().getPreferences(Context.MODE_PRIVATE).getString("city", "No city"));
    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().getPreferences(Context.MODE_PRIVATE).edit().putString("city", "Moscow").apply();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View content = inflater.inflate(R.layout.home_fragment, container, false);
        text = content.findViewById(R.id.name);
        return content;
    }
}
