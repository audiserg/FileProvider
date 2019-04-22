package ru.ecutula.fileprovider.item;

import ru.ecutula.fileprovider.R;

public class DirItem extends Item {
    public DirItem(String name,String path) {
        setImage(R.drawable.ic_folder);
        setName(name);
        setSize("");
        setPath(path);
    }
}
