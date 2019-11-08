package org.javatribe.lottery.entity;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @Author Jimzising
 * @Date 2019/10/20
 * @Desc 公众号接收或发送消息的实体对象
 */
@Data
@XmlRootElement(name = "xml")
@XmlAccessorType(XmlAccessType.FIELD)
public class WxMessage {
    /**
     * 开发者微信号
     */
    @XmlElement(name = "FromUserName")
    private String fromUserName;
    /**
     * 发送方帐号（一个OpenID）
     */
    @XmlElement(name = "ToUserName")
    private String toUserName;
    /**
     * 消息创建时间
     */
    @XmlElement(name = "CreateTime")
    private Long createTime;
    /**
     * 消息类型
     * text 文本消息
     * image 图片消息
     * voice 语音消息
     * video 视频消息
     * music 音乐消息
     */
    @XmlElement(name = "MsgType")
    private String msgType;
    /**
     * 消息id
     */
    @XmlElement(name = "MsgId")
    private Long msgId;
    /**
     * 文本内容
     */
    @XmlElement(name = "Content")
    private String content;
    /**
     * 图片链接（由系统生成）
     */
    @XmlElement(name = "PicUrl")
    private String picUrl;
    /**
     * 图片消息媒体id，可以调用多媒体文件下载接口拉取数据
     */
    @XmlElement(name = "MediaId")
    private String mediaId;
    /**
     * 消息类型
     */
    @XmlElement(name = "Event")
    private String event;
    /**
     * 事件KEY值，qrscene_为前缀，后面为二维码的参数值
     */
    @XmlElement(name = "EventKey")
    private String eventKey;
    /**
     * 二维码的ticket，可用来换取二维码图片
     */
    @XmlElement(name = "Ticket")
    private String ticket;

}
