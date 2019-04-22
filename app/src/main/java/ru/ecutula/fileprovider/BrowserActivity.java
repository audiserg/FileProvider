package ru.ecutula.fileprovider;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.io.File;
import java.util.List;

import ru.ecutula.fileprovider.item.Item;

public class BrowserActivity extends AppCompatActivity {
    private String currentDir =null;
    private  RecyclerView rv;
    private FileProvider fileProvider=new FileProvider(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browser);

        rv=findViewById(R.id.RV);
        LinearLayoutManager manager=new LinearLayoutManager(this);
        rv.setLayoutManager(manager);
        List<Item> items=null;
        if(savedInstanceState==null) items= fileProvider.GetFiles(null, currentDir);
            else items= fileProvider.GetFiles(currentDir,new File(currentDir).getParent());
        ItemAdapter adapter=new ItemAdapter(this,items);
        rv.setAdapter(adapter);




    }

    public RecyclerView getRv() {
        return rv;
    }

    void viewUpdate(Item item){
         currentDir =new File(item.getPath()).getParent();
        List<Item> items= new FileProvider(this).GetFiles(item.getPath(), currentDir);
        ((ItemAdapter)rv.getAdapter()).setItems(items);
        rv.getAdapter().notifyDataSetChanged();
        }

}
