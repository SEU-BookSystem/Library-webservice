package booksystem.pojo;


import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class AliPay {

    /*商户订单号，必填*/
    private String out_trade_no;
    /*订单名称，必填*/
    private String subject;
    /*付款金额，必填*/
    private String total_amount;
    /*商品描述，可空*/
    private String body;
    /*超时时间参数*/
    private String timeout_express="1h";
    private String product_code="FAST_INSTANT_TRADE_PAY";

    public AliPay(String out_trade_no, String subject, String total_amount, String body) {
        this.out_trade_no = out_trade_no;
        this.subject = subject;
        this.total_amount = total_amount;
        this.body = body;
        this.timeout_express = "1h";
        this.product_code = "FAST_INSTANT_TRADE_PAY";
    }

    @Override
    public String toString() {
        return  "{\"out_trade_no\":\"" + out_trade_no + '\"' +
                ", \"subject\":\"" + subject + '\"' +
                ", \"total_amount\":\"" + total_amount + '\"' +
                ", \"body\":\"" + body + '\"' +
                ", \"timeout_express\":\"" + timeout_express + '\"' +
                ", \"product_code\":\"" + product_code + '\"' +
                '}';
    }
}
