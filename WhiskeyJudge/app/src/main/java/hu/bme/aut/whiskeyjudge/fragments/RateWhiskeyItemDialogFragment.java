package hu.bme.aut.whiskeyjudge.fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;

import hu.bme.aut.whiskeyjudge.R;
import hu.bme.aut.whiskeyjudge.data.WhiskeyItem;

public class RateWhiskeyItemDialogFragment extends DialogFragment {
    public static final String TAG = "RateWhiskeyItemDialogFragment";

    private WhiskeyItem ratingItem;
    private EditText reviewEditText;
    private RatingBar qualityRatingBar;
    private Long id;

    public static RateWhiskeyItemDialogFragment newInstance(WhiskeyItem reteItem) {
        RateWhiskeyItemDialogFragment frag = new RateWhiskeyItemDialogFragment();
        Bundle temp = new Bundle();

        temp.putLong("id", reteItem.id);
        frag.setArguments(temp);
        return frag;
    }

    public interface RateWhiskeyItemDialogListener {
        void onItemChanged(WhiskeyItem ratedItem);
    }

    private RateWhiskeyItemDialogListener listener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        id = getArguments().getLong("id", 0);
        new AsyncTask<Void, Void, Boolean>() {
            @Override
            protected Boolean doInBackground(Void... voids) {
                ratingItem = ((hu.bme.aut.whiskeyjudge.activities.ListActivity) getActivity()).database.whiskeyItemDao().getOne(id);
                return true;
            }

            @Override
            protected void onPostExecute(Boolean bool) {

                //Log.d("onCreate"," We go the item");
            }

        }.execute();
        try {//be kell varni az aszinkron szalat kulonben meghal az oncreatedialog
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        FragmentActivity activity = getActivity();
        if (activity instanceof RateWhiskeyItemDialogFragment.RateWhiskeyItemDialogListener) {
            listener = (RateWhiskeyItemDialogFragment.RateWhiskeyItemDialogListener) activity;
        } else {
            throw new RuntimeException("Activity must implement the RateWhiskeyItemDialogListener interface!");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AlertDialog.Builder(requireContext())
                .setTitle("Reviewing " + ratingItem.name)
                .setView(getContentView())
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (isValid()) {                            //TODO: secondary: write out warning msg, if u wanna validate
                            listener.onItemChanged(getWhiskeyItem());
                        }
                    }
                })
                .setNegativeButton(R.string.cancel, null)
                .create();
    }

    private boolean isValid() {
        return true;  //nincs mit validani ha nem bovitem mas funkcioval
    }

    private View getContentView() {
        View contentView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_rate_whiskey_item, null);

        reviewEditText = contentView.findViewById(R.id.WhiskeyItemReviewEditText);
        if (ratingItem.review!=null)
            reviewEditText.setText(ratingItem.review);
        qualityRatingBar = contentView.findViewById(R.id.WhiskeyItemQualityRatingBar);
        qualityRatingBar.setRating(ratingItem.rating);
       /*
       qualityRatingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                Log.d("OnRatingListener","it has triggered");
                qualityRatingBar.setRating(rating);
            }
        });
        */
        return contentView;
    }

    private WhiskeyItem getWhiskeyItem() {
        ratingItem.review = reviewEditText.getText().toString();
        ratingItem.rating = qualityRatingBar.getRating();

        return ratingItem;      //minden mas marad a regi
    }


}
