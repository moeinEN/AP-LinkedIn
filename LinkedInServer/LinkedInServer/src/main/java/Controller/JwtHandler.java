package Controller;

import Database.DatabaseQueryController;
import Model.Messages;
import com.sun.net.httpserver.Headers;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.sql.SQLException;
import java.util.Base64;
import java.util.Date;
import java.util.List;

public class JwtHandler {
    // Your 384-bit secret key (base64 encoded) : "aSimpleSecretKeyBase64Encoded384BitForLinkedInProject"
    private static final String secret = "YVNpbXBsZVNlY3JldEtleUJhc2U2NEVuY29kZWQzODRCaXRGb3JMaW5rZWRJblByb2plY3Q=";

    // Decode the base64-encoded key
    private static final byte[] decodedKey = Base64.getDecoder().decode(secret);

    // Create a Key object
    private static final Key key = new SecretKeySpec(decodedKey, SignatureAlgorithm.HS384.getJcaName());

    public static String createJwtToken(String userId, String username, String password, String email, long expirationMillis) {
        // Define the expiration time
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        Date expiryDate = new Date(nowMillis + expirationMillis);

        // Create the JWT
        return Jwts.builder()
                .setId(userId)
                .setIssuedAt(now)
                .setSubject("Login Credentials")
                .claim("username", username)
                .claim("password", password)
                .claim("email", email)
                .setExpiration(expiryDate)
                .signWith(key, SignatureAlgorithm.HS384) // Use HS384 for signing
                .compact();
    }

    public static Claims decodeJwtToken(String jwt) {
        // Parse and validate the JWT token
        Jws<Claims> claimsJws = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(jwt);

        // Get the claims from the token
        Claims claims = claimsJws.getBody();
        return claims;
    }

    public static Messages validateUserSession(String jwt) {
        Claims claims = null;
        try {
            claims = decodeJwtToken(jwt);
        }
        catch (io.jsonwebtoken.MalformedJwtException e) {
            e.printStackTrace();
            return Messages.INVALID_TOKEN;
        }

        if(checkExpiryDate(claims.getExpiration())) {
            return Messages.SESSION_EXPIRED;
        }

        if(!claims.getSubject().equals("Login Credentials")) {
            return Messages.INVALID_TOKEN;
        }

        if(DatabaseQueryController.CheckJwtCredentials(claims.get("username", String.class), claims.get("password", String.class), claims.get("email", String.class)) != Messages.SUCCESS) {
            return Messages.INVALID_TOKEN;
        }
        return Messages.SUCCESS;
    }

    public static int validateSessionToken(Headers requestHeaders) {
        try {
            if (requestHeaders.containsKey("sessionToken")) {
                List<String> sessionTokens = requestHeaders.get("sessionToken");
                String sessionToken = sessionTokens.get(0);
                if(JwtHandler.validateUserSession(sessionToken) == Messages.SUCCESS) {
                    return JwtHandler.getUserIdFromJwtToken(sessionToken);
                }
                else {
                    return -1;
                }
            } else {
                return -1;
            }
        }
        catch (Exception e) {
            return -1;
        }
    }

    public static int getUserIdFromJwtToken(String jwt) throws IllegalArgumentException, SQLException {
        Claims claims = decodeJwtToken(jwt);
        String username = claims.get("username", String.class);
        return DatabaseQueryController.getUserId(username);
    }

    public static boolean checkExpiryDate(Date expiryDate) {
        Date now = new Date(System.currentTimeMillis());
        return expiryDate.before(now);
    }
}
