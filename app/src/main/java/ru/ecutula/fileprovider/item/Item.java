package ru.ecutula.fileprovider.item;

public abstract class Item {
     private int image=0;
     private String name="";
     private String size="";
     private String path="";

     public String getPath() {
          return path;
     }

     public void setPath(String path) {
          this.path = path;
     }

     public int getImage() {
          return image;
     }

     public void setImage(int image) {
          this.image = image;
     }

     public String getName() {
          return name;
     }

     public void setName(String name) {
          this.name = name;
     }

     public String getSize() {
          return size;
     }

     public void setSize(String size) {
          this.size = size;
     }



}
