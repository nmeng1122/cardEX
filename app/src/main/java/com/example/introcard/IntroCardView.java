package com.example.introcard;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

/**
 * 设置项卡片控件，支持自定义图标、标题、内容和背景
 * 使用示例：
 * <com.ycom.example.introcard.IntroCardView
 *     android:layout_width="match_parent"
 *     android:layout_height="wrap_content"
 *     app:cardIcon="@drawable/ic_bluetooth"
 *     app:cardTitle="蓝牙设置"
 *     app:cardContent="点击管理已连接的设备{link}"
 *     app:contentLinkColor="@color/blue"
 *     app:squareBottomCorners="true"
 *     app:cornerRadius="8dp"/>
 */
public class IntroCardView extends FrameLayout {
    private CardView cardView;
    private ImageView iconView;
    private TextView titleView;
    private TextView contentView;
    private ImageView backgroundImageView;

    private float cornerRadius;
    private boolean squareBottomCorners;

    public IntroCardView(@NonNull Context context) {
        super(context);
        init(context, null);
    }

    public IntroCardView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        LayoutInflater.from(context).inflate(R.layout.intro_card_layout, this);
        cardView = findViewById(R.id.card_view);
        iconView = findViewById(R.id.iv_icon);
        titleView = findViewById(R.id.tv_title);
        contentView = findViewById(R.id.tv_content);
        backgroundImageView = findViewById(R.id.iv_background);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.IntroCardView);

        try {
            // 获取自定义属性
            Drawable icon = a.getDrawable(R.styleable.IntroCardView_cardIcon);
            String title = a.getString(R.styleable.IntroCardView_cardTitle);
            String content = a.getString(R.styleable.IntroCardView_cardContent);
            int linkColor = a.getColor(R.styleable.IntroCardView_contentLinkColor,
                    getResources().getColor(android.R.color.holo_blue_dark));
            squareBottomCorners = a.getBoolean(R.styleable.IntroCardView_squareBottomCorners, false);
            cornerRadius = a.getDimension(R.styleable.IntroCardView_cornerRadius,
                    getResources().getDimension(R.dimen.default_corner_radius));
            Drawable backgroundImage = a.getDrawable(R.styleable.IntroCardView_cardBackgroundImage);

            // 设置视图属性
            setIcon(icon);
            setTitle(title);
            setContent(content, linkColor);
            setCardBackgroundImage(backgroundImage);
            updateCornerRadius();
        } finally {
            a.recycle();
        }
    }

    /**
     * 更新卡片圆角样式
     */
    private void updateCornerRadius() {
        float[] radii = new float[] {
                cornerRadius, cornerRadius,    // 左上、右上
                cornerRadius, cornerRadius,    // 右下、左下（默认圆角）
        };

        if (squareBottomCorners) {
            radii[2] = 0;
            radii[3] = 0;
        }

        cardView.setRadius(cornerRadius);
        // 需要自定义背景处理时可以使用以下代码：
        // GradientDrawable shape = new GradientDrawable();
        // shape.setCornerRadii(radii);
        // cardView.setBackground(shape);
    }

    // 公有方法
    public void setIcon(Drawable icon) {
        iconView.setImageDrawable(icon);
        iconView.setVisibility(icon != null ? VISIBLE : GONE);
    }

    public void setTitle(String title) {
        titleView.setText(title);
        titleView.setVisibility(title != null ? VISIBLE : GONE);
    }

    /**
     * 设置内容文本，支持超链接标记
     * @param content 包含{link}标记的文本，例如："点击{link}管理"
     * @param linkColor 超链接颜色
     */
    public void setContent(String content, int linkColor) {
        if (content == null) {
            contentView.setVisibility(GONE);
            return;
        }

        SpannableString spannable = new SpannableString(content);
        int linkStart = content.indexOf("{link}");
        if (linkStart != -1) {
            ClickableSpan clickableSpan = new ClickableSpan() {
                @Override
                public void onClick(@NonNull View widget) {
                    if (listener != null) {
                        listener.onLinkClicked();
                    }
                }
            };
            spannable.setSpan(clickableSpan, linkStart, linkStart + 6, 0);
            contentView.setHighlightColor(getResources().getColor(android.R.color.transparent));
            contentView.setMovementMethod(LinkMovementMethod.getInstance());
        }

        contentView.setText(spannable);
        contentView.setLinkTextColor(linkColor);
        contentView.setVisibility(VISIBLE);
    }

    public void setCardBackgroundImage(Drawable background) {
        backgroundImageView.setImageDrawable(background);
        backgroundImageView.setVisibility(background != null ? VISIBLE : GONE);
    }

    public void setSquareBottomCorners(boolean square) {
        this.squareBottomCorners = square;
        updateCornerRadius();
    }

    // 点击监听接口
    public interface LinkClickListener {
        void onLinkClicked();
    }

    private LinkClickListener listener;

    public void setLinkClickListener(LinkClickListener listener) {
        this.listener = listener;
    }

    ImageView getIconViewForTest() { return iconView; }

    TextView getTitleViewForTest() { return titleView; }
}
