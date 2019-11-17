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
        viewPager.setAdapter(new NavigationPagerAdapter(getChildFragmentManager()));
    }

    private boolean handleMenuItemSelected(MenuItem menuItem) {
        int fragmentIndex = -1;
        switch (menuItem.getItemId()) {
            case R.id.item_schedule:
                fragmentIndex = 0;
                break;
            case R.id.item_settings:
                fragmentIndex = 1;
                break;
        }
        viewPager.setCurrentItem(fragmentIndex, true);
        return true;
    }
}
