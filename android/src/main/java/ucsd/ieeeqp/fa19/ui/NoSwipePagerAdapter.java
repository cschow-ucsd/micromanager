package ucsd.ieeeqp.fa19.ui;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class NoSwipePagerAdapter extends FragmentPagerAdapter {
    private Fragment[] fragments;

    public NoSwipePagerAdapter(@NonNull FragmentManager fm, Fragment... fragments) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.fragments = fragments;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragments[position];
    }

    @Override
    public int getCount() {
        return fragments.length;
    }
}
