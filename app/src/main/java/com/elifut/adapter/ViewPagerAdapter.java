package com.elifut.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerAdapter extends FragmentPagerAdapter {
  private final List<Fragment> fragments = new ArrayList<>();
  private final List<String> fragmentTitles = new ArrayList<>();

  public ViewPagerAdapter(FragmentManager fm) {
    super(fm);
  }

  public void addFragment(Fragment fragment, String title) {
    fragments.add(fragment);
    fragmentTitles.add(title);
  }

  @Override
  public Fragment getItem(int position) {
    return fragments.get(position);
  }

  @Override
  public int getCount() {
    return fragments.size();
  }

  @Override
  public CharSequence getPageTitle(int position) {
    return fragmentTitles.get(position);
  }
}
