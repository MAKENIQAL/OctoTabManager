package com.makeniqal.easytabs;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class OctoTabManager extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final List<String> arrayList = new ArrayList<>();
    private final FragmentManager manager;
    @Nullable
    private TabListener tabListener;
    private final int containerId;
    private String selectedTabId = null;
    private final Class<?> typedFragment;
    private int tabIndex = 0;

    public String generateTabId(){
        String tabTag = tabIndex+"MAKENIQAL";
        tabIndex++;
        return tabTag;
    }

    protected void setTabListener(@Nullable TabListener tabListener) {
        this.tabListener = tabListener;
    }

    public OctoTabManager(TabManager tabManager) {
        this.tabListener = tabManager.tabListener;
        this.containerId = tabManager.containerId;
        this.manager = tabManager.manager;
        this.typedFragment = tabManager.typedFragment;
        tabManager.octoTabManager = this;
    }

    protected OctoTabManager(@NonNull FragmentManager manager,
                          @Nullable TabListener tabListener,
                          int containerId,
                          @NonNull Class<?> typedFragment) {
        this.tabListener = tabListener;
        this.containerId = containerId;
        this.manager = manager;
        this.typedFragment = typedFragment;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        holder.itemView.setOnClickListener(v -> selectTab(arrayList.get(holder.getAdapterPosition())));
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    private Fragment createInstance() throws
            IllegalAccessException,
            InstantiationException {
        return (Fragment) typedFragment.newInstance();
    }

    public void addTab() {

        if (selectedTabId != null) {
            try {
                manager.beginTransaction().hide(manager.findFragmentByTag(selectedTabId)).commit();
            } catch (Exception ignored) {
            }
        }

        String tabId = generateTabId();
        arrayList.add(tabId);
        try {
            manager.beginTransaction().add(containerId, createInstance(), tabId).commit();
        } catch (Exception error) {
        }
        selectedTabId = tabId;
        notifyItemInserted(getItemCount() - 1);
        notifyItemRangeChanged(0, getItemCount() - 1);
        if (tabListener != null)
            tabListener.onTabAdded(arrayList.get(getItemCount()-1));
    }

    public void selectTab(String tabId) {
        Fragment fragment = manager.findFragmentByTag(tabId);
        if (fragment == null) return;

        if (selectedTabId != null) {
            try {
                manager.beginTransaction().hide((manager.findFragmentByTag(selectedTabId))).commit();
            } catch (Exception ignored) {
            }
        }
        manager.beginTransaction().show(fragment).commit();
        selectedTabId = tabId;
        if (tabListener != null) {
            tabListener.onTabSelected(tabId);
        }
        notifyItemRangeChanged(0,getItemCount());
    }

    public void removeTab(int position) {
        try{
            manager.beginTransaction().remove(manager.findFragmentByTag(arrayList.get(position))).commit();
            arrayList.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(0, getItemCount());
            if (tabListener!=null)
                tabListener.onTabRemoved(arrayList.get(position));
        }catch (Exception ignored){}

    }

    @Nullable
    public Fragment findFragmentByTabId(String tabId) {
        for (String tab : arrayList){
            if (tab.equals(tabId)){
                return manager.findFragmentByTag(tabId);
            }
        }
        return null;
    }

    @Nullable
    public String getSelectedTabId() {
        return selectedTabId;
    }

}
