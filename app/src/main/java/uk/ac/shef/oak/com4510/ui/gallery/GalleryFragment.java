package uk.ac.shef.oak.com4510.ui.gallery;

import android.Manifest;
import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import uk.ac.shef.oak.com4510.databinding.FragmentDashboardBinding;
import uk.ac.shef.oak.com4510.databinding.FragmentGalleryBinding;
import uk.ac.shef.oak.com4510.R;
import uk.ac.shef.oak.com4510.mydatabase.CacheEntity;
import uk.ac.shef.oak.com4510.mydatabase.CacheService;
import uk.ac.shef.oak.com4510.mydatabase.MyImage;
import uk.ac.shef.oak.com4510.ui.history.HistoryFragment;
import uk.ac.shef.oak.com4510.ui.history.HistoryViewModel;
import uk.ac.shef.oak.com4510.ui.recyclerview.GalleryRecyclerViewAdapter;
import uk.ac.shef.oak.com4510.ui.recyclerview.RecyclerViewAdapter;

public class GalleryFragment extends Fragment {

    private HistoryViewModel historyViewModel;
    private FragmentDashboardBinding binding;

    //    RecyclerView mRecyclerView ;
    LinearLayoutManager mLayoutManager;
    GalleryRecyclerViewAdapter mRecyclerViewAdapter;

    int IS_TITLE_OR_NOT =1;
    int MESSAGE = 2;

    int key=3;
    int title=4;

    List<Map<Integer, String>> mData =new ArrayList<>();
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

    private void initUi() {
        findView();
        initData(0);
        mRecyclerViewAdapter = new GalleryRecyclerViewAdapter(this.getActivity(), mData, 3,launcher);
        binding.recyclerView.setAdapter(mRecyclerViewAdapter);
    }

    void findView(){
        //2 means the column number is 2
        mLayoutManager = new GridLayoutManager(this.getContext(), 3, LinearLayoutManager.VERTICAL, false);
        binding.recyclerView.addItemDecoration(new GalleryFragment.SpaceItemDecoration(12));//distance between items
        binding.recyclerView.setLayoutManager(mLayoutManager);
    }

    SearchView searchView;
    SearchView.OnQueryTextListener queryTextListener;

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_search, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchManager searchManager = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        }

        if (searchItem != null) {
            searchView = (SearchView) searchItem.getActionView();
        }
        if (searchView != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));

            queryTextListener = new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextChange(String newText) {
                    Log.i("onQueryTextChange", newText);

                    return true;
                }
                @Override
                public boolean onQueryTextSubmit(String query) {
                    Log.i("onQueryTextSubmit", query);

                    searchByWord(query);
                    searchView.clearFocus();
                    return true;
                }
            };
            searchView.setOnQueryTextListener(queryTextListener);
            searchView.setOnCloseListener(new SearchView.OnCloseListener() {
                @Override
                public boolean onClose() {

                    initData(0);
                    Log.i("onQueryTextSubmit","-----");
                    return false;
                }
            });
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    private void searchByWord(String query) {

        CacheEntity[] ces= CacheService.findByTitle("%"+query+"%");
        mData.clear();
        for(int n=0;n<ces.length;n++)
        {

            List<MyImage> myImageLists= ces[n].getImagebean();
            if(myImageLists!=null&&myImageLists.size()>0)
            {
                map = new HashMap<Integer, String>();
                map.put(IS_TITLE_OR_NOT , "false");
                map.put(MESSAGE , myImageLists.get(0).imageUrl);

                map.put(key , ces[n].key);
                map.put(title , ces[n].title);
                mData.add(map);
            }



            //Initialise titles
            map = new HashMap<Integer, String>();
            map.put(IS_TITLE_OR_NOT , "true");
            SimpleDateFormat dateFormat = null;
            String titleDate="";
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                dateFormat = new SimpleDateFormat("EEE, d MMM yyyy . h:m:s", Locale.ENGLISH);
                titleDate = dateFormat.format(new Date(ces[n].startTime));

                map.put(MESSAGE ,ces[n].title+"\n"+ myImageLists.size()+" photos "+titleDate);
                mData.add(map);
            }

        }

        mRecyclerViewAdapter.notifyDataSetChanged();

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
        else if (id == R.id.action_search) {

            searchView.setOnQueryTextListener(queryTextListener);
//            initData(1);
//            mRecyclerViewAdapter.notifyDataSetChanged();

            return true;
        }
        return false;
    }

    void initData(int orderBy){

        CacheEntity[] ces= CacheService.getAll(orderBy);
        mData.clear();
        for(int n=0;n<ces.length;n++)
        {

            List<MyImage> myImageLists= ces[n].getImagebean();
            if(myImageLists!=null&&myImageLists.size()>0)
            {
                map = new HashMap<Integer, String>();
                map.put(IS_TITLE_OR_NOT , "false");
                map.put(MESSAGE , myImageLists.get(0).imageUrl);

                map.put(key , ces[n].key);
                map.put(title , ces[n].title);
                mData.add(map);
            }



            map = new HashMap<Integer, String>();
            map.put(IS_TITLE_OR_NOT , "true");
            SimpleDateFormat dateFormat = null;
            String titleDate="";
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                dateFormat = new SimpleDateFormat("EEE, d MMM yyyy . h:m:s", Locale.ENGLISH);
                titleDate = dateFormat.format(new Date(ces[n].startTime));

                map.put(MESSAGE ,ces[n].title+"\n"+ myImageLists.size()+" photos "+titleDate);
                mData.add(map);
            }

        }


    }

    ActivityResultLauncher<Intent> launcher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {

                        initData(0);
                        mRecyclerViewAdapter.notifyDataSetChanged();
                    }
                }
            });

    //Set spacing between items in RecyclerView
    public class SpaceItemDecoration extends RecyclerView.ItemDecoration {
        private int space;

        public SpaceItemDecoration(int space) {
            this.space = space;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            //Set spacing between items
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