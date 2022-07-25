package com.makeniqal.easytabs;


import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class TabManager {
    protected final FragmentManager manager;
    protected TabListener tabListener;
    protected OctoTabManager octoTabManager;
    protected int containerId = 0;
    protected Class<?> typedFragment;

    public TabManager(FragmentManager fragmentManager) {
        manager = fragmentManager;
    }

    @Nullable
    public Fragment findFragmentByTabId(String tabId){
        return octoTabManager.findFragmentByTabId(tabId);
    }

    public TabManager setFragmentContainerId(int r_id_container){
        containerId = r_id_container;
        return this;
    }

    public TabManager setTabListener(TabListener tabListener) {
        this.tabListener = tabListener;
        octoTabManager.setTabListener(tabListener);
        return this;
    }

    public void addNewTab(){
        octoTabManager.addTab();
    }

    public OctoTabManager createManager(){
        octoTabManager = new OctoTabManager(manager,
                tabListener,
                containerId,
                typedFragment);
        return octoTabManager;
    }

    public TabManager setFragment(Class<?> fragment){
        typedFragment = fragment;
        return this;
    }

    public OctoTabManager getTabAdapter() {
        return octoTabManager;
    }

    public void refreshAdapter(){
        octoTabManager.notifyItemRangeChanged(0, octoTabManager.getItemCount()-1);
    }

    @Nullable
    public String getSelectedTabId(){
        return octoTabManager.getSelectedTabId();
    }

    public void removeTab(int tabId){
        octoTabManager.removeTab(tabId);
    }

    public int getTotalTabCount(){
        return octoTabManager.getItemCount();
    }

}
