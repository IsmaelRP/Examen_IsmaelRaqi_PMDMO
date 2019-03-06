package com.practica.ismael.examen_ismaelraqi_pmdmo.ui.fragment_book;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.practica.ismael.examen_ismaelraqi_pmdmo.R;
import com.practica.ismael.examen_ismaelraqi_pmdmo.base.EventObserver;
import com.practica.ismael.examen_ismaelraqi_pmdmo.base.YesNoDialogFragment;
import com.practica.ismael.examen_ismaelraqi_pmdmo.data.RepositoryImpl;
import com.practica.ismael.examen_ismaelraqi_pmdmo.data.local.AppDatabase;
import com.practica.ismael.examen_ismaelraqi_pmdmo.data.local.model.Book;
import com.practica.ismael.examen_ismaelraqi_pmdmo.databinding.FragmentBookFormBinding;
import com.practica.ismael.examen_ismaelraqi_pmdmo.ui.activity.MainViewModel;
import com.practica.ismael.examen_ismaelraqi_pmdmo.utils.KeyboardUtils;
import com.practica.ismael.examen_ismaelraqi_pmdmo.utils.SnackbarUtils;
import com.squareup.picasso.Picasso;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

public class BooksFormFragment extends Fragment implements YesNoDialogFragment.Listener {

    private BooksFormViewModel vm;
    private FragmentBookFormBinding b;
    private MainViewModel mainVM;
    private final String PLACEHOLDER = "https://via.placeholder.com/400/09f/fff.png";
    private boolean dialog;
    private boolean valid = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        b = DataBindingUtil.inflate(inflater, R.layout.fragment_book_form, container, false);
        return b.getRoot();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setupViews();
        observeResultMessages();
        observeSettings();
    }

    private void observeSettings() {
        mainVM.getShowConfirm().observe(getViewLifecycleOwner(), this::showDialog);
    }

    private void showDialog(Boolean bool) {
        this.dialog = bool;
    }

    private void observeResultMessages() {
        vm.getErrorMessage().observe(getViewLifecycleOwner(),
                new EventObserver<>(v -> showMessage("Book's name already exists!")));
        vm.getSuccessMessage().observe(getViewLifecycleOwner(),
                new EventObserver<>(v -> showMessageBack()));
    }

    private void showMessageBack() {
        Snackbar.make(requireView(), "Book inserted successfully", Snackbar.LENGTH_SHORT).addCallback(
                new BaseTransientBottomBar.BaseCallback<Snackbar>() {
                    @Override
                    public void onDismissed(Snackbar transientBottomBar, int event) {
                        super.onDismissed(transientBottomBar, event);
                        requireActivity().onBackPressed();
                    }
                }).show();
    }


    private void showMessage(String message) {
        Snackbar.make(requireView(), message, Snackbar.LENGTH_SHORT).show();
    }

    private void setupViews() {
        Objects.requireNonNull(((AppCompatActivity) Objects.requireNonNull(getActivity())).getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        Objects.requireNonNull(((AppCompatActivity) getActivity()).getSupportActionBar()).setDisplayShowHomeEnabled(true);

        Picasso picasso = Picasso.get();

        mainVM = ViewModelProviders.of(requireActivity()).get(MainViewModel.class);
        vm = ViewModelProviders.of(this,
                new BooksFormViewModelFactory(requireActivity().getApplication(),
                        new RepositoryImpl(AppDatabase.getInstance(requireContext().getApplicationContext())
                                .bookDao()))).get(BooksFormViewModel.class);

        picasso.load(PLACEHOLDER)
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .into(b.imgCover);

        b.txtUrl.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus && !b.txtUrl.getText().toString().isEmpty()) {
                picasso.load(b.txtUrl.getText().toString())
                        .placeholder(R.drawable.placeholder)
                        .error(R.drawable.placeholder)
                        .into(b.imgCover);
            }
        });
        b.txtTitle.addTextChangedListener(new GenericTextWatcher(b.txtTitle));
        b.txtAuthor.addTextChangedListener(new GenericTextWatcher(b.txtAuthor));
        b.txtYear.addTextChangedListener(new GenericTextWatcher(b.txtYear));
    }

    private void saveBook() {
        YesNoDialogFragment yesNoDialogFragment = YesNoDialogFragment.newInstance(getString(R.string.confirm_dialog_title),
                getString(R.string.confirm_dialog_message), getString(R.string.confirm_dialog_yes),
                getString(R.string.confirm_dialog_no));
        yesNoDialogFragment.setTargetFragment(this, 11);
        yesNoDialogFragment.show(Objects.requireNonNull(this.getFragmentManager()), "ConfirmDeletionDialogFragment");
    }

    public void onPositiveButtonClick(DialogInterface dialog) {
        insertBook();
    }

    private void insertBook(){
        Book book = new Book(0, b.txtTitle.getText().toString(), b.txtAuthor.getText().toString(),
                Integer.valueOf(b.txtYear.getText().toString()), b.txtUrl.getText().toString(), b.txtSynopsis.getText().toString());
        vm.insertBook(book);
    }


    public void onNegativeButtonClick(DialogInterface dialog) {
        SnackbarUtils.snackbar(requireView(), "You have canceled the insertion", Snackbar.LENGTH_SHORT);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.save_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.mnuSave) {
            KeyboardUtils.hideSoftKeyboard(requireActivity());
            if (valid()) {
                if (dialog){
                    saveBook();
                }else{
                    insertBook();
                }

                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        KeyboardUtils.hideSoftKeyboard(requireActivity());
    }

    private boolean valid() {

        if (b.txtTitle.getText().toString().isEmpty()) {
            valid = false;
            b.tilTitle.setErrorEnabled(true);
            b.tilTitle.setError(getString(R.string.errorTil));
        }
        if (b.txtAuthor.getText().toString().isEmpty()) {
            valid = false;
            b.tilAuthor.setErrorEnabled(true);
            b.tilAuthor.setError(getString(R.string.errorTil));
        }
        if (b.txtYear.getText().toString().isEmpty()) {
            valid = false;
            b.tilYear.setErrorEnabled(true);
            b.tilYear.setError(getString(R.string.errorTil));
        }
        if (!valid) {
            SnackbarUtils.snackbar(requireView(), "You have to fill all the fields!", Snackbar.LENGTH_SHORT);
        }
        return valid;
    }

    private class GenericTextWatcher implements TextWatcher {

        private final View view;

        private GenericTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.txtTitle:
                    if (b.txtTitle.getText().toString().isEmpty()){
                        valid = false;
                        b.tilTitle.setErrorEnabled(true);
                        b.tilTitle.setError(getString(R.string.errorTil));
                    }else{
                        valid = true;
                        b.tilTitle.setErrorEnabled(false);
                        b.tilTitle.setError("");
                    }
                    break;
                case R.id.txtAuthor:
                    if (b.txtAuthor.getText().toString().isEmpty()){
                        valid = false;
                        b.tilAuthor.setErrorEnabled(true);
                        b.tilAuthor.setError(getString(R.string.errorTil));
                    }else{
                        valid = true;
                        b.tilAuthor.setErrorEnabled(false);
                        b.tilAuthor.setError("");
                    }
                    break;
                case R.id.txtYear:
                    if (b.txtYear.getText().toString().isEmpty()){
                        valid = false;
                        b.tilYear.setErrorEnabled(true);
                        b.tilYear.setError(getString(R.string.errorTil));
                    }else{
                        valid = true;
                        b.tilYear.setErrorEnabled(false);
                        b.tilYear.setError("");
                    }
                    break;
            }
        }
    }
}
