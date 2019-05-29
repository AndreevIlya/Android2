package ru.homecatering;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Objects;

public class HomeFragment extends Fragment {
    private boolean bound;
    private ServiceConnection sConn = initServiceConnection();
    private ServiceBound service;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View content = inflater.inflate(R.layout.home_fragment, container, false);

        content.findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bound) Objects.requireNonNull(getActivity()).unbindService(sConn);
            }
        });
        if (bound) service.doWork();
        return content;
    }

    @Override
    public void onStart() {
        super.onStart();
        Objects.requireNonNull(getActivity()).bindService(new Intent(getActivity(), ServiceBound.class), sConn, Service.BIND_AUTO_CREATE);
    }

    @Override
    public void onStop() {
        super.onStop();
        Objects.requireNonNull(getActivity()).unbindService(sConn);
    }

    ServiceConnection initServiceConnection() {
        return new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                HomeFragment.this.service = ((ServiceBound.CustomBinder) service).getBoundService();
                bound = true;
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                bound = false;
            }
        };
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
