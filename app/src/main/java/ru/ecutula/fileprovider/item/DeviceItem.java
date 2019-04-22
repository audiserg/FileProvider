package ru.ecutula.fileprovider.item;



import ru.ecutula.fileprovider.R;

public class DeviceItem extends Item {
    public DeviceItem(String name,String size,String path) {
    setImage(R.drawable.ic_sd_storage);
    setName(name);
    setSize("");
    setPath(path);
    }
}
