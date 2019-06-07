package ru.homecatering;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.Locale;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {
    private ProductDBReader productDBReader;

    ProductAdapter(ProductDBReader productDBReader) {
        Log.i("INFO", "Adapter created");
        this.productDBReader = productDBReader;
    }

    @NonNull
    @Override
    public ProductAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.product_card, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        viewHolder.bind(productDBReader.getPosition(position));
    }

    @Override
    public int getItemCount() {
        Log.i("INFO", "adapter item count" + productDBReader.getCount());
        return productDBReader.getCount();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView image;
        private TextView title;
        private TextView price;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            Log.i("INFO", "VH created");
            image = itemView.findViewById(R.id.product_image);
            title = itemView.findViewById(R.id.product_title);
            price = itemView.findViewById(R.id.product_price);
        }

        void bind(Product product) {
            title.setText(product.getName());
            Log.i("INFO", "VH bound");
            price.setText(String.format(Locale.getDefault(), "%d", product.getPrice()));
            Log.i("INFO", "VH bound");
            Picasso.get().load(product.getImage()).into(image);
            Log.i("INFO", "VH bound");
        }
    }
}
