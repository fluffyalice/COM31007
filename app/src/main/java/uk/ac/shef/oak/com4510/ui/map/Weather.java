package uk.ac.shef.oak.com4510.ui.map;

/**
 * Weather class
 *
 * @author Feng Li, Ruiqing Xu
 *
 */
public class Weather {

    private int id;
    private String main;
    private String description;
    private String icon;
    public void setId(int id) {
         this.id = id;
     }
     public int getId() {
         return id;
     }

    public void setMain(String main) {
         this.main = main;
     }
     public String getMain() {
         return main;
     }

    public void setDescription(String description) {
         this.description = description;
     }
     public String getDescription() {
         return description;
     }

    public void setIcon(String icon) {
         this.icon = icon;
     }
     public String getIcon() {
         return icon;
     }

}