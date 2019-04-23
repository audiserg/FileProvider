package ru.ecutula.fileprovider;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.io.File;
import java.util.List;

import ru.ecutula.fileprovider.item.Item;

public class BrowserActivity extends AppCompatActivity {
  private String currentDir = null;
  private RecyclerView rv;
  public int deep = 0;
  private FileProvider fileProvider = new FileProvider(this);

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_browser);
    StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
    StrictMode.setVmPolicy(builder.build());
    rv = findViewById(R.id.RV);
    LinearLayoutManager manager = new LinearLayoutManager(this);
    rv.setLayoutManager(manager);
    getPermission();
    List<Item> items = null;
    if (savedInstanceState == null) {
      items = fileProvider.getFiles(null, currentDir);
      deep = 0;
    } else items = fileProvider.getFiles(currentDir, new File(currentDir).getParent());
    ItemAdapter adapter = new ItemAdapter(this, items);
    rv.setAdapter(adapter);
  }

  public RecyclerView getRv() {
    return rv;
  }

  public FileProvider getFileProvider() {
    return fileProvider;
  }

  boolean getPermission() {
    int permis = checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);

    if (permis == PackageManager.PERMISSION_GRANTED) {
      return true;
    }

    // android.permission.WRITE_MEDIA_STORAGE

    String[] array = {
      Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    requestPermissions(array, 0);
    return true;
  }
}
