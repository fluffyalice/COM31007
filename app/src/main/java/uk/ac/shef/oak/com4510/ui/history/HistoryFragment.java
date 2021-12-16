package uk.ac.shef.oak.com4510.ui.history;

import android.graphics.Rect;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import uk.ac.shef.oak.com4510.R;
import uk.ac.shef.oak.com4510.databinding.FragmentDashboardBinding;
import uk.ac.shef.oak.com4510.mydatabase.CacheEntity;
import uk.ac.shef.oak.com4510.mydatabase.CacheService;
import uk.ac.shef.oak.com4510.mydatabase.MyImage;
import uk.ac.shef.oak.com4510.ui.recyclerview.RecyclerViewAdapter;


import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class HistoryFragment extends Fragment {

    private HistoryViewModel historyViewModel;
    private FragmentDashboardBinding binding;

    //    RecyclerView mRecyclerView ;
    LinearLayoutManager mLayoutManager;
    RecyclerViewAdapter mRecyclerViewAdapter;

    int IS_TITLE_OR_NOT = 1;
    int MESSAGE = 2;

    List<Map<Integer, String>> mData = new ArrayList<>();
    Map<Integer, String> map = new HashMap<Integer, String>();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        historyViewModel = new ViewModelProvider(this).get(HistoryViewModel.class);

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        setHasOptionsMenu(true);

        initUi();
        return view;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.i_positive_sequence) {
            initData(0);
            mRecyclerViewAdapter.notifyDataSetChanged();
            return true;
        } else if (id == R.id.i_reverse_order) {
            initData(1);
            mRecyclerViewAdapter.notifyDataSetChanged();
            return true;
        }
        return false;
    }

    private void initUi() {
        findView();
        initData(0);
        mRecyclerViewAdapter = new RecyclerViewAdapter(this.getContext(), mData, 3);
        binding.recyclerView.setAdapter(mRecyclerViewAdapter);
    }

    void findView() {
        mLayoutManager = new GridLayoutManager(this.getContext(), 3, LinearLayoutManager.VERTICAL, false);
        binding.recyclerView.addItemDecoration(new SpaceItemDecoration(12));//item spacing
        binding.recyclerView.setLayoutManager(mLayoutManager);
    }


    void initData(int orderBy) {
        CacheEntity[] ces = CacheService.getAll(orderBy);

        mData.clear();
        for (int n = 0; n < ces.length; n++) {

            //Initialise titles
            map = new HashMap<Integer, String>();
            map.put(IS_TITLE_OR_NOT, "true");
            SimpleDateFormat dateFormat = null;
            String titleDate = "";
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                dateFormat = new SimpleDateFormat("d MMM yyyy", Locale.ENGLISH);
                titleDate = dateFormat.format(new Date(ces[n].startTime));
                map.put(MESSAGE, titleDate);
                mData.add(map);
            }
            List<MyImage> myImageLists = ces[n].getImagebean();
            for (int i = 0; i < myImageLists.size(); i++) {//Add current photos
                map = new HashMap<Integer, String>();
                map.put(IS_TITLE_OR_NOT, "false");
                map.put(MESSAGE, myImageLists.get(i).imageUrl);
                mData.add(map);
            }


            //Find current photos
            for (int m = n + 1; m < ces.length; m++) {
                String mtitleDate = "";
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                    dateFormat = new SimpleDateFormat("d MMM yyyy", Locale.ENGLISH);
                    mtitleDate = dateFormat.format(new Date(ces[m].startTime));
                }

                if (mtitleDate.equals(titleDate)) {
                    List<MyImage> mLists = ces[m].getImagebean();
                    for (int i = 0; i < mLists.size(); i++) {//Add current photos
                        map = new HashMap<Integer, String>();
                        map.put(IS_TITLE_OR_NOT, "false");
                        map.put(MESSAGE, mLists.get(i).imageUrl);
                        mData.add(map);
                    }
                    n++;
                }
            }
        }


    }

    /**
     * SpaceItemDecoration.java
     * @author Feng Li, Ruiqing Xu
     */

    //Set the upper, lower, left and right spacing of recyclerView item
    public class SpaceItemDecoration extends RecyclerView.ItemDecoration {
        private int space;

        public SpaceItemDecoration(int space) {
            this.space = space;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            //Set the spacing of items separately
            if (parent.getChildViewHolder(view).getItemViewType() == 0) {
                outRect.bottom = 0;
                outRect.top = space / 3;
            } else {
                outRect.bottom = space;
                outRect.top = space;
            }
            outRect.right = space;
            outRect.left = space;

        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}