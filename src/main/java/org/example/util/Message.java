package org.example.util;

import static org.common.utils.Message.CommonError;
import static org.common.utils.Message.CommonInfo;

public class  Message {
    public static class Error extends CommonError {
        public static final String WATCHLIST_NOTFOUND = "error.watchlist.notfound";
        public static final String WATCHLIST_EXISTED = "error.watchlist.existed";
        public static final String WATCHLIST_NAME_REQUIRED = "error.watchlist.name.required";
        public static final String LOGIN_USER_NAME_CONTAIN_SPECIAL_CHARACTER = "error.login.username.contain.special.character";
        public static final String LOGIN_FIELD_REQUIRED = "error.login.field.required";
        public static final String LOGIN_CREDENTIALS_INCORRECT = "error.login.credentials.incorrect";

        // Follow
        public static final String ERROR_FOLLOW_ITSELF = "error.customer.follow.himself";
        public static final String ERROR_FOLLOW_OTHER = "error.customer.follow.other";
        public static final String ERROR_UNFOLLOW_OTHER = "error.customer.unfollow.other";

        //Address
        public static final String ERROR_ADDRESS_MAX = "error.customer.address.max";
        public static final String ERROR_ADDRESS_MIN = "error.customer.address.min";

        //Avatar
        public static final String ERROR_AVATAR_MAX = "error.customer.avatar.max";

        // Register new account
        public static final String ERROR_FORMAT_PHONE_INVALID = "error.format.phone.invalid";
        public static final String ERROR_EMAIL_IS_BLANK = "error.email.is.blank";
        public static final String ERROR_FORMAT_EMAIL_INVALID = "error.format.email.invalid";
        public static final String ERROR_VERIFY_OTP = "error.verify.otp";
        public static final String ERROR_FULL_NAME_IS_BLANK = "error.full.name.is.blank";
        public static final String ERROR_USER_NAME_IS_BLANK = "error.user.name.is.blank";
        public static final String ERROR_DISPLAY_NAME_IS_BLANK = "error.display.name.is.blank";
        public static final String INVALID_USER_NAME = "invalid.user.name";
        public static final String EXIST_USER_NAME = "exist.user.name";
        public static final String AVAILABLE_USER_NAME = "available.user.name";

        //badword
        public static final String ERROR_VIOLATE_NAME = "error.violate.name";

        // Sharing watchlist
        public static final String WATCHLIST_SHARE_REQUIRED = "error.watchlist.share.required";

        // Trading
        public static final String ERROR_PLACE_COUPON_ORDER_WRONG_TYPE = "error.place.coupon.order.wrong.type";
        public static final String ERROR_PLACE_COUPON_ORDER_WRONG_TYPE_HOSE = "error.place.coupon.order.wrong.type.hose";

        // Coupon
        public static final String ERROR_CUSTOMER_COUPON_EXISTED = "error.customer.coupon.existed";
        public static final String ERROR_COUPON_EXPIRED = "error.coupon.expired";
        public static final String ERROR_COUPON_CLAIMED = "error.coupon.claimed";
        public static final String ERROR_REFERRAL_CODE_CREATED = "error.referral.code.create";

        // Share portfolio
        public static final String ERROR_SHARE_PORTFOLIO_NOT_PRO_ACCOUNT = "error.share.portfolio.not.pro.account";
        public static final String ERROR_UNSHARE_PORTFOLIO_NOT_ALLOW = "error.unshare.portfolio.not.allow";

        // theme
        public static final String THEME_NOT_FOUND = "error.theme.notfound";
    }

    public static class Info extends CommonInfo {

    }

    private Message() {
    }

    public class CustomerSearchLog {
        private CustomerSearchLog() {
        }

        public static final String STOCK_CODE_IS_NOT_EMPTY = "stock.code.search.is.not.empty";
    }
}
