package cn.uc.uccustomtabs.entities;

import android.app.PendingIntent;

/**
 * Created by Administrator on 2016/3/16.
 */
public class MenuItemParam {
    private String menuText;
    private PendingIntent pendingIntent;
    private int menuId;

    public MenuItemParam() {
        menuId = (int) (Math.random() * 1234567);
    }

    public int getMenuId() {
        return menuId;
    }

    public void setMenuId(int menuId) {
        this.menuId = menuId;
    }

    public String getMenuText() {
        return menuText;
    }

    public void setMenuText(String menuText) {
        this.menuText = menuText;
    }

    public PendingIntent getPendingIntent() {
        return pendingIntent;
    }

    public void setPendingIntent(PendingIntent pendingIntent) {
        this.pendingIntent = pendingIntent;
    }
}
