package uk.ac.shef.oak.com4510.mydatabase;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "cache")
public class CacheEntity {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public String key;
    public String value;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}