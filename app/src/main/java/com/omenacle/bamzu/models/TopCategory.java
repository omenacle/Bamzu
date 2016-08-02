package com.omenacle.bamzu.models;

import com.omenacle.bamzu.R;

/**
 * Created by omegareloaded on 7/19/16.
 */
public class TopCategory {

    int mCategoryDisplayImage;
    int mCategoryDisplayName;

    public TopCategory(int mCategoryDisplayImage, int mCategoryDisplayName) {
        this.mCategoryDisplayImage = mCategoryDisplayImage;
        this.mCategoryDisplayName = mCategoryDisplayName;
    }

    public int getmCategoryDisplayImage() {
        return mCategoryDisplayImage;
    }

    public void setmCategoryDisplayImage(int mCategoryDisplayImage) {
        this.mCategoryDisplayImage = mCategoryDisplayImage;
    }

    public int getmCategoryDisplayName() {
        return mCategoryDisplayName;
    }

    public void setmCategoryDisplayName(int mCategoryDisplayName) {
        this.mCategoryDisplayName = mCategoryDisplayName;
    }


    public static final TopCategory[] topCategories = new TopCategory[]{
            new TopCategory(R.drawable.display_img_house_services, R.string.topCategory_house_services),
            new TopCategory(R.drawable.display_img_electronics, R.string.topCategory_electronics),
            new TopCategory(R.drawable.display_img_photography, R.string.topCategory_photography),
            new TopCategory(R.drawable.display_img_health_fitness, R.string.topCategory_fitness_health),
            new TopCategory(R.drawable.display_img_tution, R.string.topCategory_tuition),
            new TopCategory(R.drawable.display_img_events, R.string.topCategory_events),
            new TopCategory(R.drawable.display_img_proffesional, R.string.topCategory_professional),
            new TopCategory(R.drawable.display_img_others, R.string.topCategory_others)
    };

}
