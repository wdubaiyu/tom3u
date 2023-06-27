package com.zmt51.top.bean;

/**
 * 组播地址
 */
public class Multicast {
    //序号
    private int index;
    //频道名称
    private String channelName;
    //组播地址
    private String multicast;
    //回放天数
    private int playbackDays;
    //频道ID
    private long channelId;
    //清晰度/帧率/编码
    private String videParameter;
    //回放地址
    private String playbackAddress;

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public String getMulticast() {
        return multicast;
    }

    public void setMulticast(String multicast) {
        this.multicast = multicast;
    }

    public int getPlaybackDays() {
        return playbackDays;
    }

    public void setPlaybackDays(int playbackDays) {
        this.playbackDays = playbackDays;
    }

    public long getChannelId() {
        return channelId;
    }

    public void setChannelId(long channelId) {
        this.channelId = channelId;
    }

    public String getVideParameter() {
        return videParameter;
    }

    public void setVideParameter(String videParameter) {
        this.videParameter = videParameter;
    }

    public String getPlaybackAddress() {
        return playbackAddress;
    }

    public void setPlaybackAddress(String playbackAddress) {
        this.playbackAddress = playbackAddress;
    }

    /**
     * 生成频道行数据
     *
     * @param ip ip地址
     * @return
     */

    public String toRow(String ip) {
        return "\n#EXTINF:-1," + channelName + "\r\nhttp://" + ip + ":4022/rtp/" + multicast;
    }
}
