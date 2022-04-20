package booksystem.service.Impl;

import booksystem.pojo.AliPay;
import booksystem.service.PayService;
import booksystem.utils.AliPayUtils;
import com.alipay.api.AlipayApiException;
import org.springframework.stereotype.Service;

@Service
public class PayServiceImpl implements PayService {
    @Override
    public String aliPay(AliPay alipayBean) throws AlipayApiException {
        return AliPayUtils.connect(alipayBean);
    }
}
