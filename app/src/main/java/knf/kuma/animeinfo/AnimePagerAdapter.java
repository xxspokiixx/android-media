package knf.kuma.animeinfo;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import knf.kuma.animeinfo.fragments.ChaptersFragment;
import knf.kuma.animeinfo.fragments.DetailsFragment;

public class AnimePagerAdapter extends FragmentPagerAdapter {

    private DetailsFragment detailsFragment=DetailsFragment.get();
    private ChaptersFragment chaptersFragment=ChaptersFragment.get();

    public AnimePagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            default:
            case 0:
                return "INFO";
            case 1:
                return "EPISODIOS";
        }
    }

    public void onChaptersReselect(){
        if (chaptersFragment!=null)
            chaptersFragment.onReselect();
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            default:
            case 0:
                return detailsFragment;
            case 1:
                return chaptersFragment;
        }
    }

    @Override
    public int getCount() {
        return 2;
    }
}
