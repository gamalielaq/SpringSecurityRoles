package com.SpringSecurityRoles.constant;

public class SecurityConstant {
    
    public static final long EXPIRATION_TIME = 432_000_000; //5 days expresada en milesegundos
    public static final String TOKEN_PREFIX = "Bearer";
    public static final String JWT_TOKEN_HEADER ="Jwt-Token";
    public static final String TOKEN_CANNONT_BE_VERIFIED ="Token no a sido verificado";
    public static final String GET_ARRAYS_LLC ="Get Arrays, LLC";
    public static final String GET_ARRAYS_ADMINISTRATION = "User Managment Portal";
    public static final String AUTHORITIES = "authorities";
    public static final String FORBIDDEN_MESSAGE = "Necesita iniciar sesion para acceder a esta pagina";
    public static final String ACCES_DENIED_MESSAGE = "Utest no tiene permiso para acceder a esta página";
    public static final String OPTIONS_HTTP_METHOD = "OPTIONS";
    public static final String[] PUBLIC_URLS = {"/user/login", "/user/register", "/user/resetpassword/**", "/user/image/**"};

}
