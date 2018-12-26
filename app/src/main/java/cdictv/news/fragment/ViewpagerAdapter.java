package cdictv.news.fragment;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.util.Log;

import java.util.ArrayList;

public class ViewpagerAdapter extends FragmentStatePagerAdapter {
    ArrayList<MyFragment> arrayList;
    public ViewpagerAdapter(FragmentManager fm, ArrayList<MyFragment> arrayList) {
        super(fm);
        for (MyFragment been : arrayList) {
            Log.i("arraylist222",been.getTitle());

        }
        this.arrayList=arrayList;
    }

    @Override
    public Fragment getItem(int i) {
        return arrayList.get(i);
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return arrayList.get(position).getTitle();
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return PagerAdapter.POSITION_NONE;
    }
}
