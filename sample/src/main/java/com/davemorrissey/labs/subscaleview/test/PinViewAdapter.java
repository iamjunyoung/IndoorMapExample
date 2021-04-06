package com.davemorrissey.labs.subscaleview.test;

import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.aptinfo.databindingrecylcerview.databinding.PoiItemBinding;
import com.davemorrissey.labs.subscaleview.test.extension.views.PinView;
import com.lge.robot.platform.util.poi.data.POI;

import java.util.ArrayList;
import java.util.List;

public class PinViewAdapter extends RecyclerView.Adapter<BindingViewHolder<PoiItemBinding>> implements Filterable  {
    public final static int MODE_BUILDING_AND_FLOOR = 0;
    public final static int MODE_GROUP = 1;
    public int showingMode;

    public ArrayList<PinView> listItemViewModels = new ArrayList<>();

    private OnItemClickListener listener;

    private SparseBooleanArray selectedItems;
    private int selectedIndex = -1;

    private static final String TAG = "ListViewAdapter";

    public PinViewAdapter() {
        selectedItems = new SparseBooleanArray();
        showingMode = MODE_BUILDING_AND_FLOOR; // default
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        protected PinView pinView;

        public CustomViewHolder(PinView pinView) {
            super(view);
            this.id = (TextView) view.findViewById(R.id.id_listitem);
            this.english = (TextView) view.findViewById(R.id.english_listitem);
            this.korean = (TextView) view.findViewById(R.id.korean_listitem);
        }

    public void updateItems(ArrayList<POI> listItemViewModels) {
        ListItemDiffCallback callback = new ListItemDiffCallback(this.listItemViewModels, listItemViewModels);
        DiffUtil.DiffResult result = DiffUtil.calculateDiff(callback);

        this.listItemViewModels.clear();
        this.listItemViewModels.addAll(listItemViewModels);
        result.dispatchUpdatesTo(this);
    }

    @Override
    public BindingViewHolder<PoiItemBinding> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        // new BindingViewHolder<>(inflater.inflate(R.layout.recyclerview_adapter_layout, parent, false));
        return new BindingViewHolder<>(inflater.inflate(R.layout.poi_item, parent, false));
    }

    // Long touch https://stackoverflow.com/questions/45723115/detecting-onclick-in-recycler-view-using-data-binding-in-android 참고
    // https://stackoverflow.com/questions/48269165/recyclerview-databinding-item-click
    @Override
    public void onBindViewHolder(@NonNull BindingViewHolder<PoiItemBinding> holder, int position) {
        final int pos = position;
        holder.binding().setViewModel(listItemViewModels.get(position));
        holder.binding().cardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Log.d(TAG, "img longClick " + pos + "  " + listItemViewModels.get(pos));
                if (listener != null && pos != RecyclerView.NO_POSITION) {
                    listener.onItemLongClick(listItemViewModels.get(pos), pos);
                }
                return true;
            }
        });
        holder.binding().cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "img click " + pos + "  " + listItemViewModels.get(pos));
                if (listener != null && pos != RecyclerView.NO_POSITION) {
                    listener.onItemClick(listItemViewModels.get(pos), pos);
                }
            }
        });
        holder.binding().image.setImageResource(R.drawable.coco);
        toggleIcon(holder.binding(), position);
    }

    @Override
    public int getItemCount() {
        return listItemViewModels.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String charString = constraint.toString();
                if(charString.isEmpty()) {
                    mFilteredPOIList = mUnFilteredPOIList;
                } else {
                    ArrayList<POI> filteringList = new ArrayList<>();
                    for(POI poi : mUnFilteredPOIList) {
                        if(poi.getNameKr().toLowerCase().contains(charString.toLowerCase())) {
                            filteringList.add(poi);
                        } else if (poi.getNameEn().toLowerCase().contains(charString.toLowerCase())) {
                            filteringList.add(poi);
                        } else if (poi.getAttributeDesc().toLowerCase().contains(charString.toLowerCase())) {
                            filteringList.add(poi);
                        }
                    }
                    mFilteredPOIList = filteringList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = mFilteredPOIList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                mFilteredPOIList = (ArrayList<POI>)results.values;
                listItemViewModels = mFilteredPOIList;
                notifyDataSetChanged();
            }
        };
    }

    public interface OnItemClickListener {
        void onItemClick(POI item, int position);
        void onItemLongClick(POI item, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    /*
       This method will trigger when we we long press the item and it will change the icon of the item to check icon.
     */
    private void toggleIcon(PoiItemBinding binding, int position) {
        if (selectedItems.get(position, false)) {
            binding.image.setVisibility(View.GONE);
            //bi.lytChecked.setVisibility(View.VISIBLE);
            if (selectedIndex == position) selectedIndex = -1;
        } else {
            binding.image.setVisibility(View.VISIBLE);
            //bi.lytChecked.setVisibility(View.GONE);
            if (selectedIndex == position) selectedIndex = -1;
        }
    }

    /*
       This method helps you to get all selected items from the list
     */

    public List<Integer> getSelectedItems() {
        List<Integer> items = new ArrayList<>(selectedItems.size());
        for (int i = 0; i < selectedItems.size(); i++) {
            items.add(selectedItems.keyAt(i));
        }
        return items;
    }

    /*
       this will be used when we want to delete items from our list
     */
    public void removeItems(int position) {
        listItemViewModels.remove(position);
        selectedIndex = -1;
    }

    /*
       for clearing our selection
     */

    public void clearSelection() {
        selectedItems.clear();
        notifyDataSetChanged();
    }

    /*
             this function will toggle the selection of items
     */
    public void toggleSelection(int position) {
        selectedIndex = position;
        if (selectedItems.get(position, false)) {
            selectedItems.delete(position);
        } else {
            selectedItems.put(position, true);
        }
        notifyItemChanged(position);
    }

    /*
      How many items have been selected? this method exactly the same . this will return a total number of selected items.
     */
    public int selectedItemCount() {
        return selectedItems.size();
    }



}
