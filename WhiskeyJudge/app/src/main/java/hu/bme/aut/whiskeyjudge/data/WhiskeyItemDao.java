package hu.bme.aut.whiskeyjudge.data;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface WhiskeyItemDao {
    @Query("SELECT * FROM whiskeyitem")
    List<WhiskeyItem> getAll();

    @Insert
    long insert(WhiskeyItem whiskeyItems);

    @Update
    void update(WhiskeyItem whiskeyItem);

    @Delete
    void deleteItem(WhiskeyItem whiskeyItem);
}
