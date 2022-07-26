package charlie.feng.game.sudokumasterserv.master;

/**
 * The key for i18n message
 */
public enum MsgKey {
    START_RESOLVE("Start resolving grid %s", "开始解决数独", 0),
    SUCCESS_RESOLVE("Resolved grid %s", "成功解决数独", 0),
    BRUTE_FORCE_RESOLVE("Brute force resolved grid %s", "暴力尝试解决数独", 0),
    //p1 is cell location, p2 is new value
    GET_VALUE("Get value [%s].", "填入 [%s].", 1),
    //p1 is cell location, p2 is new value, p3 is remain value
    REMOVE_CANDIDATE("Removed [%s] remains: [%s].", "消除 [%s] 剩余 [%s].", 2),

    VALUE_IN_SAME_ROW("Value exist in same row", "同行单元格已有该值", 3),
    VALUE_IN_SAME_COLUMN("Value exist in same column", "同列单元格已有该值", 3),
    VALUE_IN_SAME_BLOCK("Value exist in same block", "同块单元格已经该值", 3);

    private final String enKey;
    private final String cnKey;
    private final int level;

    MsgKey(String enKey, String cnKey, int level) {
        this.enKey = enKey;
        this.cnKey = cnKey;
        this.level = level;
    }

    private String getEnKey() {
        return enKey;
    }

    private String getCnKey() {
        return cnKey;
    }

    public int getLevel() {
        return level;
    }


    public String getMsg(String lang, String... msgParams) {
        if (lang.startsWith("ZH")) {
            return String.format(getCnKey(), (Object[]) msgParams);
        } else {
            return String.format(getEnKey(), (Object[]) msgParams);
        }
    }
}
