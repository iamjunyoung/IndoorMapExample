package com.davemorrissey.labs.subscaleview.test.viewmodel;

import android.content.Context;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ObservableArrayList;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class POIGroupSelectViewModel {
    private static POIGroupSelectViewModel viewModelInstance = null;
    private static final String TAG = "POIGroupSelectViewModel";
    List<String> list;

    public ArrayList<String> items = null;


    //BottomSheetDialog UI에서 선택된 애들을(poi list) POIGroupSelectViewModel에서 어떻게 가져오는게 좋을까?
    public POIGroupSelectViewModel() {
        //TODO
        //ArrayList<String> list = POIUtil.getInstance().getPOIDBManager().getAllGroups();
        ArrayList<String> list = new ArrayList<>();
        Log.d(TAG, "POIGroupSelectViewModel gen");

    }

    public static POIGroupSelectViewModel getInstance() {
        if(viewModelInstance == null) {
            viewModelInstance = new POIGroupSelectViewModel();
        }
        return viewModelInstance;
    }

    //http://okcodex.com/2020/08/27/how-to-get-context-in-android-mvvm-viewmodel/
    public void addNewPOIGroup(View view) {
        Context context = view.getContext();
    }
}
