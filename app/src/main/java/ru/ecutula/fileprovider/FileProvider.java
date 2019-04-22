package ru.ecutula.fileprovider;

import android.content.Context;
import android.os.storage.StorageManager;
import android.os.storage.StorageVolume;
import android.widget.Toast;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import java.util.ArrayList;
import java.util.List;

import ru.ecutula.fileprovider.item.BackItem;
import ru.ecutula.fileprovider.item.DeviceItem;
import ru.ecutula.fileprovider.item.DirItem;
import ru.ecutula.fileprovider.item.FileItem;
import ru.ecutula.fileprovider.item.Item;

import static android.support.v4.content.ContextCompat.getSystemService;

public class FileProvider {

  private Context context;

  public FileProvider(Context context) {
    this.context = context;
  }

  private List<StorageVolume> getStorage() {
    StorageManager storageManager = getSystemService(context, StorageManager.class);

    return storageManager.getStorageVolumes();
  }

  List<Item> GetFiles(String path,String previosDir) {
    List<Item> items = new ArrayList<>();

    if(previosDir!=null){
        items.add(new BackItem("Back",previosDir));
    }
    if (path == null) {

      items = storageToItem(getStorage());
    }else {

        File file=new File(path);
        File[] fileArray=file.listFiles();
        if(fileArray==null){
            Toast.makeText(context,"Please Add Permission",Toast.LENGTH_LONG).show();
            return items;
        }
        for( File element:fileArray ){
            if (element.isDirectory())items.add(new DirItem(element.getName(),element.getAbsolutePath()));
            else {
                items.add(new FileItem(element.getName(),Long.toString(element.getTotalSpace()),element.getAbsolutePath()));

            }
        }

    }

    return items;
  }

  private List<Item> storageToItem(List<StorageVolume> storageVolumes) {
    List<Item> items = new ArrayList<>();
    Method methodGetPath = null;
    Class<?> StorageVolume = null;

    try {
      StorageVolume = Class.forName("android.os.storage.StorageVolume");
      methodGetPath = StorageVolume.getDeclaredMethod("getPath");

    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    } catch (NoSuchMethodException e) {
      e.printStackTrace();
    }

    for (StorageVolume volume : storageVolumes) {
      String path = "";

      try {
        path = (String) methodGetPath.invoke(volume);
      } catch (IllegalAccessException e) {
        e.printStackTrace();
      } catch (InvocationTargetException e) {
        e.printStackTrace();
      }
      if (volume.isRemovable()) items.add(new DeviceItem("SDCard", volume.toString(), path));
      else items.add(new DeviceItem("Phone", volume.toString(), path));
    }
    return items;
  }
}
