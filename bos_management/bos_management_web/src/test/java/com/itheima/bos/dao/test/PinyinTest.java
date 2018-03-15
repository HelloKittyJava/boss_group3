package com.itheima.bos.dao.test;

import java.awt.image.DataBufferUShort;

import com.itheima.utils.PinYin4jUtils;

/**
 * ClassName:PinyinTest <br/>
 * Function: <br/>
 * Date: 2018年3月15日 上午11:04:00 <br/>
 */
public class PinyinTest {

    public static void main(String[] args) {

        String province = "广东省";
        String city = "深圳市";
        String district = "宝安区";
        // 城市编码 SHENZHEN 简码 GDSZBA

        province = province.substring(0, province.length() - 1);
        district = district.substring(0, district.length() - 1);
        city = city.substring(0, city.length() - 1);

        // String hanziToPinyin = PinYin4jUtils.hanziToPinyin(city, "").toUpperCase();
        //
        // System.out.println(hanziToPinyin);

        String[] headByString =
                PinYin4jUtils.getHeadByString(province + city + district);
        String string = PinYin4jUtils.stringArrayToString(headByString);
        System.out.println(string);

    }

}
