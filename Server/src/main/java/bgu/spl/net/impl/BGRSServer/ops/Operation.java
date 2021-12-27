package bgu.spl.net.impl.BGRSServer.ops;

public interface Operation<T> {
    public T execute();

    public void setOnlineUserName(String onlineUserName);
}
