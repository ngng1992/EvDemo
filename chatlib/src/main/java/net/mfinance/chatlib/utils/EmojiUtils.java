/**
 * Copyright (C) 2013-2014 EaseMob Technologies. All rights reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.mfinance.chatlib.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.Spannable.Factory;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ImageSpan;

import net.mfinance.chatlib.R;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmojiUtils {

    private static final String ee_1 = "[):]";
    private static final String ee_2 = "[:D]";
    private static final String ee_3 = "[;)]";
    private static final String ee_4 = "[:-o]";
    private static final String ee_5 = "[:p]";
    private static final String ee_6 = "[(H)]";
    private static final String ee_7 = "[:@]";
    private static final String ee_8 = "[:s]";
    private static final String ee_9 = "[:$]";
    private static final String ee_10 = "[:(]";
    private static final String ee_11 = "[:'(]";
    private static final String ee_12 = "[:|]";
    private static final String ee_13 = "[(a)]";
    private static final String ee_14 = "[8o|]";
    private static final String ee_15 = "[8-|]";
    private static final String ee_16 = "[+o(]";
    private static final String ee_17 = "[<o)]";
    private static final String ee_18 = "[|-)]";
    private static final String ee_19 = "[*-)]";
    private static final String ee_20 = "[:-#]";
    private static final String ee_21 = "[:-*]";
    private static final String ee_22 = "[^o)]";
    private static final String ee_23 = "[8-)]";
    private static final String ee_24 = "[(|)]";
    private static final String ee_25 = "[(u)]";
    private static final String ee_26 = "[(S)]";
    private static final String ee_27 = "[(*)]";
    private static final String ee_28 = "[(#)]";
    private static final String ee_29 = "[(R)]";
    private static final String ee_30 = "[({)]";
    private static final String ee_31 = "[(})]";
    private static final String ee_32 = "[(k)]";
    private static final String ee_33 = "[(F)]";
    private static final String ee_34 = "[(W)]";
    private static final String ee_35 = "[(D)]";

    private static final String ee_60 = "[(60D)]";
    private static final String ee_61 = "[(61D)]";
    private static final String ee_62 = "[(62D)]";
    private static final String ee_63 = "[(63D)]";
    private static final String ee_64 = "[(64D)]";
    private static final String ee_65 = "[(65D)]";
    private static final String ee_66 = "[(66D)]";
    private static final String ee_67 = "[(67D)]";
    private static final String ee_68 = "[(68D)]";
    private static final String ee_69 = "[(69D)]";
    private static final String ee_70 = "[(70D)]";

    private static final String ee_71 = "[(71D)]";
    private static final String ee_72 = "[(72D)]";
    private static final String ee_73 = "[(73D)]";
    private static final String ee_74 = "[(74D)]";
    private static final String ee_75 = "[(75D)]";
    private static final String ee_76 = "[(76D)]";
    private static final String ee_77 = "[(77D)]";
    private static final String ee_78 = "[(78D)]";
    private static final String ee_79 = "[(79D)]";
    private static final String ee_80 = "[(80D)]";

    private static final String ee_81 = "[(81D)]";
    private static final String ee_82 = "[(82D)]";
    private static final String ee_83 = "[(83D)]";
    private static final String ee_84 = "[(84D)]";
    private static final String ee_85 = "[(85D)]";
    private static final String ee_86 = "[(86D)]";
    private static final String ee_87 = "[(87D)]";
    private static final String ee_88 = "[(88D)]";
    private static final String ee_89 = "[(89D)]";
    private static final String ee_90 = "[(90D)]";

    private static final String ee_91 = "[(91D)]";
    private static final String ee_92 = "[(92D)]";
    private static final String ee_93 = "[(93D)]";
    private static final String ee_94 = "[(94D)]";
    private static final String ee_95 = "[(95D)]";
    private static final String ee_96 = "[(96D)]";
    private static final String ee_97 = "[(97D)]";
    private static final String ee_98 = "[(98D)]";
    private static final String ee_99 = "[(99D)]";
    private static final String ee_100 = "[(100D)]";


    private static final String ee_101 = "[(101D)]";
    private static final String ee_102 = "[(102D)]";
    private static final String ee_103 = "[(103D)]";
    private static final String ee_104 = "[(104D)]";
    private static final String ee_105 = "[(105D)]";
    private static final String ee_106 = "[(106D)]";
    private static final String ee_107 = "[(107D)]";
    private static final String ee_108 = "[(108D)]";
    private static final String ee_109 = "[(109D)]";
    private static final String ee_110 = "[(110D)]";


    private static final String ee_111 = "[(111D)]";
    private static final String ee_112 = "[(112D)]";
    private static final String ee_113 = "[(113D)]";

    private static final Factory spannableFactory = Factory.getInstance();

    private static final Map<Pattern, Integer> emoticons = new HashMap<>();

    static {
        addPattern(ee_1, R.drawable.ee_1);
        addPattern(ee_2, R.drawable.ee_2);
        addPattern(ee_3, R.drawable.ee_3);
        addPattern(ee_4, R.drawable.ee_4);
        addPattern(ee_5, R.drawable.ee_5);
        addPattern(ee_6, R.drawable.ee_6);
        addPattern(ee_7, R.drawable.ee_7);
        addPattern(ee_8, R.drawable.ee_8);
        addPattern(ee_9, R.drawable.ee_9);
        addPattern(ee_10, R.drawable.ee_10);
        addPattern(ee_11, R.drawable.ee_11);
        addPattern(ee_12, R.drawable.ee_12);
        addPattern(ee_13, R.drawable.ee_13);
        addPattern(ee_14, R.drawable.ee_14);
        addPattern(ee_15, R.drawable.ee_15);
        addPattern(ee_16, R.drawable.ee_16);
        addPattern(ee_17, R.drawable.ee_17);
        addPattern(ee_18, R.drawable.ee_18);
        addPattern(ee_19, R.drawable.ee_19);
        addPattern(ee_20, R.drawable.ee_20);
        addPattern(ee_21, R.drawable.ee_21);
        addPattern(ee_22, R.drawable.ee_22);
        addPattern(ee_23, R.drawable.ee_23);
        addPattern(ee_24, R.drawable.ee_24);
        addPattern(ee_25, R.drawable.ee_25);
        addPattern(ee_26, R.drawable.ee_26);
        addPattern(ee_27, R.drawable.ee_27);
        addPattern(ee_28, R.drawable.ee_28);
        addPattern(ee_29, R.drawable.ee_29);
        addPattern(ee_30, R.drawable.ee_30);
        addPattern(ee_31, R.drawable.ee_31);
        addPattern(ee_32, R.drawable.ee_32);
        addPattern(ee_33, R.drawable.ee_33);
        addPattern(ee_34, R.drawable.ee_34);
        addPattern(ee_35, R.drawable.ee_35);

        addPattern(ee_60, R.drawable.ee_60);
        addPattern(ee_61, R.drawable.ee_61);
        addPattern(ee_62, R.drawable.ee_62);
        addPattern(ee_63, R.drawable.ee_63);
        addPattern(ee_64, R.drawable.ee_64);
        addPattern(ee_65, R.drawable.ee_65);
        addPattern(ee_66, R.drawable.ee_66);
        addPattern(ee_67, R.drawable.ee_67);
        addPattern(ee_68, R.drawable.ee_68);
        addPattern(ee_69, R.drawable.ee_69);
        addPattern(ee_70, R.drawable.ee_70);

        addPattern(ee_71, R.drawable.ee_71);
        addPattern(ee_72, R.drawable.ee_72);
        addPattern(ee_73, R.drawable.ee_73);
        addPattern(ee_74, R.drawable.ee_74);
        addPattern(ee_75, R.drawable.ee_75);
        addPattern(ee_76, R.drawable.ee_76);
        addPattern(ee_77, R.drawable.ee_77);
        addPattern(ee_78, R.drawable.ee_78);
        addPattern(ee_79, R.drawable.ee_79);
        addPattern(ee_80, R.drawable.ee_80);

        addPattern(ee_81, R.drawable.ee_81);
        addPattern(ee_82, R.drawable.ee_82);
        addPattern(ee_83, R.drawable.ee_83);
        addPattern(ee_84, R.drawable.ee_84);
        addPattern(ee_85, R.drawable.ee_85);
        addPattern(ee_86, R.drawable.ee_86);
        addPattern(ee_87, R.drawable.ee_87);
        addPattern(ee_88, R.drawable.ee_88);
        addPattern(ee_89, R.drawable.ee_89);
        addPattern(ee_90, R.drawable.ee_90);

        addPattern(ee_91, R.drawable.ee_91);
        addPattern(ee_92, R.drawable.ee_92);
        addPattern(ee_93, R.drawable.ee_93);
        addPattern(ee_94, R.drawable.ee_94);
        addPattern(ee_95, R.drawable.ee_95);
        addPattern(ee_96, R.drawable.ee_96);
        addPattern(ee_97, R.drawable.ee_97);
        addPattern(ee_98, R.drawable.ee_98);
        addPattern(ee_99, R.drawable.ee_99);
        addPattern(ee_100, R.drawable.ee_100);

        addPattern(ee_101, R.drawable.ee_101);
        addPattern(ee_102, R.drawable.ee_102);
        addPattern(ee_103, R.drawable.ee_103);
        addPattern(ee_104, R.drawable.ee_104);
        addPattern(ee_105, R.drawable.ee_105);
        addPattern(ee_106, R.drawable.ee_106);
        addPattern(ee_107, R.drawable.ee_107);
        addPattern(ee_108, R.drawable.ee_108);
        addPattern(ee_109, R.drawable.ee_109);
        addPattern(ee_110, R.drawable.ee_110);

        addPattern(ee_111, R.drawable.ee_111);
        addPattern(ee_112, R.drawable.ee_112);
        addPattern(ee_113, R.drawable.ee_113);
    }

    private static void addPattern(String smile, int resource) {
        EmojiUtils.emoticons.put(Pattern.compile(Pattern.quote(smile)), resource);
    }

    public static Spannable getEmojiText(Context context, CharSequence text) {
        Spannable spannable = spannableFactory.newSpannable(text);
        addSmiles(context, spannable);
        return spannable;
    }

    /**
     * replace existing spannable with smiles
     */
    private static void addSmiles(Context context, Spannable spannable) {
        for (Entry<Pattern, Integer> entry : emoticons.entrySet()) {
            Matcher matcher = entry.getKey().matcher(spannable);
            while (matcher.find()) {
                boolean set = true;
                for (ImageSpan span : spannable.getSpans(matcher.start(), matcher.end(), ImageSpan.class)) {
                    if (spannable.getSpanStart(span) >= matcher.start() && spannable.getSpanEnd(span) <= matcher.end()) {
                        spannable.removeSpan(span);
                    } else {
                        set = false;
                        break;
                    }
                }
                if (set) {
                    Drawable drawable = context.getResources().getDrawable(entry.getValue());
                    // 原图有点大 132*0.45
                    int intrinsicWidth = drawable.getIntrinsicWidth();
                    int intrinsicHeight = drawable.getIntrinsicHeight();
                    int cWidth = (int) (intrinsicWidth * 0.45);
                    int cHeight = (int) (intrinsicHeight * 0.45);
//                    Log.e("chat", "intrinsicWidth = " + intrinsicWidth + ";intrinsicHeight = " + intrinsicHeight);
//                    Log.e("chat", "cWidth = " + cWidth + ";cHeight = " + cHeight);
                    drawable.setBounds(0, 0, cWidth, cHeight);
                    ImageSpan imageSpan = new ImageSpan(drawable);
                    spannable.setSpan(imageSpan,
                            matcher.start(), matcher.end(),
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
            }
        }
    }

    public static Spannable getEmojiText(Context context, SpannableString spannableString) {
        Spannable spannable = spannableFactory.newSpannable(spannableString);
        addSmiles(context, spannable);
        return spannable;
    }

    public static boolean containsKey(String key) {
        boolean b = false;
        for (Entry<Pattern, Integer> entry : emoticons.entrySet()) {
            Matcher matcher = entry.getKey().matcher(key);
            if (matcher.find()) {
                b = true;
                break;
            }
        }

        return b;
    }

    /**
     * 根据反射拿到对应的emoji，如果有混淆，可能会出错
     * 根据ee_1表情转义
     *
     * @param emoji ee_1
     * @return spannable
     */
    public static Spannable getString(Context context, String emoji) {
        Spannable spannable = null;
        String emojiText = null;
        switch (emoji) {
            case "ee_1":
                emojiText = ee_1;
                break;
            case "ee_2":
                emojiText = ee_2;
                break;
            case "ee_3":
                emojiText = ee_3;
                break;
            case "ee_4":
                emojiText = ee_4;
                break;
            case "ee_5":
                emojiText = ee_5;
                break;
            case "ee_6":
                emojiText = ee_6;
                break;
            case "ee_7":
                emojiText = ee_7;
                break;
            case "ee_8":
                emojiText = ee_8;
                break;
            case "ee_9":
                emojiText = ee_9;
                break;
            case "ee_10":
                emojiText = ee_10;
                break;
            case "ee_11":
                emojiText = ee_11;
                break;
            case "ee_12":
                emojiText = ee_12;
                break;
            case "ee_13":
                emojiText = ee_13;
                break;
            case "ee_14":
                emojiText = ee_14;
                break;
            case "ee_15":
                emojiText = ee_15;
                break;
            case "ee_16":
                emojiText = ee_16;
                break;
            case "ee_17":
                emojiText = ee_17;
                break;
            case "ee_18":
                emojiText = ee_18;
                break;
            case "ee_19":
                emojiText = ee_19;
                break;
            case "ee_20":
                emojiText = ee_20;
                break;
            case "ee_21":
                emojiText = ee_21;
                break;
            case "ee_22":
                emojiText = ee_22;
                break;
            case "ee_23":
                emojiText = ee_23;
                break;
            case "ee_24":
                emojiText = ee_24;
                break;
            case "ee_25":
                emojiText = ee_25;
                break;
            case "ee_26":
                emojiText = ee_26;
                break;
            case "ee_27":
                emojiText = ee_27;
                break;
            case "ee_28":
                emojiText = ee_28;
                break;
            case "ee_29":
                emojiText = ee_29;
                break;
            case "ee_30":
                emojiText = ee_30;
                break;
            case "ee_31":
                emojiText = ee_31;
                break;
            case "ee_32":
                emojiText = ee_32;
                break;
            case "ee_33":
                emojiText = ee_33;
                break;
            case "ee_34":
                emojiText = ee_34;
                break;
            case "ee_35":
                emojiText = ee_35;
                break;
            case "ee_60":
                emojiText = ee_60;
                break;
            case "ee_61":
                emojiText = ee_61;
                break;
            case "ee_62":
                emojiText = ee_62;
                break;
            case "ee_63":
                emojiText = ee_63;
                break;
            case "ee_64":
                emojiText = ee_64;
                break;
            case "ee_65":
                emojiText = ee_65;
                break;
            case "ee_66":
                emojiText = ee_66;
                break;
            case "ee_67":
                emojiText = ee_67;
                break;
            case "ee_68":
                emojiText = ee_68;
                break;
            case "ee_69":
                emojiText = ee_69;
                break;
            case "ee_70":
                emojiText = ee_70;
                break;
            case "ee_71":
                emojiText = ee_71;
                break;
            case "ee_72":
                emojiText = ee_72;
                break;
            case "ee_73":
                emojiText = ee_73;
                break;
            case "ee_74":
                emojiText = ee_74;
                break;
            case "ee_75":
                emojiText = ee_75;
                break;
            case "ee_76":
                emojiText = ee_76;
                break;
            case "ee_77":
                emojiText = ee_77;
                break;
            case "ee_78":
                emojiText = ee_78;
                break;
            case "ee_79":
                emojiText = ee_79;
                break;
            case "ee_80":
                emojiText = ee_80;
                break;
            case "ee_81":
                emojiText = ee_81;
                break;
            case "ee_82":
                emojiText = ee_82;
                break;
            case "ee_83":
                emojiText = ee_83;
                break;
            case "ee_84":
                emojiText = ee_84;
                break;
            case "ee_85":
                emojiText = ee_85;
                break;
            case "ee_86":
                emojiText = ee_86;
                break;
            case "ee_87":
                emojiText = ee_87;
                break;
            case "ee_88":
                emojiText = ee_88;
                break;
            case "ee_89":
                emojiText = ee_89;
                break;
            case "ee_90":
                emojiText = ee_90;
                break;
            case "ee_91":
                emojiText = ee_91;
                break;
            case "ee_92":
                emojiText = ee_92;
                break;
            case "ee_93":
                emojiText = ee_93;
                break;
            case "ee_94":
                emojiText = ee_94;
                break;
            case "ee_95":
                emojiText = ee_95;
                break;
            case "ee_96":
                emojiText = ee_96;
                break;
            case "ee_97":
                emojiText = ee_97;
                break;
            case "ee_98":
                emojiText = ee_98;
                break;
            case "ee_99":
                emojiText = ee_99;
                break;
            case "ee_100":
                emojiText = ee_100;
                break;
            case "ee_101":
                emojiText = ee_101;
                break;
            case "ee_102":
                emojiText = ee_102;
                break;
            case "ee_103":
                emojiText = ee_103;
                break;
            case "ee_104":
                emojiText = ee_104;
                break;
            case "ee_105":
                emojiText = ee_105;
                break;
            case "ee_106":
                emojiText = ee_106;
                break;
            case "ee_107":
                emojiText = ee_107;
                break;
            case "ee_108":
                emojiText = ee_108;
                break;
            case "ee_109":
                emojiText = ee_109;
                break;
            case "ee_110":
                emojiText = ee_110;
                break;
            case "ee_111":
                emojiText = ee_111;
                break;
            case "ee_112":
                emojiText = ee_112;
                break;
            case "ee_113":
                emojiText = ee_113;
                break;
            default:
                break;
        }
        if (!TextUtils.isEmpty(emojiText)) {
            spannable = getEmojiText(context, emojiText);
        }
        return spannable;
    }

}
