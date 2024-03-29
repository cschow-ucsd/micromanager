package ucsd.ieeeqp.fa19.ui.login;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import ucsd.ieeeqp.fa19.ui.mm.ScheduleFragment;
import ucsd.ieeeqp.fa19.ui.mm.SettingsFragment;

public class NavigationPagerAdapter extends FragmentPagerAdapter {
    private static final Fragment[] fragments = {new ScheduleFragment(), new SettingsFragment()};

    public NavigationPagerAdapter(@NonNull FragmentManager fm) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
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
