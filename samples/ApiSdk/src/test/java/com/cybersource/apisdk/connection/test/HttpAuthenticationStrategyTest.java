package com.cybersource.apisdk.connection.test;

public class HttpAuthenticationStrategyTest {

//	@Mock
//	HttpAuthenticationStrategy httpConnection;
//	
//	@Mock
//	MerchantConfig merchantConfig;
//	
//	@Mock
//	RequestContext requestContext;
//	
//	@Mock
//	Logger logger;
//
//	private Properties merchantProp;
//	private String id = "5281957982016993203523";
//	private String requestTarget = GlobalLabelParameters.REQUEST_TARGET;
//	private String requestJsonPath = "src/test/resources/request.json";
//	private String url = "https://";
//	private Response response;
//	
//	@Rule
//	public ExpectedException thrown = ExpectedException.none();
//	
//	private boolean compareMap(Map<String, String> inpFirst, Map<String, String> inpSecond) {
//	    if (inpFirst.size() != inpSecond.size()) {
//	        return false;
//	    }
//	 
//	    return inpFirst.entrySet().stream()
//	      .allMatch(e -> e.getKey().equals(inpSecond.get(e.getKey())));
//	}
//
//	@Test
//	public void setHeaderDataTestForGet() throws ConfigException, IOException, ClientException, SignatureGenerationException {
//		merchantProp = PropertiesUtil.getMerchantProperties();
//		merchantConfig = MerchantConfig.getInstance(merchantProp);
//		merchantConfig.setMerchantDetails();
//		requestContext=new RequestContext(merchantConfig.getLogger());
//		
//		if (merchantConfig.getAuthenticationType().equalsIgnoreCase(GlobalLabelParameters.JWT)) {
//			Assert.assertNull(null);
//		} else {
//			merchantConfig.setMerchantID("testrest");
//	        requestTarget = requestTarget + id;
//	        requestContext.setRequestTarget(requestTarget);
//			url = url + merchantConfig.getRequestHost() + requestContext.getRequestTarget();
//			/* Set the URL. */
//			requestContext.setUrl(url);
//			requestContext.setRequestMethodType("GET");
//			requestContext.setRequestJsonPath(requestJsonPath);
//			httpConnection = new HttpAuthenticationStrategy(merchantConfig,requestContext);
//			Map<String, String> headerMap = httpConnection.setHeaderData();
//			Map<String,String> expectedHeaderMap = new HashMap<String, String>();
//			headerMap.put(GlobalLabelParameters.V_C_MERCHANT_ID, merchantConfig.getMerchantID());
//			headerMap.put(GlobalLabelParameters.DATE, requestContext.getDate());
//			if(headerMap != null) {
//				
//			}
//	        Assert.assertNotNull(headerMap);
//		}
//	}
	
//	@Test
//	public void setHeaderDataTestForPost() throws ConfigException, ClientException, IOException {
//		merchantProp = PropertiesUtil.getMerchantProperties();
//		merchantConfig = MerchantConfig.getInstance(merchantProp);
//		merchantConfig.setMerchantDetails();
//		requestContext=new RequestContext(merchantConfig);
//		
//		if (merchantConfig.getAuthenticationType().equalsIgnoreCase(GlobalLabelParameters.JWT)) {
//			Assert.assertNull(null);
//		} else {
//			
//			merchantConfig.setMerchantID("testrest");
//	        requestContext.setRequestTarget(requestTarget);
//			url = url + merchantConfig.getRequestHost() + RequestContext.getRequestTarget();
//			/* Set the URL. */
//			requestContext.setUrl(url);
//			requestContext.setRequestJsonPath(requestJsonPath);
//			requestContext.setRequestMethodType("POST");
//			
//			httpConnection = new HttpAuthenticationStrategy(merchantConfig,requestContext.getRequestData(), logger);
//			httpConnection.openConnection();
//	        httpConnection.setHeaderData();
//	        Assert.assertNotNull(httpConnection);
//		}
//	}
//	
//	@Test
//	public void setHeaderDataTestForPatch() throws ConfigException, IOException, ClientException {
//		merchantProp = PropertiesUtil.getMerchantProperties();
//		merchantConfig = MerchantConfig.getInstance(merchantProp);
//		merchantConfig.setMerchantDetails();
//		requestContext=new RequestContext(merchantConfig);
//		
//		if (merchantConfig.getAuthenticationType().equalsIgnoreCase(GlobalLabelParameters.JWT)) {
//			Assert.assertNull(null);
//		} else {
//			merchantConfig.setMerchantID("testrest");
//	        requestContext.setRequestTarget(requestTarget);
//			url = url + merchantConfig.getRequestHost() + RequestContext.getRequestTarget();
//			/* Set the URL. */
//			requestContext.setUrl(url);
//			requestContext.setRequestJsonPath(requestJsonPath);
//     		requestContext.setRequestMethodType("PATCH");
//			logger = merchantConfig.getLogger();
//			
//	
//			httpConnection = new HttpAuthenticationStrategy(merchantConfig,requestContext.getRequestData(), logger);
//			httpConnection.openConnection();
//	        httpConnection.setHeaderData();
//	        Assert.assertNotNull(httpConnection);
//		}
//
//	}
//	
//	@Test
//	public void setHeaderDataTestForWrongMerchantId() throws ConfigException, IOException, ClientException {
//		merchantProp = PropertiesUtil.getMerchantProperties();
//		merchantConfig = MerchantConfig.getInstance(merchantProp);
//		merchantConfig.setMerchantDetails();
//		merchantConfig.setMerchantID(null);
//		requestContext=new RequestContext(merchantConfig);
//		
//		
//		if (merchantConfig.getAuthenticationType().equalsIgnoreCase(GlobalLabelParameters.JWT)) {
//			Assert.assertNull(null);
//		} else {
//			requestContext.setRequestJsonPath(requestJsonPath);
//			requestContext.setRequestMethodType("GET");
//			requestTarget = requestTarget + id;
//			requestContext.setRequestTarget(requestTarget);
//			url = url + merchantConfig.getRequestHost() + RequestContext.getRequestTarget();
//			requestContext.setUrl(url);
//			
//			logger = merchantConfig.getLogger();
//			
//			thrown.expect(ConfigException.class);
//			thrown.expectMessage(GlobalLabelParameters.MERCHANTID_NOT_SET );
//	
//			httpConnection = new HttpAuthenticationStrategy(merchantConfig,requestContext.getRequestData(), logger);
//			httpConnection.openConnection();
//			httpConnection.setRequestMethod(RequestContext.getRequestType());
//	        httpConnection.setHeaderData();
//	    }	
//	}
//
//	@Test
//	public void openConnectionTestFailForUrl() throws ClientException, IOException, ConfigException {
//		merchantProp = PropertiesUtil.getMerchantProperties();
//		merchantConfig = MerchantConfig.getInstance(merchantProp);
//		merchantConfig.setMerchantDetails();
//		requestContext=new RequestContext(merchantConfig);
//
//		if (merchantConfig.getAuthenticationType().equalsIgnoreCase(GlobalLabelParameters.JWT)) {
//			Assert.assertNull(null);
//		} else {
//			requestTarget = requestTarget + id;
//			requestContext.setRequestTarget(requestTarget);
//			requestContext.setUrl("abc");
//			requestContext.setRequestJsonPath(requestJsonPath);
//			requestContext.setRequestMethodType("GET");
//			logger = merchantConfig.getLogger();
//			
//			thrown.expect(ClientException.class);
//			thrown.expectMessage(GlobalLabelParameters.URL_NOT_SET );
//		   
//			httpConnection = new HttpAuthenticationStrategy(merchantConfig,requestContext.getRequestData(),logger);
//			httpConnection.openConnection();
//		}
//	}
//
//
//	@Test
//	public void setRequestMethodTest() throws ClientException, IOException, ConfigException {
//		merchantProp = PropertiesUtil.getMerchantProperties();
//		merchantConfig = MerchantConfig.getInstance(merchantProp);
//		merchantConfig.setMerchantDetails();
//		
//		if (merchantConfig.getAuthenticationType().equalsIgnoreCase(GlobalLabelParameters.JWT)) {
//			Assert.assertNull(null);
//		} else {
//			requestContext=new RequestContext(merchantConfig);
//			requestTarget = requestTarget + id;
//			requestContext.setRequestTarget(requestTarget);
//			url = url + merchantConfig.getRequestHost() + RequestContext.getRequestTarget();
//			/* Set the URL. */
//			requestContext.setUrl(url);
//			requestContext.setRequestJsonPath(requestJsonPath);
//			requestContext.setRequestMethodType("GET");
//			logger = merchantConfig.getLogger();
//			RequestContext.validateMerchantDetails();
//	        
//			httpConnection = new HttpAuthenticationStrategy(merchantConfig,requestContext.getRequestData(), logger);
//			httpConnection.openConnection();
//			httpConnection.setRequestMethod(RequestContext.getRequestType());
//		}
//	}
//	
//	@Test
//	public void getTestForGet() throws ClientException, IOException, ConfigException {
//		merchantProp = PropertiesUtil.getMerchantProperties();
//		merchantConfig = MerchantConfig.getInstance(merchantProp);
//		merchantConfig.setMerchantDetails();
//		
//		if (merchantConfig.getAuthenticationType().equalsIgnoreCase(GlobalLabelParameters.JWT)) {
//			Assert.assertNull(null);
//		} else {
//			requestContext=new RequestContext(merchantConfig);
//			requestTarget = requestTarget + id;
//			requestContext.setRequestTarget(requestTarget);
//			requestContext.setRequestJsonPath(requestJsonPath);
//			requestContext.setRequestMethodType("GET");
//	
//			url = url + merchantConfig.getRequestHost() + RequestContext.getRequestTarget();
//			requestContext.setUrl(url);
//	
//			logger = merchantConfig.getLogger();
//			RequestContext.validateMerchantDetails();
//	
//			httpConnection = new HttpAuthenticationStrategy(merchantConfig,requestContext.getRequestData(), logger);
//			Response str = httpConnection.getHttpConnection();
//			Assert.assertNotNull(str);
//		}
//	}
//
//	@Test
//	public void getTestForPost() throws ClientException, IOException, ConfigException {
//		merchantProp = PropertiesUtil.getMerchantProperties();
//		merchantConfig = MerchantConfig.getInstance(merchantProp);
//		merchantConfig.setMerchantDetails();
//		
//		if (merchantConfig.getAuthenticationType().equalsIgnoreCase(GlobalLabelParameters.JWT)) {
//			Assert.assertNull(null);
//		} else {
//			requestContext=new RequestContext(merchantConfig);
//			requestContext.setRequestTarget(requestTarget);
//			requestContext.setRequestJsonPath(requestJsonPath);
//			requestContext.setRequestMethodType("POST");
//	
//			url = url + merchantConfig.getRequestHost() + RequestContext.getRequestTarget();
//			requestContext.setUrl(url);
//	
//			logger = merchantConfig.getLogger();
//			RequestContext.validateMerchantDetails();
//	
//			httpConnection = new HttpAuthenticationStrategy(merchantConfig,requestContext.getRequestData(), logger);
//			Response str = httpConnection.getHttpConnection();
//			Assert.assertNotNull(str);
//	  }
//	}
//
//	@Test
//	public void getTestForPut() throws IOException, ConfigException, ClientException {
//		merchantProp = PropertiesUtil.getMerchantProperties();
//		merchantConfig = MerchantConfig.getInstance(merchantProp);
//		merchantConfig.setMerchantDetails();
//		
//		if (merchantConfig.getAuthenticationType().equalsIgnoreCase(GlobalLabelParameters.JWT)) {
//			Assert.assertNull(null);
//		} else {
//			requestContext=new RequestContext(merchantConfig);
//			requestContext.setRequestTarget(requestTarget);
//			requestContext.setRequestJsonPath(requestJsonPath);
//			requestContext.setRequestMethodType("PUT");
//	
//			url = url + merchantConfig.getRequestHost() + RequestContext.getRequestTarget();
//			requestContext.setUrl(url);
//	
//			logger = merchantConfig.getLogger();
//			RequestContext.validateMerchantDetails();
//	
//			httpConnection = new HttpAuthenticationStrategy(merchantConfig,requestContext.getRequestData(), logger);
//			Response str = httpConnection.getHttpConnection();
//			Assert.assertNotNull(str);
//		}
//
//	}
//	
//	@Test
//	public void getTestForPatch() throws IOException, ConfigException, ClientException {
//		merchantProp = PropertiesUtil.getMerchantProperties();
//		merchantConfig = MerchantConfig.getInstance(merchantProp);
//		merchantConfig.setMerchantDetails();
//		
//		if (merchantConfig.getAuthenticationType().equalsIgnoreCase(GlobalLabelParameters.JWT)) {
//			Assert.assertNull(null);
//		} else {
//			requestContext=new RequestContext(merchantConfig);
//			requestContext.setRequestTarget(requestTarget);
//			requestContext.setRequestJsonPath(requestJsonPath);
//			requestContext.setRequestMethodType("PATCH");
//	
//			url = url + merchantConfig.getRequestHost() + RequestContext.getRequestTarget();
//			requestContext.setUrl(url);
//	
//			logger = merchantConfig.getLogger();
//			RequestContext.validateMerchantDetails();
//	
//			httpConnection = new HttpAuthenticationStrategy(merchantConfig,requestContext.getRequestData(), logger);
//			response=httpConnection.executePatch(requestContext.getRequestData());
//			Assert.assertNotNull(response);
//	 } 
//	}

}
