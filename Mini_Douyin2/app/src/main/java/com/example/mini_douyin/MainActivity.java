package com.example.mini_douyin;

import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ViewPager pager = findViewById(R.id.viewpager);
        TabLayout tabLayout = findViewById(R.id.tab_layout);
        pager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int i) {
                switch (i)
                {
                    case 0:
                        return new VideoList();
                    case 1:
                        return new Update();
                        default:
                            return new BlankFragment();
                }
            }

            @Override
            public int getCount() {
                return 3;
            }

            @Nullable
            @Override
            public CharSequence getPageTitle(int position) {
                String name="";
                switch (position)
                {
                    case 0:
                    {
                        name+="首页";
                        break;
                    }
                    case 1:
                    {
                        name+="上传";
                        break;
                    }
                    case 2:
                    {
                        name+="我";
                        break;
                    }
                    default:
                        break;
                }
                return name;
            }
        });
        tabLayout.setupWithViewPager(pager);
    }
}
