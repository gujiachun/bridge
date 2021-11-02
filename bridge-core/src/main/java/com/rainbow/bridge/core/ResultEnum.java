package com.rainbow.bridge.core;

/**
 * 通用异常枚举，你可以实现 {@link ResultCode} 来自定义自己的异常码
 *
 * @author fonlin
 * @date 2020/11/24
 */
public enum ResultEnum implements ResultCode {


    //400～600 http状态码对应
    TOKEN_INVALID_ERROR(401, "登录认证无效！"),
    UNAUTHORIZED_ERROR(403, "您没有权限！"),
    NOT_FOUND_ERROR(404, "没有找到资源"),
    SERVER_ERROR(500, "系统开小差了，请联系系统管理员"),

    //600~700业务常规操作异常
    SYSTEM_OP_ERROR(601, "操作失败"),
    SYSTEM_OP_DELETE_ERROR(602, "删除失败"),
    SYSTEM_OP_EXPORT_ERROR(603, "导出失败"),
    SYSTEM_OP_IMPORT_ERROR(604, "导入失败！请联系系统管理员！"),
    SYSTEM_OP_IMPORT_EXT_ERROR(605, "导入文件格式不正确！请导入Excel文件！"),
    SYSTEM_OP_IMPORT_SIZE_ERROR(606, "导入文件超过限制大小"),
    SYSTEM_OP_UPLOAD_SIZE_ERROR(607, "上传文件超过限制大小"),
    SYSTEM_OP_UPLOAD_EXT_ERROR(608, "上传文件格式不正确！请上传【图片、PDF等】！"),
    SYSTEM_OP_UPLOAD_ERROR(609, "上传失败！请联系系统管理员！"),
    SYSTEM_OP_DOWNLOAD_ERROR(610, "下载失败！"),
    SYSTEM_OP_RETRY(611, "请稍后重试！"),

    //701~800链接问题
    NETWORK_TIMEOUT_ERROR(701, "网络链接异常！请检查网络连接！"),
    NETWORK_ERROR(702, "网络异常！请检查网络连接！"),

    DATASOURCE_TIMEOUT_ERROR(703, "数据库链接异常！请检查网络连接！"),

    //900～999 限流降级错误
    API_BLOCK_FLOW(900, "接口被限流了"),
    API_BLOCK_FOR_DEGRADE(901, "接口被降级了"),
    API_BLOCK_FOR_HOT_PARAM(902, "接口被热点参数限流了"),
    API_BLOCK_AUTHORITY(903, "接口被授权规则限制访问了"),
    API_BLOCK_SYSTEM(904, "接口被系统规则限制了"),
    REMOTES_INVOKED_FAIL(905, "远程调用失败"),
    SYSTEM_INNER_ERROR(906, "系统错误"),
    NO_AVAILABLE_INSTANCE(907, "没有服务实例"),

    //1000～1999 区间表示参数错误
    PARAM_IS_INVALID(1001, "参数无效"),
    PARAM_IS_BLANK(1002, "参数不能为空"),
    PARAM_TYPE_ERROR(1003, "参数类型错误"),
    PARAM_ERROR(1004, "参数错误,请检查！"),

    //2000～2999 区间表示用户错误
    USER_ERROR_PASSWORD(2000, "用户名或密码错误！"),
    USER_NOT_LOGIN(2001, "用户未登录"),
    USER_NOT_EXIST(2002, "用户不存在"),
    USER_IS_EXIST(2003, "用户已存在"),
    USER_IS_DISABLED(2004, "用户被禁用"),
    USER_IS_FORBIDDEN(2005, "用户访问禁止"),
    JOB_NUMBER_IS_NULL(2006, "工号为空"),
    EMAIL_IS_NULL(2007, "邮箱为空"),
    USER_NOT_AUTHENTICATED(2008, "用户未认证"),

    //3000～3999 区间表示服务内部异常
    NULL_POINT(3002, "对象为空，请检查数据是否正确"),
    ARRAY_INDEX_OUT_OF_BOUND(3003, "系统开小差了，请联系系统管理员"),
    NUMBER_FORMAT(3004, "系统开小差了，请联系系统管理员"),
    PARSE_ERROR(3005, "系统开小差了，请联系系统管理员"),
    CLASS_CAST(3006, "系统开小差了，请联系系统管理员"),
    ARITH_ERROR(3007, "系统开小差了，请联系系统管理员"),
    ILLEGAL_ARGUMENT(3008, "系统开小差了，请联系系统管理员"),
    BUFFER_OVERFLOW(3009, "系统开小差了，请联系系统管理员"),
    ILLEGAL_ACCESS(3010, "系统开小差了，请联系系统管理员"),
    INTERRUPTED_ERROR(3011, "系统开小差了，请联系系统管理员"),
    STRING_INDEX_OUT_OF_BOUND(3012, "系统开小差了，请联系系统管理员"),
    NO_SUCH_FIELD(3013, "系统开小差了，请联系系统管理员"),
    IO_ERROR(3014, "系统开小差了，请联系系统管理员"),
    SQL_ERROR(3015, "系统开小差了，请联系系统管理员"),
    INTERNAL_SERVER_ERROR(3016, "系统开小差了，请联系系统管理员"),
    NO_SUCH_METHOD(3017, "系统开小差了，请联系系统管理员"),
    HTTP_CLIENT_ERROR(3018, "系统开小差了，请联系系统管理员"),
    NOT_FOUND(3019, "接口/页面地址不存在"),
    FILE_ERROR(3020, "文件上传失败"),
    SAVE_ERROR(3021, "保存失败"),
    FIELD_NULL_ERROR(3022, "系统开小差了，请联系系统管理员"),
    ILLEGAL_STATE(3023, "系统开小差了，请联系系统管理员"),
    CLASS_NOT_FOUND(3024, "系统开小差了，请联系系统管理员"),

    //4000～4999 区间表示服务内部异常
    INTERFACE_NOT_FOUND(4001, "接口/页面地址不存在"),

    //===============================================
    //大于等于9000的异常code，国际化由业务系统 自行解决 传入
    //业务抛异常 可采用此code，message自行传入
    SERVICE_ERROR_UNKOWN(9000, "未知异常"),
    //参数异常 需要额外处理
    SERVICE_PARAM_INVALID(9001, "参数有问题，请检查！"),

    ;

    private Integer code;

    private String message;

    ResultEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public Integer getCode() {
        return this.code;
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}
