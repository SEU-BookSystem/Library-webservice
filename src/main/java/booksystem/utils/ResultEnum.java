package booksystem.utils;


public enum ResultEnum {
    //可自行定义，与前端交互
    UNKNOWN_ERROR(-1,"未知错误"),
    SUCCESS(200,"成功"),
    User_NOT_EXIST(1,"用户不存在"),
    User_IS_EXISTS(2,"用户已存在"),
    DATA_IS_NULL(3,"数据为空"),
    DELETE_FAIL(5,"删除失败"),
    UPDATE_FAIL(6,"更新失败"),
    VERIFY_FAIL(7,"验证失败"),
    AUTHORITY_FAIL(8,"无权限访问"),
    TOKEN_FAIL(9,"token错误或已过期"),
    TOKEN_IS_NULL(10,"token为空"),
    LOGIN_FAIL(11,"登录失败"),
    REGISTER_FAIL(12,"注册失败"),
    CODE_FAIL(13,"验证码错误"),
    EMAIL_FAIL(14,"邮箱信息错误"),
    Code_OUTTIME(15,"验证码过期"),
    SHOP_IS_EXISTS(16,"店铺已存在"),
    SHOP_IS_APPLY(17,"店铺在审核"),
    CATEGORY_IS_EXIST(18,"目录已存在"),
    REPEAT_ADD(19,"重复添加"),
    NUM_IS_NOT_ENOUGH(20,"库存不足"),
    NUM_FAIL(21,"数量错误"),
    NUM_LOWER_ZERO(22,"数据为负"),
    APPLY_OUT_TIME(23,"已超时过期，无法申请"),
    RATE_NUM_NOT_ENOUGH(24,"评分数量过少，无法计算评分"),
    BOOK_IS_NOT_ENOUGH(25,"书籍可被预约、可被借阅数量不足"),
    BORROW_IS_MAX(26,"借阅数量已达最大值"),
    OVERTIME_RECODE(27,"有逾期记录"),
    RENEW_REPEAT(28,"重复续借")


    ;
    private Integer code;
    private String msg;

    ResultEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}