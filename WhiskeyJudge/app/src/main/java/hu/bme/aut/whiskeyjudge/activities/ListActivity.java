package hu.bme.aut.whiskeyjudge.activities;

import android.arch.persistence.room.Room;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import java.util.List;

import hu.bme.aut.whiskeyjudge.R;
import hu.bme.aut.whiskeyjudge.adapter.WhiskeyAdapter;
import hu.bme.aut.whiskeyjudge.data.WhiskeyItem;
import hu.bme.aut.whiskeyjudge.data.WhiskeyJudgeDatabase;
import hu.bme.aut.whiskeyjudge.fragments.ModifyWhiskeyItemDialogFragment;
import hu.bme.aut.whiskeyjudge.fragments.NewWhiskeyItemDialogFragment;
import hu.bme.aut.whiskeyjudge.fragments.RateWhiskeyItemDialogFragment;

public class ListActivity extends AppCompatActivity
                          implements    NewWhiskeyItemDialogFragment.NewWhiskeyItemDialogListener,
                                        WhiskeyAdapter.WhiskeyItemClickListener,
                                        ModifyWhiskeyItemDialogFragment.ModifyWhiskeyItemDialogListener,
                                        RateWhiskeyItemDialogFragment.RateWhiskeyItemDialogListener{

    private RecyclerView recyclerView;
    private WhiskeyAdapter adapter;
    private WhiskeyJudgeDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new NewWhiskeyItemDialogFragment().show(getSupportFragmentManager(), NewWhiskeyItemDialogFragment.TAG);
            }
        });

        database = Room.databaseBuilder(
                getApplicationContext(),
                WhiskeyJudgeDatabase.class,
                "whiskey-list"
        )       .fallbackToDestructiveMigration()
                .build();

        initRecyclerView();
    }

    public  void requestItemChanging(WhiskeyItem changingItem){
        new ModifyWhiskeyItemDialogFragment().show(getSupportFragmentManager(), ModifyWhiskeyItemDialogFragment.TAG);
    }

    @Override
    public void onWhiskeyItemRate(WhiskeyItem ratedItem) {
        new RateWhiskeyItemDialogFragment().show(getSupportFragmentManager(), RateWhiskeyItemDialogFragment.TAG);
    }

    private void initRecyclerView() {
        recyclerView = findViewById(R.id.MainRecyclerView);
        adapter = new WhiskeyAdapter(this);
        loadItemsInBackground();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    private void loadItemsInBackground() {
        new AsyncTask<Void, Void, List<WhiskeyItem>>() {

            @Override
            protected List<WhiskeyItem> doInBackground(Void... voids) {
                return database.whiskeyItemDao().getAll();
            }

            @Override
            protected void onPostExecute(List<WhiskeyItem> whiskeyItems) {
                adapter.update(whiskeyItems);
            }
        }.execute();
    }

    @Override
    public void onItemChanged(final WhiskeyItem item) {
        Log.d("ListActivity", "WhiskeyItem "+item.name+" want to update");
        new AsyncTask<Void, Void, Boolean>() {
            List<WhiskeyItem> items;

            @Override
            protected Boolean doInBackground(Void... voids) {
                database.whiskeyItemDao().update(item);
                items=database.whiskeyItemDao().getAll();
                return true;
            }

            @Override
            protected void onPostExecute(Boolean isSuccessful) {
               adapter.update(items);
               Log.d("ListActivity", "WhiskeyItem update was successful");
            }
        }.execute();
    }

    @Override
    public void onWhiskeyItemCreated(final WhiskeyItem newItem) {
        new AsyncTask<Void, Void, WhiskeyItem>() {

            @Override
            protected WhiskeyItem doInBackground(Void... voids) {
                newItem.id = database.whiskeyItemDao().insert(newItem);
                return newItem;
            }

            @Override
            protected void onPostExecute(WhiskeyItem whiskeyItem) {
                adapter.addItem(whiskeyItem);
                Log.d("ListActivity", "WhiskeyItem creation was successful");
            }
        }.execute();
    }

    @Override
    public void onItemDeleted(final WhiskeyItem item) {
        new AsyncTask<Void, Void, Boolean>() {
            @Override
            protected Boolean doInBackground(Void... voids) {
                database.whiskeyItemDao().deleteItem(item);
                return true;
            }

            @Override
            protected void onPostExecute(Boolean isSuccessful) {
                adapter.deleteItem(item);
                Log.d("ListActivity", "WhiskeyItem delete was successful");
            }
        }.execute();
    }


}
