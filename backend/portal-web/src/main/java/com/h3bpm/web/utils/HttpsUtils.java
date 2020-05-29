package com.h3bpm.web.utils;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.ssl.SSLSocketFactory;

public class HttpsUtils {
	public static final String SCHEME = "https";

	private static TrustManager truseAllManager = new X509TrustManager() {

		public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
			// Does nothing.
		}

		public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
			// Does nothing.
		}

		public java.security.cert.X509Certificate[] getAcceptedIssuers() {
			// Does nothing.

			return null;
		}
	};

	public static boolean isHttpsScheme(String url) {

		if (StringUtils.isBlank(url)) {
			return false;

		} else if (url.startsWith(SCHEME + ":")) {
			return true;
		}

		return false;
	}

	public static void enableSsl(HttpClient httpClient) throws Exception {
		SSLContext sslc = SSLContext.getInstance("TLS");
		sslc.init(null, new TrustManager[] { truseAllManager }, null);

		SSLSocketFactory ssls = new SSLSocketFactory(sslc, SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

		Scheme https = new Scheme(SCHEME, 443, ssls);
		httpClient.getConnectionManager().getSchemeRegistry().register(https);
	}
}
