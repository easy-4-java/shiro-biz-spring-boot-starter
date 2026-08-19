package org.apache.shiro.spring.boot.biz.web.servlet;
import org.apache.shiro.biz.web.servlet.AuthenticatingHttpServlet;


/**
 * An HTTP servlet for RSA key-pair-based login authentication. Generates an RSA key pair,
 * stores the private key in the HTTP session, and returns the public key to the client
 * for encrypting credentials.
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
public class RsaKeyPairHttpServlet extends AuthenticatingHttpServlet {

	private static final String PRIVATE_KEY_ATTRIBUTE_NAME = "privateKey";

}