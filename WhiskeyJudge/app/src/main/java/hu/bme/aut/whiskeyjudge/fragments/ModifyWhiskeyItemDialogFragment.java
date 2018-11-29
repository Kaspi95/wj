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
import android.widget.Spinner;

import hu.bme.aut.whiskeyjudge.R;
import hu.bme.aut.whiskeyjudge.data.WhiskeyItem;

public class ModifyWhiskeyItemDialogFragment  extends DialogFragment {

    public static final String TAG = "ModifyWhiskeyItemDialogFragment";

    private EditText nameEditText;
    private EditText descriptionEditText;
    private EditText estimatedPriceEditText;
    private Spinner categorySpinner;
    private EditText alcoholPercentageEditText;
    //private EditText reviewEditText;

    private ModifyWhiskeyItemDialogListener listener;

    public interface ModifyWhiskeyItemDialogListener {
        void onItemChanged(WhiskeyItem Item);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentActivity activity = getActivity();
        if (activity instanceof ModifyWhiskeyItemDialogFragment.ModifyWhiskeyItemDialogListener) {
            listener = (ModifyWhiskeyItemDialogFragment.ModifyWhiskeyItemDialogListener) activity;
        } else {
            throw new RuntimeException("Activity must implement the ModifyWhiskeyItemDialogListener interface!");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AlertDialog.Builder(requireContext())
                .setTitle("Modify details")    //TODO:primary: atirni a kivalasztott nevere itt
                .setView(getContentView())              //TODO:primary: felulirni a behelyettesiteseket itt
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (isValid()) {
                            listener.onItemChanged(getWhiskeyItem());
                        }
                    }
                })
                .setNegativeButton(R.string.cancel, null)
                .create();
    }

    private boolean isValid() {
        return nameEditText.getText().length() > 0;
    }       //TODO:secondary: improve validation

    private WhiskeyItem getWhiskeyItem() {
        WhiskeyItem whiskeyItem = new WhiskeyItem();
        whiskeyItem.name = nameEditText.getText().toString();
        whiskeyItem.description = descriptionEditText.getText().toString();
       // whiskeyItem.review = reviewEditText.getText().toString();
        try {
            whiskeyItem.estimatedPrice = Integer.parseInt(estimatedPriceEditText.getText().toString());
        } catch (NumberFormatException e) {
            whiskeyItem.estimatedPrice = 0;
        }
        try {
            whiskeyItem.alcoholPercentage = Integer.parseInt(alcoholPercentageEditText.getText().toString());
        } catch (NumberFormatException e) {
            whiskeyItem.alcoholPercentage = 0;
        }
        whiskeyItem.category = WhiskeyItem.Category.getByOrdinal(categorySpinner.getSelectedItemPosition());
        return whiskeyItem;
    }

    private View getContentView() {
        View contentView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_modify_whiskey_item, null);
        nameEditText = contentView.findViewById(R.id.WhiskeyItemNameEditText);
        descriptionEditText = contentView.findViewById(R.id.WhiskeyItemDescriptionEditText);
        estimatedPriceEditText = contentView.findViewById(R.id.WhiskeyItemEstimatedPriceEditText);
        categorySpinner = contentView.findViewById(R.id.WhiskeyItemCategorySpinner);
        categorySpinner.setAdapter(new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_spinner_dropdown_item,
                getResources().getStringArray(R.array.category_items)));
        alcoholPercentageEditText = contentView.findViewById(R.id.WhiskeyItemAlcoholPercentageEditText);
        //reviewEditText = contentView.findViewById(R.id.WhiskeyItemReviewEditText);

        return contentView;
    }

}


