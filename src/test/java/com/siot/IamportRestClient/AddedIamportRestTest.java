package com.siot.IamportRestClient;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.request.AgainPaymentData;
import com.siot.IamportRestClient.request.CancelData;
import com.siot.IamportRestClient.request.CardInfo;
import com.siot.IamportRestClient.request.OnetimePaymentData;
import com.siot.IamportRestClient.request.naver.NaverApproveReturnData;
import com.siot.IamportRestClient.request.naver.NaverCancelData;
import com.siot.IamportRestClient.request.naver.NaverPlaceData;
import com.siot.IamportRestClient.request.naver.NaverRejectReturnData;
import com.siot.IamportRestClient.request.naver.NaverRequestReturnData;
import com.siot.IamportRestClient.request.naver.NaverResolveReturnData;
import com.siot.IamportRestClient.request.naver.NaverShipData;
import com.siot.IamportRestClient.request.naver.NaverWithholdReturnData;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import com.siot.IamportRestClient.response.naver.NaverProductOrder;
import com.siot.IamportRestClient.response.naver.NaverReview;

public class AddedIamportRestTest {

	IamportClient client;

	private IamportClient getNaverTestClient() {
		String test_api_key = "5978210787555892";
		String test_api_secret = "9e75ulp4f9Wwj0i8MSHlKFA9PCTcuMYE15Kvr9AHixeCxwKkpsFa7fkWSd9m0711dLxEV7leEAQc6Bxv";

		return new IamportClient(test_api_key, test_api_secret);
	}

	private IamportClient getBillingTestClient() {
		String test_api_key = "7544324362787472";
		String test_api_secret = "9frnPjLAQe3evvAaJl3xLOODfO3yBk7LAy9pRV0H93VEzwPjRSQDHFhWtku5EBRea1E1WEJ6IEKhbAA3";

		return new IamportClient(test_api_key, test_api_secret);
	}

	@Before
	public void setup() {
		String test_api_key = "imp_apikey";
		String test_api_secret = "ekKoeW8RyKuT0zgaZsUtXXTLQ4AhPFW3ZGseDA6bkA5lamv9OqDMnxyeB9wqOsuO9W3Mx9YSJ4dTqJ3f";
		client = new IamportClient(test_api_key, test_api_secret);
	}

	// @Test
	public void testOnetimePayment() throws Exception {
		String customer_uid = "ks950505";

		// 1. first payment
		String first_merchant_uid = "randomtest" + Math.random() * 10000;
		System.out.println("first_merchant_uid : " + first_merchant_uid);
		CardInfo card = new CardInfo("", "", "", ""); // TODO card info erased.
		OnetimePaymentData onetime_data = new OnetimePaymentData(first_merchant_uid, BigDecimal.valueOf(1004), card);
		onetime_data.setName("ActiveX없는결제테스트");
		onetime_data.setBuyerName("구매자");
		onetime_data.setBuyerEmail("ks5050577@gmail.com");
		onetime_data.setBuyerTel("01091019320");
		onetime_data.setCustomer_uid(customer_uid);

		IamportResponse<Payment> payment_response = client.onetimePayment(onetime_data);
		try {
			Thread.sleep(3000);
		} catch (Exception e) {
			e.printStackTrace();
		}
		cancelPayment(first_merchant_uid);
		System.out.println("[OnetimePayment] " + payment_response.getCode());
		System.out.println("[OnetimePayment] " + payment_response.getMessage());
		System.out.println("[OnetimePayment] " + payment_response.getResponse().getStatus());
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		// 2. again payment by billing key
		String again_merchant_uid = "randomtest" + Math.random() * 10000;
		System.out.println("again_merchant_uid : " + again_merchant_uid);

		AgainPaymentData again_data = new AgainPaymentData(customer_uid, again_merchant_uid, BigDecimal.valueOf(1005));
		payment_response = client.againPayment(again_data);
		try {
			Thread.sleep(3000);
		} catch (Exception e) {
			e.printStackTrace();
		}
		cancelPayment(again_merchant_uid);
		System.out.println("[AgainPayment] " + payment_response.getCode());
		System.out.println("[AgainPayment] " + payment_response.getMessage());
		System.out.println("[AgainPayment] " + payment_response.getResponse().getStatus());
	}

	public void cancelPayment(String merchant_uid) { // 결제 후 취소를 위한 function
		CancelData cancel_data = new CancelData(merchant_uid, false); // merchant_uid를 통한 전액취소
		cancel_data.setEscrowConfirmed(true); // 에스크로 구매확정 후 취소인 경우 true설정

		try {
			IamportResponse<Payment> payment_response = client.cancelPaymentByImpUid(cancel_data);

			System.out.println("[cancelPayment] " + payment_response.getCode());
			System.out.println("[cancelPayment] " + payment_response.getMessage());
			System.out.println("[cancelPayment] " + payment_response.getResponse().getStatus());
			assertNotNull(payment_response.getResponse());
		} catch (IamportResponseException e) {
			System.out.println(e.getMessage());

			switch (e.getHttpStatusCode()) {
			case 401:
				// TODO
				break;
			case 500:
				// TODO
				break;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void testNaverReviews() {
		IamportClient naverClient = getNaverTestClient();

		try {
			IamportResponse<List<NaverReview>> r = naverClient.naverReviews();
			System.out.println("[testNaverReviews] " + r.getCode());
			System.out.println("[testNaverReviews] " + r.getMessage());
			assertEquals(r.getCode(), -1);
		} catch (IamportResponseException e) {
			System.out.println(e.getMessage());

			switch (e.getHttpStatusCode()) {
			case 401:
				// TODO
				break;
			case 500:
				// TODO
				break;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void testNaverPlaceOrders() {
		IamportClient naverClient = getNaverTestClient();
		NaverPlaceData naverPlaceData = new NaverPlaceData();

		String impUid = "imp_630554823245";

		try {
			IamportResponse<List<NaverProductOrder>> r = naverClient.naverPlaceOrders(impUid, naverPlaceData);
			System.out.println("[testNaverPlaceOrders] " + r.getCode());
			System.out.println("[testNaverPlaceOrders] " + r.getMessage());
		} catch (IamportResponseException e) {
			System.out.println("[testNaverPlaceOrders][ERROR] " + e.getMessage());
			System.out.println("[testNaverPlaceOrders] http status : " + e.getHttpStatusCode());

			switch (e.getHttpStatusCode()) {
			case 207:
				// TODO 일부 성공
				break;
			case 500:
				// TODO 모두 실패
				break;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void testNaverCancelOrders() {
		IamportClient naverClient = getNaverTestClient();
		NaverCancelData naverCancelData = new NaverCancelData("test");

		String impUid = "imp_630554823245";

		try {
			IamportResponse<List<NaverProductOrder>> r = naverClient.naverCancelOrders(impUid, naverCancelData);
			System.out.println("[testNaverCancelOrders] " + r.getCode());
			System.out.println("[testNaverCancelOrders] " + r.getMessage());
		} catch (IamportResponseException e) {
			System.out.println("[testNaverCancelOrders][ERROR] " + e.getMessage());
			System.out.println("[testNaverCancelOrders] http status : " + e.getHttpStatusCode());

			switch (e.getHttpStatusCode()) {
			case 207:
				// TODO 일부 성공
				break;
			case 500:
				// TODO 모두 실패
				break;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void testNaverShippingOrders() {
		IamportClient naverClient = getNaverTestClient();
		NaverShipData naverShippingOrders = new NaverShipData("DELIVERY", new Date());

		String impUid = "imp_630554823245";

		try {
			IamportResponse<List<NaverProductOrder>> r = naverClient.naverShippingOrders(impUid, naverShippingOrders);
			System.out.println("[testNaverShippingOrders] " + r.getCode());
			System.out.println("[testNaverShippingOrders] " + r.getMessage());
		} catch (IamportResponseException e) {
			System.out.println("[testNaverShippingOrders][ERROR] " + e.getMessage());
			System.out.println("[testNaverShippingOrders] http status : " + e.getHttpStatusCode());

			switch (e.getHttpStatusCode()) {
			case 207:
				// TODO 일부 성공
				break;
			case 500:
				// TODO 모두 실패
				break;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void testNaverRequestReturnOrders() {
		IamportClient naverClient = getNaverTestClient();
		NaverRequestReturnData naverRequestReturnData = new NaverRequestReturnData("DELIVERY");

		String impUid = "imp_630554823245";

		try {
			IamportResponse<List<NaverProductOrder>> r = naverClient.naverRequestReturnOrders(impUid,
					naverRequestReturnData);
			System.out.println("[testNaverRequestReturnOrders] " + r.getCode());
			System.out.println("[testNaverRequestReturnOrders] " + r.getMessage());
		} catch (IamportResponseException e) {
			System.out.println("[testNaverRequestReturnOrders][ERROR] " + e.getMessage());
			System.out.println("[testNaverRequestReturnOrders] http status : " + e.getHttpStatusCode());

			switch (e.getHttpStatusCode()) {
			case 207:
				// TODO 일부 성공
				break;
			case 500:
				// TODO 모두 실패
				break;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void testNaverApproveReturnOrders() {
		IamportClient naverClient = getNaverTestClient();
		NaverApproveReturnData naverApproveReturnData = new NaverApproveReturnData();

		String impUid = "imp_630554823245";

		try {
			IamportResponse<List<NaverProductOrder>> r = naverClient.naverApproveReturnOrders(impUid,
					naverApproveReturnData);
			System.out.println("[testNaverApproveReturnOrders] " + r.getCode());
			System.out.println("[testNaverApproveReturnOrders] " + r.getMessage());
		} catch (IamportResponseException e) {
			System.out.println("[testNaverApproveReturnOrders][ERROR] " + e.getMessage());
			System.out.println("[testNaverApproveReturnOrders] http status : " + e.getHttpStatusCode());

			switch (e.getHttpStatusCode()) {
			case 207:
				// TODO 일부 성공
				break;
			case 500:
				// TODO 모두 실패
				break;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void testNaverRejectReturnOrders() {
		IamportClient naverClient = getNaverTestClient();
		NaverRejectReturnData naverRejectReturnData = new NaverRejectReturnData("test");

		String impUid = "imp_630554823245";

		try {
			IamportResponse<List<NaverProductOrder>> r = naverClient.naverRejectReturnOrders(impUid,
					naverRejectReturnData);
			System.out.println("[testNaverRejectReturnOrders] " + r.getCode());
			System.out.println("[testNaverRejectReturnOrders] " + r.getMessage());
		} catch (IamportResponseException e) {
			System.out.println("[testNaverRejectReturnOrders][ERROR] " + e.getMessage());
			System.out.println("[testNaverRejectReturnOrders] http status : " + e.getHttpStatusCode());

			switch (e.getHttpStatusCode()) {
			case 207:
				// TODO 일부 성공
				break;
			case 500:
				// TODO 모두 실패
				break;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void testNaverWithholdReturnOrders() {
		IamportClient naverClient = getNaverTestClient();
		NaverWithholdReturnData naverWithholdReturnData = new NaverWithholdReturnData("test");

		String impUid = "imp_630554823245";

		try {
			IamportResponse<List<NaverProductOrder>> r = naverClient.naverWithholdReturnOrders(impUid,
					naverWithholdReturnData);
			System.out.println("[testNaverWithholdReturnOrders] " + r.getCode());
			System.out.println("[testNaverWithholdReturnOrders] " + r.getMessage());
		} catch (IamportResponseException e) {
			System.out.println("[testNaverWithholdReturnOrders][ERROR] " + e.getMessage());
			System.out.println("[testNaverWithholdReturnOrders] http status : " + e.getHttpStatusCode());

			switch (e.getHttpStatusCode()) {
			case 207:
				// TODO 일부 성공
				break;
			case 500:
				// TODO 모두 실패
				break;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void testNaverResolveReturnOrders() {
		IamportClient naverClient = getNaverTestClient();
		NaverResolveReturnData naverResolveReturnData = new NaverResolveReturnData();

		String impUid = "imp_630554823245";

		try {
			IamportResponse<List<NaverProductOrder>> r = naverClient.naverResolveReturnOrders(impUid,
					naverResolveReturnData);
			System.out.println("[testNaverResolveReturnOrders] " + r.getCode());
			System.out.println("[testNaverResolveReturnOrders] " + r.getMessage());
		} catch (IamportResponseException e) {
			System.out.println("[testNaverResolveReturnOrders][ERROR] " + e.getMessage());
			System.out.println("[testNaverResolveReturnOrders] http status : " + e.getHttpStatusCode());

			switch (e.getHttpStatusCode()) {
			case 207:
				// TODO 일부 성공
				break;
			case 500:
				// TODO 모두 실패
				break;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}