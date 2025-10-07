package com.example.demo.entities;

public class MessageUser {
    Long messageid;
    Long userid;
    public MessageUser(long mid ,long uid){
        messageid=mid;
        userid=uid;
    }
    public MessageUser(){}
    public void setMessageId(int id) {messageid=(long)id;};
    public void setMessageId(Long id){messageid=id;};
    public void setUserId(int id) {userid=(long)id;};
    public void setUserId(Long id){userid=id;};
    public Long getUserId()   {return userid;};
    public Long getMessageId(){return messageid;};
}
