package it.sephiroth.android.library.bottomnavigation;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.IdRes;

import java.util.HashMap;
import java.util.HashSet;

import it.sephiroth.android.library.bottonnavigation.R;
import proguard.annotation.Keep;
import proguard.annotation.KeepClassMembers;

/**
 * Created by alessandro crugnola on 4/12/16.
 * BadgeProvider
 */
@Keep
@KeepClassMembers
public class BadgeProvider {
    private final BottomNavigation navigation;
    private final HashSet<Integer> map = new HashSet<>();
    private final int badgeSize;

    /*0:显示原点
    * 1:显示数字*/
    private int badgetype = 0;
    private final HashMap<Integer, Integer> countMap = new HashMap<>();

    public BadgeProvider(final BottomNavigation navigation) {
        this.navigation = navigation;
        this.badgeSize = navigation.getContext().getResources().getDimensionPixelSize(R.dimen.bbn_badge_size);
    }

    protected Bundle save() {
        Bundle bundle = new Bundle();
        bundle.putSerializable("map", map);
        return bundle;
    }

    @SuppressWarnings("unchecked")
    public void restore(final Bundle bundle) {
        HashSet<Integer> set = (HashSet<Integer>) bundle.getSerializable("map");
        if (null != set) {
            map.addAll(set);
        }
    }

    /**
     * Returns if the menu item will require a badge
     *
     * @param itemId the menu item id
     * @return true if the menu item has to draw a badge
     */
    public boolean hasBadge(@IdRes final int itemId) {
        return map.contains(itemId);
    }

    Drawable getBadge(@IdRes final int itemId) {
        if (map.contains(itemId)) {
            return newDrawable(itemId, navigation.menu.getBadgeColor());
        }
        return null;
    }

/*    @SuppressWarnings ("unused")
    protected Drawable newDrawable(@IdRes final int itemId, final int preferredColor) {
        return new BadgeDrawable(preferredColor, badgeSize);
    }*/

    /**
     * Request to display a new badge over the passed menu item id
     *
     * @param itemId the menu item id
     */
    public void show(@IdRes final int itemId) {
        map.add(itemId);
        navigation.invalidateBadge(itemId);
    }

    /**
     * Remove the currently displayed badge
     *
     * @param itemId the menu item id
     */
    public void remove(@IdRes final int itemId) {
        countMap.remove(itemId);
        if (map.remove(itemId)) {
            navigation.invalidateBadge(itemId);
        }
    }

    public int getBadgeTextCount(@IdRes final int itemId) {
        if (countMap.containsKey(itemId)) {
            return countMap.get(itemId);
        }
        return 0;
    }

    public void show(@IdRes final int itemId, int count) {
        countMap.put(itemId, count);
        show(itemId);
    }

    protected Drawable newDrawable(@IdRes final int itemId, final int preferredColor) {
        int count = 1;
        if (countMap.containsKey(itemId)) {
            count = countMap.get(itemId);
        }
        if (badgetype == 0)
            return new BadgeDrawable(preferredColor, badgeSize);
        else
            return new Badge(preferredColor, count);
    }

    public static final class Badge extends Drawable {
        final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        final Paint textPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.LINEAR_TEXT_FLAG | Paint.SUBPIXEL_TEXT_FLAG);

        private String text;
        private float top;
        private float left;

        public Badge(final int color, final int count) {
            super();
            this.text = String.valueOf(count);

            paint.setColor(color);

            textPaint.setColor(Color.BLACK);
            textPaint.setTextSize(24);
        }

        @Override
        public void draw(final Canvas canvas) {
            final Rect rect = getBounds();
            canvas.drawCircle(rect.centerX(), rect.centerY(), rect.width() / 2, paint);
            canvas.drawText(text, 0, text.length(), left, top, textPaint);
        }

        @Override
        protected void onBoundsChange(final Rect bounds) {
            super.onBoundsChange(bounds);
            bounds.offset(bounds.width() / 2, -bounds.height() / 2);
            Paint.FontMetrics metrics = textPaint.getFontMetrics();
            float size = textPaint.measureText(text, 0, text.length());
            left = (bounds.left + (bounds.width() - size) / 2);
            top = bounds.centerY() - (metrics.ascent / 2) - metrics.descent / 2;
        }

        @Override
        public void setAlpha(final int alpha) {
            paint.setAlpha(alpha);
            textPaint.setAlpha(alpha);
        }

        @Override
        public void setColorFilter(final ColorFilter colorFilter) {
        }

        @Override
        public int getOpacity() {
            return PixelFormat.TRANSLUCENT;
        }

        @Override
        public int getIntrinsicWidth() {
            return 50;
        }

        @Override
        public int getIntrinsicHeight() {
            return 50;
        }
    }

    public void setBadgetype(int badgetype) {
        this.badgetype = badgetype;
    }
}
