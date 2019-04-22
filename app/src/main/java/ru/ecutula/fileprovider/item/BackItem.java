package ru.ecutula.fileprovider.item;

import ru.ecutula.fileprovider.R;

public class BackItem extends Item {
 public BackItem(String name,String path) {
        setImage(R.drawable.ic_back_24dp);
        setName(name);
        setSize("");
        setPath(path);
    }
}
