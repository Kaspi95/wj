package hu.bme.aut.whiskeyjudge.data;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;

@Database(
        entities = {WhiskeyItem.class},
        version = 7
)
@TypeConverters(value = {WhiskeyItem.Category.class})
public abstract class WhiskeyJudgeDatabase extends RoomDatabase {
    public abstract WhiskeyItemDao whiskeyItemDao();
}

