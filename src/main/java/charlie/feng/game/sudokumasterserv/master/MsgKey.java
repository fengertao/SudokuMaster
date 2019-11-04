package charlie.feng.game.sudokumasterserv.master;

public enum MsgKey {
    START_RESOLVE("Start resolving grid %s", "开始解决数独", 0),
    SUCCESS_RESOLVE("Resolved grid %s", "成功解决数独", 0),
    ABORT_RESOLVE("Abort resolving grid %s", "放弃解决数独", 0),
    GET_VALUE("Get value [%s].", "填入 [%s]", 1), //p1 is cell location, p2 is new value
    REMOVE_CANDIDATE("Removed [%s] remains: [%s].", "消除 [%s] 剩余 [%s].", 2); //p1 is cell location, p2 is new value, p3 is remain value

    private String enKey;
    private String cnKey;
    private int level;

    MsgKey(String enKey, String cnKey, int level) {
        this.enKey = enKey;
        this.cnKey = cnKey;
        this.level = level;
    }

    public String getEnKey() {
        return enKey;
    }

    public String getCnKey() {
        return cnKey;
    }

    public int getLevel() {
        return level;
    }
}
