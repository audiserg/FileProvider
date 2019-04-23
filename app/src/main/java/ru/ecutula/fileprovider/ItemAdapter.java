package ru.ecutula.fileprovider;

import android.content.Context;
import android.content.DialogInterface;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.List;

import ru.ecutula.fileprovider.item.BackItem;
import ru.ecutula.fileprovider.item.DeviceItem;
import ru.ecutula.fileprovider.item.DirItem;
import ru.ecutula.fileprovider.item.FileItem;
import ru.ecutula.fileprovider.item.Item;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {
  public List<Item> getItems() {
    return items;
  }

  public void setItems(List<Item> items) {
    this.items = items;
  }

  private List<Item> items;
  private LayoutInflater inflater;
  private Context context;
  private BrowserActivity activity;

  public ItemAdapter(Context context, List<Item> items) {
    this.items = items;
    this.context = context;
    this.activity = (BrowserActivity) context;
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
      // Log.d(">>"," position-> "+position);
      // Prevent exception, when fast click
      if (position < 0) position = 0;

      Item currentItem = items.get(position);
      if (currentItem instanceof DirItem
          || currentItem instanceof BackItem
          || currentItem instanceof DeviceItem) {

        adapterListUpdate(currentItem);
        return;
      }
      // if item is file
      activity.getFileProvider().startActivityForFile(currentItem.getPath());
    }

    @Override
    public boolean onLongClick(View v) {
      int position = getAdapterPosition();
      Item currentItem = items.get(position);
      if (currentItem instanceof FileItem || currentItem instanceof DirItem)
        deleteFileDialog(currentItem);
      return false;
    }

    private void deleteFileDialog( final Item currentItem) {
      String type =
          currentItem instanceof FileItem
              ? "file"
              : currentItem instanceof DirItem ? "folder" : "device";
      android.support.v7.app.AlertDialog.Builder builder =
          new android.support.v7.app.AlertDialog.Builder(context)
              .setTitle("DELETE FILE?")
              .setIcon(android.R.drawable.ic_dialog_alert)
              .setCancelable(true)
              .setNegativeButton("Cancel", null)
              .setPositiveButton(
                  "OK",
                  new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                      boolean success = false;
                      success = activity.getFileProvider().deleteFile(currentItem);
                      if (success) {
                        items.remove(currentItem);
                        notifyDataSetChanged();
                      } else Toast.makeText(context, "Sorry, i can't delete this file", Toast.LENGTH_SHORT).show();
                    }
                  });

      if (!type.equals("file"))
        builder.setMessage("Are you want delete " + type + " " + currentItem.getName() + "\n");
      else
        builder.setMessage(
            "Are you want delete  "
                + type
                + "\nFile: "
                + getNameAndExt(currentItem.getName())
                + "\n");
      builder.show();
    }

    void adapterListUpdate(Item item) {
      // update view and get new files list from FileProvider
      // variable deep  used for check directory tree and prevent out of border
      String currentDir = new File(item.getPath()).getParent();
      List<Item> itemList;
      if (!(item instanceof BackItem)) {
        itemList = activity.getFileProvider().getFiles(item.getPath(), currentDir);
        activity.deep++;
      } else {
        if (--activity.deep > 0)
          itemList = activity.getFileProvider().getFiles(item.getPath(), currentDir);
        else {
          itemList = activity.getFileProvider().getFiles(null, null);
          activity.deep = 0;
        }
      }
      setItems(itemList);
      notifyDataSetChanged();
    }
  }

  private String getNameAndExt(String fullname) {
    int index = fullname.lastIndexOf('.');
    if (index == -1) return fullname;
    String filename = fullname.substring(0, index);
    String extension = fullname.substring(index).replace(".", "");
    String add = "";
    // if (extension!=null||extension!="")
    add = "\nExtention: " + extension;
    return filename + add;
  }
}
