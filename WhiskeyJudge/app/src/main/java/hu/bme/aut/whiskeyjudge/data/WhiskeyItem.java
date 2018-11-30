package hu.bme.aut.whiskeyjudge.data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverter;

@Entity(tableName = "whiskeyitem")
public class WhiskeyItem {
    public enum Category {
        SCOTTISH, IRISH, BOURBON, CANADIAN;

        @TypeConverter
        public static Category getByOrdinal(int ordinal) {
            Category ret = null;
            for (Category cat : Category.values()) {
                if (cat.ordinal() == ordinal) {
                    ret = cat;
                    break;
                }
            }
            return ret;
        }

        @TypeConverter
        public static int toInt(Category category) {
            return category.ordinal();
        }
    }

    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = true)
    public Long id;

    @ColumnInfo(name = "name")
    public String name;

    @ColumnInfo(name = "description")
    public String description;

    @ColumnInfo(name = "category")
    public Category category;

    @ColumnInfo(name = "estimated_price")
    public int estimatedPrice;

    @ColumnInfo(name = "alcohol_percentage")
    public int alcoholPercentage;

    @ColumnInfo(name = "review")
    public String review;

    @ColumnInfo(name = "rating")
    public float rating;
}