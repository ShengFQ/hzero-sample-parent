package org.hzero.samples.core.context;

/**
 * 线程变量上下文
 */
public class EssContextHolder {

    private EssContextHolder() {
    }

    /**
     * sid
     */
    private final static ThreadLocal<String> SID = new ThreadLocal<>();

    /**
     * token
     */
    private final static ThreadLocal<String> TOKEN = new ThreadLocal<>();

    /**
     * 联盟code
     */
    private final static ThreadLocal<String> UNION_CODE = new ThreadLocal<>();

    /**
     * 联盟unionId
     */
    private final static ThreadLocal<String> UNION_ID = new ThreadLocal<>();

    /**
     * 设置SID
     *
     * @param sid
     */
    public static void setSID(String sid) {
        EssContextHolder.SID.set(sid);
    }

    /**
     * 获取SID
     */
    public static String getSID() {
        return EssContextHolder.SID.get();
    }

    /**
     * 设置TOKEN
     *
     * @param token
     */
    public static void setToken(String token) {
        EssContextHolder.TOKEN.set(token);
    }

    /**
     * 获取TOKEN
     */
    public static String getToken() {
        return EssContextHolder.TOKEN.get();
    }

    /**
     * 设置unionCode
     */
    public static void setUnionCode(String unionCode) {
        EssContextHolder.UNION_CODE.set(unionCode);
    }

    /**
     * 获取unionCode
     */
    public static String getUnionCode() {
        return EssContextHolder.UNION_CODE.get();
    }


    /**
     * 设置unionId
     */
    public static void setUnionId(String unionId) {
        EssContextHolder.UNION_ID.set(unionId);
    }

    /**
     * 获取联盟unionId
     */
    public static String getUnionId() {
        return EssContextHolder.UNION_ID.get();
    }
}
