package app.miniprogram.security.jwt;

import app.miniprogram.application.login.entity.UserEntity;
import com.app.redis.RedisClient;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Objects;
import java.util.UUID;

/**
 * @author :wkh.m
 * @date :2019/8/30.
 */
@Service
public class JwtService {

    @Value("${jwt.secret.key}")
    private String secretKey;

    @Value("${jwt.expire.time}")
    private long jwtExpireYime;

    @Autowired
    private RedisClient redisClient;

    /**
     * 生产token
     *
     * @param userEntity 用户信息
     * @return jwt生成的token
     */
    public String createTokenByWxAccount(UserEntity userEntity) {
        //JWT 随机ID,做为验证的key
        String jwtId = UUID.randomUUID().toString();
        // 生成签名
        Algorithm algorithm = Algorithm.HMAC256(secretKey);
        String token = JWT.create()
                .withClaim("wxOpenId", userEntity.getOpenId())
                .withClaim("sessionKey", userEntity.getSessionKey())
                .withClaim("jwt-id", jwtId)
                .withExpiresAt(new Date(System.currentTimeMillis() + jwtExpireYime * 1000))
                .sign(algorithm);
        // 设置到redis 在验证时从redis获取在对比（过期时间为两小时）
        redisClient.set("JWT-SESSION-" + jwtId, token, jwtExpireYime);

        return token;
    }

    /**
     * 验证jwt token
     *
     * @param token token
     * @return 验证结果
     */
    public boolean verifyToken(String token) {
        try {
            String jwtId = getJwtIdByToken(token);
            Object redisTokenObj = redisClient.get("JWT-SESSION-" + jwtId);
            if (Objects.isNull(redisTokenObj)) {
                return false;
            }
            String redisToken = (String) redisTokenObj;
            if (!token.equals(redisToken)) {
                return false;
            }

            // 解密签名 续签时间
            Algorithm algorithm = Algorithm.HMAC256(secretKey);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withClaim("wxOpenId", getWxOpenIdByToken(token))
                    .withClaim("sessionKey", getSessionKeyByToken(token))
                    .withClaim("jwt-id", jwtId)
                    .acceptExpiresAt(System.currentTimeMillis() + jwtExpireYime * 1000)
                    .build();
            verifier.verify(redisToken);
            redisClient.set("JWT-SESSION-" + jwtId, token, jwtExpireYime);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
     * 根据Token获取wxOpenId
     */
    public String getWxOpenIdByToken(String token) throws JWTDecodeException {
        return JWT.decode(token).getClaim("wxOpenId").asString();
    }

    /**
     * 根据Token获取sessionKey
     */
    public String getSessionKeyByToken(String token) throws JWTDecodeException {
        return JWT.decode(token).getClaim("sessionKey").asString();
    }

    /**
     * 根据Token 获取jwt-id
     */
    private String getJwtIdByToken(String token) throws JWTDecodeException {
        return JWT.decode(token).getClaim("jwt-id").asString();
    }
}
