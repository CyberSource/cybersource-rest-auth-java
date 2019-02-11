package com.cybersource.authsdk.http;

import com.cybersource.authsdk.core.ConfigException;
import com.cybersource.authsdk.core.MerchantConfig;
import com.cybersource.authsdk.core.RequestContext;
import com.cybersource.authsdk.payloaddigest.PayloadDigest;
import com.cybersource.authsdk.util.GlobalLabelParameters;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/* This method returns value for parameter Signature which is then passed to Signature header
 * parameter 'Signature' is calculated based on below key values and then signed with SECRET KEY -
 * host: SandBox (apitest.cybersource.com) or Production (api.cybersource.com) hostName
 * date: "HTTP-date" format as defined by RFC7231.
 * (request-target): Should be in format of httpMethod: path
                     Example: "post /pts/v2/payments"
 * Digest: Only needed for POST calls.
 * v-c-merchant-id: set value to CyberSource Merchant ID
                     This ID can be found on EBC portal */
public class SignatureGenerator {
    private SignatureGenerator()
    {}

    /**
     * @return generated signature.
     * @throws NoSuchAlgorithmException     - if algorithm is not available.
     * @throws InvalidKeyException          - if key is not valid.
     * @throws ConfigException              - if some value is missing for merchant.
     * @throws UnsupportedEncodingException - if encoding is not supported.
     */
    public static String generateSignature(MerchantConfig merchantConfig, RequestContext authRequestContext)
            throws NoSuchAlgorithmException, InvalidKeyException, ConfigException, UnsupportedEncodingException {
        String httpMethod = authRequestContext.getRequestMethodType();
        StringBuilder signatureString = new StringBuilder();
        signatureString.append('\n');
        signatureString.append(GlobalLabelParameters.HOST.toLowerCase());
        signatureString.append(": ");
        signatureString.append(merchantConfig.getRequestHost());
        signatureString.append('\n');
        signatureString.append(GlobalLabelParameters.DATE.toLowerCase());
        signatureString.append(": ");
        signatureString.append(authRequestContext.getDate());
        signatureString.append('\n');
        signatureString.append("(request-target)");
        signatureString.append(": ");

        String requestTarget = getRequestTarget(authRequestContext);
        signatureString.append(requestTarget);
        signatureString.append('\n');

        if (httpMethod.equalsIgnoreCase(GlobalLabelParameters.POST)
                || httpMethod.equalsIgnoreCase(GlobalLabelParameters.PUT)
                || (httpMethod.equalsIgnoreCase(GlobalLabelParameters.PATCH))) {
            signatureString.append(GlobalLabelParameters.DIGEST.toLowerCase());
            signatureString.append(": ");
            signatureString.append(new PayloadDigest(authRequestContext.getRequestBody()).getDigest());
            signatureString.append('\n');
        }

        signatureString.append(GlobalLabelParameters.V_C_MERCHANTID);
        signatureString.append(": ");
        signatureString.append(merchantConfig.getMerchantID());
        signatureString.delete(0, 1);
        String signatureStr = signatureString.toString();

		/*
         * Signature string generated from above parameters is Signed with
		 * SecretKey hased with SHA256 and base64 encoded. Secret Key is Base64
		 * decoded before signing
		 */

        SecretKeySpec secretKey = new SecretKeySpec(Base64.getDecoder().decode(merchantConfig.getMerchantSecretKey()),
                "HmacSHA256");
        Mac aKeyId = Mac.getInstance("HmacSHA256");
        aKeyId.init(secretKey);
        aKeyId.update(signatureStr.getBytes());
        byte[] aHeaders = aKeyId.doFinal();
        return Base64.getEncoder().encodeToString(aHeaders);
    }

    /**
     * @param
     * @return request target as per request type.
     */
    private static String getRequestTarget(RequestContext authRequestContext) {
        String requestTarget;
        String requestType = authRequestContext.getRequestMethodType();
        switch (requestType) {
            case GlobalLabelParameters.POST:
                requestTarget = GlobalLabelParameters.POST.toLowerCase() + GlobalLabelParameters.SPACE
                        + authRequestContext.getRequestTarget();
                break;
            case GlobalLabelParameters.GET:
                requestTarget = GlobalLabelParameters.GET.toLowerCase() + GlobalLabelParameters.SPACE
                        + authRequestContext.getRequestTarget();
                break;
            case GlobalLabelParameters.PUT:
                requestTarget = GlobalLabelParameters.PUT.toLowerCase() + GlobalLabelParameters.SPACE
                        + authRequestContext.getRequestTarget();
                break;
            case GlobalLabelParameters.DELETE:
                requestTarget = GlobalLabelParameters.DELETE.toLowerCase() + GlobalLabelParameters.SPACE
                        + authRequestContext.getRequestTarget();
                break;
            case GlobalLabelParameters.PATCH:
                requestTarget = GlobalLabelParameters.PATCH.toLowerCase() + GlobalLabelParameters.SPACE
                        + authRequestContext.getRequestTarget();
                break;
            default:
                requestTarget = null;
                break;
        }
        return requestTarget;
    }
}
