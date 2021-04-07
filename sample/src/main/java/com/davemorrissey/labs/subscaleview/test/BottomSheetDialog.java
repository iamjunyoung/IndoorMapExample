package com.davemorrissey.labs.subscaleview.test;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.BindingAdapter;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ObservableArrayList;
import androidx.recyclerview.widget.RecyclerView;

import com.davemorrissey.labs.subscaleview.test.databinding.ModalBottomSheetLayoutBinding;
import com.davemorrissey.labs.subscaleview.test.viewmodel.POIGroupSelectViewModel;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.List;

//https://in-idea.tistory.com/46
public class BottomSheetDialog extends BottomSheetDialogFragment {
    private BottomSheetBehavior sheetBehavior;
    private LinearLayout layout_bottom_sheet;

    POIGroupSelectViewModel viewModel;
    ModalBottomSheetLayoutBinding binding;
    List<String> list;
    public BottomSheetDialog(List<String> list) {
        this.list = list;
    }
    public BottomSheetDialog() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable
            ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.modal_bottom_sheet_layout, container, false);
        //layout_bottom_sheet = binding.layoutBottomSheet;
        sheetBehavior = BottomSheetBehavior.from(binding.layoutBottomSheet);
        sheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View view, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_HIDDEN:
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED: {
                        Log.d("JYN", "Close Sheet");
                    }
                    break;
                    case BottomSheetBehavior.STATE_COLLAPSED: {
                        Log.d("JYN", "Expand Sheet");
                    }
                    break;
                    case BottomSheetBehavior.STATE_DRAGGING:
                        Log.d("JYN", "Dragging Sheet");
                        break;
                    case BottomSheetBehavior.STATE_SETTLING:
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View view, float v) {

            }
        });

        viewModel = POIGroupSelectViewModel.getInstance();
        binding.setViewModel(viewModel);
        View v = binding.getRoot();

        Button algo_button = binding.addNewGroup;

        algo_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Toast.makeText(getActivity(),
                        "Algorithm Shared", Toast.LENGTH_SHORT)
                        .show();
                dismiss();
            }
        });
        return v;
    }

    /*
    @BindingAdapter("items")
    public static void setItems(RecyclerView recyclerView, ObservableArrayList<POIGroupItemViewModel> list) {
        POIGroupListAdapter adapter;
        Context context = MyApplication.getAppContext();
        if (recyclerView.getAdapter() == null) {
            adapter = new POIGroupListAdapter();
            recyclerView.setAdapter(adapter);
        } else { // getAdapter()가 있으면 위에서 생성한 adapter를 get
            adapter = (POIGroupListAdapter)recyclerView.getAdapter();
        }
        adapter.updateItems(list);
    }
     */
}
