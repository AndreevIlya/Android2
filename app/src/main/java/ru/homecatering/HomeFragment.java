package ru.homecatering;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class HomeFragment extends Fragment {

    private View.OnClickListener listener = initListener();
    private View content;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        content = inflater.inflate(R.layout.home_fragment, container, false);
        content.findViewById(R.id.button).setOnClickListener(listener);
        return content;
    }

    private View.OnClickListener initListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AsyncTaskMaker maker = new AsyncTaskMaker(new AsyncTaskMaker.OnTaskListener() {
                    @Override
                    public void onStart() {
                        ((TextView) content.findViewById(R.id.result)).setText("Going to start.");
                    }

                    @Override
                    public void onStatusProgress(String string) {
                        ((TextView) content.findViewById(R.id.result)).setText(string);
                    }

                    @Override
                    public void onComplete() {
                        ((TextView) content.findViewById(R.id.result)).setText("Tired of a hard work done. Need a drink.");
                    }
                });
                maker.doWork(24);
            }
        };
    }
}
