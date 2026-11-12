package com.cybersource.authsdk.util.test;

import java.math.BigInteger;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.Security;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.config.Property;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.cybersource.authsdk.util.GlobalLabelParameters;
import com.cybersource.authsdk.util.Utility;

/**
 * Verifies {@link Utility#validateCertificateExpiry} emits the correct warning on the
 * 90-day boundary (unit fix) and uses path-specific wording for the JWT signing cert.
 */
public class CertificateExpiryValidationTest {

    private static final long DAY_MS = 24L * 60 * 60 * 1000;

    private Logger utilityLogger;
    private CapturingAppender appender;

    @BeforeClass
    public static void registerProvider() {
        if (Security.getProvider(BouncyCastleProvider.PROVIDER_NAME) == null) {
            Security.addProvider(new BouncyCastleProvider());
        }
    }

    @Before
    public void attachAppender() {
        utilityLogger = (Logger) LogManager.getLogger(Utility.class);
        appender = new CapturingAppender();
        appender.start();
        utilityLogger.addAppender(appender);
        utilityLogger.setLevel(Level.WARN);
    }

    @After
    public void detachAppender() {
        utilityLogger.removeAppender(appender);
        appender.stop();
    }

    @Test
    public void certExpiringInsideNinetyDaysWarns() throws Exception {
        // 30 days out: with the millisecond fix this is inside the 90-day window and must warn.
        X509Certificate cert = buildCert(now() - DAY_MS, now() + 30 * DAY_MS);

        Utility.validateCertificateExpiry(cert, "alias-30d", GlobalLabelParameters.CERT_IDENTIFIER_FOR_JWT_SIGNING);

        Assert.assertEquals(1, appender.messages.size());
        Assert.assertTrue(appender.messages.get(0).contains("going to expired"));
        Assert.assertTrue(appender.messages.get(0).contains("alias-30d"));
    }

    @Test
    public void certExpiringOutsideNinetyDaysDoesNotWarn() throws Exception {
        // 200 days out: comfortably outside the 90-day window, no warning expected.
        X509Certificate cert = buildCert(now() - DAY_MS, now() + 200 * DAY_MS);

        Utility.validateCertificateExpiry(cert, "alias-200d", GlobalLabelParameters.CERT_IDENTIFIER_FOR_JWT_SIGNING);

        Assert.assertTrue("No warning expected for a cert far from expiry", appender.messages.isEmpty());
    }

    @Test
    public void certJustInsideNinetyDayBoundaryWarns() throws Exception {
        // 89 days out: inside the window. Guards against the old seconds-vs-milliseconds bug,
        // under which this (and anything beyond ~2.2 hours) would silently NOT warn.
        X509Certificate cert = buildCert(now() - DAY_MS, now() + 89 * DAY_MS);

        Utility.validateCertificateExpiry(cert, "alias-89d", GlobalLabelParameters.CERT_IDENTIFIER_FOR_JWT_SIGNING);

        Assert.assertEquals(1, appender.messages.size());
        Assert.assertTrue(appender.messages.get(0).contains("going to expired"));
    }

    @Test
    public void alreadyExpiredCertWarnsAndDoesNotThrow() throws Exception {
        X509Certificate cert = buildCert(now() - 10 * DAY_MS, now() - DAY_MS);

        Utility.validateCertificateExpiry(cert, "alias-expired", GlobalLabelParameters.CERT_IDENTIFIER_FOR_JWT_SIGNING);

        Assert.assertEquals(1, appender.messages.size());
        Assert.assertTrue(appender.messages.get(0).contains("is expired as of"));
    }

    @Test
    public void jwtIdentifierSelectsJwtWording() throws Exception {
        X509Certificate cert = buildCert(now() - DAY_MS, now() + 10 * DAY_MS);

        Utility.validateCertificateExpiry(cert, "alias-jwt", GlobalLabelParameters.CERT_IDENTIFIER_FOR_JWT_SIGNING);

        Assert.assertEquals(1, appender.messages.size());
        Assert.assertTrue(appender.messages.get(0).startsWith("JWT signing certificate"));
    }

    private static long now() {
        return System.currentTimeMillis();
    }

    private static X509Certificate buildCert(long notBeforeMs, long notAfterMs) throws Exception {
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
        kpg.initialize(2048);
        KeyPair keyPair = kpg.generateKeyPair();

        org.bouncycastle.asn1.x500.X500Name subject = new org.bouncycastle.asn1.x500.X500Name("CN=Test Cert");
        JcaX509v3CertificateBuilder builder = new JcaX509v3CertificateBuilder(
                subject,
                BigInteger.valueOf(notAfterMs),
                new Date(notBeforeMs),
                new Date(notAfterMs),
                subject,
                keyPair.getPublic());

        ContentSigner signer = new JcaContentSignerBuilder("SHA256WithRSA")
                .setProvider(BouncyCastleProvider.PROVIDER_NAME)
                .build(keyPair.getPrivate());

        return new JcaX509CertificateConverter()
                .setProvider(BouncyCastleProvider.PROVIDER_NAME)
                .getCertificate(builder.build(signer));
    }

    private static final class CapturingAppender extends AbstractAppender {
        private final List<String> messages = new ArrayList<>();

        CapturingAppender() {
            super("capture", null, null, true, Property.EMPTY_ARRAY);
        }

        @Override
        public void append(LogEvent event) {
            messages.add(event.getMessage().getFormattedMessage());
        }
    }
}
