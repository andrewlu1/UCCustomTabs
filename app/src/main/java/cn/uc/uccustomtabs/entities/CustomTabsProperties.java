package cn.uc.uccustomtabs.entities;

import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.customtabs.CustomTabsIntent;
import android.support.customtabs.CustomTabsSessionToken;
import android.view.Menu;

import java.util.ArrayList;

/**
 * Created by andrewlu on 2016/3/9.
 * 通过Intent提取可控参数内容.
 */
public class CustomTabsProperties {

    public static CustomTabsProperties getFromIntent(Intent intent) {
        return new CustomTabsProperties(intent);
    }

    private CustomTabsProperties(Intent intent) {
        buildProperties(intent);
    }

    public CustomTabsSessionToken getToken() {
        return token;
    }

    public int getToolbarColor() {
        return toolbarColor;
    }

    public int getSecondaryToolbarColor() {
        return secondaryToolbarColor;
    }

    public boolean isEnableUrlBarHiding() {
        return enableUrlBarHiding;
    }

    public Bitmap getCloseButtonIcon() {
        return closeButtonIcon;
    }

    public boolean isShowTitle() {
        return showTitle;
    }

    public ArrayList<MenuItemParam> getMenuItems() {
        return menuItems;
    }

    public MenuItemParam getMenuItem(int menuId) {
        if (menuItems != null)
            for (MenuItemParam param : menuItems) {
                if (menuId == param.getMenuId()) {
                    return param;
                }
            }
        return null;
    }

    public Bundle getExitAnimationBundle() {
        return exitAnimationBundle;
    }

    public ArrayList<ActionBtnParam> getActionButtons() {
        return actionButtons;
    }

    public ActionBtnParam getActionButton(int id) {
        for (ActionBtnParam p : actionButtons) {
            if (p.getId() == id) {
                return p;
            }
        }
        return null;
    }

    public boolean isShowDefaultShareMenuItem() {
        return showDefaultShareMenuItem;
    }

    public boolean isTintActionBar() {
        return tintActionBar;
    }

    public Bundle getActionBarBundle() {
        return actionBarBundle;
    }

    public Uri getData() {
        return data;
    }


    private void buildProperties(Intent intent) {
        if (intent == null) return;
        this.data = intent.getData();
        if (intent.hasExtra(CustomTabsIntent.EXTRA_SESSION)) {
            this.token = CustomTabsSessionToken.getSessionTokenFromIntent(intent);
        }
        this.toolbarColor = intent.getIntExtra(CustomTabsIntent.EXTRA_TOOLBAR_COLOR, Color.BLUE);
        this.secondaryToolbarColor = intent.getIntExtra(CustomTabsIntent.EXTRA_SECONDARY_TOOLBAR_COLOR, Color.WHITE);
        this.enableUrlBarHiding = intent.getBooleanExtra(CustomTabsIntent.EXTRA_ENABLE_URLBAR_HIDING, false);
        this.closeButtonIcon = intent.getParcelableExtra(CustomTabsIntent.EXTRA_CLOSE_BUTTON_ICON);
        this.showTitle = intent.getIntExtra(CustomTabsIntent.EXTRA_TITLE_VISIBILITY_STATE, 0) == 1 ? true : false;

        ArrayList<Bundle> items = intent.getParcelableArrayListExtra(CustomTabsIntent.EXTRA_MENU_ITEMS);
        if (items != null)
            for (Bundle item : items) {
                MenuItemParam param = new MenuItemParam();
                param.setMenuText(item.getString(CustomTabsIntent.KEY_MENU_ITEM_TITLE));
                param.setPendingIntent((PendingIntent) item.getParcelable(CustomTabsIntent.KEY_PENDING_INTENT));
                menuItems.add(param);
            }

        ArrayList<Bundle> actionBtns = intent.getParcelableArrayListExtra(CustomTabsIntent.EXTRA_TOOLBAR_ITEMS);
        if (actionBtns != null) {
            for (Bundle actionBtn : actionBtns) {
                ActionBtnParam p = new ActionBtnParam();
                p.setId(actionBtn.getInt(CustomTabsIntent.KEY_ID));
                p.setIcon((Bitmap) actionBtn.getParcelable(CustomTabsIntent.KEY_ICON));
                p.setLabel(actionBtn.getString(CustomTabsIntent.KEY_DESCRIPTION));
                p.setPendingIntent((PendingIntent) actionBtn.getParcelable(CustomTabsIntent.KEY_PENDING_INTENT));
                this.actionButtons.add(p);
            }
        }

        this.exitAnimationBundle = intent.getBundleExtra(CustomTabsIntent.EXTRA_EXIT_ANIMATION_BUNDLE);
        this.showDefaultShareMenuItem = intent.getBooleanExtra(CustomTabsIntent.EXTRA_DEFAULT_SHARE_MENU_ITEM, false);
        this.tintActionBar = intent.getBooleanExtra(CustomTabsIntent.EXTRA_TINT_ACTION_BUTTON, false);
        this.actionBarBundle = intent.getBundleExtra(CustomTabsIntent.EXTRA_ACTION_BUTTON_BUNDLE);
    }

    private CustomTabsSessionToken token;
    private int toolbarColor;
    private int secondaryToolbarColor;
    private boolean enableUrlBarHiding;
    private Bitmap closeButtonIcon;
    private boolean showTitle;
    ArrayList<MenuItemParam> menuItems = new ArrayList<MenuItemParam>();
    // private Bundle startAnimationBundle;
    private Bundle exitAnimationBundle;
    private ArrayList<ActionBtnParam> actionButtons = new ArrayList<ActionBtnParam>();
    private boolean showDefaultShareMenuItem;
    private boolean tintActionBar;
    private Bundle actionBarBundle;
    private Uri data;

}
