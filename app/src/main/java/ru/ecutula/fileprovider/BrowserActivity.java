package ru.ecutula.fileprovider;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.StrictMode;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import java.io.File;
import java.util.List;

import ru.ecutula.fileprovider.item.Item;

import static android.support.v7.app.ActionBar.DISPLAY_SHOW_HOME;

public class BrowserActivity extends AppCompatActivity {
    private String currentDir = null;
    private RecyclerView rv;
    public int deep = 0;
    private FileProvider fileProvider = new FileProvider(this);


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("deep", deep);
        outState.putString("currentDir", currentDir);
    }


    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.help, menu);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            deep = savedInstanceState.getInt("deep");
            currentDir = savedInstanceState.getString("currentDir");
        }

        setContentView(R.layout.activity_browser);
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        rv = findViewById(R.id.RV);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        rv.setLayoutManager(manager);
        getPermission();
        List<Item> items = null;
        if (savedInstanceState == null || deep == 0) {
            items = fileProvider.getFiles(null, currentDir);
            deep = 0;
        } else {

            items = fileProvider.getFiles(currentDir, new File(currentDir).getParent());
        }
        ItemAdapter adapter = new ItemAdapter(this, items);
        rv.setAdapter(adapter);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int itemThatWasClickedId = item.getItemId();
        if (itemThatWasClickedId == R.id.action_search) {
            // DO (4) Remove the Toast message when the search menu item is clicked
            new AlertDialog.Builder(this)
                    .setTitle(R.string.helptile)
                    .setIcon(android.R.drawable.ic_menu_help)
                    .setMessage(R.string.helpinfo)
                    .show();

            return true;
        }
        return super.onOptionsItemSelected(item);
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

    public void setCurrentDir(String currentDir) {
        this.currentDir = currentDir;
    }
}
