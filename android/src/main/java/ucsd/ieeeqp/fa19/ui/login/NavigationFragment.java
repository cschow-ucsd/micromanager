package ucsd.ieeeqp.fa19.ui.login;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import ucsd.ieeeqp.fa19.R;
import ucsd.ieeeqp.fa19.ui.NoSwipePagerAdapter;
import ucsd.ieeeqp.fa19.ui.all_schedules.AllSchedulesFragment;
import ucsd.ieeeqp.fa19.ui.all_schedules.ResultsFragment;
import ucsd.ieeeqp.fa19.ui.all_schedules.SettingsFragment;

public class NavigationFragment extends Fragment {
    private ViewPager viewPager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_navigation, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        BottomNavigationView navigationView = view.findViewById(R.id.bottomnav_nav_nav);
        navigationView.setOnNavigationItemSelectedListener(this::handleMenuItemSelected);
        viewPager = view.findViewById(R.id.viewpager_nav_container);
        viewPager.setAdapter(new NoSwipePagerAdapter(getChildFragmentManager(),
                new AllSchedulesFragment(), new ResultsFragment(), new SettingsFragment()));
    }

    private boolean handleMenuItemSelected(MenuItem menuItem) {
        int fragmentIndex = -1;
        switch (menuItem.getItemId()) {
            case R.id.menu_item_schedules:
                fragmentIndex = 0;
                break;
            case R.id.menu_item_results:
                fragmentIndex = 1;
                break;
            case R.id.menu_item_settings:
                fragmentIndex = 2;
                break;
        }
        viewPager.setCurrentItem(fragmentIndex, true);
        return true;
    }
}
