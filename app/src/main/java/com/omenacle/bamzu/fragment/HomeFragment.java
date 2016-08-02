package com.omenacle.bamzu.fragment;


import android.app.Fragment;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.omenacle.bamzu.R;
import com.omenacle.bamzu.models.TopCategory;
import com.omenacle.bamzu.viewholder.CaptionedImageAdapter;

public class HomeFragment extends Fragment {

    public final String TAG = "HomeFragment";


    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup container, Bundle savedBundleInstance) {
        View view = layoutInflater.inflate(R.layout.fragment_home, container, false);
        return view;

    }

    @Override
    public void onAttach(Context _context) {
        super.onAttach(_context);


    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
       new LoadTopCategories().execute();

    }



    private class LoadTopCategories extends AsyncTask<Integer, Void, Boolean> {
        int mNumOfTopCategories;
        int[] mDisplayImages, mCategoryNames;
        final GridLayoutManager mLayoutManager = new GridLayoutManager(getActivity(), getResources().getInteger(R.integer.category_per_row));;
        CaptionedImageAdapter mTopCategoriesAdapter;
        RecyclerView recyclerView_topCategories;
        View view;

        public void onPreExecute() {
            mNumOfTopCategories = TopCategory.topCategories.length;
            recyclerView_topCategories = (RecyclerView) getView().findViewById(R.id.topCategories_recycler_view);
            //get data for recyclerView
            int mNumOfTopCategories = TopCategory.topCategories.length;
            mDisplayImages = new int[mNumOfTopCategories];
            mCategoryNames = new int[mNumOfTopCategories];


        }

        protected Boolean doInBackground(Integer... topCategories) {
            mTopCategoriesAdapter = new CaptionedImageAdapter(mCategoryNames, mDisplayImages);
            for (int i = 0; i < mNumOfTopCategories; i++) {
                mDisplayImages[i] = TopCategory.topCategories[i].getmCategoryDisplayImage();
                mCategoryNames[i] = TopCategory.topCategories[i].getmCategoryDisplayName();
            }
            return true;
        }

        protected void onPostExecute(Boolean success) {
            if (success) {
                recyclerView_topCategories.setAdapter(mTopCategoriesAdapter);
                recyclerView_topCategories.setLayoutManager(mLayoutManager);


            } else {
                Log.d(TAG, "Failed To Load Top Categories RecyclerView");
            }
        }
    }
}