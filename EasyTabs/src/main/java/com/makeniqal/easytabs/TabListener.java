package com.makeniqal.easytabs;

public interface TabListener {
    void onTabAdded(String tabId);
    void onTabSelected(String tabId);
    void onTabRemoved(String tabId);
}
