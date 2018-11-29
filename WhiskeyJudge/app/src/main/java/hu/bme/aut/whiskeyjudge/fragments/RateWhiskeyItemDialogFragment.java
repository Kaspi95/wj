package hu.bme.aut.whiskeyjudge.fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RatingBar;

import hu.bme.aut.whiskeyjudge.R;
import hu.bme.aut.whiskeyjudge.adapter.WhiskeyAdapter;
import hu.bme.aut.whiskeyjudge.data.WhiskeyItem;

public class RateWhiskeyItemDialogFragment extends DialogFragment {
    public static final String TAG = "RateWhiskeyItemDialogFragment";

    private EditText reviewEditText;
    private RatingBar QuantityRatingBar;

    public interface RateWhiskeyItemDialogListener {
        void onItemChanged(WhiskeyItem ratedItem);
    }

    private RateWhiskeyItemDialogListener listener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
                .setTitle("Reviewing")
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
        return true;
    }      //TODO:secondary: make validation

    private View getContentView() {
        View contentView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_rate_whiskey_item, null);
        //TODO:primary: star rating, and previus values
        reviewEditText = contentView.findViewById(R.id.WhiskeyItemReviewEditText);
        QuantityRatingBar = contentView.findViewById(R.id.WhiskeyItemQualityRatingBar);
        return contentView;
    }

    private WhiskeyItem getWhiskeyItem() {
        WhiskeyItem whiskeyItem = new WhiskeyItem();//TODO:primary: ne irja felul az alapadatokat!!! - a parameterben kapottatt hasznalja fel
        //TODO:primary: star ratingbar
        whiskeyItem.review = reviewEditText.getText().toString();
        // whiskeyItem.

        return whiskeyItem;
    }


}
