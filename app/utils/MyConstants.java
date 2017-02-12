package utils;

import java.util.Arrays;
import java.util.List;

public class MyConstants {

	public class AppConstants {
		public static final int UNKNOWN_DEVICE_ID = 4;
		public static final String UNKNOWN = "UNKNOWN";
	}

	public class APIResponseKeys {
		public static final String MESSAGE = "message";
		public static final String ERROR = "error";
		public static final String COUNT = "count";
		public static final String RESULT_KEY = "result";
		public static final String SUCCESS_MESSAGE = "success";
		public static final String FAILURE_MESSAGE = "error";
		public static final String MESSAGE_CODE = "messageCode";
		public static final String SYSTEM_MESSAGE = "systemMessage";
		public static final String DATA = "data";
		public static final String SESSION_TOKEN = "sessionToken";
		public static final String USER_ID = "userId";
		public static final String TOKEN = "token";
		public static final String NAME = "name";
		public static final String EMAIL = "email";
		public static final String PHONE_NO = "phoneNo";
		public static final String CITY = "city";
		public static final String EXTRA_FEE = "extraFee";
		public static final String REFERRAL_CODE = "referralCode";
		public static final String WALLET_AMOUNT = "walletAmount";

		public static final String IS_NEW_USER = "isNewUser";
		public static final String CITY_ID = "cityId";
		public static final String ID = "id";
		public static final String TYPE = "type";

		public static final String VENDOR_ID = "vendorId";
		public static final String VENDOR_NAME = "vendorName";
		public static final String VENDOR_ADDRESS = "vendorAddress";
		public static final String DESCRIPTION = "description";

		public static final String PRODUCT_ID = "productId";
		public static final String PRODUCT_NAME = "productName";

		public static final String CART_PRODUCT_ID = "cartProductId";
		public static final String SHIPPING_DISTANCE = "shippingDistance";
		public static final String PRICE = "price";
		public static final String CATEGORY_ID = "categoryId";
		public static final String STATUS = "status";
		public static final String QUANTITY = "quantity";
		public static final String PRODUCT_IMAGE_URL = "productImageUrl";
		public static final String PRODUCT_THUMBNAIL_IMAGE_URL = "productThumbnailImageUrl";
		public static final String IMAGE_URL = "imageUrl";
		public static final String VENDOR_IMAGE_URL = "vendorImageUrl";
		public static final String VENDOR_THUMBNAIL_IMAGE_URL = "vendorThumbnailImageUrl";

		public static final String PRODUCTS = "products";
		public static final String PRODUCT_TYPE = "productType";

		public static final String CATEGORY_LIST = "categoryList";
		public static final String CITY_LIST = "cityList";
		public static final String PRODUCT_TYPES = "productTypes";
		public static final String ORDER_STATUS = "orderStatus";
		public static final String PAYMENT_TYPE = "paymentType";
		public static final String PAYMENT_STATUS = "paymentStatus";
		public static final String ORDER_TYPES = "orderTypes";
		public static final String UNIT_TYPES = "unitTypes";
		public static final String TOTAL_PRICE = "totalPrice";

		public static final String TOTAL_COUNT = "totalCount";
		public static final String ORDERS = "orders";
		public static final String UNITS = "units";
		public static final String UNIT_TYPE = "unitType";

		public static final String ORDER_ID = "orderId";
		public static final String ADDRESS = "address";
		public static final String PINCODE = "pincode";
		public static final String IS_AVAILABLE = "isAvailable";
		public static final String IS_VERIFIED = "isVerified";

		public static final String LATITUDE = "latitude";
		public static final String LONGITUDE = "longitude";
		public static final String CUSTOMER_NAME = "customerName";
		public static final String ORDER_TYPE = "orderType";
		public static final String DELIVERY_TIME = "deliveryTime";
		public static final String ORDERED_TIME = "orderedTime";
		public static final String IS_UPDATED = "isUpdated";

		public static final String VENDOR_DETAILS = "vendorDetails";
		public static final String VENDOR_LIST = "vendorList";
		public static final String ORDER = "order";

		public static final String STANDARD_IMAGE_URL = "standardImageUrl";
		public static final String THUMBNAIL_IMAGE_URL = "thumbnailImageUrl";
		public static final String ANDROID_IMAGE_URL = "androidImageUrl";
		public static final String WEB_IMAGE_URL = "webImageUrl";

		public static final String ADDRESS_LIST = "addressList";

		public static final String NOTIFICATION_TYPE = "notificationType";
		public static final String UNREAD_MESSAGE_COUNT = "unreadMessageCount";

		public static final String DELIVERY_BOY_ID = "deliveryBoyId";

		public static final String TRANSACTION_ID = "transactionId";
		public static final String TRANSACTION_STATUS = "transactionStatus";
		public static final String CURRENCY = "currency";
		public static final String AMOUNT = "amount";
		public static final String CREATED_TIME = "createdTime";
		public static final String IS_FEATURED = "isFeatured";
		public static final String SHIPPING_FEE_DISTANCE_LIMIT = "shippingFeeDistanceLimit";


	}

	public class APIRequestKeys {
		public static final String CONTENT_TYPE = "Content-Type";

		public static final String USER_TOKEN_HEADER = "token";
		public static final String ENCRYPTED_USER_ID = "encryptedUserId";
		public static final String TOKEN = "token";
		public static final String EMAIL = "email";
		public static final String PHONE_NO = "phoneNo";
		public static final String DEVICE_ID = "deviceId";
		public static final String DEVICE_TYPE = "deviceType";
		public static final String DEVICE_TOKEN = "deviceToken";
		public static final String NAME = "name";
		public static final String PASSWORD = "password";
		public static final String ADDRESS = "address";
		public static final String ADDRESS_TYPE = "addressType";
		public static final String IS_AVAILABLE = "isAvailable";

		public static final String CITY_ID = "cityId";
		public static final String PINCODE = "pincode";
		public static final String VENDOR_TYPE = "vendorType";
		public static final String IS_VERIFIED = "isVerified";

		public static final String VENDOR_TOKEN = "token";
		public static final String ENCRYPTED_VENDOR_ID = "encryptedVendorId";

		public static final String DELIVERY_BOY_TOKEN = "token";
		public static final String ENCRYPTED_DELIVERY_BOY_ID = "encryptedId";

		public static final String VENDOR_NAME = "vendorName";
		public static final String VENDOR_ADDRESS = "vendorAddress";
		public static final String DESCRIPTION = "description";
		public static final String LATITUDE = "latitude";
		public static final String LONGITUDE = "longitude";

		public static final String PRODUCT_ID = "productId";
		public static final String PRODUCT_ID_LIST = "productIdList";
		public static final String PRODUCT_NAME = "productName";
		public static final String PRICE = "price";
		public static final String QUANTITY = "quantity";
		public static final String CATEGORY_ID = "categoryId";
		public static final String STATUS = "status";
		public static final String IS_DELETED = "isDeleted";
		public static final String UNIT_TYPE = "unitType";
		public static final String UNITS = "unit";
		public static final String PRODUCT_TYPE = "productType";

		public static final String AVAILABLE_PRODUCT_IDS = "availableProductIds";
		public static final String UNAVAILABLE_PRODUCT_IDS = "unavailableProductIds";

		public static final String PRODUCTS = "products";

		public static final String ORDER_ID = "orderId";
		public static final String ORDER_STATUS = "orderStatus";
		public static final String ORDER_TYPE = "orderType";
		public static final String DELIVERY_BOY_ID = "deliveryBoyId";

		public static final String FILTERS = "filters";
		public static final String IS_VENDOR_LIST = "isVendorList";
		public static final String IS_PRODUCT_LIST = "isProductList";

		public static final String PAGE = "page";
		public static final String LIMIT = "limit";
		public static final String VENDOR_ID = "vendorId";

		public static final String SEARCH_TEXT = "searchText";
		public static final String MIN_PRICE = "minPrice";
		public static final String MAX_PRICE = "maxPrice";

		public static final String CART_PRODUCT_ID = "cartProductId";
		public static final String IS_FEATURED = "isFeatured";

		public static final String FACEBOOK_ID = "facebookId";
		public static final String FACEBOOK_TOKEN = "fbToken";

		public static final String PROMOTIONS = "promotions";
		public static final String PROMOTION_ID = "promotionId";
		public static final String IMAGE_URL = "imageUrl";
		public static final String IS_CLICK_ENABLED = "isClickEnabled";

		public static final String LOCALITY_ID = "localityId";
		public static final String SHIPPING_DISTANCE = "shippingDistance";
		public static final String IS_EMPTY_CART = "isEmptyCart";
		public static final String SHIPPING_FEE = "shippingFee";

		public static final String SHIPPING_FEE_DISTANCE_LIMIT = "shippingFeeDistanceLimit";

	}

	public class JsonSchemaKeys {
		public static final String JSONSCHEMA_LEVEL_KEY = "level";
		public static final String JSONSCHEMA_MISSING_KEY = "missing";
		public static final String JSONSCHEMA_UNWANTED_KEY = "unwanted";
		public static final String JSONSCHEMA_INSTANCE_KEY = "instance";
		public static final String JSONSCHEMA_POINTER_KEY = "pointer";
		public static final String JSON_PARSE_ERROR = "json.parse.error";
		public static final String JSON_MAPPING_ERROR = "json.mapping.error";
		public static final String JSON_PROCESSING_ERROR = "json.processing.error";
		public static final String IO_ERROR = "io.error";
		public static final String TECHNICAL_ERROR = "technical.error";
		public static final String INVALID_INPUT = "invalid.input";
		public static final String MIN_LENGTH_KEY = "minLength";
		public static final String MAX_LENGTH_KEY = "maxLength";
		public static final String INSTANCE_KEY = "instance";
		public static final String KEYWORD_KEY = "keyword";
		public static final String POINTER_KEY = "pointer";
		public static final String EXPECTED_KEY = "expected";
	}

	public class SuccessMessages {
		public static final String USER_UPDATE_SUCCESS = "user.update.success";
		public static final String VENDOR_UPDATE_SUCCESS = "vendor.update.success";
		public static final String PRODUCT_UPDATE_SUCCESS = "product.update.success";
		public static final String PRODUCT_DELETE_SUCCESS = "product.delete.success";
		public static final String ORDER_STATUS_UPDATE_SUCCESS = "order.status.update.success";
		public static final String ORDER_ASSIGN_SUCCESS = "order.assign.success";
		public static final String PRODUCT_ADDED_TO_CART_SUCCESS = "product.added.to.cart.success";
		public static final String PRODUCT_DELETED_FROM_CART_SUCCESS = "product.deleted.from.cart.success";
		public static final String CART_PRODUCT_UPDATE_SUCCESS = "cart.product.update.success";
		public static final String ADDRESS_ADD_SUCCESS = "address.added.success";
		public static final String ADDRESS_DELETE_SUCCESS = "address.delete.success";
		public static final String LOGOUT_SUCCESS = "logout.success";
		public static final String PRODUCT_UPDATE_AVAILABILITY_SUCCESS = "product.update.availability.success";
		public static final String FCM_REGISTER_SUCCESS = "fcm.register.success";

	}

	public class FailureMessages {
		public static final String FILE_CONTENT_TYPE_NOT_SUPPORTED = "file.content.type.not.supported";
		public static final String UNSUPPORTED_FILE_TYPE = "unsupported.file.type";

		public static final String USER_NOT_EXISTS = "user.not.exists";
		public static final String USER_ALREADY_EXISTS = "user.already.exists";
		public static final String INVALID_JSON = "invalid.json";
		public static final String BAD_REQUEST = "bad.request";
		public static final String INVALID_API_CALL = "invalid.api.call";
		public static final String INVALID_INPUT = "invalid.input";
		public static final String INVALID_EMAIL = "invalid.email";
		public static final String JSON_PARSE_ERROR = "json.parse.error";
		public static final String JSON_MAPPER_ERROR = "json.mapper.error";
		public static final String IO_ERROR = "io.error";
		public static final String TECHNICAL_ERROR = "technical.error";
		public static final String SESSION_CREATION_FAILED = "session.creation.failed";
		public static final String SESSION_INVALID = "session.invalid";
		public static final String DATABASE_ERROR = "db.error";
		public static final String SIGNOUT_CONFLICT = "signout.conflict";
		public static final String EMAIL_ALREADY_EXISTS = "email.already.exists";
		public static final String PHONE_NO_ALREADY_EXISTS = "phone.no.already.exists";
		public static final String EMAIL_PHONE_NOT_FOUND = "email.phone.not.found";
		public static final String PASSWORD_NOT_FOUND = "password.not.found";
		public static final String INCORRECT_PASSWORD = "incorrect.password";
		public static final String PHONE_ALREADY_REGISTERED = "phone.already.registered";
		public static final String EMAIL_ALREADY_REGISTERED = "email.already.registered";
		public static final String CITY_ID_DOESNT_EXIST = "city.id.doesnt.exist";
		public static final String INCORRECT_EMAIL_PHONE = "incorrect.email.phone";
		public static final String USER_DOESNT_EXIST = "user.doesnt.exist";
		public static final String CITY_DOESNT_EXIST = "city.doesnt.exist";
		public static final String UNIQUE_DEVICE_ID_TOKEN = "unique.device.id.token";
		public static final String VENDOR_NOT_VERIFIED = "vendor.not.verified";
		public static final String PRODUCT_DOESNT_EXIST = "product.doesnt.exist";
		public static final String CATEGORY_DOESNT_EXIST = "category.doesnt.exist";
		public static final String VENDOR_DOESNT_EXIST = "vendor.doesnt.exist";
		public static final String TAG_DOESNT_EXIST = "tag.doesnt.exist";
		public static final String LOCALITY_DOESNT_EXIST = "locality.doesnt.exist";
		public static final String VENDOR_NOT_OWNER_OF_PRODUCT = "vendor.not.owner.of.product";
		public static final String INVALID_PRODUCT_STATUS = "invalid.product.status";
		public static final String INVALID_PRODUCT_TYPE = "invalid.product.type";
		public static final String INVALID_UNIT_TYPE = "invalid.unit.type";
		public static final String INVALID_SEARCH_OPERATION = "invalid.search.operation";
		public static final String SEARCH_TEXT_MIN_LENGTH_TWO = "search.text.min.length.two";
		public static final String SEND_VALID_ORDER_STATUS = "send.valid.order.status";
		public static final String INVALID_ACCESS = "invalid.access";
		public static final String SIGN_UP_FAILED = "sign.up.failed";
		public static final String YET_TO_IMPLEMENT = "yet.to.implement";
		public static final String INVALID_PAYMENT_TYPE = "invalid.payment.type";
		public static final String CANNOT_CANCEL_CONFIRMED_ORDER = "cannot.cancel.confirmed.order";

		public static final String FACEBOOK_LINK_ABSENT = "facebook.account.absent";
		public static final String INVALID_FACEBOOK_CREDENTIALS = "invalid.facebook.credentials";
		public static final String ORDER_DOESNT_EXIST = "order.doesnt.exist";
		public static final String ORDER_DOESNT_BELONG_TO_VENDOR = "order.doesnt.belong.to.vendor";
		public static final String ORDER_DOESNT_BELONG_TO_DELIVERY_BOY = "order.doesnt.belong.to.delivery.boy";
		public static final String ORDER_ID_NOT_FOUND = "order.id.not.found";
		public static final String DELIVERY_BOY_ID_NOT_FOUND = "delivery.boy.id.not.found";

		public static final String ORDER_STATUS_NOT_FOUND = "order.status.not.found";
		public static final String ORDER_DOESNT_BELONG_TO_USER = "order.doesnt.belong.to.user";

		public static final String ALREADY_CONFIRMED = "already.confirmed";
		public static final String ALREADY_DECLINED = "already.declined";
		public static final String ALREADY_CANCELLED = "already.cancelled";
		public static final String ALREADY_OUT_FOR_DELIVERY = "already.out.for.delivery";
		public static final String ALREADY_UNDER_PROGRESS = "already.under.progress";
		public static final String ALREADY_DELIVERED = "already.delivered";

		public static final String PRODUCT_NOT_ADDED_TO_CART = "product.not.added.to.cart";
		public static final String CART_EMPTY = "cart.empty";
		public static final String INVALID_ORDER_TYPE = "invalid.order.type";
		public static final String INVALID_ORDER_STATUS = "invalid.order.status";

		public static final String PRODUCTS_BELONGS_TO_MULTIPLE_VENDORS = "products.belongs.to.multiple.vendors";
		public static final String ERROR_WHILE_UPLOADING_IMAGE = "error.while.uploading.image";

		public static final String PRODUCT_VENDOR_ID_NOT_FOUND = "product.or.vendor.id.not.found";
		public static final String FACEBOOK_LINK_CONFLICT = "facebook.conflict";

		public static final String INVALID_PROMOTION_ID = "invalid.promotion.id";
		public static final String INVALID_ADDRESS_ID = "invalid.address.id";
		public static final String ADDRESS_NOT_FOUND = "address.not.found";
		public static final String CANNOT_DELETE_DEFAULT_ADDRESS = "cannot.delete.default.address";

		public static final String INVALID_ADDRESS_TYPE = "invalid.address.type";
		public static final String ADDRESS_TYPE_NOT_FOUND = "address.type.not.found";
		public static final String UNIT_TYPE_DOESNT_EXIST = "unit.type.doesnt.exist";

		public static final String VENDOR_LOCATION_DOESNT_EXIST = "vendor.location.doesnt.exist";
		public static final String DELIVERY_BOY_DOESNT_EXIST = "delivery.boy.doesnt.exist";

		public static final String TRANSACTION_DOESNT_EXIST = "transaction.doesnt.exist";
		public static final String TRANSACTION_ALREADY_DONE = "transaction.already.done";
		public static final String INVALID_TRANSACTION = "invalid.transaction";
		public static final String TRANSACTION_FAILED = "transaction.failed";
		public static final String VENDOR_NOT_AVAILABLE = "vendor.not.available";
		public static final String PINCODE_NOT_FOUND = "pincode.not.found";
		public static final String UPDATE_AVAILABILITY_FAILED = "update.availability.failed";
		public static final String DEVICE_TOKEN_NOT_FOUND = "device.token.not.found";
		public static final String DEVICE_TYPE_NOT_FOUND = "device.type.not.found";
		public static final String IMAGE_NOT_FOUND = "image.not.found";

		public static final String PRODUCT_ALREADY_DELETED = "product.already.deleted";
		public static final String INVALID_ADDRESS_INFO = "invalid.address.info";
		public static final String CONTACT_FOR_VENDOR_VERIFICATION = "contact.for.vendor.verification";
		public static final String SIGN_IN_CONTACT_FOR_VENDOR_VERIFICATION = "sign.in.contact.for.vendor.verification";

		public static final String VENDOR_ID_NOT_FOUND = "vendor.id.not.found";

		public static final String PRODUCT_DOESNT_EXIST_IN_CART = "product.doesnt.exist.in.cart";
		public static final String DIFFERENT_VENDOR = "different.vendor";

	}

	public class OsType {
		public static final int ANDROID = 1;
		public static final int BROWSER = 2;
		public static final int IOS = 3;
		public static final int UNKNOWN = 4;
	}

	public class JsonSchemaFilePath {
		/* ########################### CUSTOMER ############################# */
		public static final String USER_SIGN_IN = "/JsonSchemas/userSignIn.json";
		public static final String USER_SIGN_UP = "/JsonSchemas/userSignUp.json";
		public static final String USER_FB_SIGN_IN = "/JsonSchemas/signInWithFacebook.json";
		public static final String USER_FB_SIGN_UP = "/JsonSchemas/signUpWithFacebook.json";

		public static final String UPDATE_USER = "/JsonSchemas/updateUser.json";

		public static final String VENDOR_SIGN_IN = "/JsonSchemas/vendorSignIn.json";
		public static final String VENDOR_SIGN_UP = "/JsonSchemas/vendorSignUp.json";
		public static final String UPDATE_VENDOR = "/JsonSchemas/updateVendor.json";

		public static final String ADD_PRODUCT = "/JsonSchemas/addProduct.json";
		public static final String UPDATE_PRODUCT = "/JsonSchemas/updateProduct.json";
		public static final String PRODUCT_FEEDS = "/JsonSchemas/productFeeds.json";
		public static final String VENDOR_FEEDS = "/JsonSchemas/vendorFeeds.json";

	}

	public class ProductStatus {
		public static final int AVAILABLE = 1;
		public static final int UNAVAILABLE = 2;
		public static final int INCOMPLETE = 3;
	}

	public class NetworkConstants {
		public static final String HOST_URL_HTTP = "http://";
		public static final String HOST_URL_HTTPS = "https://";
	}

	public class ProductType {
		public static final int NONE = 0;
		public static final int VEG = 1;
		public static final int NON_VEG = 2;

		public static final String NONE_STR = "None";
		public static final String VEG_STR = "Veg";
		public static final String NON_VEG_STR = "Non-Veg";
	}

	public static List<Integer> productStatusList = Arrays.asList(ProductStatus.AVAILABLE, ProductStatus.UNAVAILABLE,
			ProductStatus.INCOMPLETE);
	public static List<Integer> productTypeList = Arrays.asList(ProductType.NONE, ProductType.VEG, ProductType.NON_VEG);

	public static List<Integer> orderTypeList = Arrays.asList(OrderType.PICK_UP, OrderType.DELIVER);
	public static List<Integer> orderStatusList = Arrays.asList(OrderStatus.CONFIRMED, OrderStatus.UNDER_PROGRESS,
			OrderStatus.OUT_FOR_DELIVERY, OrderStatus.DECLINED, OrderStatus.CANCELLED, OrderStatus.DELIVERED);

	public static List<Integer> addressTypeList = Arrays.asList(AddressType.HOME, AddressType.OTHERS, AddressType.WORK);

	public class OrderStatus {
		public static final int PENDING = 1;
		public static final int CONFIRMED = 2;
		public static final int UNDER_PROGRESS = 3;
		public static final int OUT_FOR_DELIVERY = 4;
		public static final int DECLINED = 5;
		public static final int CANCELLED = 6;
		public static final int DELIVERED = 7;
	}

	public class PaymentStatus {
		public static final int PENDING = 1;
		public static final int SUCCESS = 2;
		public static final int FAILURE = 3;
		public static final int REFUNDED = 4;
		public static final int REFUND_FAILED = 5;
		public static final int COMPLETED = 6;
	}

	public class TransactionStatus {
		public static final int SUCCESS = 1;
		public static final int FAILURE = 2;
	}

	public class OrderType {
		public static final int DELIVER = 1;
		public static final int PICK_UP = 2;

		public static final String DELIVER_STR = "Deliver";
		public static final String PICK_UP_STR = "Pick-up";
	}

	public class PaymentType {
		public static final int COD = 1;
		public static final int CARD = 2;
		public static final int INTERNET_BANKING = 3;
		public static final int REFUND = 4;
	}

	public class CurrencyType {
		public static final int INR = 1;
	}

	public class AddressType {
		public static final int HOME = 1;
		public static final int WORK = 2;
		public static final int OTHERS = 3;
	}

	public class AccountType {
		public static final int USER = 1;
		public static final int VENDOR = 2;
		public static final int DELIVERY_BOY = 3;
	}

	public class VendorType {
		public static final int HOTEL = 1;
		public static final int BAKERY = 2;
	}

	public class ImageResizeType {
		public static final int STANDARD = 0;
		public static final int THUMBNAIL_SIZE = 1;
		public static final int ANDROID_SIZE = 2;
		public static final int WEB_SIZE = 3;
	}

	public class ReferralStatus {
		public static final int NOT_CREDITED = 1;
		public static final int CREDITED = 2;
		public static final int WALLET_OVERFLOW = 3;
	}

	public static final int DEFAULT_PAGINATION_LIMIT = 10;

	public static final int SHIPPING_DISTANCE = 20000; // In meters

	// In milliseconds (24*60*60*1000)
	public static final int EXPIRY_TIME = 86400000;
	public static final int CONFLICT_HTTP_CODE = 409;

	public static final int MIN_SEARCH_LENGTH = 2;
	public static final String PREREQUISITE_TOKEN = "token";
	public static final String PREREQUISITE_KEY = "sa2131sadsfsdsad4534534dsfsd";
	public static final String CMS_KEY = "ash2e3we32e3asadsade3e32e3enkitarn3243242332wdsbaeakk1211938951238";

	public static final float COMPANY_FEE_PERCENTAGE = 5.0f;
	public static final float DELIVERY_FEE = 20;

	public static final String IMAGE_UPLOAD_KEY = "image";

}
