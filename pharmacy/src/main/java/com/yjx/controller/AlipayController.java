package com.yjx.controller;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@RestController
public class AlipayController {
    private static final String APP_ID = "2021003100683105";

    private static final String APP_PRIVATE_KEY = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCUXMZcOArT0nnqiEg14mFxrx/uUHIOs91CZ/YBzOBV98Q6QLNO5cUJCVePu7WmBpNejl+l3Qv6iPKuIE1IICHcZjslT3F36M3EK+qtEMzRxXSBvAbbaCYTHT7dwvIRideGZO/D8J+h7FQEThp+F0+B4p1EcqwxZeIGDBfTpbbPQT0aN0928tBr0JPLqbT7GynMgVXf3abmJmVF72mxIXzKn+OdfOR14UWO6dmQ+LQw5YA27vU7mEc8ZpV+rddDSJyr+GgKe1IUA47hj3/kRoFiLjpvPLbfidiGhdnPhNard0TR5Xmm2rrBG1WBos/R/WmhR5cz04kMrGw/nCko1IDtAgMBAAECggEAK+Nh/ofJlC3QSPP1wJLuVo4CuSHtC7JjgrB0UsYDmwrHbqATKwTHi8vKrkdODgYl9DPT0aIZWAzJFX+SvQJcYfGQD5J3s4wXyq9fJcExheCOfY5czOz7Tm45WarApzWKn0QetWzqHCFoHTnplmFY8MhT05rEm3xTJ4zOQ5rjAF4MXEmSXV8oqpPx8KgwIOtuntdf+eXVCOL7GkqXa6QN8Xtwxnm2yqpZ1RoMoPsHxZeZ+RgFKMemjFs67hFN7OtVikGkOAWzHEvqPxia7+/ETCaHBY08NAeyzYxOt9IUv7C5/Py0i52xaZjg/OH0a3rocPTQ1aSZIYgvbh8srt1NwQKBgQDlQmDpttutDKiUII79cnruajTPBwf2H4gONeS/KGJC6bQvEJx0c9OoDM3V0vaj5QXXhXdz8KMPt2ydcQrEMKP720IqfXz1OeCvVqXl5Zb4VVFbVMQ7t5quj1QjzuDCI9HON+EA7boXJ8oi7RN/u7CT69vUGdJP8ZkHfQ+8cID4sQKBgQClqtYw1vr/lyLeUYj3TEJ8B9weCZow4OwQMHaa+dbUE4eEgzsVnd33Tr6fBCSSJLd7k6Mu1PnmdJ5j8fTxorCjb2rE51QFHrVA7WUyg/cCanyRMclqg7i5GgAY53my3uj7uOTnvoZrsxAC5rUI5QrMi+Af+kFBedDDV2JbZRja/QKBgQC4X9PU4ex1lXKV/kFA7yqSRvPFc6AhfmZLF2ipm2665Xy1RSSuOuCDjBiV2B3m3S7/jyTsJyDcaOV/EQRZl8yppkHmLhmqlitul76/zw7FFMhjLR7L7/MEY9yFF8GGIIyAzku0FXvNV4Cm6gb+eRGfRNX8E/AhvukVLqzTHZ97YQKBgQCi+P+CxknfG/gCSqV52fxhdX310q5rvSic+IAFDxhmQtwC4PJD3qxYOI/PcMzlM9eIawGR9ZPtQPX8Di9PciNgdB/NmmAONVpLh/4Qhr99EfJ/B0JEVVZoSYqk9TnEXANIdmhBDi3AT0PGBt065c8Mz+Fos9B0L/jN4e3xSO/9pQKBgBXt+q3stougthgxNXn2nchevFWZMHM09uOYqVJHY7Xyi2mM45WTIIp8DF/UhuRHtTEQZUDxf1GogPMJI7ElYK3wX4TB2bSaLiRZbXwkuj5tU3zlF9Sdve5io8Icmwr/agOCS+1dE/gqc5yBIyVxevHE1BNPA3uN7MvFWvst35u9";

    private static final String ALIPAY_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAmeAMXjI17ign4jAV2JBa+sb5vbGwUrGgH1F8SCnTlmQeWqjVz6jzsk7BMHKyJ9BbVw7DVDAuNA0SE5DzC+sFPpFt/2A27sb447K0g0jSri3htuiEo43jgz1WNUeBTP9fPjBiqhlhTjNSoteFo+vAT+3wG3sjEgCWYN5r5HPN7ClzPwXLLNsK9nP4UMqxY6KoIfRe87eWPZmw1cfJ599gUAwPUX5F1p/cgD72oKXy54ENK+ZG0pDsCsl2I7Gjb8voJ1Hb53B2btQTc3oKz8jsFb2Zsaj3VFsz2fLzLNArbGlONKMZwzXSLt3lfsbXMJoQuf83/0egd4rqdVmt6/GUfQIDAQAB";

    @GetMapping("/alipay")
    public void aLiPayMoney(HttpServletRequest httpRequest,
                            HttpServletResponse httpResponse) throws ServletException, IOException {

        AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do",
                APP_ID, APP_PRIVATE_KEY, "json", "utf-8", ALIPAY_PUBLIC_KEY, "RSA2");
        //创建API对应的request
        AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();
        alipayRequest.setReturnUrl( "http://domain.com/CallBack/return_url.jsp" );
        alipayRequest.setNotifyUrl( "http://domain.com/CallBack/notify_url.jsp" ); //在公共参数中设置回跳和通知地址
        alipayRequest.setBizContent( "{"  +
                "    \"out_trade_no\":\"20150320010101001\","  +
                "    \"product_code\":\"FAST_INSTANT_TRADE_PAY\","  +
                "    \"total_amount\":88.88,"  +
                "    \"subject\":\"Iphone6 16G\","  +
                "    \"body\":\"Iphone6 16G\","  +
                "    \"passback_params\":\"merchantBizType%3d3C%26merchantBizNo%3d2016010101111\","  +
                "    \"extend_params\":{"  +
                "    \"sys_service_provider_id\":\"2088511833207846\""  +
                "    }" +
                "  }" ); //填充业务参数
        String form= "" ;
        try  {
            form = alipayClient.pageExecute(alipayRequest).getBody();  //调用SDK生成表单
        }  catch  (AlipayApiException e) {
            e.printStackTrace();
        }
        httpResponse.setContentType( "text/html;charset=utf-8");
        httpResponse.getWriter().write(form); //直接将完整的表单html输出到页面
        httpResponse.getWriter().flush();
        httpResponse.getWriter().close();
    }



}
