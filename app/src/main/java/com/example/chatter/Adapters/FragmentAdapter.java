package com.example.chatter.Adapters;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.chatter.Fragments.callFragments;
import com.example.chatter.Fragments.chatsFragment;
import com.example.chatter.Fragments.statusFragments;


public class FragmentAdapter extends FragmentPagerAdapter {
    public FragmentAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }


    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0: return new chatsFragment();
            case 1: return new statusFragments();
            case 2: return new callFragments();
            default: return new chatsFragment();
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        String title=null;
        if(position==0){
            title="Chats";
        }
        else if(position==1)
        {
            title="Status";
        }
        else
        {
            title="Call";
        }
        return title;
    }
}
