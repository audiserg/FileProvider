package ru.ecutula.fileprovider;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.storage.StorageManager;
import android.os.storage.StorageVolume;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
    // Get registered storage

    StorageManager storageManager = getSystemService(context, StorageManager.class);

    return storageManager.getStorageVolumes()!=null?storageManager.getStorageVolumes():new ArrayList<StorageVolume>();
  }

  List<Item> getFiles(String path, String previosDir) {
    // provide files, devices  and if need return element
    List<Item> items = new ArrayList<>();

    if (previosDir != null) {
      items.add(new BackItem("Back", path, previosDir));
    }
    if (path == null) {

      items = storageToItem(getStorage());
    } else {

      File file = new File(path);
      File[] fileArray = file.listFiles();
      if (fileArray == null) {
        Toast.makeText(context, "Please Add Permission", Toast.LENGTH_LONG).show();
        return items;
      }
      for (File element : fileArray) {
        if (element.isDirectory())
          items.add(new DirItem(element.getName(), element.getAbsolutePath()));
        else {
          items.add(
              new FileItem(
                  element.getName(),
                  Long.toString(element.length() / 1000) + " Kb",
                  element.getAbsolutePath()));
        }
      }
    }

    return sorted(items);
  }

  private List<Item> storageToItem(List<StorageVolume> storageVolumes) {
    // Convert storage Volumes to items
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

  private List<Item> sorted(List<Item> list) {
    // Sort
    Collections.sort(
        list,
        new Comparator<Item>() {
          @Override
          public int compare(Item o1, Item o2) {
            // back first
            // dir second
            // file a-b
            if (o2 instanceof BackItem) return 1;
            if (o1 instanceof BackItem) return -1;
            if (o2 instanceof DirItem && o1 instanceof DirItem)
              return o1.getName().compareTo(o2.getName());
            if (o2 instanceof DirItem && o1 instanceof FileItem) return 1;
            if (o1 instanceof DirItem && o2 instanceof FileItem) return -1;
            if (o2 instanceof FileItem && o1 instanceof FileItem)
              return o1.getName().compareTo(o2.getName());

            return 0;
          }
        });

    return list;
  }

  public void startActivityForFile(String filename) {
    // Snippet from git
    File file = new File(filename);
    // File file = new File(file, filename);

    Uri uri = Uri.fromFile(file); // .normalizeScheme();
    String mime = getMimeType(uri.toString());

    // Open file with user selected app

    Intent intent = new Intent();
    intent.setAction(Intent.ACTION_VIEW);
    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

    // this crash without strict mode
    intent.setDataAndType(uri, mime);

    // this start without strict mode but not show
    // intent.setData(uri);
    // intent.setType(mime);
    context.startActivity(intent);
  }

  public String getMimeType(String url) {
    String ext = MimeTypeMap.getFileExtensionFromUrl(url);
    String mime = null;
    if (ext != null) {
      mime = MimeTypeMap.getSingleton().getMimeTypeFromExtension(ext);
    }
    return mime;
  }

  public boolean deleteFile( Item currentItem) {
    File file = new File(currentItem.getPath());
    boolean absolute = file.isAbsolute();
    boolean write = file.canWrite();

    //  boolean success=context.deleteFile();
    //  delete(currentItem.getPath());


      return file.delete();
  }

  public static void delete(String path) {
    //deletion via system call
    File file = new File(path);
    if (file.exists()) {
      String deleteCmd = "rm -r " + path;
      Runtime runtime = Runtime.getRuntime();
      try {
        runtime.exec(deleteCmd);
      } catch (IOException e) {
      }
    }
  }


}
