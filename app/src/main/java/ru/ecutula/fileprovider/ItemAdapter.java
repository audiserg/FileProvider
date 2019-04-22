package ru.ecutula.fileprovider;

import android.app.AlertDialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import ru.ecutula.fileprovider.item.BackItem;
import ru.ecutula.fileprovider.item.DeviceItem;
import ru.ecutula.fileprovider.item.DirItem;
import ru.ecutula.fileprovider.item.Item;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {
  private List<Item> items;
  private LayoutInflater inflater;
  private Context context;

  private ItemAdapter(Context context, List<Item> items) {
    this.items = items;
    this.context = context;
    this.inflater = LayoutInflater.from(context);
  }

  @NonNull
  @Override
  public ItemAdapter.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
    View view = inflater.inflate(R.layout.itemlayout, viewGroup, false);
    return new ItemViewHolder(view, i);
  }

  @Override
  public void onBindViewHolder(@NonNull ItemAdapter.ItemViewHolder itemViewHolder, int i) {
    Item item = items.get(i);
    itemViewHolder.imageView.setImageResource(item.getImage());
    itemViewHolder.nameView.setText(item.getName());
    itemViewHolder.sizeView.setText(item.getSize());
  }

  @Override
  public int getItemCount() {
    return items.size();
  }

  class ItemViewHolder extends RecyclerView.ViewHolder
      implements View.OnClickListener, View.OnLongClickListener {
    ImageView imageView;
    TextView nameView;
    TextView sizeView;
    // Context contex;
    int pos;

    private ItemViewHolder(@NonNull View itemView, int i) {
      super(itemView);
      this.pos = i;
      imageView = itemView.findViewById(R.id.Image);
      nameView = itemView.findViewById(R.id.name);
      sizeView = itemView.findViewById(R.id.size);
      itemView.setOnClickListener(this);
      itemView.setOnLongClickListener(this);
    }

    @Override
    public void onClick(View v) {
      int position = getAdapterPosition();
      Item currentItem = items.get(position);
      // Toast.makeText(v.getContext(),"Pressed "+position,Toast.LENGTH_SHORT).show();
      if (currentItem instanceof DirItem
          || currentItem instanceof BackItem
          || currentItem instanceof DeviceItem) {
        BrowserActivity browserActivity = (BrowserActivity) ItemAdapter.this.context;
        browserActivity.viewUpdate(currentItem);
      }
    }

    @Override
    public boolean onLongClick(View v) {
        int position = getAdapterPosition();
        Item currentItem = items.get(position);

        new android.support.v7.app.AlertDialog.Builder(context).setMessage("Delete File?").show();
        Toast.makeText(v.getContext(), "Pressed long " + getAdapterPosition(), Toast.LENGTH_SHORT)
          .show();

      return false;
    }
  }
}
