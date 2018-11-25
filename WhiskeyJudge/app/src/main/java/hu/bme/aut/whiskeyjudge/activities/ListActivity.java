package hu.bme.aut.whiskeyjudge.activities;

import android.arch.persistence.room.Room;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import java.util.List;

import hu.bme.aut.whiskeyjudge.R;
import hu.bme.aut.whiskeyjudge.adapter.WhiskeyAdapter;
import hu.bme.aut.whiskeyjudge.data.WhiskeyItem;
import hu.bme.aut.whiskeyjudge.data.WhiskeyJudgeDatabase;
import hu.bme.aut.whiskeyjudge.fragments.NewWhiskeyItemDialogFragment;

public class ListActivity extends AppCompatActivity
                          implements NewWhiskeyItemDialogFragment.NewWhiskeyItemDialogListener, WhiskeyAdapter.WhiskeyItemClickListener {

    private RecyclerView recyclerView;
    private WhiskeyAdapter adapter;
    private WhiskeyJudgeDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
        ).build();

        initRecyclerView();
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
    public void onItemSelected(final WhiskeyItem item) {
        new AsyncTask<Void, Void, Boolean>() {

            @Override
            protected Boolean doInBackground(Void... voids) {
                Intent intent = new Intent(ListActivity.this, DetailsActivity.class);   //Any item navigate to the DetailsActivity
                startActivity(intent);
                return true;
            }

            @Override
            protected void onPostExecute(Boolean isSuccessful) {
                Log.d("ListActivity", item.name+" was selected.");
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
                Log.d("ListActivity", "WhiskeyItem creation was successful with name: "+ newItem.name);
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
                Log.d("ListActivity", item.name+" was deleted successfully");
            }
        }.execute();
    }       //TODO: wrong element deleted!!!!!!!!!!!!!!!!!!!!!!


}
