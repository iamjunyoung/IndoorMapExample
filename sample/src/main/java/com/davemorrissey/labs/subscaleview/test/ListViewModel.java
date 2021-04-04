package com.davemorrissey.labs.subscaleview.test;

//import androidx.databinding.ObservableArrayList;

//import com.aptinfo.databindingrecylcerview.SingleLiveEvent;

public class ListViewModel {
    private int count = 0;
    //private ObservableArrayList<MovieItemViewModel> listItemViewModels;
    //private SingleLiveEvent<Boolean> singleLiveEvent;

    public ListViewModel() {
        //listItemViewModels = new ObservableArrayList<>();
        //singleLiveEvent = new SingleLiveEvent<>();
    }

    /*
    public ObservableArrayList<MovieItemViewModel> getListItemViewModels() {
        return this.listItemViewModels;
    }

    public void addListItem() {
        ++count;
        listItemViewModels.add(new MovieItemViewModel("title" + count, R.drawable.coco));
    }

    public void showBottomSheet() {
        singleLiveEvent.setValue(true);
        singleLiveEvent.call();
        //BottomSheetDialog bottomSheet = new BottomSheetDialog();
        //bottomSheet.show(getSupportFragmentManager(), "ModalBottomSheet");
    }

    public SingleLiveEvent getButtonSheetEvent() {
        return singleLiveEvent;
    }

    public void removeListItem() {
        listItemViewModels.remove(count-1);
        --count;
    }*/
}
