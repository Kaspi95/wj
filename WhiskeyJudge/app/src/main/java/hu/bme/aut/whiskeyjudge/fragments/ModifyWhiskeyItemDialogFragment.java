package hu.bme.aut.whiskeyjudge.fragments;

import android.app.Dialog;
import android.app.ListActivity;
import android.content.Context;
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
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.List;

import hu.bme.aut.whiskeyjudge.R;
import hu.bme.aut.whiskeyjudge.data.WhiskeyItem;

public class ModifyWhiskeyItemDialogFragment extends DialogFragment {
    public static final String TAG = "ModifyWhiskeyItemDialogFragment";

    private WhiskeyItem changingItem;
    private EditText nameEditText;
    private EditText descriptionEditText;
    private EditText estimatedPriceEditText;
    private Spinner categorySpinner;
    private EditText alcoholPercentageEditText;
    private Long id;


    public static ModifyWhiskeyItemDialogFragment newInstance(WhiskeyItem changeItem) {
        ModifyWhiskeyItemDialogFragment frag = new ModifyWhiskeyItemDialogFragment();
        Bundle temp = new Bundle();

        temp.putLong("id", changeItem.id);
        frag.setArguments(temp);
        return frag;
    }

    private ModifyWhiskeyItemDialogListener listener;

    public interface ModifyWhiskeyItemDialogListener {
        void onItemChanged(WhiskeyItem Item);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        id = getArguments().getLong("id", 0);
        new AsyncTask<Void, Void, Boolean>() {
            @Override
            protected Boolean doInBackground(Void... voids) {
                changingItem = ((hu.bme.aut.whiskeyjudge.activities.ListActivity) getActivity()).database.whiskeyItemDao().getOne(id);
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
                .setTitle("Modify " + changingItem.name + "'s details")
                .setView(getContentView())
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

        return nameEditText.getText().length() > 0                          //TODO:secondary:s improve validation
                // && Integer.parseInt(alcoholPercentageEditText.getText().toString())<=100 &&
                //Integer.parseInt(alcoholPercentageEditText.getText().toString())>=0 &&
                //Integer.parseInt(estimatedPriceEditText.getText().toString())>=0
                ;

    }

    private WhiskeyItem getWhiskeyItem() {

        changingItem.name = nameEditText.getText().toString();
        changingItem.description = descriptionEditText.getText().toString();

        try {
            changingItem.estimatedPrice = Integer.parseInt(estimatedPriceEditText.getText().toString());
        } catch (NumberFormatException e) {
            changingItem.estimatedPrice = 0;
        }
        try {
            changingItem.alcoholPercentage = Integer.parseInt(alcoholPercentageEditText.getText().toString());
        } catch (NumberFormatException e) {
            changingItem.alcoholPercentage = 0;
        }
        changingItem.category = WhiskeyItem.Category.getByOrdinal(categorySpinner.getSelectedItemPosition());
        return changingItem;
    }


    private View getContentView() {
        View contentView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_modify_whiskey_item, null);

        nameEditText = contentView.findViewById(R.id.WhiskeyItemNameEditText);
        nameEditText.setText(changingItem.name);
        descriptionEditText = contentView.findViewById(R.id.WhiskeyItemDescriptionEditText);
        if (!changingItem.description.isEmpty())
            descriptionEditText.setText(changingItem.description);
        estimatedPriceEditText = contentView.findViewById(R.id.WhiskeyItemEstimatedPriceEditText);
        estimatedPriceEditText.setText(Integer.toString(changingItem.estimatedPrice));
        categorySpinner = contentView.findViewById(R.id.WhiskeyItemCategorySpinner);
        categorySpinner.setAdapter(new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_spinner_dropdown_item,
                getResources().getStringArray(R.array.category_items)));
        categorySpinner.setSelection(changingItem.category.toInt(changingItem.category));
        alcoholPercentageEditText = contentView.findViewById(R.id.WhiskeyItemAlcoholPercentageEditText);
        alcoholPercentageEditText.setText(Integer.toString(changingItem.alcoholPercentage));                //TODO a tobbinel is tostring kell!!!!!!
        return contentView;
    }

}






