package com.example.introcard;

import android.content.Context;
import android.text.SpannableString;
import android.text.style.ClickableSpan;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
//@RunWith(AndroidJUnit4.class)
//public class ExampleInstrumentedTest {
//    @Test
//    public void useAppContext() {
//        // Context of the app under test.
//        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
//        assertEquals("com.example.introcard", appContext.getPackageName());
//    }
//}

@RunWith(AndroidJUnit4.class)
public class IntroCardViewTest {
    private final Context context = InstrumentationRegistry.getInstrumentation().getContext();
    private IntroCardView cardView;

    @Before
    public void setup() {
        cardView = new IntroCardView(context);
    }

    @Test
    public void testInitialState() {
        // 验证初始可见性
        assertEquals(View.GONE, cardView.getIconViewForTest().getVisibility());
        assertEquals(View.GONE, cardView.getTitleViewForTest().getVisibility());
        assertEquals(View.GONE, cardView.getContentViewForTest().getVisibility());
    }

    @Test
    public void testLinkFunctionality() {
        // 设置带链接的文本
        cardView.setContent("点击{link}管理", Color.BLUE);

        // 验证链接创建
        SpannableString spannable = (SpannableString) cardView.getContentViewForTest().getText();
        ClickableSpan[] spans = spannable.getSpans(0, spannable.length(), ClickableSpan.class);
        assertEquals(1, spans.length);

        // 验证点击事件
        cardView.setLinkClickListener(() -> { /* mock implementation */ });
        spans[0].onClick(cardView.getContentViewForTest());
    }
}