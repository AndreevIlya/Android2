package ru.homecatering;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;

public class StewFragment extends Fragment {
    private ProductAdapter adapter;
    private ProductDBConnector connector;
    private ProductDBReader productReader;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View content = inflater.inflate(R.layout.stew_fragment, container, false);
        initSource();
        initProducts(content);
        return content;
    }

    private void initSource() {
        this.connector = new ProductDBConnector(getActivity().getApplicationContext());
        connector.open("stew");
        this.productReader = connector.getProductDBReader();
    }

    private void initProducts(View content) {
        RecyclerView recycler = content.findViewById(R.id.products_wrapper);
        GridLayoutManager grid = new GridLayoutManager(getActivity(), 2);
        recycler.setLayoutManager(grid);
        adapter = new ProductAdapter(productReader);
        recycler.setAdapter(adapter);
    }

    private void initAddToDB(final View content) {
        Button button = content.findViewById(R.id.addToDBButton);
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                showPopUpMenu(content);
            }
        });
    }

    private void showPopUpMenu(View content) {
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View popupContent = inflater.inflate(R.layout.popup_window, null, false);
        PopupWindow popupWindow = new PopupWindow(popupContent, 100, 100, true);
        popupWindow.showAtLocation(content, Gravity.CENTER, 0, 0);
        Button button = popupContent.findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = ((TextInputEditText) popupContent.findViewById(R.id.name)).getText().toString();
                int price = Integer.parseInt(((TextInputEditText) popupContent.findViewById(R.id.price)).getText().toString());
                String image = ((TextInputEditText) popupContent.findViewById(R.id.image)).getText().toString();
                connector.addProduct(name, 4, price, image);
                productReader.refresh("stew");
            }
        });
    }
}
