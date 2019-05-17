package org.mayanjun.util;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.mayanjun.core.Assert;
import org.mayanjun.core.Message;
import org.mayanjun.core.ServiceException;
import org.mayanjun.core.Status;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Chinese ID card validator
 * @author mayanjun
 * @since 22/02/2018
 */
public class ChineseIDCardParser extends AbstractParser<String, ChineseIDCardParser.IDCard> {

    public static Status CODE_NUMBER_EMPTY = new Status(2018001, "身份证号为空");
    public static Status CODE_NUMBER_ERROR = new Status(2018002, "身份证号错误");
    public static Status CODE_BIRTHDATE_ERROR = new Status(2018003, "出生日期错误");
    public static Status CODE_PROVINCE_ERROR = new Status(2018004, "地区编码错误");
    public static Status CODE_CHECKSUM_ERROR = new Status(2018005, "身份证无效，最后一位错误");

    private static Map<String, String> PROVINCE_CODES = new HashMap<String, String>();

    private static String[] VAL_CODES = {"1", "0", "x", "9", "8", "7", "6", "5", "4", "3", "2"};

    private static int CHECK_SUM_CODE [] = {7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2};

    private static String[] PROVINCE_CODES_ARR = { "11", "北京", "12", "天津", "13", "河北", "14", "山西", "15", "内蒙古", "21", "辽宁", "22", "吉林", "23", "黑龙江", "31", "上海", "32", "江苏", "33", "浙江", "34", "安徽", "35", "福建", "36", "江西", "37", "山东", "41", "河南", "42", "湖北", "43", "湖南", "44", "广东", "45", "广西", "46", "海南", "50", "重庆", "51", "四川", "52", "贵州", "53", "云南", "54", "西藏", "61", "陕西", "62", "甘肃", "63", "青海", "64", "宁夏", "65", "新疆", "71", "台湾", "81", "香港", "82", "澳门", "91", "国外"};

    static {
        for (int i = 0; i < PROVINCE_CODES_ARR.length; i += 2) PROVINCE_CODES.put(PROVINCE_CODES_ARR[i], PROVINCE_CODES_ARR[i + 1]);
    }

    @Override
    protected IDCard doParse(String input) {
        String id = input;
        String province;
        String provinceCode;
        int generation;

        Assert.notBlank(id, CODE_NUMBER_EMPTY);
        id = id.replaceAll(" ", "").toLowerCase();

        String ai;

        if (id.length() == 18) {
            ai = id.substring(0, 17);
            generation = 2;
        } else if (id.length() == 15) {
            ai = id.substring(0, 6) + "19" + id.substring(6, 15);
            generation = 1;
        } else {
            throw new ServiceException(CODE_NUMBER_ERROR, "号码长度应该为15位或18位,您的身份证号码为" + id.length() + "位");
        }

        Assert.isTrue(Strings.isNumeric(ai), CODE_NUMBER_ERROR, "15位号码都应为数字; 18位号码除最后一位外，都应为数字");

        String birthdateString = ai.substring(6, 14);

        DateTime birthdate;
        try {
            birthdate = DateTime.parse(birthdateString, DateTimeFormat.forPattern("yyyyMMdd").withZone(DateTimeZone.forOffsetHours(8)));
        } catch (Exception e) {
            throw new ServiceException(CODE_BIRTHDATE_ERROR);
        }

        Assert.notNull(PROVINCE_CODES.get(ai.substring(0, 2)), CODE_PROVINCE_ERROR);

        int checkSum = 0;
        for (int i = 0; i < 17; i++) checkSum = checkSum + Integer.parseInt(String.valueOf(ai.charAt(i))) * CHECK_SUM_CODE[i];
        String strVerifyCode = VAL_CODES[checkSum % 11];
        ai = ai + strVerifyCode;
        Assert.isTrue(ai.equals(id), CODE_CHECKSUM_ERROR);

        provinceCode = ai.substring(0, 2).toString();
        province = PROVINCE_CODES.get(provinceCode);

        Character sexInfo = id.charAt(id.length() - 2);
        int sexInt = Integer.parseInt(sexInfo.toString());
        Gender gender = sexInt % 2 == 0 ? Gender.FEMALE : Gender.MALE;
        return new IDCard(id, gender, province, provinceCode, generation, birthdate.toDate(), ai);
    }

    /**
     * Represents an Id card
     *
     * @author mayanjun
     */
    public static class IDCard extends Message {

        private String number;
        private Gender gender;
        private String province;
        private String provinceCode;
        private int generation;
        private Date birthdate;
        private String number2;

        public IDCard() {
            super(Status.OK.getCode(), "OK");
        }

        public IDCard(int code) {
            super(code, "");
        }

        public IDCard(String number, Gender gender, String province, String provinceCode, int generation, Date birthdate, String number2) {
            super(Status.OK.getCode(), "OK");
            this.number = number;
            this.gender = gender;
            this.province = province;
            this.provinceCode = provinceCode;
            this.generation = generation;
            this.birthdate = birthdate;
            this.number2 = number2;
        }

        public String getIDCardNumber() {
            return number;
        }

        public String getProvince() {
            return province;
        }

        public String getProvinceCode() {
            return provinceCode;
        }

        public int getGeneration() {
            return generation;
        }

        public Date getBirthdate() {
            return birthdate;
        }

        public String getNumber2() {
            return number2;
        }

        public String getNumber() {
            return number;
        }

        public void setNumber(String number) {
            this.number = number;
        }

        public void setProvince(String province) {
            this.province = province;
        }

        public void setProvinceCode(String provinceCode) {
            this.provinceCode = provinceCode;
        }

        public void setGeneration(int generation) {
            this.generation = generation;
        }

        public void setBirthdate(Date birthdate) {
            this.birthdate = birthdate;
        }

        public void setNumber2(String number2) {
            this.number2 = number2;
        }

        public Gender getGender() {
            return gender;
        }

        public void setGender(Gender gender) {
            this.gender = gender;
        }

        @Override
        public String toString() {
            return "IDCard{" +
                    "number='" + number + '\'' +
                    ", gender=" + gender +
                    ", province='" + province + '\'' +
                    ", provinceCode='" + provinceCode + '\'' +
                    ", generation=" + generation +
                    ", birthdate=" + birthdate +
                    ", number2='" + number2 + '\'' +
                    '}';
        }
    }

    /**
     * @author mayanjun
     * @since 2018/7/6
     */
    public static enum Gender {

        UNKNOWN,
        MALE,
        FEMALE
    }
}