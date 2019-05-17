package org.mayanjun.util;

import org.mayanjun.core.Assert;
import org.mayanjun.core.Message;
import org.mayanjun.core.ServiceException;

import java.util.HashMap;
import java.util.Map;

/**
 * @author mayanjun
 * @since 2018/7/6
 */
public class ChinesePhoneNumberParser extends AbstractParser<String, ChinesePhoneNumberParser.PhoneNumber> {

    private int mobileNumberLength = 11;

    @Override
    protected PhoneNumber doParse(String input) {
        // remove white space
        char cs[] = input.toCharArray();
        PhoneNumber number = new PhoneNumber(input);
        char cs0 = cs[0];
        switch (cs0) {
            case '0':
                number.setOperator(MobileOperator.CHINA_TELECOM);
                parseRegionNumber(number, cs);
                break;
            case '1':
                validateMobileNumber(cs);
                number.setOperator(MobileOperator.getMobileOperator(String.valueOf(cs, 0, 3)));
                break;
        }
        return number;
    }

    private void validateMobileNumber(char[] cs) {
        Assert.isTrue(cs.length == mobileNumberLength, "手机号码长度必须是" + mobileNumberLength + "位");
        for (char c : cs) {
            if (!Character.isWhitespace(c)) {
                if (!Character.isDigit(c)) throw new ServiceException("号码中含有非数字字符");
            }
        }
    }

    private void parseRegionNumber(PhoneNumber number, char[] cs) {
        StringBuffer rn = new StringBuffer();
        StringBuffer pn = new StringBuffer();
        boolean numberBegin = false;
        for (char c : cs) {
            if (Character.isDigit(c)) {
                if (numberBegin) pn.append(c);
                else rn.append(c);
            } else {
                numberBegin = true;
            }
        }
        number.setRegionNumber(rn.toString());
        number.setPhoneNumber(pn.toString());
    }

    public int getMobileNumberLength() {
        return mobileNumberLength;
    }

    public void setMobileNumberLength(int mobileNumberLength) {
        this.mobileNumberLength = mobileNumberLength;
    }



    /**
     * @author mayanjun
     * @since 2018/7/7
     */
    public static class PhoneNumber extends Message {

        private String number;

        private MobileOperator operator;

        private String regionNumber;

        private String phoneNumber;

        public PhoneNumber(String number) {
            this.number = number;
        }

        public PhoneNumber(int code, String description, Throwable exception) {
            super(code, description, exception);
        }

        public PhoneNumber(int code, String description) {
            super(code, description);
        }

        public PhoneNumber() {
        }

        public String getNumber() {
            return number;
        }

        public void setNumber(String number) {
            this.number = number;
        }

        public MobileOperator getOperator() {
            return operator;
        }

        public void setOperator(MobileOperator operator) {
            this.operator = operator;
        }

        public String getRegionNumber() {
            return regionNumber;
        }

        public void setRegionNumber(String regionNumber) {
            this.regionNumber = regionNumber;
        }

        public String getPhoneNumber() {
            return phoneNumber;
        }

        public void setPhoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
        }

        @Override
        public String toString() {
            return "PhoneNumber{" +
                    "number='" + number + '\'' +
                    ", operator=" + operator +
                    ", regionNumber='" + regionNumber + '\'' +
                    ", phoneNumber='" + phoneNumber + '\'' +
                    '}';
        }
    }

    /**
     * @author mayanjun
     * @since 2018/7/7
     */
    public static enum MobileOperator {

        UNKNOWN,
        CHINA_UNICOME("130", "131", "132", "155", "156", "185", "186"),
        CHINA_TELECOM("133", "153", "180", "189"),
        CMCC("134", "135", "136", "137", "138", "139", "147", "150", "151", "152", "157", "158", "159", "187", "188");

        private static Map<String, MobileOperator> OPERATORS_INDEX = new HashMap<String, MobileOperator>();

        static {
            MobileOperator operators[] = MobileOperator.values();
            for (MobileOperator mo : operators) {
                for(String p : mo.numberPrefixes) OPERATORS_INDEX.put(p, mo);
            }
        }

        public static MobileOperator getMobileOperator(String number) {
            String num = number;
            if (num.length() > 3) num = num.substring(0, 3);
            MobileOperator operator = OPERATORS_INDEX.get(num);
            if (operator != null) return operator;
            return MobileOperator.UNKNOWN;
        }

        private String numberPrefixes[];

        MobileOperator(String ... numberPrefixes) {
            this.numberPrefixes = numberPrefixes;
        }

        public String[] getNumberPrefixes() {
            return numberPrefixes;
        }
    }


}
