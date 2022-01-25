package org.example.util;

public class Constant {
    public final static String REGEX_SPECIFIC_CHARACTER = "^[A-Za-z0-9_\\s]+";
    public final static String PARAM_SUGGESTED_STOCK_CODES = "SUGGESTED_STOCK_CODES";
    public final static String PARAM_PERMISSION_STOCK_CODES_BUYING = "PERMISSION_STOCK_CODES_BUYING";
    public final static String LANGUAGE_VI = "vi";
    public final static int COUPON_EXPIRE_DAY_DEFAULT = 90;
    public final static int COUPON_MAXIMUM_PER_USER_DEFAULT = 1000;
    public final static double COUPON_AMOUNT_DEFAULT = 100000;
    public final static double KOL_POINT_DEFAULT = 0;
    public static final String GROUP_CORE = "CORE";
    public static final String CHANNEL = "07";
    public static final String COUPONID_FORMAT = "IDCOUPON%s";
    public static final String SPLIT_COMMA = ",";
    public static final String EMAIL_OTP_VERIFY = "EMAIL_OTP_VERIFY";
    public static final double KOL_POINT_AT_LEAST_01_PORTFOLIO_DEFAULT = 50;
    public static final double KOL_POINT_AT_LEAST_03_PORTFOLIO_DEFAULT = 100;
    public static final double KOL_POINT_FOR_EACH_POST_DEFAULT = 5;
    public static final double KOL_POINT_FOR_EACH_FOLLOWER_DEFAULT = 1;
    public static final double KOL_POINT_FOR_EACH_FOLLOWING_DEFAULT = 0.5;
    public static final double KOL_POINT_AT_LEAST_01_WATCHLIST_DEFAULT = 10;
    public static final double KOL_POINT_AT_LEAST_03_WATCHLIST_DEFAULT = 30;
    public static final int STOCK_CODE_SUGGESTION_IN_SAME_TYPE_POINT = 5;
    public static final int STOCK_CODE_SUGGESTION_IN_VN30_OR_HNX30_POINT = 3;
    public static final int STOCK_CODE_SUGGESTION_HAVE_POSITIVE_ROA_ROE_POINT = 3;
    public static final int STOCK_CODE_SUGGESTION_IN_POSITIVE_PER_CHANGE_POINT = 5;
    public static final int COUPON_DAY_CAN_CHANGE_SETTING_DEFAULT = 30;
    public static final String PINEX_LAUNCHING_DAY = "PINEX_LAUNCHING_DAY";
    public static final String PINEX_LAUNCHING_DAY_DEFAULT = "2021-09-30";
    public static final String REMIND_REFERRAL_COUPON_TITLE = "Lời nhắc mở tài khoản PineX";
    public static final String REMIND_REFERRAL_COUPON_MESSAGE = " nhắc bạn hoàn thiện mở tài khoản để nhận thưởng nhé!";
    public static final String COUPON_ALLOW_STOCK_EXCHANGE_LIST = "COUPON_ALLOW_STOCK_EXCHANGE_LIST";
    public static final String COUPON_HOSE_ALLOW_TRADING_TYPE_LIST = "COUPON_HOSE_ALLOW_TRADING_TYPE_LIST";
    public static final String COUPON_HNX_ALLOW_TRADING_TYPE_LIST = "COUPON_HNX_ALLOW_TRADING_TYPE_LIST";
    public static final String DAY_CHANGE_COUPON_AWARD_FROM_200_TO_100 = "DAY_CHANGE_COUPON_AWARD_FROM_200_TO_100";
    public static final String DAY_CHANGE_COUPON_AWARD_FROM_200_TO_100_DEFAULT = "2022-01-21";
    public static final long REFERRAL_COUPON_AMOUNT_200 = 200000;

    public static final String sqlNativeForCustomerDTO = "select DISTINCT c.id, c.username, c.account_no as accountNo,c.sub_account as subAccount, c.vsd, c.cif, c.address,c.position, c.name, c.avatar, c.caption, " +
            " c.is_kol as isKol, c.has_sync_contact as hasSyncContact,c.auth_def as authDef, c.first_login as firstLogin, " +
            " c.phone, c.email, c.customer_state as state,c.guid, c.device_type as deviceType, " +
            " c.fcm_token as fcmToken," +
            " c.kol_point as kolPoint, c.dob, c.identity_card_no as identityCardNo, c.contact_address as contactAddress,c.gender, c.display_name as displayName, c.deleted_at as deletedAt, " +
            " c.created_at as createdAt,c.updated_at as updatedAt, c.open_date as openDate from customer c";

    public enum DeviceType {
        MOBILE_ANDROID, MOBILE_IOS, WEB
    }

    public enum StockExchange {
        HOSE, HNX, UPCOM, OTC
    }

    public enum SearchKeywordType {
        ALL, STOCK, FRIEND, NEWS, POST
    }

    public enum AuthDef {
        MATRIX, TOP, CA, SMART
    }

    public enum SourceFollow {
        NORMAL, REGISTER
    }

    private Constant() {
    }

    public enum CouponCoreStatus {
        NOT_JOIN("0", "NOT_JOIN"),
        NOT_CLAIM("1", "NOT_CLAIM"),
        CLAIM_PENDING("2", "CLAIM_PENDING"),
        CLAIMED("3", "CLAIMED");

        public final String code;
        public final String value;

        CouponCoreStatus(String code, String value) {
            this.code = code;
            this.value = value;
        }

        public static CouponCoreStatus fromCode(String code) {
            for (CouponCoreStatus is : CouponCoreStatus.values()) {
                if (is.code.equalsIgnoreCase(code)) {
                    return is;
                }
            }
            return CouponCoreStatus.NOT_JOIN;
        }
    }

    public enum CouponResponseStatus {
        NOT_JOIN, JOINED_NOT_ACTIVE, CLAIM, COUPON_EXPIRED, CLAIMED, CLAIMED_PENDING
    }

    /*
        status definition from WTS:
        1 - active
        2 - pending transfer to money
        3 - deactive/ or expired
     */
    public enum CouponCoreStatusWTSResponse {
        UNKNOWN("0", "UNKNOWN"),
        ACTIVE("1", "ACTIVE"),
        PENDING_TRANSFER_TO_MONEY("2", "PENDING_TRANSFER_TO_MONEY"),
        DEACTIVATE_OR_EXPIRED("3", "DEACTIVATE_OR_EXPIRED");

        public final String code;
        public final String value;

        CouponCoreStatusWTSResponse(String code, String value) {
            this.code = code;
            this.value = value;
        }

        public static CouponCoreStatusWTSResponse fromCode(String code) {
            for (CouponCoreStatusWTSResponse is : CouponCoreStatusWTSResponse.values()) {
                if (is.code.equalsIgnoreCase(code)) {
                    return is;
                }
            }
            return CouponCoreStatusWTSResponse.UNKNOWN;
        }
    }

    public enum AccountTypeFromLoginSSO {
        NORMAL("N"), MARGIN("M");
        private String type;

        AccountTypeFromLoginSSO(String type) {
            this.type = type;
        }

        public String getType() {
            return type;
        }
    }

    public enum OpenAccountChannel {
        PINEX, ALPHA, OTHER;
    }

    public enum OrderType {
        LO("01"),
        ATO("02"),
        ATC("03"),
        MP("04"),
        MTL("05"),
        MOK("06"),
        MAK("07"),
        PLO("08");

        private String code;

        OrderType(String code) {
            this.code = code;
        }

        public String getCode() {
            return code;
        }

        public static OrderType fromCode(String code) {
            for (OrderType orderType : OrderType.values()) {
                if (orderType.code.equalsIgnoreCase(code)) {
                    return orderType;
                }
            }
            return null;
        }
    }

    public static String replaceNewCouponAmountValue(String content) {
        content = content.replaceAll("200000", "100000").replaceAll("200", "100").replaceAll("200.000", "100.000");
        return content;
    }

    public static void main(String[] args) {
        String code = "04";
        System.out.println(OrderType.fromCode(code).name());
    }
}
