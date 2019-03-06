package com.practica.ismael.examen_ismaelraqi_pmdmo.ui.fragment_list;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.snackbar.Snackbar;
import com.practica.ismael.examen_ismaelraqi_pmdmo.R;
import com.practica.ismael.examen_ismaelraqi_pmdmo.base.EventObserver;
import com.practica.ismael.examen_ismaelraqi_pmdmo.data.RepositoryImpl;
import com.practica.ismael.examen_ismaelraqi_pmdmo.data.local.AppDatabase;
import com.practica.ismael.examen_ismaelraqi_pmdmo.data.local.model.Book;
import com.practica.ismael.examen_ismaelraqi_pmdmo.databinding.FragmentListBinding;
import com.practica.ismael.examen_ismaelraqi_pmdmo.ui.activity.MainViewModel;
import com.practica.ismael.examen_ismaelraqi_pmdmo.utils.SnackbarUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

public class ListFragment extends Fragment {

    private ListAdapter listAdapter;
    private ListViewModel vm;
    private MainViewModel mainVM;
    private FragmentListBinding b;
    private BottomSheetBehavior bsb;
    private NavController navController;
    private boolean undo;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        b = DataBindingUtil.inflate(inflater, R.layout.fragment_list, container, false);
        return b.getRoot();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        navController = NavHostFragment.findNavController(this);
        setupViews();
        observeBooks();
        observeResultMessages();
        observeSettings();
    }

    private void observeSettings() {
        mainVM.getUndoDelete().observe(getViewLifecycleOwner(), this::showUndo);
    }

    private void showUndo(Boolean bool) {
        this.undo = bool;
    }

    private void observeResultMessages() {
        vm.getErrorMessage().observe(getViewLifecycleOwner(),
                new EventObserver<>(v -> showMessage(getString(R.string.book_error_deleting))));
    }

    private void showMessage(String message) {
        Snackbar.make(requireView(), message, Snackbar.LENGTH_SHORT).show();
    }

    private void setupViews() {
        bsb = BottomSheetBehavior.from(b.bottomSheet.bottomSheet);
        vm = ViewModelProviders.of(this,
                new ListViewModelFactory(requireActivity().getApplication(),
                        new RepositoryImpl(AppDatabase.getInstance(requireContext().getApplicationContext())
                                .bookDao()))).get(ListViewModel.class);
        mainVM = ViewModelProviders.of(requireActivity()).get(MainViewModel.class);
        listAdapter = new ListAdapter(vm, mainVM);

        b.lstStudents.setHasFixedSize(true);
        b.lstStudents.setLayoutManager(new GridLayoutManager(requireContext(), 1));
        b.lstStudents.setItemAnimator(new DefaultItemAnimator());
        b.lstStudents.setAdapter(listAdapter);
        b.fabBooks.setOnClickListener(v -> navigateToForm());
        b.lblEmptyBooks.setOnClickListener(v -> navigateToForm());
        enableSwipeToDeleteAndUndo();
        b.bottomSheet.imgClose.setOnClickListener(view -> bsb.setState(BottomSheetBehavior.STATE_COLLAPSED));
    }

    @Override
    public void onResume() {
        super.onResume();
        bsb.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }

    private void enableSwipeToDeleteAndUndo() {
        SwipeToDeleteCallback swipeToDeleteCallback = new SwipeToDeleteCallback(requireContext()) {
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {



                final int position = viewHolder.getAdapterPosition();
                final Book item = listAdapter.getData();

                listAdapter.removeItem(position);
                if (undo){
                    Snackbar snackbar = Snackbar
                            .make(b.coordinatorLayout, getString(R.string.book_removed), Snackbar.LENGTH_LONG);
                    snackbar.setAction("UNDO", view -> {

                        listAdapter.restoreItem(item, position);
                        b.lstStudents.scrollToPosition(position);
                    });
                    snackbar.setActionTextColor(Color.WHITE);

                    snackbar.show();
                }else{
                    SnackbarUtils.snackbar(requireView(), getString(R.string.book_removed), Toast.LENGTH_SHORT);
                }

            }
        };

        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeToDeleteCallback);
        itemTouchhelper.attachToRecyclerView(b.lstStudents);
    }


    private void navigateToForm() {
        Navigation.findNavController(requireView()).navigate(R.id.desListFragmentToBooksFragment);
    }

    private void observeBooks() {
        vm.getBooks().observe(getViewLifecycleOwner(), students -> {
            listAdapter.submitList(students);
            b.lblEmptyBooks.setVisibility(students.isEmpty() ? View.VISIBLE : View.INVISIBLE);
        });
        mainVM.getShowBookLiveData().observe(getViewLifecycleOwner(), book -> {
            b.bottomSheet.lblSheetTitle.setText(book.getNameBook());

            if (b.bottomSheet.lblSheetSynopsis.getText().toString().isEmpty()){
                b.bottomSheet.lblSheetSynopsis.setText(getString(R.string.lblSheet_notAvailable));
            }else{
                b.bottomSheet.lblSheetSynopsis.setText(book.getSynopsis());
            }
            bsb.setState(BottomSheetBehavior.STATE_EXPANDED);
        });
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.preferences_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return NavigationUI.onNavDestinationSelected(item, navController)
                || super.onOptionsItemSelected(item);
    }

}
