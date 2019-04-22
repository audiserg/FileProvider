package ru.ecutula.fileprovider.item;

import ru.ecutula.fileprovider.R;

public class FileItem extends Item {
    public FileItem(String name,String size,String path) {
    setImage(R.drawable.ic_file);
    setName(name);
    setSize(size);
    setPath(path);
    }
}
