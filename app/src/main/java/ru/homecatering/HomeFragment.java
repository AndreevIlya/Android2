package ru.homecatering;

import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class HomeFragment extends Fragment {

    private TextView result;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View content = inflater.inflate(R.layout.home_fragment, container, false);
        result = content.findViewById(R.id.result);
        Button button = content.findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("INFO", "clicked");
                PendingIntent pendingIntent = getActivity().createPendingResult(0, new Intent(), 0);
                Intent intent = new Intent(getActivity(), MyService.class);
                intent.putExtra("home", pendingIntent);
                getActivity().startService(intent);
            }
        });
        setRetainInstance(true);
        return content;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i("INFO", "resultCode" + resultCode);
        if (resultCode == 0) {
            Log.i("INFO", data.getStringExtra("answer"));
            String str = data.getStringExtra("answer");
            result.setText(str);
        } else {
            result.setText("smth wrong");
        }
    }
}
