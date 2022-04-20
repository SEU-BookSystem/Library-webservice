package booksystem.service;

import booksystem.pojo.AliPay;
import com.alipay.api.AlipayApiException;

public interface PayService {
    String aliPay(AliPay alipayBean) throws AlipayApiException;

}
