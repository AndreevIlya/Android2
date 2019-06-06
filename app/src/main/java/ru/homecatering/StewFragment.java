package ru.homecatering;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

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
        initAddToDB(content);
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
                Log.i("INFO", "clicked");
                showPopUpMenu(content);
            }
        });
    }

    private void showPopUpMenu(View content) {
        LayoutInflater factory = LayoutInflater.from(getActivity());
        final View alertView = factory.inflate(R.layout.popup_window, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(content.getContext());
        builder.setView(alertView);
        builder.setTitle(R.string.add_element_title);
        builder.setNegativeButton(R.string.cancel, null);
        builder.setPositiveButton(R.string.add_element, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                String name = ((TextInputEditText) alertView.findViewById(R.id.name)).getText().toString();
                int price = Integer.parseInt(((TextInputEditText) alertView.findViewById(R.id.price)).getText().toString());
                String image = ((TextInputEditText) alertView.findViewById(R.id.image)).getText().toString();
                if (!name.equals("") && price != 0 && !image.equals("")) {
                    connector.addProduct(name, 4, price, image);
                    productReader.refresh("stew");
                    adapter.notifyDataSetChanged();
                }
            }
        });
        builder.show();
    }
}
