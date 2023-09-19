package com.session.constant;

import org.springframework.stereotype.Component;

@Component
public class ConstantUsage {

	private ConstantUsage() {
	}

	public static final long MAX_DAYS = 10;

	public static final String CUSTOMER_NOT_FOUND = "CUSTOMER WITH ID NOT FOUND";

	public static final String SESSION_CANT_BE_ARCHIVED = "SESSION CAN NOT BE ARCHIVED ";

	public static final String SESSION_NOT_FOUND = "SESSION WITH ID NOT FOUND";

	public static final String SESSION_NOT_ACTIVE = "SESSION NOT ACTIVE";

	public static final String SESSION_CANT_BE_DELETED = "SESSION CAN NOT BE DELETED";

	public static final String CUSTOMER_NAME_INVALID_EXCEPTION = "CUSTOMER NAME INVALID";

	public static final String ARCHIVE_FLAG_NO = "N";

	public static final String ARCHIVE_FLAG_YES = "Y";

	public static final char STATUS_ACTIVE = 'A';

	public static final char STATUS_DELETE = 'D';

	public static final char STATUS_ARCHIVE = 'X';

	public static final String PAGE_NUMBER = "1";

	public static final String PAGE_SIZE = "5";

	public static final String SORT_BY = "createdOn";

	public static final String SORT_DIR = "desc";

	public static final String SESSION_MAPPING = "/sessions";

	public static final String ARCHIVE_MAPPING = "/archive/{sessionId}";

	public static final String STATUS_MAPPING = "/{status}";

	public static final String CUSTOMER_MAPPING = "/customer";

	public static final String GET_BY_CUSTOMERID = "/getCustomerById/{customerId}";

	public static final String SESSION_NAME_ERR = "Session name should not be empty";

	public static final String SESSION_REMARK_ERR = "Remark should not be empty";

	public static final String SESSION_UPDATEDBY_ERR = "UpdatedBy should not be empty";

	public static final String SESSION_CUSTOMER_ERR = " CustomerBy should not be empty";

	public static final String CUSTOMER_ID_ERR = " Customer Id should not be empty";

	public static final String CUSTOMER_NAME_ERR = " Customer Name should not be empty";

	public static final String SESSION_NAME_INVALID = " Session name is invalid";

	public static final String UUID = "uuid";

	public static final String STRATEGY = "org.hibernate.id.UUIDGenerator";

	public static final String COLUMN_SESSIONID = "Session_Id";

	public static final String COLUMN_SESSIONNAME = "Session_Name";
	public static final String SESSION_ID = "{sessionId}";

	public static final String STATUS_REQ = "{status}";

	public static final String COLUMN_REMARK = "Remark";

	public static final String COLUMN_CUSTOMER_SESSION = "Customer_Session";

	public static final String COLUMN_CREATEDBY = "Created_By";

	public static final String COLUMN_CREATEDON = "Created_On";

	public static final String COLUMN_UPDATEDON = "Updated_On";

	public static final String COLUMN_STATUS = "Status";

	public static final String COLUMN_CUSTOMERID = "Customer_Id";

	public static final String COLUMN_CUSTOMERNAME = "Customer_Name";

	public static final String COLUMN_CUSTOMERSESSIONS = "Total_Sessions";

	public static final String CUSTOMER_LTRL = "customer";

	public static final String ASPECT_ARGS = "args=";

	public static final String ASPECT_SIGNATURE = "signature=";

	public static final String ASPECT_RESULT = "result";

	public static final String ASPECT_EXECUTION_PATH = "execution(* com.session.service.SessionServiceImpl.*(..))";

	public static final String ERR_MSG = "ErrMsg";

	public static final String SESSION_NOT_FOUND_BY_STATUS = "Session not found by status";

	public static final String QUERY_SESSION = "SELECT MAX(CAST(SUBSTRING(session_id, 8) AS UNSIGNED)) FROM session";

	public static final String PREFIX_CUSTOMER = "CB";

	public static final String PREFIX_SESSION = "Session";

	public static final String QUERY_CUSTOMER = "SELECT MAX(CAST(SUBSTRING(customer_id, 3) AS UNSIGNED)) FROM customer";

	public static final String PREFIX_FORMAT = "001";

	public static final String FORMAT = "%03d";

	public static final String SESSION_STRATEGY = "com.session.generator.SessionIdGenerator";
	public static final String CUSTOMER_STRATEGY = "com.session.generator.CustomerIdGenerator";

	public static final String CUSTOMER_ID = "customer_id";

}
